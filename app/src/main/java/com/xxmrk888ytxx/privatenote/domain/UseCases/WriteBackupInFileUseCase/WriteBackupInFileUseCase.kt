package com.xxmrk888ytxx.privatenote.domain.UseCases.WriteBackupInFileUseCase

import android.content.Context

interface WriteBackupInFileUseCase {
    suspend fun execute(jsonBackupString:String,path:String) : Boolean
}