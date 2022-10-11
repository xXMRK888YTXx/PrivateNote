package com.xxmrk888ytxx.privatenote.presentation.Screen.EditNoteScreen

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.security.crypto.EncryptedFile
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.xxmrk888ytxx.privatenote.presentation.Activity.MainActivity.ActivityController
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.Audio
import com.xxmrk888ytxx.privatenote.domain.RecordManager.RecordManager
import com.xxmrk888ytxx.privatenote.domain.PlayerManager.PlayerState
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Category
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Note
import com.xxmrk888ytxx.privatenote.Utils.Exception.FailedDecryptException
import com.xxmrk888ytxx.privatenote.domain.InputHistoryManager.InputHistoryManager
import com.xxmrk888ytxx.privatenote.Utils.LifeCycleState
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.Player.PlayerController
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.domain.Repositories.CategoryRepository.CategoryRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.NoteReposiroty.NoteRepository
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.SelectionCategoryDialog.SelectionCategoryController
import com.xxmrk888ytxx.privatenote.domain.Repositories.ImageRepository.Image
import com.xxmrk888ytxx.privatenote.presentation.Screen.EditNoteScreen.States.SaveNoteState
import com.xxmrk888ytxx.privatenote.presentation.Screen.EditNoteScreen.States.ShowDialogState
import com.xxmrk888ytxx.privatenote.presentation.Screen.Screen
import com.xxmrk888ytxx.privatenote.domain.SecurityUtils.SecurityUtils
import com.xxmrk888ytxx.privatenote.Utils.*
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.SELECT_IMAGE_EVENT
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.SELECT_IMAGE_EVENT_ERROR
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.SELECT_IMAGE_EVENT_OK
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.Utils.CoroutineScopes.ApplicationScope
import com.xxmrk888ytxx.privatenote.domain.PlayerManager.PlayerManager
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.AudioRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.ImageRepository.ImageRepository
import com.xxmrk888ytxx.privatenote.domain.ToastManager.ToastManager
import com.xxmrk888ytxx.privatenote.domain.UseCases.ExportAudioUseCase.ExportAudioUseCase
import com.xxmrk888ytxx.privatenote.domain.UseCases.ExportImageUseCase.ExportImageUseCase
import com.xxmrk888ytxx.privatenote.presentation.Activity.MainActivity.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val categoryRepository: CategoryRepository,
    private val securityUtils: SecurityUtils,
    private val toastManager: ToastManager,
    private val lifeCycleState: MutableStateFlow<LifeCycleState>,
    private val inputHistoryManager: InputHistoryManager,
    private val analytics: AnalyticsManager,
    private val recordManager: RecordManager,
    private val playerManager: PlayerManager,
    private val audioRepository: AudioRepository,
    private val imageRepository: ImageRepository,
    private val exportImageUseCase: ExportImageUseCase,
    private val exportAudioUseCase: ExportAudioUseCase
) : ViewModel() {

    init {
        //очистка фото заметки добавление которой небыло завершино
        viewModelScope.launch(Dispatchers.IO) {
            imageRepository.clearTempDir()
            audioRepository.clearTempDir()
        }
        viewModelScope.launch(Dispatchers.IO) {
            imageRepository.clearBufferImages()
            audioRepository.clearAudioBuffer()
        }
        //Наблюдение за жизненым циклом
        viewModelScope.launch {
            lifeCycleState.collect() {
                try {
                    if(isNotLock.first) {
                        if(it == LifeCycleState.onPause){
                            if(note.id != 0) saveNote()
                            return@collect
                        }
                        isNotLock.second()
                        isNotLock = Pair(false){}
                    }
                }catch (e:Exception) {}
                if(it == LifeCycleState.onPause) {
                    stopRecord()
                    if(saveNoteState.value == SaveNoteState.CryptSaveNote) {
                        isHideText.value = true
                        dialogShowState.value = ShowDialogState.DecryptDialog
                    }
                    if(note.id == 0) return@collect
                    saveNote()
                }
            }
        }
    }
    val titleTextField = mutableStateOf("")

    val textField = mutableStateOf("")

    val currentTime = mutableStateOf(0L)

    private var recordStopwatch:CountDownTimer? = null

    private val currentRecordTime = mutableStateOf("00:00")

    private var activityController:ActivityController? = null

    fun getCurrentRecordTime() = currentRecordTime

    private var note: Note = Note(title = "", text = "")

    //режим сохранения заметки
    private val saveNoteState = mutableStateOf<SaveNoteState>(SaveNoteState.None)

    val isDropDownMenuShow = mutableStateOf(false)
        //состояние показа диалоговых окон
    val dialogShowState = mutableStateOf<ShowDialogState>(ShowDialogState.None)

    private var notePassword:String? = null

    val isHideText = mutableStateOf(false)

    private var isHaveImages = false

    private var isHaveAudio = false

    private val isShowRemoveImageDialog = mutableStateOf(Pair(false){})

    private val playerDialogState = mutableStateOf(Pair<Boolean, Audio?>(false,null))

    private val audioRemoveDialogState = mutableStateOf(Pair<Boolean,() -> Unit>(false,{}))

    private val addAudioDropDownState = mutableStateOf(false)

    fun addAudioDropDownState() = addAudioDropDownState.toState()

    fun showAddAudioDropDown() {
        addAudioDropDownState.value = true
    }

    fun hideAddAudioDropDown() {
        addAudioDropDownState.value = false
    }

    fun getAudioRemoveDialogState() = audioRemoveDialogState

    fun showAudioRemoveDialogState(audioId:Long) {
        audioRemoveDialogState.value = Pair(true) {
            viewModelScope.launch(Dispatchers.IO) {
                audioRepository.removeAudio(note.id,audioId)
            }
        }
    }

    fun hideAudioRemoveDialog() {
        audioRemoveDialogState.value = Pair(false){}
    }

    fun getPlayerDialogState() = playerDialogState

    fun showPlayerDialog(audio: Audio) {
        playerDialogState.value = Pair(true,audio)
    }

    fun hidePlayerDialog() {
        playerDialogState.value = Pair(false,null)
        viewModelScope.launch(Dispatchers.IO) {
            playerManager.resetPlayer()
        }
    }

    fun getShowRemoveImageState() = isShowRemoveImageDialog

    fun showRemoveImageDialog(imageId: Long) {
        isShowRemoveImageDialog.value = Pair(true) {
            removeImage(imageId)
        }
    }

    fun hideRemoveImageDialog() {
        isShowRemoveImageDialog.value = Pair(false){}
    }

    private var primaryNoteVersion:Note? = null

    private var currentCategory:MutableState<Category?> = mutableStateOf(null)

    val isChosenNoteState = mutableStateOf(false)

    private val currentSelectedCategory = mutableStateOf(0)

    private val isAudioRecorderDialogState = mutableStateOf(false)

    fun isAudioRecorderDialogShow() = isAudioRecorderDialogState

    fun showAudioRecorderDialog() {
        isAudioRecorderDialogState.value = true
    }

    fun hideAudioRecorderDialog() {
        isAudioRecorderDialogState.value = false
        stopRecordStopWatch()
        viewModelScope.launch(Dispatchers.IO) {
            recordManager.stopRecord()
        }
        viewModelScope.launch {
            updateAudiosCount()
        }
    }

    private var isNotLock:Pair<Boolean,suspend () -> Unit> = Pair(false){}
    //отвечает за блокировку при выходе из приложения, лямба будет выполнена после возвращаения
    // в приложение

    private val isShowCategoryChangeDialog = mutableStateOf(false)
        //сохроняет версию до изменений
    fun savePrimaryVersion(note: Note) {
        if(primaryNoteVersion != null) return
        inputHistoryManager.setPrimaryVersion(note.text)
        primaryNoteVersion = note
    }
    //получает заметку из БД
    fun getNote(id:Int) {
        if(id != 0) {
            note = noteRepository.getNoteById(id).getData()
            savePrimaryVersion(note.copy())
            saveCategory(note.category)
            if(!note.isEncrypted) {
                viewModelScope.launch(Dispatchers.IO) {
                    imageRepository.loadImagesInBuffer(id)
                    audioRepository.loadAudioInBuffer(id)
                }
                    titleTextField.value = note.title
                    textField.value = note.text
                saveNoteState.value = SaveNoteState.DefaultSaveNote
            }
            else {
                dialogShowState.value = ShowDialogState.DecryptDialog
            }
            currentTime.value = note.created_at
            isChosenNoteState.value = note.isChosen

        }
        else {
            if(saveNoteState.value != SaveNoteState.CryptSaveNote) {
                saveNoteState.value = SaveNoteState.DefaultSaveNote
            }
            else {
                dialogShowState.value = ShowDialogState.DecryptDialog
                isHideText.value = true
            }
        }
    }

    private fun saveCategory(categoryID: Int?) {
        if(categoryID == null) return
        viewModelScope.launch(Dispatchers.IO) {
            val category = categoryRepository.getCategoryById(categoryID)?.getData()
            currentCategory.value = category
            currentSelectedCategory.value = category?.categoryId ?: 0
        }
    }

    //проверяет наличие изменений
    fun checkChanges() {
        viewModelScope.launch {
            if(!isHavePrimaryVersion()) return@launch
            if (isEncryptNote()) {
                val title = securityUtils.encrypt(titleTextField.value, notePassword!!)
                val text = securityUtils.encrypt(textField.value, notePassword!!)
                isHaveChanges.value = !(text == primaryNoteVersion?.text && title ==
                        primaryNoteVersion?.title)
            } else {
                isHaveChanges.value =
                    !(textField.value == primaryNoteVersion?.text && titleTextField.value ==
                            primaryNoteVersion?.title)
            }
        }
    }

    private fun isHaveAudios() : Boolean {
        return isHaveAudio
    }

    fun updateImagesCount() {
        viewModelScope.launch {
           isHaveImages = getNoteImage().first().isNotEmpty()
        }
    }

     suspend fun updateAudiosCount() {
         isHaveAudio = audioRepository.isHaveAudios(note.id)
    }

    val isHaveChanges = mutableStateOf(false)
    get() = field
        //сохрание заметки(зависит от режима)
        private fun saveNote() {
            val noteId = note.id
        ApplicationScope.launch(Dispatchers.IO+CoroutineName("SaveNoteCoroutine")) {
            when(saveNoteState.value) {
                is SaveNoteState.DefaultSaveNote -> {
                    if((textField.value == note.text&&
                        titleTextField.value == note.title &&
                        !checkChangeNoteConfiguration())
                    )
                        return@launch

                    noteRepository.insertNote(note.copy(created_at = System.currentTimeMillis(),
                        title = titleTextField.value,
                        text = textField.value,
                        isChosen = isChosenNoteState.value,
                        category = currentCategory.value?.categoryId
                    ))
                }
                is SaveNoteState.RemoveNote -> {
                    if(note.id != 0) {
                        noteRepository.removeNote(note.id)
                    }
                }
                is SaveNoteState.NotSaveChanges -> {
                    noteRepository.insertNote(primaryNoteVersion!!)
                }
                is SaveNoteState.CryptSaveNote -> {
                    try {
                        val title = securityUtils.encrypt(titleTextField.value,notePassword!!)
                        val text = securityUtils.encrypt(textField.value,notePassword!!)
                        if(text == note.text&&title == note.title
                            &&!checkChangeNoteConfiguration()) return@launch
                        noteRepository.insertNote(note.copy(created_at = System.currentTimeMillis(),
                            title = title,
                            text = text,
                            isChosen = isChosenNoteState.value,
                            category = currentCategory.value?.categoryId
                        ))
                    }catch (e:Exception){}

                }

                is SaveNoteState.None -> return@launch
            }
            if(noteId == 0) {
                val newNoteId = noteRepository.getAllNote().getData().maxBy { it.id }.id
                imageRepository.tempDirToImageDir(newNoteId)
                audioRepository.tempDirToAudioDir(newNoteId)
            }
        }
    }

    private fun checkChangeNoteConfiguration(): Boolean {
        if(!isHavePrimaryVersion()&&(isHaveImages()||isHaveAudios())) return true
        if(!isHavePrimaryVersion()) return false
        if (currentCategory.value?.categoryId != primaryNoteVersion?.category ) return true
        return primaryNoteVersion?.isChosen != isChosenNoteState.value
    }


    fun removeNote(navController: NavController) {
        isDropDownMenuShow.value = false
        saveNoteState.value = SaveNoteState.RemoveNote
        navController.navigateUp()
    }

    fun changeStateToEncryptNote(password:String) {
        saveNoteState.value = SaveNoteState.CryptSaveNote
        note.isEncrypted = true
        notePassword = securityUtils.passwordToHash(password)
        dialogShowState.value = ShowDialogState.None
        toastManager.showToast(R.string.Note_encrypted)
    }

    fun isEncryptNote() = note.isEncrypted

   fun decrypt(password: String) {
       if(notePassword != null) {
           if(notePassword == securityUtils.passwordToHash(password)) {
               dialogShowState.value = ShowDialogState.None
               isHideText.value = false
               return
           }
           else {
               throw FailedDecryptException("Invalid password")
           }
       }
        try {
            val hashPassword = securityUtils.passwordToHash(password)
            isHideText.value = false
            titleTextField.value = securityUtils.decrypt(note.title,hashPassword)
            textField.value = securityUtils.decrypt(note.text,hashPassword)
            notePassword = hashPassword
            viewModelScope.launch(Dispatchers.IO) {
                imageRepository.loadImagesInBuffer(note.id)
                audioRepository.loadAudioInBuffer(note.id)
            }
            saveNoteState.value = SaveNoteState.CryptSaveNote
            dialogShowState.value = ShowDialogState.None
        }catch (e:Exception) {
            throw FailedDecryptException("Invalid password")
        }

    }

    fun changeStateToDefaultNote() {
        saveNoteState.value = SaveNoteState.DefaultSaveNote
        note.isEncrypted = false
        notePassword = null
        toastManager.showToast(R.string.Note_decrypted)
    }

    fun notSaveChanges(navController: NavController) {
        dialogShowState.value = ShowDialogState.None
        saveNoteState.value = SaveNoteState.NotSaveChanges
        navController.navigateUp()
    }

    override fun onCleared() {
        saveNote()
        inputHistoryManager.clearBuffer()
        ApplicationScope.launch(Dispatchers.IO) {
            recordManager.stopRecord()
        }
        ApplicationScope.launch(Dispatchers.IO) {
            imageRepository.clearBufferImages()
            audioRepository.clearAudioBuffer()
        }
        super.onCleared()
    }
    fun getToast() = toastManager

    fun isHavePrimaryVersion() = primaryNoteVersion != null

    val isHaveUndo = mutableStateOf(false)
    val isHaveRepo = mutableStateOf(false)

    //проверяет есть ли возможность откатиться назад или вперёд
    fun checkHistoryState() {
        isHaveRepo.value = inputHistoryManager.isHaveRedo()
        isHaveUndo.value = inputHistoryManager.isHaveUndo()
    }
    //перемещает указатель истории изменений вперёд
    fun redo() {
        try {
            textField.value = inputHistoryManager.getRedo()
            checkHistoryState()
        }catch (e:IndexOutOfBoundsException) {
            toastManager.showToast(R.string.Text_rollback_error)
        }
    }
    //перемещает указатель истории изменений назад
    fun undo() {
        try {
            textField.value = inputHistoryManager.getUndo()
            checkHistoryState()
        }catch (e:IndexOutOfBoundsException) {
            toastManager.showToast(R.string.Text_rollback_error)
        }
    }
    //добавление изменений в историю
    fun addInHistoryChanges() {
        viewModelScope.launch(Dispatchers.IO) {
            inputHistoryManager.addInHistory(textField.value)
            checkHistoryState()
        }
    }

    fun addInChosen() {
        isChosenNoteState.value = true
    }

    fun removeFromChosen() {
        isChosenNoteState.value = false
    }

    fun getCategory() = currentCategory

    fun getCurrentSelectedCategory() = currentSelectedCategory

    fun getChangeCategoryDialogStatus() = isShowCategoryChangeDialog

    fun changeCurrentCategory(id: Int) {
        viewModelScope.launch {
            currentCategory.value = categoryRepository.getCategoryById(id)?.getData()
            currentSelectedCategory.value = currentCategory.value?.categoryId ?: 0
        }
    }

    fun getDialogDispatcher(): SelectionCategoryController {
        return object : SelectionCategoryController {
            override fun onCanceled() {
                dialogShowState.value = ShowDialogState.None
                currentSelectedCategory.value = currentCategory.value?.categoryId ?: 0
            }

            override fun onConfirmed() {
                dialogShowState.value = ShowDialogState.None
                changeCurrentCategory(currentSelectedCategory.value)
            }

            override fun getCategory(): Flow<List<Category>> {
                return categoryRepository.getAllCategory()
            }

        }

    }

    fun changeCategoryEditDialogStatus(status:Boolean) {
        isShowCategoryChangeDialog.value = status
    }

    fun getNoteImage() : SharedFlow<List<Image>> {
        return imageRepository.getNoteImages()
    }

    fun addImage(activityController: ActivityController) {
        isNotLock = Pair(true){}
        analytics.sendEvent(SELECT_IMAGE_EVENT, Bundle())
        activityController.pickImage(
            onComplete = {
                isHaveImages = true
                viewModelScope.launch(Dispatchers.IO) {
                    imageRepository.addImage(it,note.id)
                }
                isNotLock = Pair(false){}
                analytics.sendEvent(SELECT_IMAGE_EVENT_OK,Bundle())
            },
            onError = {
                isNotLock = Pair(false){}
                analytics.sendEvent(SELECT_IMAGE_EVENT_ERROR,Bundle())
            }
        )
    }

    fun isHaveImages() : Boolean {
        return isHaveImages
    }

    fun openImageInImageViewer(imageFile:EncryptedFile,activityController: ActivityController) {
        viewModelScope.launch(Dispatchers.IO) {
            isNotLock = Pair(true){
                activityController.clearShareDir()
            }
            activityController.sendShowImageIntent(imageFile)
        }
    }

    private fun removeImage(imageId:Long) {
        viewModelScope.launch(Dispatchers.IO) {
            imageRepository.removeImage(note.id,imageId)
            updateImagesCount()
        }
    }

    fun toDrawScreen(navController: NavController) {
        navController.navigate(Screen.DrawScreen.route) {launchSingleTop = true}
    }

    fun getImageRequest(context: Context, bytes: ByteArray?): ImageRequest {
        return ImageRequest.Builder(context)
            .data(bytes)
            .memoryCachePolicy(CachePolicy.DISABLED)
           // .size(100)
            .build()
    }



    @OptIn(ExperimentalPermissionsApi::class)
    fun requestAudioPermission(permission: PermissionState) {
        com.xxmrk888ytxx.privatenote.presentation.MultiUse.requestPermission(permission = permission,
            onGranted = {
                showAudioRecorderDialog()
            },
            onDeny = {
                toastManager.showToast {
                    it.getString(R.string.Record_give_permissions)
                }
            }
        )
    }

    fun getAudioRecorderState() = recordManager.getRecorderState()

    fun getAudioFiles() = audioRepository.getAudioList()

    fun startRecord() {
        viewModelScope.launch(Dispatchers.IO) {
            recordManager.startRecord(note.id) {
                toastManager.showToast {
                    it.getString(R.string.Record_error)
                }
            }
        }
    }



    fun stopRecord() {
        viewModelScope.launch(Dispatchers.IO) {
            recordManager.stopRecord()
        }
        viewModelScope.launch {
            updateAudiosCount()
        }
    }

    fun playAudio(file:EncryptedFile) {
        viewModelScope.launch(Dispatchers.IO) {
            playerManager.startPlayer(file) {
                Log.d("MyLog",it.stackTraceToString())
            }
        }
    }

    fun startRecordStopWatch(startRecordTime:Long) {
        if(recordStopwatch != null) return
        recordStopwatch = object :CountDownTimer(Long.MAX_VALUE,1000) {
            override fun onTick(p: Long) {
                currentRecordTime.value = (System.currentTimeMillis() - startRecordTime).milliSecondToSecond()
            }

            override fun onFinish() {}

        }.start()
    }

    fun stopRecordStopWatch() {
        recordStopwatch.ifNotNull {
            it.cancel()
            recordStopwatch = null
            currentRecordTime.value = "00:00"
        }
    }

    fun getPlayerController() : PlayerController {
        return object : PlayerController {
            override fun getPlayerState(): SharedFlow<PlayerState> = playerManager.getPlayerState()

            override fun play(audio: Audio) {
                viewModelScope.launch(Dispatchers.IO) {
                    playAudio(audio.file)
                }
            }

            override fun pause() {
                viewModelScope.launch(Dispatchers.IO) {
                    playerManager.pausePlayer()
                }
            }

            override fun reset() {
                viewModelScope.launch(Dispatchers.IO) {
                    playerManager.pausePlayer()
                }
            }

            override fun seekTo(pos:Long) {
                viewModelScope.launch(Dispatchers.IO) {
                    playerManager.seekTo(pos)
                }
            }

        }
    }

    fun selectAudioFromExternalStorage() {
        activityController?.pickAudio(
            onComplete = {
                viewModelScope.launch(Dispatchers.IO) {
                    audioRepository.saveAudioFromExternalStorage(it,note.id)
                }
            },
            onError = {

            }
        )
    }

    fun initActivityController(activityController: ActivityController) {
        this.activityController = activityController
    }

    fun exportImage(image: Image) {
        activityController?.selectExportFile(
            onComplete = {
               ApplicationScope.launch(Dispatchers.IO) {
                   try {
                        exportImageUseCase.execute(image,it)
                       withContext(Dispatchers.Main) {
                           toastManager.showToast(R.string.Export_file_complited)
                       }

                   }catch (e:Exception) {
                       withContext(Dispatchers.Main) {
                           toastManager.showToast(R.string.Export_file_error)
                       }
                   }
               }
            },
            onError = {

            },
            exportFileType = MainActivity.IMAGE_EXPORT_TYPE
        )
    }

    fun exportAudio(audio: Audio) {
        activityController?.selectExportFile(
            onComplete = {
                ApplicationScope.launch(Dispatchers.IO) {
                    try {
                        exportAudioUseCase.execute(audio,it)
                        withContext(Dispatchers.Main) {
                            toastManager.showToast(R.string.Export_file_complited)
                        }
                    }catch (e:Exception) {
                        withContext(Dispatchers.Main) {
                            toastManager.showToast(R.string.Export_file_error)
                        }
                    }
                }
            },
            onError = {

            },
            exportFileType = MainActivity.AUDIO_EXPORT_TYPE
        )
    }

    fun getImageRepositoryLoadState() = imageRepository.getLoadState()

    fun getAudioRepositoryLoadState() = audioRepository.getLoadState()

}