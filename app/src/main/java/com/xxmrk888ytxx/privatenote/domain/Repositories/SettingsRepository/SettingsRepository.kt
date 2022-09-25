package com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository

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

    fun getAppLanguage() : Flow<String>

    suspend fun setAppLanguage(languageCode:String)

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
}