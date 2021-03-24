package com.sange.base.util

import java.math.BigDecimal

/**
 * Double 扩展方法
 * 转Int类型(四舍五入)
 *
 * ROUND_HALF_UP
向“最接近的”数字舍入，如果与两个相邻数字的距离相等，则为向上舍入的舍入模式。
如果舍弃部分 >= 0.5，则舍入行为与 ROUND_UP 相同;否则舍入行为与 ROUND_DOWN 相同。
注意，这是我们大多数人在小学时就学过的舍入模式(四舍五入)。

ROUND_HALF_EVEN  均匀舍入法(奇入偶舍)
向“最接近的”数字舍入，如果与两个相邻数字的距离相等，则向相邻的偶数舍入。
如果舍弃部分左边的数字为奇数，则舍入行为与 ROUND_HALF_UP 相同;
如果为偶数，则舍入行为与 ROUND_HALF_DOWN 相同。
注意，在重复进行一系列计算时，此舍入模式可以将累加错误减到最小。
此舍入模式也称为“银行家舍入法”，主要在美国使用。四舍六入，五分两种情况。
如果前一位为奇数，则入位，否则舍去。
以下例子为保留小数点1位，那么这种舍入方式下的结果。
1.15>1.2 1.25>1.2
 */
fun Double.toHalfInt(): Int = BigDecimal(this).setScale(0, BigDecimal.ROUND_HALF_EVEN).toInt()