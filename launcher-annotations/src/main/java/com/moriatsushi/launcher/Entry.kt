package com.moriatsushi.launcher

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
annotation class Entry(
    val default: Boolean = false
)
