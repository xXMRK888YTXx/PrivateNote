package com.xxmrk888ytxx.privatenote.DB.Entity

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity(
    indices =[
        Index("categoryId", unique = true),
        Index("categoryName")
    ]
)
data class Category(
    @PrimaryKey(autoGenerate = true) val categoryId: Int = 0,
    val categoryName:String,
    val red:Float = (0..1).random().toFloat(),
    val green:Float =(0..1).random().toFloat(),
    val blue:Float = (0..1).random().toFloat()
)