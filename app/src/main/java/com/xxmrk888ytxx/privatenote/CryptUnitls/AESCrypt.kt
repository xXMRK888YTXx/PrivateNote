package com.xxmrk888ytxx.privatenote.CryptUnitls

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class AESCrypt {
    fun encrypt(input:String,password:String): String {
        val cipher = Cipher.getInstance("AES")
        val keySpec: SecretKeySpec = SecretKeySpec(password.toByteArray(),"AES")
        cipher.init(Cipher.ENCRYPT_MODE,keySpec)
        val encrypt = cipher.doFinal(input.toByteArray())
        return Base64.encodeToString(encrypt, Base64.DEFAULT)
    }
    // Расшифровать
    fun decrypt(input:String,password:String): String {
        val cipher = Cipher.getInstance("AES")
        val keySpec: SecretKeySpec = SecretKeySpec(password.toByteArray(),"AES")
        cipher.init(Cipher.DECRYPT_MODE,keySpec)
        val encrypt = cipher.doFinal(Base64.decode(input, Base64.DEFAULT))
        return String(encrypt)
    }
}