package com.xxmrk888ytxx.privatenote.SecurityUtils

import android.util.Base64
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.inject.Singleton

@Singleton
class SecurityUtils {
    fun encrypt(input:String,password:String): String {
        if(input.isEmpty()) return ""
        val cipher = Cipher.getInstance("AES")
        val keySpec: SecretKeySpec = SecretKeySpec(password.toByteArray(),"AES")
        cipher.init(Cipher.ENCRYPT_MODE,keySpec)
        val encrypt = cipher.doFinal(input.toByteArray())
        return Base64.encodeToString(encrypt, Base64.DEFAULT)
    }

    fun decrypt(input:String,password:String): String {
        if(input.isEmpty()) return ""
        val cipher = Cipher.getInstance("AES")
        val keySpec: SecretKeySpec = SecretKeySpec(password.toByteArray(),"AES")
        cipher.init(Cipher.DECRYPT_MODE,keySpec)
        val encrypt = cipher.doFinal(Base64.decode(input, Base64.DEFAULT))
        return String(encrypt)
    }

    fun passwordToHash(password:String, limit:Int = 32) : String {
        if(password.isEmpty()) return ""
        return MessageDigest
            .getInstance("SHA-256")
            .digest(password.toByteArray())
            .fold("", { str, it -> str + "%02x".format(it) }).take(limit)
    }
}