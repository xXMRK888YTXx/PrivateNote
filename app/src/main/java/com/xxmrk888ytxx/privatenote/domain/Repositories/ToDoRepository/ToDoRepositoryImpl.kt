package com.xxmrk888ytxx.privatenote.domain.Repositories.ToDoRepository

import com.xxmrk888ytxx.privatenote.data.Database.DAO.TodoDao
import com.xxmrk888ytxx.privatenote.data.Database.Entity.TodoItem
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Change_Mark_Status
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Insert_Todo_Event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Remove_Todo_Event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.Utils.SendAnalytics
import com.xxmrk888ytxx.privatenote.domain.UseCases.NotifyWidgetDataChangedUseCase.NotifyWidgetDataChangedUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.RemoveNotifyTaskIfTodoCompletedUseCase.RemoveNotifyTaskIfTodoCompletedUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
@SendAnalytics
class ToDoRepositoryImpl @Inject constructor(
    private val toDoDao: TodoDao,
    private val notifyWidgetDataChangedUseCase: NotifyWidgetDataChangedUseCase,
    private val removeNotifyTaskIfTodoCompletedUseCase: RemoveNotifyTaskIfTodoCompletedUseCase,
    private val analytics: AnalyticsManager
) : ToDoRepository {
    override fun getAllToDo(): Flow<List<TodoItem>> = runBlocking(Dispatchers.IO) {
       return@runBlocking toDoDao.getAllToDo()
    }

    override fun getToDoById(id: Int): Flow<TodoItem> = runBlocking(Dispatchers.IO) {
        return@runBlocking toDoDao.getToDoById(id)
    }

    override fun insertToDo(toDoItem: TodoItem) = runBlocking(Dispatchers.IO){
       analytics.sendEvent(Insert_Todo_Event,null)
        toDoDao.insertToDo(toDoItem)
        notifyWidgetDataChangedUseCase.execute()
    }

    override fun removeToDo(id: Int) = runBlocking(Dispatchers.IO) {
        analytics.sendEvent(Remove_Todo_Event,null)
        toDoDao.removeToDo(id)
        notifyWidgetDataChangedUseCase.execute()
        removeNotifyTaskIfTodoCompletedUseCase.execute(id)
    }

    override fun changeMarkStatus(id: Int, status: Boolean) = runBlocking(Dispatchers.IO) {
        analytics.sendEvent(Change_Mark_Status,null)
        toDoDao.changeMarkStatus(id,status)
        notifyWidgetDataChangedUseCase.execute()
        if(status) {
            removeNotifyTaskIfTodoCompletedUseCase.execute(id)
        }
    }
}