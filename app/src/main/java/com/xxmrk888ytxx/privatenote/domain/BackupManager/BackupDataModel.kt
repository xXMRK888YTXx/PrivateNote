package com.xxmrk888ytxx.privatenote.domain.BackupManager

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Category
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Note
import com.xxmrk888ytxx.privatenote.data.Database.Entity.TodoItem
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class BackupDataModel(
    val notes:List<Note>,
    val category: List<Category>,
    val todo:List<TodoItem>
) : Parcelable
