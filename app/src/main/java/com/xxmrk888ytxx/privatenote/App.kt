package com.xxmrk888ytxx.privatenote

import android.app.Application
import android.content.res.Configuration
import com.xxmrk888ytxx.privatenote.domain.UseCases.TodoWidgetProvideUseCase.TodoWidgetProvideUseCase
import dagger.hilt.android.HiltAndroidApp
import java.util.*
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}