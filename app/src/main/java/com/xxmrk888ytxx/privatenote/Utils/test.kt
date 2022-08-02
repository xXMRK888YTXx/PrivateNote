package com.xxmrk888ytxx.privatenote.Utils

import android.annotation.SuppressLint
import android.os.Build
import android.util.Base64
import android.util.Base64.encode
import androidx.annotation.RequiresApi
import com.google.android.gms.common.util.Base64Utils.encode
import kotlinx.coroutines.*
import java.security.Key
import java.security.Security
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.SecretKeySpec


fun main() = runBlocking {
    val one = getInt()
    val two = getInt()
    println(one.await()+two.await())
}

fun getInt() = GlobalScope.async {
    delay(2000)
    return@async 1
}

