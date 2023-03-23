package com.xxmrk888ytxx.privatenote.presentation.theme

sealed class ThemeType(val id:Int) {
    object System : ThemeType(-1)

    object White : ThemeType(0)

    object Black : ThemeType(1)

    companion object {
        fun fromInt(id:Int) : ThemeType {
           return  when(id) {
                0 -> White

                1 -> Black

                else -> System
            }
        }
    }
}
