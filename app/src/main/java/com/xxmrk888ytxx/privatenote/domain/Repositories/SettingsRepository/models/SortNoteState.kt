package com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.models

import android.annotation.SuppressLint
import androidx.annotation.IdRes
import com.xxmrk888ytxx.privatenote.R

@SuppressLint("ResourceType")
sealed class SortNoteState(val id:Int,@IdRes val title:Int) {
    object ByAscending : SortNoteState(1, R.string.Ascending)
    object ByDescending : SortNoteState(2,R.string.Descending)
}
