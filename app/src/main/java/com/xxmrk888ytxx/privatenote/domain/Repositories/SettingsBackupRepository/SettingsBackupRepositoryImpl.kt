package com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsBackupRepository

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.xxmrk888ytxx.privatenote.Utils.CoroutineScopes.ApplicationScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsBackupRepositoryImpl constructor(
    private val context:Context
) : SettingsBackupRepository {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "backup_settings")

    private val isEnableBackup = booleanPreferencesKey("isEnableBackup")
    private val isBackupNotEncryptedNoteKey = booleanPreferencesKey("isBackupNotEncryptedNoteKey")
    private val isBackupEncryptedNoteKey = booleanPreferencesKey("isBackupEncryptedNoteKey")
    private val isBackupNoteImagesKey = booleanPreferencesKey("isBackupNoteImagesKey")
    private val isBackupNoteAudioKey = booleanPreferencesKey("isBackupNoteAudioKey")
    private val isBackupNoteCategoryKey = booleanPreferencesKey("isBackupNoteCategoryKey")
    private val isBackupNotCompletedTodoKey = booleanPreferencesKey("isBackupNotCompletedTodoKey")
    private val isBackupCompletedTodoKey = booleanPreferencesKey("isBackupCompletedTodoKey")

    override fun getBackupSettings(): SharedFlow<BackupSettings> = settingsBackup

    private suspend fun restoreSettings() {
        val isEnableBackup = getIsEnableBackup().first()
        val isBackupNotEncryptedNoteKey = getIsBackupNotEncryptedNote().first()
        val isBackupEncryptedNoteKey = getIsBackupEncryptedNote().first()
        val isBackupNoteImagesKey = getIsBackupNoteImages().first()
        val isBackupNoteAudioKey = getIsBackupNoteAudio().first()
        val isBackupNoteCategoryKey = getIsBackupNoteCategory().first()
        val isBackupNotCompletedTodoKey = getIsBackupNotCompletedTodo().first()
        val isBackupCompletedTodoKey = getIsBackupCompletedTodo().first()
        _settingsBackup.emit(BackupSettings(
            isEnableBackup,
            isBackupNotEncryptedNoteKey,
            isBackupEncryptedNoteKey,
            isBackupNoteImagesKey,
            isBackupNoteAudioKey,
            isBackupNoteCategoryKey,
            isBackupNotCompletedTodoKey,
            isBackupCompletedTodoKey
        ))
    }

    private suspend fun notifySettingsChanges(changes:(BackupSettings) -> BackupSettings) {
        val oldSettings = _settingsBackup.first()
        _settingsBackup.emit(changes(oldSettings))
    }

    private val _settingsBackup:MutableSharedFlow<BackupSettings> = MutableSharedFlow(1)
    private val settingsBackup:SharedFlow<BackupSettings> = _settingsBackup

    init {
        ApplicationScope.launch(Dispatchers.IO) {
            restoreSettings()
        }
    }

    override suspend fun updateIsEnableBackup(newState: Boolean) {
        context.dataStore.edit {
            it[isEnableBackup] = newState
        }
        notifySettingsChanges {
            it.copy(isEnableBackup = newState)
        }
    }

    override suspend fun updateIsBackupNotEncryptedNote(newState: Boolean) {
        context.dataStore.edit {
            it[isBackupNotEncryptedNoteKey] = newState
        }
        notifySettingsChanges {
            it.copy(isBackupNotEncryptedNote = newState)
        }
    }

    override suspend fun updateIsBackupEncryptedNote(newState: Boolean) {
        context.dataStore.edit {
            it[isBackupEncryptedNoteKey] = newState
        }
        notifySettingsChanges {
            it.copy(isBackupEncryptedNote = newState)
        }
    }

    override suspend fun updateIsBackupNoteImages(newState: Boolean) {
        context.dataStore.edit {
            it[isBackupNoteImagesKey] = newState
        }
        notifySettingsChanges {
            it.copy(isBackupNoteImages = newState)
        }
    }

    override suspend fun updateIsBackupNoteAudio(newState: Boolean) {
        context.dataStore.edit {
            it[isBackupNoteAudioKey] = newState
        }
        notifySettingsChanges {
            it.copy(isBackupNoteAudio = newState)
        }
    }

    override suspend fun updateIsBackupNoteCategory(newState: Boolean) {
        context.dataStore.edit {
            it[isBackupNoteCategoryKey] = newState
        }
        notifySettingsChanges {
            it.copy(isBackupNoteCategory = newState)
        }
    }

    override suspend fun updateIsBackupNotCompletedTodo(newState: Boolean) {
        context.dataStore.edit {
            it[isBackupNotCompletedTodoKey] = newState
        }
        notifySettingsChanges {
            it.copy(isBackupNotCompletedTodo = newState)
        }
    }

    override suspend fun updateIsBackupCompletedTodo(newState: Boolean) {
        context.dataStore.edit {
            it[isBackupCompletedTodoKey] = newState
        }
        notifySettingsChanges {
            it.copy(isBackupCompletedTodo = newState)
        }
    }

    private fun getIsEnableBackup() : Flow<Boolean> {
        return context.dataStore.data.map {
            it[isEnableBackup] ?: false
        }
    }

    private fun getIsBackupNotEncryptedNote() : Flow<Boolean> {
        return context.dataStore.data.map {
            it[isBackupNotEncryptedNoteKey] ?: true
        }
    }
    private fun getIsBackupEncryptedNote() : Flow<Boolean> {
        return context.dataStore.data.map {
            it[isBackupEncryptedNoteKey] ?: true
        }
    }

    private fun getIsBackupNoteImages() : Flow<Boolean> {
        return context.dataStore.data.map {
            it[isBackupNoteImagesKey] ?: true
        }
    }

    private fun getIsBackupNoteAudio() : Flow<Boolean> {
        return context.dataStore.data.map {
            it[isBackupNoteAudioKey] ?: true
        }
    }

    private fun getIsBackupNoteCategory() : Flow<Boolean> {
        return context.dataStore.data.map {
            it[isBackupNoteCategoryKey] ?: true
        }
    }

    private fun getIsBackupNotCompletedTodo() : Flow<Boolean> {
        return context.dataStore.data.map {
            it[isBackupNotCompletedTodoKey] ?: true
        }
    }

    private fun getIsBackupCompletedTodo() : Flow<Boolean> {
        return context.dataStore.data.map {
            it[isBackupCompletedTodoKey] ?: false
        }
    }
}