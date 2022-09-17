package com.xxmrk888ytxx.privatenote.domain.presentation.Screen.DrawScreen

import android.graphics.Bitmap
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.domain.MainDispatcherRule
import com.xxmrk888ytxx.privatenote.domain.Repositories.ImageRepository.ImageRepository
import com.xxmrk888ytxx.privatenote.domain.ToastManager.ToastManager
import com.xxmrk888ytxx.privatenote.presentation.Screen.DrawScreen.DrawViewModel
import io.ak1.drawbox.DrawController
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DrawViewModelTest {
    private lateinit var viewModel:DrawViewModel
    private val imageRepository: ImageRepository = mockk(relaxed = true)
    private val toastManager:ToastManager = mockk(relaxed = true)

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun init() {
        viewModel = DrawViewModel(toastManager,imageRepository)
    }

    @Test
    fun `test getController single call and send DrawController expect return this DrawController`() {
        val drawController = mockk<DrawController>(relaxed = true)

        val returnedController = viewModel.getController(drawController)

        Assert.assertSame(drawController,returnedController.value)
    }

    @Test
    fun `test getController multi call and send DrawControllers expect return first DrawController`() {
        val drawController = mockk<DrawController>(relaxed = true)
        val drawController2 = mockk<DrawController>(relaxed = true)

        viewModel.getController(drawController)
        val returnedController = viewModel.getController(drawController2)

        Assert.assertSame(drawController,returnedController.value)
    }

}