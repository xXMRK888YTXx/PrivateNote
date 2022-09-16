package com.xxmrk888ytxx.privatenote.domain.SecurityUtils

import android.util.Base64
import com.xxmrk888ytxx.privatenote.Utils.Exception.PasswordIsEmptyException
import java.security.MessageDigest
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.inject.Singleton
import kotlin.jvm.Throws


@Singleton
class SecurityUtilsImpl : SecurityUtils {

    @Throws(
        PasswordIsEmptyException::class,
        BadPaddingException::class
    )
    override fun encrypt(input:String,password:String): String {
        if(input.isEmpty()) return ""
        if(password.isEmpty()) throw PasswordIsEmptyException()
        val cipher = Cipher.getInstance("AES")
        val keySpec: SecretKeySpec = SecretKeySpec(password.toByteArray(),"AES")
        cipher.init(Cipher.ENCRYPT_MODE,keySpec)
        val encrypt = cipher.doFinal(input.toByteArray())
        return Base64.encodeToString(encrypt, Base64.DEFAULT)
    }

    @Throws(
        PasswordIsEmptyException::class,
        BadPaddingException::class
    )
    override fun decrypt(input:String,password:String): String {
        if(input.isEmpty()) return ""
        if(password.isEmpty()) throw PasswordIsEmptyException()
        val cipher = Cipher.getInstance("AES")
        val keySpec: SecretKeySpec = SecretKeySpec(password.toByteArray(),"AES")
        cipher.init(Cipher.DECRYPT_MODE,keySpec)
        val encrypt = cipher.doFinal(Base64.decode(input, Base64.DEFAULT))
        return String(encrypt)
    }

    @Throws(
        PasswordIsEmptyException::class
    )
    override fun passwordToHash(password:String, limit:Int) : String {
        if(password.isEmpty()) throw PasswordIsEmptyException()
        val text =  MessageDigest
            .getInstance("SHA-256")
            .digest(password.toByteArray())
            .fold("", { str, it -> str + "%02x".format(it) })
        if(limit > 0) return text.take(limit)
        else return text
    }
}