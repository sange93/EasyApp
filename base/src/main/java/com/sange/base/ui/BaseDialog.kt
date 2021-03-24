package com.sange.base.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.BaseDialogFragment
import androidx.viewbinding.ViewBinding
import com.sange.base.weight.CenterDialog

/**
 * 弹窗基类
 *
 * @author ssq
 */
abstract class BaseDialog<VB : ViewBinding>: BaseDialogFragment() {
    /**
     * ViewBinding 实例
     */
    protected lateinit var mBinding: VB

    /**
     * 提供ViewBinding对象
     */
    protected abstract fun providerVB(inflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean = false): VB

    /**
     * 初始化界面
     */
    protected abstract fun initView()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = providerVB(inflater, container)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = CenterDialog(context!!)
}