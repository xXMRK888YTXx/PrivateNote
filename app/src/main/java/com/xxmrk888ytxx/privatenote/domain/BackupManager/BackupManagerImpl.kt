package com.xxmrk888ytxx.privatenote.domain.BackupManager

import android.content.Context
import com.xxmrk888ytxx.privatenote.Utils.Exception.NotSetBackupPathException
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsBackupRepository.SettingsBackupRepository
import kotlinx.coroutines.flow.first

class BackupManagerImpl constructor(
    private val settingsBackupRepository: SettingsBackupRepository,
    private val context:Context
) : BackupManager {

    @Throws(
        NotSetBackupPathException::class
    )
    override suspend fun createBackup() {
        val settings = settingsBackupRepository.getBackupSettings().first()
        if(settings.isEnableBackup) return
        if(settings.backupPath == null) throw NotSetBackupPathException()

    }
}