package com.xxmrk888ytxx.privatenote.domain.Repositories.TodoWidgetRepository

import com.xxmrk888ytxx.privatenote.data.Database.DAO.TodoDao
import com.xxmrk888ytxx.privatenote.data.Database.Entity.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoWidgetRepositoryImpl @Inject constructor(
    private val toDoDao: TodoDao,
) : TodoWidgetRepository {
    override val todoListFlow: Flow<List<TodoItem>> = toDoDao.getAllToDo().map {
        it.filter { !it.isCompleted }.sortedByDescending { it.isImportant }
    }

}