package com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.xxmrk888ytxx.privatenote.Utils.Exception.InvalidPasswordException
import com.xxmrk888ytxx.privatenote.Utils.LanguagesCodes.SYSTEM_LANGUAGE_CODE
import com.xxmrk888ytxx.privatenote.Utils.getData
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.models.SortNoteState
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class SettingsRepositoryImpl (
    private val context: Context
) : SettingsRepository {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private val toDoWithDateVisibleKey = booleanPreferencesKey("ToDoWithDateVisibleKey")
    private val completedToDoVisible = booleanPreferencesKey("CompletedToDoVisible")
    private val toDoWithoutDateVisible = booleanPreferencesKey("ToDoWithoutDateVisible")
    private val missedToDoVisible = booleanPreferencesKey("MissedToDoVisible")
    private val navigationSwipeState = booleanPreferencesKey("NavigationSwipeState")
    private val splashScreenVisibleState = booleanPreferencesKey("SplashScreenVisibleState")
    private val appLanguage = stringPreferencesKey("AppLanguage")
    private val appPassword = stringPreferencesKey("AppId")
    private val biometricAuthorizationState = booleanPreferencesKey("BiometricAuthorizationState")
    private val lockWhenLeaveState = booleanPreferencesKey("LockWhenLeaveState")
    private val lockWhenLeaveTime = intPreferencesKey("lockWhenLeaveTime")
    private val saveLockTime = longPreferencesKey("saveLockTime")
    private val applicationTheme = intPreferencesKey("ApplicationTheme")
    private val dontKillMyAppState = booleanPreferencesKey("dontKillMyAppState")
    private val policyAndTermsDialog = booleanPreferencesKey("PolicyAndTermsDialog")
    private val sortNoteStateKey = intPreferencesKey("sortNoteState")

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

    override fun getSplashScreenVisibleState(): Flow<Boolean> = runBlocking(Dispatchers.IO) {
        return@runBlocking context.dataStore.data.map {
            it[splashScreenVisibleState] ?: true
        }
    }

    override suspend fun setSplashScreenVisibleState(state: Boolean) {
        context.dataStore.edit {
            it[splashScreenVisibleState] = state
        }
    }

    override fun getAppLanguage(): Flow<String> = runBlocking(Dispatchers.IO) {
       return@runBlocking  context.dataStore.data.map {
            it[appLanguage] ?: SYSTEM_LANGUAGE_CODE
        }
    }

    override suspend fun setAppLanguage(languageCode: String) {
        context.dataStore.edit {
            it[appLanguage] = languageCode
        }
    }

    override fun isAppPasswordEnable(): Flow<Boolean> = runBlocking(Dispatchers.IO) {
        return@runBlocking context.dataStore.data.map {
            it[appPassword] != null
        }

    }

    override suspend fun setupAppPassword(password: String) {
        context.dataStore.edit {
            it[appPassword] = password
        }
    }

    override suspend fun removeAppPassword(passwordHash: String) {
        if(!checkAppPassword(passwordHash)) throw InvalidPasswordException("")
        context.dataStore.edit {
            it.remove(appPassword)
            it.remove(biometricAuthorizationState)
            it.remove(lockWhenLeaveState)
            it.remove(lockWhenLeaveTime)
        }
    }

    override suspend fun checkAppPassword(enterPassword: String) : Boolean {
        val password =  context.dataStore.data.map {
            it[appPassword]
        }.getData()

        return password == enterPassword
    }

    override fun getBiometricAuthorizationState(): Flow<Boolean> = runBlocking(Dispatchers.IO) {
       return@runBlocking context.dataStore.data.map {
            it[biometricAuthorizationState] ?: false
        }
    }

    override suspend fun setBiometricAuthorizationState(state: Boolean) {
        context.dataStore.edit {
            it[biometricAuthorizationState] = state
        }
    }

    override fun getLockWhenLeaveState(): Flow<Boolean> = runBlocking(Dispatchers.IO) {
       return@runBlocking context.dataStore.data.map {
            it[lockWhenLeaveState] ?: false
        }
    }

    override suspend fun setLockWhenLeaveState(state: Boolean) {
        context.dataStore.edit {
            it[lockWhenLeaveState] = state
        }
    }

    override fun getLockWhenLeaveTime(): Flow<Int> = runBlocking(Dispatchers.IO) {
       return@runBlocking context.dataStore.data.map {
            it[lockWhenLeaveTime] ?: 1000
        }
    }

    override suspend fun setLockWhenLeaveTime(time: Int) {
        context.dataStore.edit {
            it[lockWhenLeaveTime] = time
        }
    }

    override suspend fun setSaveLockTime(time: Long?) {
        context.dataStore.edit {
            if(time != null)
            it[saveLockTime] = time
            else it.remove(saveLockTime)
        }
    }

    override fun getSaveLockTime(): Flow<Long?> = runBlocking(Dispatchers.IO) {
        context.dataStore.data.map {
           it[saveLockTime]
        }
    }

    override fun getApplicationThemeId(): Flow<Int> = runBlocking(Dispatchers.IO) {
        context.dataStore.data.map {
            it[applicationTheme] ?: ThemeManager.SYSTEM_THEME
        }
    }

    override suspend fun setApplicationThemeId(themeId: Int) {
        context.dataStore.edit {
            it[applicationTheme] = themeId
        }
    }

    override fun getDontKillMyAppDialogState(): Flow<Boolean> {
        return context.dataStore.data.map {
            it[dontKillMyAppState] ?: true
        }
    }

    override suspend fun hideDontKillMyAppDialogForever() {
        context.dataStore.edit {
            it[dontKillMyAppState] = false
        }
    }

    override fun getPolicyAndTermsDialogState(): Flow<Boolean> {
        return context.dataStore.data.map {
            it[policyAndTermsDialog] ?: false
        }
    }

    override suspend fun disablePolicyAndTermsDialogState() {
        context.dataStore.edit {
            it[policyAndTermsDialog] = true
        }
    }

    override suspend fun changeSortNoteState(sortNoteState: SortNoteState) {
        context.dataStore.edit {
            it[sortNoteStateKey] = sortNoteState.id
        }
    }

    override fun getSortNoteState(): Flow<SortNoteState> {
        return context.dataStore.data.map {
            it[sortNoteStateKey] ?: SortNoteState.ByDescending.id
        }.map {
            when(it) {
                SortNoteState.ByDescending.id -> SortNoteState.ByDescending
                SortNoteState.ByAscending.id -> SortNoteState.ByAscending
                else -> error("Incorrect sort id")
            }
        }
    }
}