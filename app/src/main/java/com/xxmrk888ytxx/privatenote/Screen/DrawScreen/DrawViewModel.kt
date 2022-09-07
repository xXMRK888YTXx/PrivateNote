package com.xxmrk888ytxx.privatenote.Screen.DrawScreen

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.R
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

    private val currentStrokeWidth = mutableStateOf(5f)

    private val currentBrushColor = mutableStateOf(Color.Red)

    private val currentSelectedColor = mutableStateOf(Color.White)

    fun getCurrentBrushColor() = currentBrushColor

    fun getCurrentStrokeWidth() = currentStrokeWidth

    fun changeCurrentStrokeWidth(newValue: Float) {
        currentStrokeWidth.value = newValue
        currentController.value?.setStrokeWidth(newValue)
    }

    private var noteId = 0

    private val saveLoadDialogState = mutableStateOf(false)

    private val isStrokeWidthSliderShow = mutableStateOf(false)

    private val isSelectColorListShow = mutableStateOf(false)

    private val selectColorDialogState = mutableStateOf(false)

    private val exitDialogState = mutableStateOf(false)

    fun getExitDialogState() = exitDialogState

    fun showExitDialog() {
        exitDialogState.value = true
    }

    fun hideExitDialog() {
        exitDialogState.value = false
    }

    fun getSelectColorDialogState() = selectColorDialogState

    fun getCurrentSelectedColor() = currentSelectedColor

    fun showSelectColorDialog() {
        selectColorDialogState.value = true
    }

    fun hideSelectColorDialog() {
        selectColorDialogState.value = false
    }

    fun changeCurrentSelectedColor(newColor:Color) {
        currentSelectedColor.value = newColor
    }


    fun isStrokeWidthSliderShow() = isStrokeWidthSliderShow

    fun isSelectColorListShow() = isSelectColorListShow

    fun changeBrushColor(newColor: Color) {
        currentBrushColor.value = newColor
        currentController.value?.setStrokeColor(newColor)
    }

    fun changeStrokeWidthSliderState() {
        if(isSelectColorListShow.value) isSelectColorListShow.value = false
        isStrokeWidthSliderShow.value = !isStrokeWidthSliderShow.value
    }

    fun changeSelectColorListShow() {
        if(isStrokeWidthSliderShow.value) isStrokeWidthSliderShow.value = false
        isSelectColorListShow.value = !isSelectColorListShow.value
    }

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

    fun saveDraw(navController: NavController) {
        val handler = Handler(Looper.getMainLooper())
        viewModelScope.launch(Dispatchers.IO) {
            val image = currentController.value?.getDrawBoxBitmap() ?: return@launch
            saveLoadDialogState.value = true
            noteRepository.addPaintImage(image,noteId) {
                showToast.showToast() { context ->
                    val text = context.getText(R.string.Error_saving)
                    return@showToast "$text: ${it.message.toString()}"
                }
                saveLoadDialogState.value = false
            }
            saveLoadDialogState.value = false
            handler.post { navController.navigateUp() }
        }
    }




}