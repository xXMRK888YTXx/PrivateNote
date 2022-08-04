package com.xxmrk888ytxx.privatenote.SecurityUtils

interface SecurityUtils {
    fun encrypt(input:String,password:String): String
    fun decrypt(input:String,password:String): String
    fun passwordToHash(password:String, limit:Int = 32) : String
}