package com.xxmrk888ytxx.privatenote.data.Database.Entity


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "TODO",
    indices = [
        Index("id", unique = true)
    ],
)
@JsonClass(generateAdapter = true)
@Parcelize
data class TodoItem(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val todoText:String,
    val isCompleted:Boolean = false,
    val isImportant:Boolean,
    val todoTime:Long? = null,
) : Parcelable