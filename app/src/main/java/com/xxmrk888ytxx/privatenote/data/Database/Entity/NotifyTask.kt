package com.xxmrk888ytxx.privatenote.data.Database.Entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Notify_Tasks",
    indices = [
        Index("taskId", unique = true),
        Index("todoId", unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            entity = ToDoItem::class,
            parentColumns = ["id"],
            childColumns = ["todoId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class NotifyTask (
    @PrimaryKey(autoGenerate = true) val taskId:Int,
    val todoId:Int,
    val enable:Boolean,
    val time:Long,
    val isPriority:Boolean
    )