package com.xxmrk888ytxx.privatenote.MultiUse.SelectionCategoryDialog

import com.xxmrk888ytxx.privatenote.DB.Entity.Category
import kotlinx.coroutines.flow.Flow

interface SelectionCategoryController {
    fun onCanceled()
    fun onConfirmed()
    fun getCategory() : Flow<List<Category>>
}