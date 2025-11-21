package com.xxmrk888ytxx.privatenote.domain.UseCases.GenerateBackupFileUseCase

import android.content.Context
import com.squareup.moshi.Moshi
import com.xxmrk888ytxx.privatenote.Utils.Const.BACKUP_FILE_EXTENSION
import com.xxmrk888ytxx.privatenote.Utils.getBytes
import com.xxmrk888ytxx.privatenote.domain.BackupManager.BackupDataModel
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.AudioRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.ImageRepository.ImageRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsAutoBackupRepository.BackupSettings
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import net.lingala.zip4j.ZipFile
import java.io.File
import java.io.FileOutputStream

class GenerateBackupFileUseCase @AssistedInject constructor(
    @param:ApplicationContext private val context:Context,
    private val imageRepository: ImageRepository,
    private val audioRepository: AudioRepository,
    @Assisted tempDirName:String
) {
    private val tempBackupDir = File(context.cacheDir,tempDirName)
    private val imageTempDir = File(tempBackupDir,"Notes")
    private val audioTempDir = File(tempBackupDir,"Audios")
    private val backupDataFile = File(tempBackupDir,"BackupData.json")
    private val finalBackupFileOutputPath = File(tempBackupDir,"backup.$BACKUP_FILE_EXTENSION")

    suspend fun execute(backupModel: BackupDataModel,settings: BackupSettings): File {
        clearTempDir()
        initDirs()

        if(settings.isBackupNoteImages) {
            collectNotes(backupModel.notes.map { it.id })
        }
        if(settings.isBackupNoteAudio) {
            collectAudios(backupModel.notes.map { it.id })
        }

        createJsonDataFile(backupModel)
        createBackupArcher()

        return finalBackupFileOutputPath
    }

     fun clearTempDir() {
        if(tempBackupDir.exists()) {
            tempBackupDir.deleteRecursively()
        }
        if(finalBackupFileOutputPath.exists()) {
            finalBackupFileOutputPath.delete()
        }
    }

    private suspend fun createBackupArcher() {
        val zipFile = ZipFile(finalBackupFileOutputPath)
        zipFile.addFolder(imageTempDir)
        zipFile.addFolder(audioTempDir)
        zipFile.addFile(backupDataFile)
        zipFile.close()
    }

    private suspend fun createJsonDataFile(backupModel: BackupDataModel) {
        val jsonDataModel = parseBackupModelToJson(backupModel)
        val stream = FileOutputStream(backupDataFile)
        stream.write(jsonDataModel.toByteArray())
        stream.close()
    }

    private suspend fun collectAudios(audiosIds: List<Int>) {
        val audios = audioRepository.getAudiosForBackup(audiosIds)
        audios.forEach {
            val backupNoteAudiosDir = File(audioTempDir,it.key.toString())
            backupNoteAudiosDir.mkdir()

            it.value.forEach { audiosFile ->
                val bytes = audiosFile.file.getBytes()
                if(bytes != null) {
                    val outputStream = FileOutputStream(File(backupNoteAudiosDir,audiosFile.id.toString()))
                    outputStream.write(bytes)
                    outputStream.close()
                }
            }
        }
    }

    private suspend fun collectNotes(notesIds:List<Int>) {
        val images = imageRepository.getImagesFromBackup(notesIds)
        images.forEach {
            val backupNotesImagesDir = File(imageTempDir,it.key.toString())
            backupNotesImagesDir.mkdir()
            it.value.forEach { imageFile ->
                val bytes = imageFile.image.getBytes()
                if(bytes != null) {
                    val outputStream = FileOutputStream(File(backupNotesImagesDir,imageFile.id.toString()))
                    outputStream.write(bytes)
                    outputStream.close()
                }
            }
        }
    }

    private fun parseBackupModelToJson(backupModel:BackupDataModel) : String {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(BackupDataModel::class.java)
        return adapter.toJson(backupModel)
    }

    private fun initDirs() {
        tempBackupDir.mkdir()
        imageTempDir.mkdir()
        audioTempDir.mkdir()
    }

    @AssistedFactory
    interface Factory {
        fun create(dirName:String) : GenerateBackupFileUseCase
    }
}