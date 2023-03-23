package com.xxmrk888ytxx.privatenote.domain.UseCases.ProvideDataFromFileUriUseCase

import android.net.Uri

interface ProvideDataFromFileUriUseCase {
    suspend fun <T> provideFromFileUri(uri: Uri?,onMapBytes:(ByteArray) -> T?) : T?
}