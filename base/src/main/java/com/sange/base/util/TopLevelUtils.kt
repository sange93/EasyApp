package com.sange.base.util

import android.content.Context
import android.media.AsyncPlayer
import android.media.AudioAttributes
import android.media.AudioManager
import android.net.Uri
import android.os.Build

/**————————————————顶级函数————————————————————*/

infix fun Byte.and(mask: Int): Int = toInt() and mask
infix fun Short.and(mask: Int): Int = toInt() and mask
infix fun Int.and(mask: Long): Long = toLong() and mask

/**
 * 音乐播放
 * 方法过时兼容
 */
@Suppress("DEPRECATION")
fun play(player: AsyncPlayer, context: Context, uri: Uri, looping: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        player.play(context, uri, looping, AudioAttributes.Builder().build())
    } else {
        player.play(context, uri, looping, AudioManager.STREAM_MUSIC)
    }
}