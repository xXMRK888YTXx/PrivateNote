package com.xxmrk888ytxx.privatenote.Screen.DrawScreen

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.Repositories.NoteReposiroty.NoteRepository
import com.xxmrk888ytxx.privatenote.Utils.MustBeLocalization
import com.xxmrk888ytxx.privatenote.Utils.ShowToast
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ak1.drawbox.DrawController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val showToast: ShowToast
) : ViewModel() {
    private val currentController:MutableState<DrawController?> = mutableStateOf(null)

    private var noteId = 0

    private val saveLoadDialogState = mutableStateOf(false)

    fun getSaveLoadDialogState() = saveLoadDialogState

    fun getController(newController: DrawController) : MutableState<DrawController?> {
        if(currentController.value != null) return currentController
        currentController.value = newController
        return currentController
    }

    fun redo() {
        currentController.value?.reDo()
    }

    fun undo() {
        currentController.value?.unDo()
    }

    fun clearDrawPlace() {
        currentController.value?.reset()
    }

    fun saveNoteId(noteId: Int) {
        this.noteId = noteId
    }

    @MustBeLocalization
    fun saveDraw(navController: NavController) {
        val handler = Handler(Looper.getMainLooper())
        viewModelScope.launch(Dispatchers.IO) {
            val image = currentController.value?.getDrawBoxBitmap() ?: return@launch
            saveLoadDialogState.value = true
            noteRepository.addImage(image,noteId) {
                showToast.showToast("Во время сохронения произошла ошибка: ${it.message.toString()}")
                saveLoadDialogState.value = false
            }
            saveLoadDialogState.value = false
            handler.post { navController.navigateUp() }
        }
    }
}