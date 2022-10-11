package com.xxmrk888ytxx.privatenote.domain.presentation.Screen.EditNoteScreen

import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.Utils.Exception.FailedDecryptException
import com.xxmrk888ytxx.privatenote.Utils.LifeCycleState
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Note
import com.xxmrk888ytxx.privatenote.domain.InputHistoryManager.InputHistoryManager
import com.xxmrk888ytxx.privatenote.domain.MainDispatcherRule
import com.xxmrk888ytxx.privatenote.domain.PlayerManager.PlayerManager
import com.xxmrk888ytxx.privatenote.domain.RecordManager.RecordManager
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.Audio
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.AudioRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.CategoryRepository.CategoryRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.ImageRepository.ImageRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.NoteReposiroty.NoteRepository
import com.xxmrk888ytxx.privatenote.domain.SecurityUtils.SecurityUtils
import com.xxmrk888ytxx.privatenote.domain.ToastManager.ToastManager
import com.xxmrk888ytxx.privatenote.domain.UseCases.ExportAudioUseCase.ExportAudioUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.ExportImageUseCase.ExportImageUseCase
import com.xxmrk888ytxx.privatenote.presentation.Screen.EditNoteScreen.EditNoteViewModel
import com.xxmrk888ytxx.privatenote.presentation.Screen.EditNoteScreen.States.ShowDialogState
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EditNoteViewModelTest {
    lateinit var viewModel: EditNoteViewModel
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    private val noteRepository: NoteRepository = mockk(relaxed = true)
    private val categoryRepository: CategoryRepository = mockk(relaxed = true)
    private val securityUtils: SecurityUtils = mockk(relaxed = true)
    private val toastManager: ToastManager = mockk(relaxed = true)
    private val lifeCycleState: MutableStateFlow<LifeCycleState> = MutableStateFlow(LifeCycleState.onResume)
    private val inputHistoryManager: InputHistoryManager = mockk(relaxed = true)
    private val analytics: AnalyticsManager = mockk(relaxed = true)
    private val recordManager: RecordManager = mockk(relaxed = true)
    private val playerManager: PlayerManager = mockk(relaxed = true)
    private val audioRepository: AudioRepository = mockk(relaxed = true)
    private val imageRepository: ImageRepository = mockk(relaxed = true)
    private val exportImageUseCase : ExportImageUseCase = mockk(relaxed = true)
    private val exportAudioUseCase : ExportAudioUseCase = mockk(relaxed = true)

    @Before
    fun init() {
        viewModel = EditNoteViewModel(
            noteRepository,
            categoryRepository,
            securityUtils,
            toastManager,
            lifeCycleState,
            inputHistoryManager,
            analytics,
            recordManager,
            playerManager,
            audioRepository,
            imageRepository,
            exportImageUseCase,
            exportAudioUseCase
        )
    }

    @After
    fun restore() = runTest {
        lifeCycleState.emit(LifeCycleState.onResume)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test init viewModel expect temps dir has been cleared`() = runTest {
        delay(100)

        coVerify(exactly = 1) {
            imageRepository.clearTempDir()
            audioRepository.clearTempDir()
        }
    }

    @Test
    fun `test init viewModel if lifecycle state is onPause and note not encrypt and noteID not 0 expect saveNote and stopRecord`() = runTest {
        val note = Note(4,"test,","test")

        every { noteRepository.getNoteById(note.id) } returns flowOf(note)
        viewModel.getNote(4)
        viewModel.titleTextField.value = "rtefg"

        lifeCycleState.emit(LifeCycleState.onPause)
        delay(100)

        verify(exactly = 1) {
            viewModel.stopRecord()
            noteRepository.insertNote(allAny())
        }
    }

    @Test
    fun `test init viewModel if lifecycle state is onPause and note encrypt and noteID not 0 expect saveNote and stopRecord`() = runTest {
        val note = Note(4,"h","", isEncrypted = true)

        every { noteRepository.getNoteById(note.id) } returns flowOf(note)
        viewModel.getNote(4)
        viewModel.titleTextField.value = "rtefg"

        lifeCycleState.emit(LifeCycleState.onPause)
        delay(100)

        Assert.assertEquals(ShowDialogState.DecryptDialog,viewModel.dialogShowState.value)
    }

    @Test
    fun `test showPlayerDialog expect change properties`() {
        val audio = Audio(
            4,
            file = mockk(relaxed = true),
            duration = 10
        )

        viewModel.showPlayerDialog(audio)

        Assert.assertEquals(Pair(true,audio),viewModel.getPlayerDialogState().value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test hidePlayerDialog expect change properties`() = runTest {
        val audio = Audio(
            4,
            file = mockk(relaxed = true),
            duration = 10
        )

        viewModel.showPlayerDialog(audio)
        Assert.assertEquals(Pair(true,audio),viewModel.getPlayerDialogState().value)
        viewModel.hidePlayerDialog()
        delay(100)

        Assert.assertEquals(Pair(false,null),viewModel.getPlayerDialogState().value)
        coVerify {
            playerManager.resetPlayer()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test saveNote if note not changes expect note not save`() = runTest {
        val note = Note(4,"h","", isEncrypted = true)

        every { noteRepository.getNoteById(note.id) } returns flowOf(note)
        viewModel.getNote(4)

        lifeCycleState.emit(LifeCycleState.onPause)
        delay(100)

        verify(exactly = 0) {
            noteRepository.insertNote(any())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test saveNote if note have changes expect note not save`() = runTest {
        val note = Note(4,"h","", isEncrypted = false)

        every { noteRepository.getNoteById(note.id) } returns flowOf(note)
        viewModel.getNote(4)
        viewModel.titleTextField.value = "rtefg"
        lifeCycleState.emit(LifeCycleState.onPause)
        delay(100)

        verify(exactly = 1) {
            noteRepository.insertNote(any())
        }
    }

    @Test
    fun `test changeStateToEncryptNote expect note is encrypt`() {
        val password = "passtest"

        viewModel.changeStateToEncryptNote(password)

        verify(exactly = 1) {
            securityUtils.passwordToHash(password)
            toastManager.showToast(R.string.Note_encrypted)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test saveNote if remove note expect invoke remove note methods`() = runTest {
        val note = Note(4,"h","", isEncrypted = false)

        every { noteRepository.getNoteById(note.id) } returns flowOf(note)
        viewModel.getNote(4)
        viewModel.titleTextField.value = "rtefg"
        viewModel.removeNote(mockk(relaxed = true))
        lifeCycleState.emit(LifeCycleState.onPause)
        delay(100)

        coVerify(exactly = 1) {
            noteRepository.removeNote(note.id)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test saveNote if notSaveMode note expect invoke restore primaryVersion`() = runTest {
        val note = Note(4,"h","", isEncrypted = false)

        every { noteRepository.getNoteById(note.id) } returns flowOf(note)
        viewModel.getNote(4)
        viewModel.titleTextField.value = "rtefg"
        viewModel.notSaveChanges(mockk(relaxed = true))
        lifeCycleState.emit(LifeCycleState.onPause)
        delay(100)

        coVerify(exactly = 1) {
            noteRepository.insertNote(note)
        }
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test saveNote if not save changes in new note expect invoke this not save`() = runTest {
        val note = Note(0,"h","", isEncrypted = false)

        every { noteRepository.getNoteById(note.id) } returns flowOf(note)
        viewModel.getNote(0)
        viewModel.titleTextField.value = "rtefg"
        viewModel.removeNote(mockk(relaxed = true))
        lifeCycleState.emit(LifeCycleState.onPause)
        delay(100)

        coVerify(exactly = 0) {
            noteRepository.removeNote(any())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test decrypt input password expect invoke decrypt and load file methods`()  = runTest {
        val note = Note(1,"h","", isEncrypted = true)
        every { noteRepository.getNoteById(note.id) } returns flowOf(note)
        viewModel.getNote(1)
        val password = "test"

        viewModel.decrypt(password)
        delay(100)

        coVerify(exactly = 1) {
            imageRepository.loadImagesInBuffer(any())
            audioRepository.loadAudioInBuffer(any())
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test(expected = FailedDecryptException::class)
    fun `test decrypt input password expect throws exception`()  = runTest {
        val note = Note(1,"h","", isEncrypted = true)
        every { noteRepository.getNoteById(note.id) } returns flowOf(note)
        every { securityUtils.decrypt(any(),any()) } throws FailedDecryptException("Invalid password")
        viewModel.getNote(1)
        val password = "test"

        viewModel.decrypt(password)
        delay(100)
    }

    @Test
    fun `test startRecord expect invoke recorder start method`() {

        viewModel.startRecord()

        coVerify(exactly = 1) { recordManager.startRecord(any(),any()) }
    }

    @Test
    fun `test stopRecord expect invoke recorder stop method`() {

        viewModel.stopRecord()

        coVerify(exactly = 1) { recordManager.stopRecord(any()) }
    }

}