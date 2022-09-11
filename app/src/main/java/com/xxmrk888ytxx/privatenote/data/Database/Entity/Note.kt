package com.xxmrk888ytxx.privatenote.data.Database.Entity

import androidx.room.*

@Entity(
    indices =[
        Index("id", unique = true),
        Index("category")
    ],
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["categoryId"],
            childColumns = ["category"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.NO_ACTION
        )
    ]
)
data class Note(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val title:String,
    val text:String,
    val created_at:Long = System.currentTimeMillis(),
    var isChosen:Boolean = false,
    var isEncrypted:Boolean = false,
    val category:Int? = null
)