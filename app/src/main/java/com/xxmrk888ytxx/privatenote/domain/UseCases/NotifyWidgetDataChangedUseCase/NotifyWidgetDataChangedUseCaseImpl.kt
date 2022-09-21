package com.xxmrk888ytxx.privatenote.domain.UseCases.NotifyWidgetDataChangedUseCase

import com.xxmrk888ytxx.privatenote.domain.Repositories.TodoWidgetRepository.TodoWidgetRepository

class NotifyWidgetDataChangedUseCaseImpl constructor(
    private val todoWidgetRepository: TodoWidgetRepository
) : NotifyWidgetDataChangedUseCase {

    override fun execute() {
        todoWidgetRepository.updateWidgetData()
    }

}