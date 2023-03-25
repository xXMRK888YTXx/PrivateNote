package com.xxmrk888ytxx.privatenote.domain.Repositories.NotifyTaskRepository

import com.xxmrk888ytxx.privatenote.data.Database.DAO.NotifyTaskDao
import com.xxmrk888ytxx.privatenote.data.Database.Entity.NotifyTask
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Add_NotifyTask
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Remove_NotifyTask
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Remove_NotifyTask_By_TodoId
import com.xxmrk888ytxx.privatenote.domain.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.Utils.SendAnalytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject
@SendAnalytics
class NotifyTaskRepositoryImpl @Inject constructor(
    private val notifyTaskDao: NotifyTaskDao,
    private val analytics: AnalyticsManager
) : NotifyTaskRepository {
    override fun getAllTasks(): Flow<List<NotifyTask>> {
        return notifyTaskDao.getAllTasks()
    }

    override fun getTaskByTodoId(todoId: Int): Flow<NotifyTask?> {
         return notifyTaskDao.getTaskByTodoId(todoId)
    }

    override suspend fun insertTask(task: NotifyTask) = withContext(Dispatchers.IO) {
        analytics.sendEvent(Add_NotifyTask,null)
        notifyTaskDao.insertTask(task)
    }

    override suspend fun removeTask(taskId: Int) = withContext(Dispatchers.IO) {
        analytics.sendEvent(Remove_NotifyTask,null)
        notifyTaskDao.removeTask(taskId)
    }

    override suspend fun removeTaskByTodoId(todoId: Int) = withContext(Dispatchers.IO) {
        analytics.sendEvent(Remove_NotifyTask_By_TodoId,null)
        notifyTaskDao.removeTaskByTodoId(todoId)
    }

    override suspend fun getTaskEnableStatus(taskId: Int): Boolean?
    = withContext(Dispatchers.IO) {
        return@withContext notifyTaskDao.getTaskEnableStatus(taskId)
    }
}