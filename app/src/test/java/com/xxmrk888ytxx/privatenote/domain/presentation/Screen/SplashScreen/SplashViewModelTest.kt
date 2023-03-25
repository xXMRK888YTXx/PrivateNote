package com.xxmrk888ytxx.privatenote.domain.presentation.Screen.SplashScreen

import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.domain.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
import com.xxmrk888ytxx.privatenote.domain.SecurityUtils.SecurityUtils
import com.xxmrk888ytxx.privatenote.domain.ToastManager.ToastManager
import com.xxmrk888ytxx.privatenote.presentation.Screen.Screen
import com.xxmrk888ytxx.privatenote.presentation.Screen.SplashScreen.SplashViewModel
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SplashViewModelTest {
    private lateinit var viewModel: SplashViewModel
    private val settingsRepository:SettingsRepository = mockk(relaxed = true)
    private val securityUtils:SecurityUtils = mockk(relaxed = true)
    private val toastManager:ToastManager = mockk(relaxed = true)
    private val analytics: AnalyticsManager = mockk(relaxed = true)
    @Before
    fun init() {
        viewModel = SplashViewModel(settingsRepository,securityUtils,toastManager,analytics)
    }

    @Test
    fun test_showToastForLeaveApp_Expect_Show_Toast() {
        viewModel.showToastForLeaveApp()

        verifySequence {
            toastManager.showToast(R.string.Press_again_to_exit)
        }
    }

    @Test
    fun test_checkPassword_Input_Password_Expect_Returns_True() = runBlocking {
        val password = "TestPass"
        every { securityUtils.passwordToHash(password,0) } returns password
        coEvery { settingsRepository.checkAppPassword(password) } returns true

        val result =  viewModel.checkPassword(password)

        Assert.assertEquals(true,result)
    }

    @Test
    fun test_checkPassword_Input_Password_Expect_Returns_False() = runBlocking {
        val password = "TestPass"
        every { securityUtils.passwordToHash(password,0) } returns password
        coEvery { settingsRepository.checkAppPassword(password) } returns false

        val result =  viewModel.checkPassword(password)

        Assert.assertEquals(false,result)
    }

    @Test
    fun test_checkPassword_Input_Password_Expect_Throw_Exception_In_Method_And_Return_False() = runBlocking {
        val password = "TestPass"
        every { securityUtils.passwordToHash(password,0) } throws Exception()
        coEvery { settingsRepository.checkAppPassword(password) } returns true

        val result = viewModel.checkPassword(password)

        Assert.assertEquals(false,result)
    }
}