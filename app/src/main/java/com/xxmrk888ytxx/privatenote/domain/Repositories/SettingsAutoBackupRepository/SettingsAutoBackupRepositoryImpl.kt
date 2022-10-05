package com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsAutoBackupRepository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.xxmrk888ytxx.privatenote.Utils.CoroutineScopes.ApplicationScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsAutoBackupRepositoryImpl constructor(
    private val context:Context
) : SettingsAutoBackupRepository {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "backup_settings")

    private val isEnableLocalBackup = booleanPreferencesKey("isEnableBackup")
    private val isEnableGDriveBackup = booleanPreferencesKey("isEnableGDriveBackup")
    private val isBackupNotEncryptedNoteKey = booleanPreferencesKey("isBackupNotEncryptedNoteKey")
    private val isBackupEncryptedNoteKey = booleanPreferencesKey("isBackupEncryptedNoteKey")
    private val isBackupNoteImagesKey = booleanPreferencesKey("isBackupNoteImagesKey")
    private val isBackupNoteAudioKey = booleanPreferencesKey("isBackupNoteAudioKey")
    private val isBackupNoteCategoryKey = booleanPreferencesKey("isBackupNoteCategoryKey")
    private val isBackupNotCompletedTodoKey = booleanPreferencesKey("isBackupNotCompletedTodoKey")
    private val isBackupCompletedTodoKey = booleanPreferencesKey("isBackupCompletedTodoKey")
    private val backupPathKey = stringPreferencesKey("backupPathKey")
    private val repeatAutoBackupTimeAtHours = longPreferencesKey("repeatAutoBackupTimeAtHours")

    override fun getBackupSettings(): SharedFlow<BackupSettings> = settingsBackup

    private suspend fun restoreSettings() {
        val isEnableLocalBackup = getIsEnableLocalBackup().first()
        val isEnableGDriveBackup = getIsEnableGDriveBackup().first()
        val isBackupNotEncryptedNoteKey = getIsBackupNotEncryptedNote().first()
        val isBackupEncryptedNoteKey = getIsBackupEncryptedNote().first()
        val isBackupNoteImagesKey = getIsBackupNoteImages().first()
        val isBackupNoteAudioKey = getIsBackupNoteAudio().first()
        val isBackupNoteCategoryKey = getIsBackupNoteCategory().first()
        val isBackupNotCompletedTodoKey = getIsBackupNotCompletedTodo().first()
        val isBackupCompletedTodoKey = getIsBackupCompletedTodo().first()
        val backupPath = getBackupPath().first()
        val repeatAutoBackupTimeAtHours = getAutoBackupTime().first()
        _settingsBackup.emit(BackupSettings(
            isEnableLocalBackup,
            isEnableGDriveBackup,
            isBackupNotEncryptedNoteKey,
            isBackupEncryptedNoteKey,
            isBackupNoteImagesKey,
            isBackupNoteAudioKey,
            isBackupNoteCategoryKey,
            isBackupNotCompletedTodoKey,
            isBackupCompletedTodoKey,
            backupPath,
            repeatAutoBackupTimeAtHours
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

    override suspend fun updateIsEnableLocalBackup(newState: Boolean) {
        context.dataStore.edit {
            it[isEnableLocalBackup] = newState
        }
        notifySettingsChanges {
            it.copy(isEnableLocalBackup = newState)
        }
    }

    override suspend fun updateIsEnableGDriveBackup(newState: Boolean) {
        context.dataStore.edit {
            it[isEnableGDriveBackup] = newState
        }
        notifySettingsChanges {
            it.copy(isEnableGDriveBackup = newState)
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

    override suspend fun updateBackupPath(newPath: String?) {
        if(newPath != null) {
            context.dataStore.edit {
                it[backupPathKey] = newPath
            }
        }
        else {
            context.dataStore.edit {
                it.remove(backupPathKey)
            }
        }
        notifySettingsChanges {
            it.copy(backupPath = newPath)
        }
    }

    override suspend fun changeAutoBackupTime(newTime: Long) {
        context.dataStore.edit {
            it[repeatAutoBackupTimeAtHours] = newTime
        }
        notifySettingsChanges {
            it.copy(repeatAutoBackupTimeAtHours = newTime)
        }
    }

    private fun getIsEnableLocalBackup() : Flow<Boolean> {
        return context.dataStore.data.map {
            it[isEnableLocalBackup] ?: false
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

    private fun getBackupPath() : Flow<String?> {
        return context.dataStore.data.map {
            it[backupPathKey]
        }
    }

    private fun getAutoBackupTime() : Flow<Long> {
        return context.dataStore.data.map {
            it[repeatAutoBackupTimeAtHours] ?: 5
        }
    }

    private fun getIsEnableGDriveBackup() : Flow<Boolean> {
        return context.dataStore.data.map {
            it[isEnableGDriveBackup] ?: false
        }
    }
}