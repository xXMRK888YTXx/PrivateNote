package com.xxmrk888ytxx.privatenote.domain.presentation.Screen.SettingsViewModel

import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.Utils.Exception.InvalidPasswordException
import com.xxmrk888ytxx.privatenote.domain.BiometricAuthorizationManager.BiometricAuthorizationManager
import com.xxmrk888ytxx.privatenote.domain.MainDispatcherRule
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
import com.xxmrk888ytxx.privatenote.domain.SecurityUtils.SecurityUtils
import com.xxmrk888ytxx.privatenote.domain.ToastManager.ToastManager
import com.xxmrk888ytxx.privatenote.presentation.Screen.SettingsScreen.SettingsViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {
    lateinit var viewModel: SettingsViewModel
    private val settingsRepository: SettingsRepository = mockk(relaxed = true)
    private val securityUtils: SecurityUtils = mockk(relaxed = true)
    private val toastManager: ToastManager = mockk(relaxed = true)
    private val authorizationManager: BiometricAuthorizationManager = mockk(relaxed = true)

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun init() {
        viewModel = SettingsViewModel(
            settingsRepository,
            securityUtils,
            authorizationManager,
        )
    }

    @Test
    fun test_leaveFromSettingScreen_Expect_Invoke_NavController_NavigateUp_Method() {
        val navController = mockk<NavController>(relaxed = true)

        viewModel.leaveFromSettingScreen(navController)

        verify(exactly = 1) {
            navController.navigateUp()
        }
    }

    @Test
    fun `test getEnterAppPasswordDialogState show&hideEnterAppPasswordDialog change state on true and return to false`() {
        Assert.assertEquals(false, viewModel.getEnterAppPasswordDialogState().value)

        viewModel.showEnterAppPasswordDialog()
        Assert.assertEquals(true, viewModel.getEnterAppPasswordDialogState().value)
        viewModel.hideEnterAppPasswordDialog()
        Assert.assertEquals(false, viewModel.getEnterAppPasswordDialogState().value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test disableAppPassword input password expect method completed without exception`() =
        runTest {
            val password = "testPass"
            every { securityUtils.passwordToHash(password, 0) } returns password
            coEvery { settingsRepository.removeAppPassword(password) } just Runs

            viewModel.disableAppPassword(password)

            coVerify {
                securityUtils.passwordToHash(password, 0)
                settingsRepository.removeAppPassword(password)
                viewModel.hideEnterAppPasswordDialog()
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test(expected = InvalidPasswordException::class)
    fun `test disableAppPassword input password expect method completed with exception`() =
        runTest() {
            coEvery { settingsRepository.removeAppPassword(any()) } throws InvalidPasswordException(
                ""
            )

            viewModel.disableAppPassword("qw")
        }

    @Test
    fun `test getShowAppPasswordState show&hideAppPasswordDialog change state on true and return to false`() {
        Assert.assertEquals(false, viewModel.getShowAppPasswordState().value)

        viewModel.showAppPasswordDialog()
        Assert.assertEquals(true, viewModel.getShowAppPasswordState().value)
        viewModel.hideAppPasswordDialog()
        Assert.assertEquals(false, viewModel.getShowAppPasswordState().value)
    }
}