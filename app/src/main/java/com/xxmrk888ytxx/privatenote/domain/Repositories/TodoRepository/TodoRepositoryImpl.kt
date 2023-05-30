package com.xxmrk888ytxx.privatenote.domain.Repositories.TodoRepository

import com.xxmrk888ytxx.privatenote.data.Database.DAO.TodoDao
import com.xxmrk888ytxx.privatenote.data.Database.Entity.TodoItem
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Change_Mark_Status
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Insert_Todo_Event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Remove_Todo_Event
import com.xxmrk888ytxx.privatenote.domain.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.Utils.SendAnalytics
import com.xxmrk888ytxx.privatenote.domain.UseCases.RemoveNotifyTaskIfTodoCompletedUseCase.RemoveNotifyTaskIfTodoCompletedUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject
@SendAnalytics
class TodoRepositoryImpl @Inject constructor(
    private val todoDao: TodoDao,
    private val removeNotifyTaskIfTodoCompletedUseCase: RemoveNotifyTaskIfTodoCompletedUseCase,
    private val analytics: AnalyticsManager
) : TodoRepository {
    override fun getAllToDo(): Flow<List<TodoItem>> {
       return todoDao.getAllToDo()
    }

    override fun getToDoById(id: Int): Flow<TodoItem> {
        return todoDao.getToDoById(id)
    }

    override suspend fun insertToDo(toDoItem: TodoItem) = withContext(Dispatchers.IO) {
       analytics.sendEvent(Insert_Todo_Event,null)
        todoDao.insertToDo(toDoItem)
    }

    override suspend fun removeToDo(id: Int) = withContext(Dispatchers.IO) {
        analytics.sendEvent(Remove_Todo_Event,null)
        todoDao.removeToDo(id)
        removeNotifyTaskIfTodoCompletedUseCase.execute(id)
    }

    override suspend fun changeMarkStatus(id: Int, status: Boolean) = withContext(Dispatchers.IO) {
        analytics.sendEvent(Change_Mark_Status,null)
        todoDao.changeMarkStatus(id,status)
        if(status) {
            removeNotifyTaskIfTodoCompletedUseCase.execute(id)
        }
    }
}