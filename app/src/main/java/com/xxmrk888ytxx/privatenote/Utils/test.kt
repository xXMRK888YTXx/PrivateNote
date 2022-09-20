package com.xxmrk888ytxx.privatenote.Utils

import android.annotation.SuppressLint
import android.os.Build
import android.os.CountDownTimer
import android.util.Base64
import android.util.Base64.encode
import androidx.annotation.RequiresApi
import com.google.android.gms.common.util.Base64Utils.encode
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.xxmrk888ytxx.privatenote.MainActivity
import com.xxmrk888ytxx.privatenote.data.Database.Entity.ToDoItem
import kotlinx.coroutines.*
import java.security.Key
import java.security.Security
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.SecretKeySpec

fun main() = runBlocking {
    val testClass = ToDoItem(
        id = 8,
        todoText = "parse to JSON",
        isCompleted = false,
        isImportant = true,
        todoTime = 888888
    )
    val moshi: Moshi = Moshi.Builder().build()
    val jsonAdapter: JsonAdapter<ToDoItem> = moshi.adapter(ToDoItem::class.java)
    val json: String = jsonAdapter.toJson(testClass)
    println(json)
    val test =  jsonAdapter.fromJson(json)
    println(test == testClass)
}




