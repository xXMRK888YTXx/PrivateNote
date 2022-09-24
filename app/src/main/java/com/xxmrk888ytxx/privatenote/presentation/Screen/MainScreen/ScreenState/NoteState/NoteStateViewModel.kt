package com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.NoteState

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Category
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Note
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.domain.Repositories.CategoryRepository.CategoryRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.NoteReposiroty.NoteRepository
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.SelectionCategoryDialog.SelectionCategoryController
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.MainScreenController
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.MainScreenState
import com.xxmrk888ytxx.privatenote.presentation.Screen.Screen
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.BackToDefaultMode_In_NoteScreen
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Hide_Category_List
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Hide_EditCategory_Dialog
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Remove_Category
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.SearchMode_In_NoteScreen
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.SelectionMode_In_NoteScreen
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Show_Category_List
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.Show_EditCategory_Dialog
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.Utils.Const.CHOSEN_ONLY
import com.xxmrk888ytxx.privatenote.Utils.Const.IGNORE_CATEGORY
import com.xxmrk888ytxx.privatenote.Utils.Const.getNoteId
import com.xxmrk888ytxx.privatenote.Utils.NavArguments
import com.xxmrk888ytxx.privatenote.Utils.SendAnalytics
import com.xxmrk888ytxx.privatenote.domain.ToastManager.ToastManager
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.PrimaryFontColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@SendAnalytics
@HiltViewModel
class NoteStateViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val toastManager: ToastManager,
    private val categoryRepository: CategoryRepository,
    private val analytics: AnalyticsManager
) : ViewModel() {
    val searchFieldText = mutableStateOf("")
        get() = field

    val isSearchLineHide = mutableStateOf(false)
    get() = field

    private val currentNoteMode = mutableStateOf<NoteScreenMode>(NoteScreenMode.Default)

    private val categoryFilterStatus = mutableStateOf(IGNORE_CATEGORY)

    private var mainScreenController: MainScreenController? = null

    fun setMainScreenController(mainScreenController: MainScreenController?) {
        if(mainScreenController == null) return
        this.mainScreenController = mainScreenController
        SetupFloatButtonOptions()
    }

    private fun SetupFloatButtonOptions() {
        mainScreenController?.setFloatButtonOnClickListener(SCREEN_ID) {
            toEditNoteScreen(it,0)
        }

    }

    fun getCategoryFilterStatus() = categoryFilterStatus

    fun changeCategoryFilterStatus(categoryOrStatus: Int) {
        categoryFilterStatus.value = categoryOrStatus
    }

    private val showEditCategoryDialog = mutableStateOf(Pair<Boolean,Category?>(false,null))
    
    val nameCategoryFieldText = mutableStateOf("")

    val currentCategoryColor = mutableStateOf(PrimaryFontColor)

    val isShowSelectedCategoryMenu = mutableStateOf(false)

    val currentSelectedCategoryId = mutableStateOf(0)

    private val isShowDeleteDialog = mutableStateOf(Pair<Boolean,Int?>(false,null))

    fun getDeleteDialogState() = isShowDeleteDialog

    fun showDeleteDialog(id:Int) {
        isShowDeleteDialog.value = Pair(true,id)
    }

    fun hideDeleteDialog() {
        isShowDeleteDialog.value = Pair(false,null)
    }

    fun removeNote(id:Int?) {
        if(id == null) return
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.removeNote(id)
        }
    }

    fun getCurrentMode() = currentNoteMode

    fun getNoteList() : Flow<List<Note>> {
        return noteRepository.getAllNote()
    }

    fun toEditNoteScreen(navController: NavController, id:Int) {
        NavArguments.bundle.putInt(getNoteId,id)
        navController.navigate(Screen.EditNoteScreen.route) {launchSingleTop = true}
    }
    fun toSelectionMode() {
        analytics.sendEvent(SelectionMode_In_NoteScreen,null)
        currentNoteMode.value = NoteScreenMode.SelectionScreenMode
        mainScreenController?.changeBottomBarVisibleStatus(false)
        mainScreenController?.changeScrollBetweenScreenState(false)
    }

    fun toDefaultMode() {
        analytics.sendEvent(BackToDefaultMode_In_NoteScreen,null)
        currentNoteMode.value = NoteScreenMode.Default
        selectedNoteList.clear()
        mainScreenController?.changeBottomBarVisibleStatus(true)
        mainScreenController?.changeScrollBetweenScreenState(true)
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
             val selectedItem = selectedNoteList
             currentNoteMode.value = NoteScreenMode.Default
             selectedItem.forEach {
                 viewModelScope.launch(Dispatchers.IO) {
                     noteRepository.removeNote(it)
                 }
             }
    }

    fun toSearchMode() {
        analytics.sendEvent(SearchMode_In_NoteScreen,null)
        currentNoteMode.value = NoteScreenMode.SearchScreenMode
        mainScreenController?.changeBottomBarVisibleStatus(false)
        mainScreenController?.changeScrollBetweenScreenState(false)
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
        analytics.sendEvent(Show_Category_List,null)
        mainScreenController?.changeBottomBarVisibleStatus(false)
        mainScreenController?.changeScrollBetweenScreenState(false)
        currentNoteMode.value = NoteScreenMode.ShowCategoryMenu
    }

    fun hideCategoryList() {
        analytics.sendEvent(Hide_Category_List,null)
        mainScreenController?.changeBottomBarVisibleStatus(true)
        mainScreenController?.changeScrollBetweenScreenState(true)
        currentNoteMode.value = NoteScreenMode.Default
        showEditCategoryDialog.value = Pair(false,null)
    }

    fun showEditCategoryDialog(changedCategory:Category? = null) {
        analytics.sendEvent(Show_EditCategory_Dialog,null)
        showEditCategoryDialog.value = Pair(true,changedCategory)
    }

    fun hideEditCategoryDialog() {
        analytics.sendEvent(Hide_EditCategory_Dialog,null)
        showEditCategoryDialog.value = Pair(false,null)
        nameCategoryFieldText.value = ""
        currentCategoryColor.value = PrimaryFontColor
    }

    fun getEditCategoryStatus() = showEditCategoryDialog

    fun saveCategory(categoryName:String, iconColor: Color, categoryId: Int = 0){
        viewModelScope.launch {
            hideEditCategoryDialog()
            val category = Category(
                categoryId = categoryId,
                categoryName = categoryName,
                red = iconColor.red,
                green = iconColor.green,
                blue = iconColor.blue
            )
            if(categoryId == 0)
            categoryRepository.insertCategory(category)
            else categoryRepository.updateCategory(category)
        }
    }


    fun removeCategory(category: Category,context: Context) {
        analytics.sendEvent(Remove_Category,null)
        viewModelScope.launch {
            if(categoryFilterStatus.value == category.categoryId)
            changeCategoryFilterStatus(IGNORE_CATEGORY)
            categoryRepository.removeCategory(category.categoryId)
            toastManager.showToast("${context.getString(R.string.Categoty)} \"${category.categoryName}\" " +
                    context.getString(R.string.has_been_deleted)
            )
        }
    }

    val savedCategory = mutableStateOf(listOf<Category>())

    fun getSelectionCategoryDispatcher() : SelectionCategoryController {
        return object : SelectionCategoryController {
            override fun onCanceled() {
                isShowSelectedCategoryMenu.value = false
                currentSelectedCategoryId.value = 0
            }

            override fun onConfirmed() {
                isShowSelectedCategoryMenu.value = false
                val currentCategory = currentSelectedCategoryId.value
                currentSelectedCategoryId.value = 0
                changeCategorySelected(currentCategory)
            }

            override fun getCategory() : Flow<List<Category>> {
                return getAllCategory()
            }

        }
    }

    fun changeCategorySelected(categoryId: Int) {
        viewModelScope.launch {
            selectedNoteList.forEach {
                noteRepository.changeCurrentCategory(it,categoryId)
            }
        }
    }

    fun changeFloatButtonVisible(isVisible:Boolean) {
        mainScreenController?.changeEnableFloatButtonStatus(isVisible)
    }

    fun getDefaultTitle(context: Context,id:Int) : String {
        when(id) {
            IGNORE_CATEGORY -> return context.getString(R.string.All_Notes)
            CHOSEN_ONLY -> return context.getString(R.string.Chosen)
            else -> return ""
        }
    }

    fun changeChosenStatus(id: Int,currentStatus:Boolean) {
        noteRepository.changeChosenStatus(!currentStatus,id)
    }
    companion object {
        val SCREEN_ID = MainScreenState.NoteScreen.id
    }
}