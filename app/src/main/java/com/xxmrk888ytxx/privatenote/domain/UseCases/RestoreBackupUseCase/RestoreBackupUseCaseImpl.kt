package com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreBackupUseCase

import com.squareup.moshi.Moshi
import com.xxmrk888ytxx.privatenote.Utils.Exception.ConvertBackupFileToDataException
import com.xxmrk888ytxx.privatenote.Utils.Exception.RestoreBackupException
import com.xxmrk888ytxx.privatenote.domain.BackupManager.BackupDataModel
import com.xxmrk888ytxx.privatenote.domain.BackupManager.BackupRestoreSettings
import com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreCategoryFromUseCase.RestoreCategoryFromBackupUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreNoteFromBackupUseCase.RestoreNoteFromBackupUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.RestoreTodoFromUseCase.RestoreTodoFromUseCase
import java.io.File
import java.io.FileInputStream
import java.nio.charset.Charset
import javax.inject.Inject

class RestoreBackupUseCaseImpl @Inject constructor(
    private val restoreCategoryFromBackupUseCase: RestoreCategoryFromBackupUseCase,
    private val restoreNoteFromBackupUseCase: RestoreNoteFromBackupUseCase,
    private val restoreTodoFromUseCase: RestoreTodoFromUseCase,
) : RestoreBackupUseCase {
    private fun getJsonDataFile(backupDir:File) = File(backupDir,"BackupData.json")
    private fun getImageBackupDir(backupDir:File) = File(backupDir,"Notes")
    private fun getAudioBackupDir(backupDir:File) = File(backupDir,"Audios")

    override suspend fun execute(backupDir: File, settings: BackupRestoreSettings) {
        val backupModel = getBackupModel(backupDir)

        if(settings.restoreCategory) {
            restoreCategoryFromBackupUseCase.execute(backupModel.category)
        }
        if(settings.restoreTodo) {
            restoreTodoFromUseCase.execute(backupModel.todo)
        }
        if(settings.restoreNotes) {
            restoreNoteFromBackupUseCase.execute(backupModel.notes,getImageBackupDir(backupDir),
                getAudioBackupDir(backupDir))
        }
    }

    private suspend fun getBackupModel(backupDir: File): BackupDataModel {
        val jsonDataFile = getJsonDataFile(backupDir)
        if(!jsonDataFile.exists()) throw RestoreBackupException("BackupData.json not exist")
        val stream = FileInputStream(jsonDataFile)
        val bytes = stream.readBytes()
        stream.close()
        return jsonToModel(bytes.toString(Charset.defaultCharset()))
    }

    private fun jsonToModel(jsonString: String) : BackupDataModel {
        try {
            val moshi = Moshi.Builder().build()
            val adapter = moshi.adapter(BackupDataModel::class.java).lenient()
            return adapter.fromJson(jsonString)!!
        }catch (e:Exception) {
            throw ConvertBackupFileToDataException()
        }
    }
}