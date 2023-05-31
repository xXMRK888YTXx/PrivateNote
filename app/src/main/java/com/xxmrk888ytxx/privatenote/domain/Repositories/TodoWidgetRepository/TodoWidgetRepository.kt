package com.xxmrk888ytxx.privatenote.domain.Repositories.TodoWidgetRepository

import com.xxmrk888ytxx.privatenote.data.Database.Entity.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoWidgetRepository {
    val todoListFlow: Flow<List<TodoItem>>
}