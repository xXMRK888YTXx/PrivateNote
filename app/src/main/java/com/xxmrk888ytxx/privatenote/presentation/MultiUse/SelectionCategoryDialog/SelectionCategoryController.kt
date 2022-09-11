package com.xxmrk888ytxx.privatenote.presentation.MultiUse.SelectionCategoryDialog

import com.xxmrk888ytxx.privatenote.data.Database.Entity.Category
import kotlinx.coroutines.flow.Flow

interface SelectionCategoryController {
    fun onCanceled()
    fun onConfirmed()
    fun getCategory() : Flow<List<Category>>
}