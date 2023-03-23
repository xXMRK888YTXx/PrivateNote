package com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository

import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.models.SortNoteState
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.NoteState.models.ViewNoteListState
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

    fun getSplashScreenVisibleState() : Flow<Boolean>

    suspend fun setSplashScreenVisibleState(state: Boolean)

    fun isAppPasswordEnable() : Flow<Boolean>

    suspend fun setupAppPassword(password:String)

    suspend fun removeAppPassword(passwordHash:String)

    suspend fun checkAppPassword(enterPassword:String) : Boolean

    fun getBiometricAuthorizationState() : Flow<Boolean>

    suspend fun setBiometricAuthorizationState(state:Boolean)

    fun getLockWhenLeaveState() : Flow<Boolean>

    suspend fun setLockWhenLeaveState(state: Boolean)

    fun getLockWhenLeaveTime() : Flow<Int>

    suspend fun setLockWhenLeaveTime(time:Int)

    suspend fun setSaveLockTime(time:Long?)

    fun getSaveLockTime() : Flow<Long?>

    fun getApplicationThemeId() : Flow<Int>

    suspend fun setApplicationThemeId(themeId:Int)

    fun getDontKillMyAppDialogState() : Flow<Boolean>

    suspend fun hideDontKillMyAppDialogForever()

    fun getPolicyAndTermsDialogState() : Flow<Boolean>

    suspend fun disablePolicyAndTermsDialogState()

    suspend fun changeSortNoteState(sortNoteState:SortNoteState)

    fun getSortNoteState() : Flow<SortNoteState>

    suspend fun changeViewNoteListState(viewNoteListState: ViewNoteListState)

    fun getViewNoteListState() : Flow<ViewNoteListState>

    fun getAdState() : Flow<Boolean>

    suspend fun setAdState(state:Boolean)
}