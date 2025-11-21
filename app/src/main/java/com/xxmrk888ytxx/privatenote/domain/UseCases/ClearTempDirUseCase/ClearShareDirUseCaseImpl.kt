package com.xxmrk888ytxx.privatenote.domain.UseCases.ClearTempDirUseCase

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class ClearShareDirUseCaseImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ClearShareDirUseCase {
    override suspend fun execute() {
        val shareImageDir = File(context.cacheDir, "share_files")

        shareImageDir.listFiles()?.forEach {
            it.delete()
        }
    }
}