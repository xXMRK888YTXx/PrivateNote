package com.xxmrk888ytxx.privatenote.Repositories.ToDoRepository

import com.google.firebase.analytics.FirebaseAnalytics
import com.xxmrk888ytxx.privatenote.DB.DAO.ToDoDao
import com.xxmrk888ytxx.privatenote.DB.Entity.ToDoItem
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Change_Mark_Status
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Insert_Todo_Event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Remove_Todo_Event
import com.xxmrk888ytxx.privatenote.Utils.SendAnalytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
@SendAnalytics
class ToDoRepositoryImpl @Inject constructor(
     private val toDoDao: ToDoDao,
     private val analytics: FirebaseAnalytics
) : ToDoRepository {
    override fun getAllToDo(): Flow<List<ToDoItem>> = runBlocking(Dispatchers.IO) {
       return@runBlocking toDoDao.getAllToDo()
    }

    override fun getToDoById(id: Int): Flow<ToDoItem> = runBlocking(Dispatchers.IO) {
        return@runBlocking toDoDao.getToDoById(id)
    }

    override fun insertToDo(toDoItem: ToDoItem) = runBlocking(Dispatchers.IO){
       analytics.logEvent(Insert_Todo_Event,null)
        toDoDao.insertToDo(toDoItem)
    }

    override fun removeToDo(id: Int) = runBlocking(Dispatchers.IO) {
        analytics.logEvent(Remove_Todo_Event,null)
        toDoDao.removeToDo(id)
    }

    override fun changeMarkStatus(id: Int, status: Boolean) = runBlocking(Dispatchers.IO) {
        analytics.logEvent(Change_Mark_Status,null)
        toDoDao.changeMarkStatus(id,status)
    }
}