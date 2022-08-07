package com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.NoteState

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.DB.Entity.Category
import com.xxmrk888ytxx.privatenote.DB.Entity.Note
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Repositories.CategoryRepository.CategoryRepository
import com.xxmrk888ytxx.privatenote.Repositories.NoteReposiroty.NoteRepository
import com.xxmrk888ytxx.privatenote.Screen.Screen
import com.xxmrk888ytxx.privatenote.Utils.Const.getNoteId
import com.xxmrk888ytxx.privatenote.Utils.NavArguments
import com.xxmrk888ytxx.privatenote.Utils.ShowToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteStateViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val showToast: ShowToast,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    val searchFieldText = mutableStateOf("")
        get() = field

    val isSearchLineHide = mutableStateOf(false)
    get() = field

    private val currentNoteMode = mutableStateOf<NoteScreenMode>(NoteScreenMode.Default)

    val dialogState = mutableStateOf<NoteDialogState>(NoteDialogState.None)

    private val showEditCategoryDialog = mutableStateOf(false)

    fun getCurrentMode() = currentNoteMode

    fun getNoteList() : Flow<List<Note>> {
        return noteRepository.getAllNote()
    }

    fun toEditNoteScreen(navController: NavController, id:Int) {
        NavArguments.bundle.putInt(getNoteId,id)
        navController.navigate(Screen.EditNoteScreen.route) {launchSingleTop = true}
    }
    fun toSelectionMode() {
        currentNoteMode.value = NoteScreenMode.SelectionScreenMode
    }

    fun toDefaultMode() {
        currentNoteMode.value = NoteScreenMode.Default
        selectedNoteList.clear()
    }

    private val selectedNoteList = mutableSetOf<Int>()

    fun isItemSelected(noteId:Int) : Boolean {
        return selectedNoteList.any{it == noteId}
    }

    fun changeSelectedState(noteId: Int,checkState:Boolean) {
        if(!checkState) {
            selectedNoteList.remove(noteId)
        }
        else {
            selectedNoteList.add(noteId)
        }
        isSelectedItemNotEmpty.value = isSelectedNotEmpty()
        Log.d("MyLog",selectedNoteList.toString())
        selectionItemCount.value = selectedNoteList.size
    }
    var isSelectedItemNotEmpty = mutableStateOf(false)

    var selectionItemCount = mutableStateOf(0)
    get() = field

    fun selectAll() {
        viewModelScope.launch {
            val noteList = getNoteList().first()
            val selectAllOrNot = noteList.size != selectedNoteList.size
            noteList.forEach {
                changeSelectedState(it.id,selectAllOrNot)
            }
        }
    }

    fun isSelectedNotEmpty() = selectedNoteList.isNotEmpty()

     fun removeSelected() {
         viewModelScope.launch {
             val selectedItem = selectedNoteList
             currentNoteMode.value = NoteScreenMode.Default
             selectedItem.forEach {
                 noteRepository.removeNote(it)
             }
         }
    }

    fun toSearchMode() {
        currentNoteMode.value = NoteScreenMode.SearchScreenMode
    }
    val lastNoteCount = mutableStateOf(0)
    get() = field

    fun addInChosenSelected() {
        viewModelScope.launch {
            val selectedItem = selectedNoteList
            currentNoteMode.value = NoteScreenMode.Default
            selectedItem.forEach {
                noteRepository.changeChosenStatus(true,it)
            }
        }
    }

    fun getCategoryById(categoryId:Int?) : Flow<Category>? {
        if(categoryId == null) return null
        return categoryRepository.getCategoryById(categoryId)
    }

    fun getAllCategory() = categoryRepository.getAllCategory()

    fun showCategoryList() {
        currentNoteMode.value = NoteScreenMode.ShowCategoryMenu
    }

    fun hideCategoryList() {
        currentNoteMode.value = NoteScreenMode.Default
        showEditCategoryDialog.value = false
    }

    fun showEditCategoryDialog() {
        showEditCategoryDialog.value = true
    }

    fun hideEditCategoryDialog() {
        showEditCategoryDialog.value = false
    }

    fun editCategoryStatus() = showEditCategoryDialog

    fun addCategory(categoryName:String,iconColor: Color){
        viewModelScope.launch {
            showEditCategoryDialog.value = false
            val category = Category(
                categoryName = categoryName,
                red = iconColor.red,
                green = iconColor.green,
                blue = iconColor.blue
            )
            categoryRepository.insertCategory(category)
        }
    }

    fun removeCategory(category: Category,context: Context) {
        viewModelScope.launch {
            categoryRepository.removeCategory(category.categoryId)
            showToast.showToast("${context.getString(R.string.Categoty)} \"${category.categoryName}\" " +
                    context.getString(R.string.has_been_deleted)
            )
        }
    }

    val savedCategory = mutableStateOf(listOf<Category>())
}