package androidx.fragment.app

/**
 * 自定义dialogFragment
 * @author ssq
 */
open class BaseDialogFragment : DialogFragment() {

    /**
     * 显示弹窗（允许状态丢失）
     * @param manager The FragmentManager this fragment will be added to.
     * @param tag The tag for this fragment, as per
     */
    override fun show(manager: FragmentManager, tag: String?) {
        // 如果已有此弹窗 就移除
        if (isAdded || manager.findFragmentByTag(tag) != null) {
            manager.beginTransaction().remove(this).commit()
//            return
        }
        this.mDismissed = false
        this.mShownByMe = true
        val ft = manager.beginTransaction()
        ft.add(this, tag)
        manager.executePendingTransactions()
        // 解决Bug：java.lang.IllegalStateException Can not perform this action after onSaveInstanceState
        // 这里把原来的commit()方法换成了commitAllowingStateLoss()
        ft.commitAllowingStateLoss()
    }
}