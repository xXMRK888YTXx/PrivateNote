package com.xxmrk888ytxx.privatenote.SecurityUtils

import android.util.Base64
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.spec.SecretKeySpec
import javax.inject.Singleton

@Singleton
class SecurityUtilsImpl : SecurityUtils {
    override fun encrypt(input:String,password:String): String {
        if(input.isEmpty()) return ""
        val cipher = Cipher.getInstance("AES")
        val keySpec: SecretKeySpec = SecretKeySpec(password.toByteArray(),"AES")
        cipher.init(Cipher.ENCRYPT_MODE,keySpec)
        val encrypt = cipher.doFinal(input.toByteArray())
        return Base64.encodeToString(encrypt, Base64.DEFAULT)
    }

    override fun decrypt(input:String,password:String): String {
        if(input.isEmpty()) return ""
        val cipher = Cipher.getInstance("AES")
        val keySpec: SecretKeySpec = SecretKeySpec(password.toByteArray(),"AES")
        cipher.init(Cipher.DECRYPT_MODE,keySpec)
        val encrypt = cipher.doFinal(Base64.decode(input, Base64.DEFAULT))
        return String(encrypt)
    }

    override fun passwordToHash(password:String, limit:Int) : String {
        if(password.isEmpty()) return ""
        val text =  MessageDigest
            .getInstance("SHA-256")
            .digest(password.toByteArray())
            .fold("", { str, it -> str + "%02x".format(it) })
        if(limit > 0) return text.take(limit)
        else return text
    }

    override suspend fun encryptFile(filePath: String, password: String) {
        try {
            val stream = FileInputStream(filePath)
            val fos = FileOutputStream("$filePath.png")
            var key: ByteArray? =
                (password).toByteArray(charset("UTF-8"))
            val sha = MessageDigest.getInstance("SHA-1")
            key = sha.digest(key)
            key = Arrays.copyOf(key, 16)
            val sks = SecretKeySpec(key, "AES")
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.ENCRYPT_MODE, sks)
            val cos = CipherOutputStream(fos, cipher)
            var b: Int
            val d = ByteArray(8)
            while (stream.read(d).also { b = it } != -1) {
                cos.write(d, 0, b)
            }
            cos.flush()
            cos.close()
            stream.close()
        }catch (e:Exception) {
            Log.d("MyLog",e.message.toString())
        }
    }
}