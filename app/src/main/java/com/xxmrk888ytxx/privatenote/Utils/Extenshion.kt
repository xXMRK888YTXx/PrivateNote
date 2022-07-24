package com.xxmrk888ytxx.privatenote.Utils

fun String.getFirstLine() : String {
    try {
       return this.lines()[0]
    }catch (e:Exception) {
        return ""
    }
}