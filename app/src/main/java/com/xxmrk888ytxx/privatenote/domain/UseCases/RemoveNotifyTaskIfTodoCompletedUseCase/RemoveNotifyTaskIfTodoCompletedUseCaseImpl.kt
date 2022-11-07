package com.xxmrk888ytxx.privatenote.domain.UseCases.RemoveNotifyTaskIfTodoCompletedUseCase

import com.xxmrk888ytxx.privatenote.Utils.CoroutineScopes.ApplicationScope
import com.xxmrk888ytxx.privatenote.Utils.getData
import com.xxmrk888ytxx.privatenote.domain.NotifyTaskManager.NotifyTaskManager
import com.xxmrk888ytxx.privatenote.domain.Repositories.NotifyTaskRepository.NotifyTaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RemoveNotifyTaskIfTodoCompletedUseCaseImpl @Inject constructor(
    private val notifyTaskRepository: NotifyTaskRepository
) : RemoveNotifyTaskIfTodoCompletedUseCase {
    override fun execute(todoId: Int) {
        ApplicationScope.launch(Dispatchers.IO) {
            val task = notifyTaskRepository.getTaskByTodoId(todoId).getData() ?: return@launch
            notifyTaskRepository.removeTask(task.taskId)
        }
    }
}