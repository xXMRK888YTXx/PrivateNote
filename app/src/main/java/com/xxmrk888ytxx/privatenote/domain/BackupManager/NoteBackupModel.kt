package com.xxmrk888ytxx.privatenote.domain.BackupManager

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Note
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class NoteBackupModel(
    val note:Note,
    val images:List<String>,
    val audio:List<String>
) : Parcelable
