@file:OptIn(ExperimentalCoroutinesApi::class)

package com.xxmrk888ytxx.privatenote.domain.presentation.Screen.MainScreen.NoteState

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.domain.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.Utils.Const
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Category
import com.xxmrk888ytxx.privatenote.domain.MainDispatcherRule
import com.xxmrk888ytxx.privatenote.domain.Repositories.CategoryRepository.CategoryRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.NoteReposiroty.NoteRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
import com.xxmrk888ytxx.privatenote.domain.ToastManager.ToastManager
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.MainScreenController
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.NoteState.NoteScreenMode
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.NoteState.NoteStateViewModel
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NoteStateViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    lateinit var viewModel: NoteStateViewModel
    private val noteRepository:NoteRepository = mockk(relaxed = true)
    private val toastManager:ToastManager = mockk(relaxed = true)
    private val categoryRepository = mockk<CategoryRepository>(relaxed = true)
    private val analytics = mockk<AnalyticsManager>(relaxed = true)
    private val settingsRepository:SettingsRepository = mockk(relaxed = true)
    @Before
    fun init() {
        viewModel = NoteStateViewModel(noteRepository,toastManager,categoryRepository,analytics,settingsRepository)
    }

    @Test
    fun `test setMainScreenController send not null controller expect setup floatButton settings`() {
        val mainScreenController:MainScreenController = mockk(relaxed = true)

        viewModel.setMainScreenController(mainScreenController)

        verifySequence {
            mainScreenController.setFloatButtonOnClickListener(any(),any())
        }
    }

    @Test
    fun `test removeNote send note id expect call repository from delete`() = runTest {
        val id = 3

        viewModel.removeNote(id)
        delay(250)

        coVerifySequence {
            noteRepository.removeNote(id)
        }
    }

    @Test
    fun `test toSelectionMode expect mode be changed`() {
        val mainScreenController:MainScreenController = mockk(relaxed = true)
        viewModel.setMainScreenController(mainScreenController)

        viewModel.toSelectionMode()

        verify(exactly = 1) {
            viewModel.getCurrentMode().value = NoteScreenMode.SelectionScreenMode
            mainScreenController.changeBottomBarVisibleStatus(false)
            mainScreenController.changeScrollBetweenScreenState(false)
        }
    }

    @Test
    fun `test toDefaultMode expect mode be changed`() {
        val mainScreenController:MainScreenController = mockk(relaxed = true)
        viewModel.setMainScreenController(mainScreenController)

        viewModel.toDefaultMode()

        verify(exactly = 1) {
            viewModel.getCurrentMode().value = NoteScreenMode.Default
            mainScreenController.changeBottomBarVisibleStatus(true)
            mainScreenController.changeScrollBetweenScreenState(true)
        }
    }

    @Test
    fun `test toSearchMode expect mode be changed`() {
        val mainScreenController:MainScreenController = mockk(relaxed = true)
        viewModel.setMainScreenController(mainScreenController)

        viewModel.toSearchMode()

        verify(exactly = 1) {
            viewModel.getCurrentMode().value = NoteScreenMode.SearchScreenMode
            mainScreenController.changeBottomBarVisibleStatus(false)
            mainScreenController.changeScrollBetweenScreenState(false)
        }
    }

    @Test
    fun `test showCategoryList expect same properties has been changed`() {
        val mainScreenController:MainScreenController = mockk(relaxed = true)
        viewModel.setMainScreenController(mainScreenController)

        Assert.assertEquals(NoteScreenMode.Default,viewModel.getCurrentMode().value)
        viewModel.showCategoryList()

        Assert.assertEquals(NoteScreenMode.ShowCategoryMenu,viewModel.getCurrentMode().value)
    }

    @Test
    fun `test hideCategoryList expect same properties has been changed`() {
        val mainScreenController:MainScreenController = mockk(relaxed = true)
        viewModel.setMainScreenController(mainScreenController)

        Assert.assertEquals(NoteScreenMode.Default,viewModel.getCurrentMode().value)
        viewModel.showCategoryList()
        Assert.assertEquals(NoteScreenMode.ShowCategoryMenu,viewModel.getCurrentMode().value)
        viewModel.hideCategoryList()

        Assert.assertEquals(NoteScreenMode.Default,viewModel.getCurrentMode().value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test saveCategory if categoryId Not 0 expect invoke insert repository method`() = runTest {
        val id = 4

        viewModel.saveCategory("test", Color.Black,id)
        delay(100)

        coVerifySequence {
            categoryRepository.updateCategory(any())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test saveCategory if categoryId 0 expect invoke insert repository method`() = runTest {
        val id = 0

        viewModel.saveCategory("test", Color.Black,id)
        delay(100)

        coVerifySequence {
            categoryRepository.insertCategory(any())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test removeCategory input category not in filter expect call repository remove method and not change filter`() = runTest {
        val context = mockk<Context>(relaxed = true)
        val categoryID = 4

        viewModel.removeCategory(Category(categoryId = categoryID,categoryName = "test"),context)
        delay(100)

        coVerifySequence {
            categoryRepository.removeCategory(categoryID)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test removeCategory input category in filter expect call repository remove method and set default value in filter`() = runTest {
        val context = mockk<Context>(relaxed = true)
        val categoryID = 4
        viewModel.changeCategoryFilterStatus(4)
        Assert.assertEquals(categoryID,viewModel.getCategoryFilterStatus().value)

        viewModel.removeCategory(Category(categoryId = categoryID,categoryName = "test"),context)
        delay(100)

        Assert.assertEquals(Const.IGNORE_CATEGORY,viewModel.getCategoryFilterStatus().value)
        coVerifySequence {
            categoryRepository.removeCategory(categoryID)
        }
    }

    @Test
    fun `test getDefaultTitle if sent IGNORE_CATEGORY expect return stringId`() {
        val mode = Const.IGNORE_CATEGORY
        val context = mockk<Context>(relaxed = true)
        val expectedString = "All notes"
        every { context.getString(R.string.All_Notes) } returns expectedString

        val text = viewModel.getDefaultTitle(context,mode)

        Assert.assertEquals(expectedString,text)
    }

    @Test
    fun `test getDefaultTitle if sent CHOSEN_ONLY expect return stringId`() {
        val mode = Const.CHOSEN_ONLY
        val context = mockk<Context>(relaxed = true)
        val expectedString = "Favorite"
        every { context.getString(R.string.Chosen) } returns expectedString

        val text = viewModel.getDefaultTitle(context,mode)

        Assert.assertEquals(expectedString,text)
    }

    @Test
    fun `test getDefaultTitle if sent Unknown id expect return empty string`() {
        val mode = 100
        val context = mockk<Context>(relaxed = true)

        val text = viewModel.getDefaultTitle(context,mode)

        Assert.assertEquals("",text)
    }

    @Test
    fun `test changeChosenStatus send id and currentState(true) expect inversion state and invoke repository method`() = runTest {
        val currentState = true
        val id = 5

        viewModel.changeChosenStatus(id,currentState)

        delay(100)
        coVerify(exactly = 1) {
            noteRepository.changeChosenStatus(!currentState,id)
        }
    }

    @Test
    fun `test changeChosenStatus send id and currentState(false) expect inversion state and invoke repository method`() = runTest {
        val currentState = false
        val id = 1

        viewModel.changeChosenStatus(id,currentState)

        delay(100)
        coVerifySequence {
            noteRepository.changeChosenStatus(!currentState,id)
        }
    }
}