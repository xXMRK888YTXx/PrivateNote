package com.xxmrk888ytxx.privatenote.Screen.Dialogs.SelectionCategoryDialog

import com.xxmrk888ytxx.privatenote.DB.Entity.Category
import kotlinx.coroutines.flow.Flow

interface SelectionCategoryDispatcher {
    fun onCanceled()
    fun onConfirmed()
    fun getCategory() : Flow<List<Category>>
}