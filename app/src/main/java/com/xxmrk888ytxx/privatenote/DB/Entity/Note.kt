package com.xxmrk888ytxx.privatenote.DB.Entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices =[
        Index("id", unique = true)
    ]
)
data class Note(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val title:String,
    val text:String,
    val created_at:Long = System.currentTimeMillis(),
    val isChosen:Boolean = false,
    var isEncrypted:Boolean = false
)