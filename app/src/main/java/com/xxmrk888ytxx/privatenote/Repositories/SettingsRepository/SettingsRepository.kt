package com.xxmrk888ytxx.privatenote.Repositories.SettingsRepository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getToDoWithDateVisible() : Flow<Boolean>

    suspend fun changeToDoWithDateVisible()

    fun getCompletedToDoVisible() : Flow<Boolean>

    suspend fun changeCompletedToDoVisible()

    fun getToDoWithoutDateVisible() : Flow<Boolean>

    suspend fun changeToDoWithoutDateVisible()

    fun getMissedToDoVisible() : Flow<Boolean>

    suspend fun changeMissedToDoVisible()

    fun getNavigationSwipeState() : Flow<Boolean>

    suspend fun setNavigationSwipeState(state:Boolean)
}