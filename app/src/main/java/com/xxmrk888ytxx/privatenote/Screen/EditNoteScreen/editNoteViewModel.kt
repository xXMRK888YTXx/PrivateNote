package com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.navArgument
import com.google.firebase.analytics.FirebaseAnalytics
import com.xxmrk888ytxx.privatenote.ActivityController
import com.xxmrk888ytxx.privatenote.DB.Entity.Category
import com.xxmrk888ytxx.privatenote.DB.Entity.Note
import com.xxmrk888ytxx.privatenote.Exception.FailedDecryptException
import com.xxmrk888ytxx.privatenote.InputHistoryManager.InputHistoryManager
import com.xxmrk888ytxx.privatenote.LifeCycleState
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Repositories.CategoryRepository.CategoryRepository
import com.xxmrk888ytxx.privatenote.Repositories.NoteReposiroty.NoteRepository
import com.xxmrk888ytxx.privatenote.MultiUse.SelectionCategoryDialog.SelectionCategoryController
import com.xxmrk888ytxx.privatenote.NoteFileManager.Image
import com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen.States.SaveNoteState
import com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen.States.ShowDialogState
import com.xxmrk888ytxx.privatenote.Screen.Screen
import com.xxmrk888ytxx.privatenote.SecurityUtils.SecurityUtils
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.SELECT_IMAGE_EVENT
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.SELECT_IMAGE_EVENT_ERROR
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.SELECT_IMAGE_EVENT_OK
import com.xxmrk888ytxx.privatenote.Utils.NavArguments
import com.xxmrk888ytxx.privatenote.Utils.ShowToast
import com.xxmrk888ytxx.privatenote.Utils.getData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class editNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val categoryRepository: CategoryRepository,
    private val securityUtils: SecurityUtils,
    private val showToast: ShowToast,
    private val lifeCycleState: MutableStateFlow<LifeCycleState>,
    private val inputHistoryManager: InputHistoryManager,
    private val analytics: FirebaseAnalytics
) : ViewModel() {

    init {
        //очистка фото заметки добавление которой небыло завершино
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.clearTempDir()
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

    private var note: Note = Note(title = "", text = "")

    //режим сохранения заметки
    private val saveNoteState = mutableStateOf<SaveNoteState>(SaveNoteState.None)

    val isDropDownMenuShow = mutableStateOf(false)
        //состояние показа диалоговых окон
    val dialogShowState = mutableStateOf<ShowDialogState>(ShowDialogState.None)

    private var notePassword:String? = null

    val isHideText = mutableStateOf(false)

    private val isShowRemoveImageDialog = mutableStateOf(Pair(false){})

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
                    noteRepository.loadImages(id)
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

    val isHaveChanges = mutableStateOf(false)
    get() = field
        //сохрание заметки(зависит от режима)
    fun saveNote() {
            val noteId = note.id
        GlobalScope.launch(Dispatchers.IO) {
            when(saveNoteState.value) {
                is SaveNoteState.DefaultSaveNote -> {
                    if(textField.value == note.text&&
                        titleTextField.value == note.title&&!checkChangeNoteConfiguration()) return@launch
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
                noteRepository.tempDirToImageDir(newNoteId)
            }
        }
    }

    private fun checkChangeNoteConfiguration(): Boolean {
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
        showToast.showToast(R.string.Note_encrypted)
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
                noteRepository.loadImages(note.id)
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
        showToast.showToast(R.string.Note_decrypted)
    }

    fun notSaveChanges(navController: NavController) {
        dialogShowState.value = ShowDialogState.None
        saveNoteState.value = SaveNoteState.NotSaveChanges
        navController.navigateUp()
    }

    override fun onCleared() {
        saveNote()
        inputHistoryManager.clearBuffer()
        GlobalScope.launch(Dispatchers.IO) {
            noteRepository.clearLoadImages()
        }
        super.onCleared()
    }
    fun getToast() = showToast

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
            showToast.showToast(R.string.Text_rollback_error)
        }
    }
    //перемещает указатель истории изменений назад
    fun undo() {
        try {
            textField.value = inputHistoryManager.getUndo()
            checkHistoryState()
        }catch (e:IndexOutOfBoundsException) {
            showToast.showToast(R.string.Text_rollback_error)
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
        return noteRepository.getNoteImages()
    }

    fun addImage(activityController: ActivityController) {
        isNotLock = Pair(true){}
        analytics.logEvent(SELECT_IMAGE_EVENT, Bundle())
        activityController.pickImage(
            onComplete = {
                viewModelScope.launch(Dispatchers.IO) {
                    noteRepository.addImage(it,note.id)
                }
                isNotLock = Pair(false){}
                analytics.logEvent(SELECT_IMAGE_EVENT_OK,Bundle())
            },
            onError = {
                isNotLock = Pair(false){}
                analytics.logEvent(SELECT_IMAGE_EVENT_ERROR,Bundle())
            }
        )
    }

    fun openImageInImageViewer(image:Bitmap,activityController: ActivityController) {
        viewModelScope.launch(Dispatchers.IO) {
            isNotLock = Pair(true){
                activityController.clearShareDir()
            }
            activityController.sendShowImageIntent(image)
        }
    }

    private fun removeImage(imageId:Long) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.removeImage(note.id,imageId)
        }
    }

    fun toDrawScreen(navController: NavController) {
        navController.navigate(Screen.DrawScreen.route) {launchSingleTop = true}
    }
}