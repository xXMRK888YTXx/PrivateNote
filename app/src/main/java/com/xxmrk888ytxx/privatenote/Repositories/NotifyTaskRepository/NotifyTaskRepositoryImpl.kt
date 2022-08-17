package com.xxmrk888ytxx.privatenote.Repositories.NotifyTaskRepository

import com.xxmrk888ytxx.privatenote.DB.DAO.NotifyTaskDao
import com.xxmrk888ytxx.privatenote.DB.Entity.NotifyTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class NotifyTaskRepositoryImpl @Inject constructor(
    private val notifyTaskDao: NotifyTaskDao
) : NotifyTaskRepository {
    override fun getAllTasks(): Flow<List<NotifyTask>> = runBlocking(Dispatchers.IO) {
        return@runBlocking notifyTaskDao.getAllTasks()
    }

    override fun getTaskByTodoId(taskId: Int): Flow<NotifyTask?> = runBlocking(Dispatchers.IO) {
         return@runBlocking notifyTaskDao.getTaskByTodoId(taskId)
    }

    override fun insertTask(task: NotifyTask) = runBlocking(Dispatchers.IO) {
        notifyTaskDao.insertTask(task)
    }

    override fun removeTask(taskId: Int) = runBlocking(Dispatchers.IO) {
        notifyTaskDao.removeTask(taskId)
    }

    override fun getTaskEnableStatus(taskId: Int): Boolean?  = runBlocking(Dispatchers.IO) {
        return@runBlocking notifyTaskDao.getTaskEnableStatus(taskId)
    }
}