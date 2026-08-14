package com.micrantha.morifolium

object PlatformContract {
    const val PROFILE = "android-kotlin"
    const val MILESTONE = "v0.1"

    fun identifier(): String = "$MILESTONE:$PROFILE"
}
