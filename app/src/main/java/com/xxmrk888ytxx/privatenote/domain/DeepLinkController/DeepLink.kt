package com.xxmrk888ytxx.privatenote.domain.DeepLinkController

import com.xxmrk888ytxx.privatenote.data.Database.Entity.ToDoItem

sealed class DeepLink(var isActiveDeepLink: Boolean,val idDeepLink: Int) {
    data class TodoDeepLink(
        private val id:Int,
        val todo:ToDoItem?,
        private var Active:Boolean = true,
    ) : DeepLink(Active,id)
}
