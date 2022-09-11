package com.xxmrk888ytxx.privatenote.data.Database.Entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "TODO",
    indices = [
        Index("id", unique = true)
    ],
)
data class ToDoItem(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val todoText:String,
    val isCompleted:Boolean = false,
    val isImportant:Boolean,
    val todoTime:Long? = null,
)