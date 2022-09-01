package com.xxmrk888ytxx.privatenote.Repositories.NotifyTaskRepository

import com.google.firebase.analytics.FirebaseAnalytics
import com.xxmrk888ytxx.privatenote.DB.DAO.NotifyTaskDao
import com.xxmrk888ytxx.privatenote.DB.Entity.NotifyTask
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Add_NotifyTask
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Remove_NotifyTask
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Remove_NotifyTask_By_TodoId
import com.xxmrk888ytxx.privatenote.Utils.SendAnalytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
@SendAnalytics
class NotifyTaskRepositoryImpl @Inject constructor(
    private val notifyTaskDao: NotifyTaskDao,
    private val analytics: FirebaseAnalytics
) : NotifyTaskRepository {
    override fun getAllTasks(): Flow<List<NotifyTask>> = runBlocking(Dispatchers.IO) {
        return@runBlocking notifyTaskDao.getAllTasks()
    }

    override fun getTaskByTodoId(todoId: Int): Flow<NotifyTask?> = runBlocking(Dispatchers.IO) {
         return@runBlocking notifyTaskDao.getTaskByTodoId(todoId)
    }

    override fun insertTask(task: NotifyTask) = runBlocking(Dispatchers.IO) {
        analytics.logEvent(Add_NotifyTask,null)
        notifyTaskDao.insertTask(task)
    }

    override fun removeTask(taskId: Int) = runBlocking(Dispatchers.IO) {
        analytics.logEvent(Remove_NotifyTask,null)
        notifyTaskDao.removeTask(taskId)
    }

    override fun removeTaskByTodoId(todoId: Int) = runBlocking(Dispatchers.IO) {
        analytics.logEvent(Remove_NotifyTask_By_TodoId,null)
        notifyTaskDao.removeTaskByTodoID(todoId)
    }

    override fun getTaskEnableStatus(taskId: Int): Boolean?  = runBlocking(Dispatchers.IO) {
        return@runBlocking notifyTaskDao.getTaskEnableStatus(taskId)
    }
}