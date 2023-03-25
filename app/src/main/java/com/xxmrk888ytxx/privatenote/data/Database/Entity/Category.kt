package com.xxmrk888ytxx.privatenote.data.Database.Entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Entity(
    indices =[
        Index("categoryId", unique = true),
        Index("categoryName")
    ]
)
@JsonClass(generateAdapter = true)
@Parcelize
data class Category(
    @PrimaryKey(autoGenerate = true) val categoryId: Int = 0,
    val categoryName:String,
    val red:Float = (0..1).random().toFloat(),
    val green:Float =(0..1).random().toFloat(),
    val blue:Float = (0..1).random().toFloat()
) : Parcelable