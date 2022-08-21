package com.xxmrk888ytxx.privatenote.Repositories.SettingsRepository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SettingsRepositoryImpl (
    private val context: Context
) : SettingsRepository {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private val toDoWithDateVisibleKey = booleanPreferencesKey("ToDoWithDateVisibleKey")
    private val completedToDoVisible = booleanPreferencesKey("CompletedToDoVisible")
    private val toDoWithoutDateVisible = booleanPreferencesKey("ToDoWithoutDateVisible")
    private val missedToDoVisible = booleanPreferencesKey("MissedToDoVisible")
    private val navigationSwipeState = booleanPreferencesKey("NavigationSwipeState")

    override fun getToDoWithDateVisible(): Flow<Boolean> = runBlocking(Dispatchers.IO) {
        return@runBlocking context.dataStore.data.map {
            it[toDoWithDateVisibleKey] ?: true
        }
    }

    override suspend fun changeToDoWithDateVisible() {
        val currentState = getToDoWithDateVisible()
        context.dataStore.edit {
            it[toDoWithDateVisibleKey] = !currentState.first()
        }
    }

    override fun getCompletedToDoVisible(): Flow<Boolean> = runBlocking(Dispatchers.IO) {
        return@runBlocking context.dataStore.data.map {
            it[completedToDoVisible] ?: true
        }
    }

    override suspend fun changeCompletedToDoVisible() {
        val currentState = getCompletedToDoVisible()
        context.dataStore.edit {
            it[completedToDoVisible] = !currentState.first()
        }
    }

    override fun getToDoWithoutDateVisible(): Flow<Boolean> = runBlocking(Dispatchers.IO) {
        return@runBlocking context.dataStore.data.map {
            it[toDoWithoutDateVisible] ?: true
        }
    }

    override suspend fun changeToDoWithoutDateVisible() {
        val currentState = getToDoWithoutDateVisible()
        context.dataStore.edit {
            it[toDoWithoutDateVisible] = !currentState.first()
        }
    }

    override fun getMissedToDoVisible(): Flow<Boolean>  = runBlocking(Dispatchers.IO) {
        return@runBlocking context.dataStore.data.map {
            it[missedToDoVisible] ?: true
        }
    }

    override suspend fun changeMissedToDoVisible() {
        val currentState = getMissedToDoVisible()
        context.dataStore.edit {
            it[missedToDoVisible] = !currentState.first()
        }
    }

    override fun getNavigationSwipeState(): Flow<Boolean> = runBlocking(Dispatchers.IO) {
        return@runBlocking context.dataStore.data.map {
            it[navigationSwipeState] ?: true
        }
    }

    override suspend fun setNavigationSwipeState(state: Boolean) {
        context.dataStore.edit {
            it[navigationSwipeState] = state
        }
    }
}