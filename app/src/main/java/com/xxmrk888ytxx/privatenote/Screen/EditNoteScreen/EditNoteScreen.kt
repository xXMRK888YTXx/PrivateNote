package com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen

import android.annotation.SuppressLint
import android.widget.ImageButton
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.Exception.FailedDecryptException
import com.xxmrk888ytxx.privatenote.MultiUse.PasswordEditText.PasswordEditText
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.MultiUse.SelectionCategoryDialog
import com.xxmrk888ytxx.privatenote.MultiUse.WarmingText.WarmingText
import com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen.States.ShowDialogState
import com.xxmrk888ytxx.privatenote.Utils.*
import com.xxmrk888ytxx.privatenote.Utils.Const.getNoteId
import com.xxmrk888ytxx.privatenote.ui.theme.*
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EditNoteScreen(editNoteViewModel: editNoteViewModel = hiltViewModel(), navController: NavController) {
    val dialogState = remember {
        editNoteViewModel.dialogShowState
    }
    val changeCategoryDialogStatus = remember {
        editNoteViewModel.getChangeCategoryDialogStatus()
    }
    val currentSelectedItem = remember {
        editNoteViewModel.getCurrentSelectedCategory()
    }
    LaunchedEffect(key1 = editNoteViewModel, block = {
        editNoteViewModel.getNote(NavArguments.bundle.getInt(getNoteId))
    })
    val textFieldFocus = remember { FocusRequester() }
        Column(
            Modifier
                .fillMaxSize()
                .background(MainBackGroundColor)
        ) {
            Toolbar(editNoteViewModel,navController)
            TitleEditField(editNoteViewModel,textFieldFocus)
            TimeCreated(editNoteViewModel)
            CategorySelector(editNoteViewModel)
            NoteTextEdit(editNoteViewModel,textFieldFocus)
        }
    when(dialogState.value) {
       is ShowDialogState.EncryptDialog -> {CryptDialog(editNoteViewModel)}
        is ShowDialogState.DecryptDialog -> {DecriptDialog(editNoteViewModel,navController)}
        is ShowDialogState.ExitDialog -> { ExitDialog(editNoteViewModel,navController)}
        is ShowDialogState.EditCategoryDialog -> {SelectionCategoryDialog(currentSelected = currentSelectedItem,
            dialogController = editNoteViewModel.getDialogDispatcher())}
        is ShowDialogState.FileDialog -> {
            FilesDialog(editNoteViewModel)
        }
        is ShowDialogState.None -> {}
    }
    val isHaveChanges = remember {
        editNoteViewModel.isHaveChanges
    }
    BackPressController.setHandler(isHaveChanges.value&&editNoteViewModel.isHavePrimaryVersion()) {
        editNoteViewModel.dialogShowState.value = ShowDialogState.ExitDialog
    }
//    if(changeCategoryDialogStatus.value) {
//        SelectionCategoryDialog(currentSelected = currentSelectedItem,
//            dialogDispatcher = editNoteViewModel.getDialogDispatcher())
//    }
}

@Composable
fun TimeCreated(editNoteViewModel: editNoteViewModel) {
    if(editNoteViewModel.currentTime.value == 0L) return
    Text(text = editNoteViewModel.currentTime.value.secondToData(LocalContext.current),
        color = PrimaryFontColor.copy(0.5f),
        fontSize = 14.sp,
        fontWeight = FontWeight.Light,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 13.dp)
    )
}

@Composable
fun NoteTextEdit(editNoteViewModel: editNoteViewModel, textFieldFocus: FocusRequester) {
    val textInField = remember {
        editNoteViewModel.textField
    }
    val isHideText = remember {
        editNoteViewModel.isHideText
    }

    val stub = remember {
        mutableStateOf("")
    }
    val textState = if(!isHideText.value) textInField else stub
        TextField(
            value = textState.value,
            onValueChange = {textState.value = it
                editNoteViewModel.checkChanges()
                editNoteViewModel.addInHistoryChanges()
                            },
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(textFieldFocus),
            placeholder = {Text(text = stringResource(R.string.Write_something_here),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
            )},
            shape = RoundedCornerShape(80),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White.copy(0f),
                focusedIndicatorColor = Color.White.copy(0f),
                unfocusedIndicatorColor = Color.White.copy(0f),
                cursorColor = CursorColor,
                placeholderColor = TitleHintColor.copy(0.6f)
            ),
            textStyle = TextStyle(
                color = PrimaryFontColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
            )

        )
    }

@Composable
fun Toolbar(editNoteViewModel: editNoteViewModel,navController: NavController) {
    val isDropDownMenuShow = remember {
        editNoteViewModel.isDropDownMenuShow
    }
    val isHaveChanges = remember {
        editNoteViewModel.isHaveChanges
    }
    val isNoteChosen = remember {
        editNoteViewModel.isChosenNoteState
    }
    val dropDownItemList = listOf(
        DropDownItem(stringResource(R.string.Encrypt_note), isEnable = !editNoteViewModel.isEncryptNote()) {
            if(editNoteViewModel.textField.value.isEmpty()&&editNoteViewModel.titleTextField.value.isEmpty()) {
                editNoteViewModel.getToast().showToast(R.string.note_is_empty)
                isDropDownMenuShow.value = false
                return@DropDownItem
            }
            isDropDownMenuShow.value = false
            editNoteViewModel.dialogShowState.value = ShowDialogState.EncryptDialog
        },
        DropDownItem(stringResource(id = R.string.Decript_note),editNoteViewModel.isEncryptNote()){
            isDropDownMenuShow.value = false
            editNoteViewModel.changeStateToDefaultNote()
        },
        DropDownItem(stringResource(R.string.Delete)){
            editNoteViewModel.removeNote(navController)
        },
        DropDownItem(stringResource(R.string.cancel_changes),isHaveChanges.value) {
            editNoteViewModel.notSaveChanges(navController)
        },
        DropDownItem(stringResource(R.string.to_chosen),!isNoteChosen.value) {
            isDropDownMenuShow.value = false
            editNoteViewModel.addInChosen()
        },
        DropDownItem(stringResource(R.string.Remove_from_chosen),isNoteChosen.value) {
            isDropDownMenuShow.value = false
            editNoteViewModel.removeFromChosen()
        }

    )
    val isUndoAvailable = remember {
        editNoteViewModel.isHaveUndo
    }
    val isRedoAvailable = remember {
        editNoteViewModel.isHaveRepo
    }
    val undoArrowColor = if(isUndoAvailable.value) PrimaryFontColor else PrimaryFontColor.copy(0.4f)
    val redoArrowColor = if(isRedoAvailable.value) PrimaryFontColor else PrimaryFontColor.copy(0.4f)
    Row(
        Modifier
            .fillMaxWidth()
            .background(MainBackGroundColor),
        verticalAlignment = Alignment.CenterVertically,
        ) {
        IconButton(
            onClick = {
                if (isHaveChanges.value)
                    editNoteViewModel.dialogShowState.value = ShowDialogState.ExitDialog
                else navController.navigateUp()
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back_arrow),
                contentDescription = "Back Arrow",
                modifier = Modifier
                    .size(30.dp),
                tint = PrimaryFontColor
            )
        }
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(end = 45.dp)
        ) {
            IconButton(
                onClick = {
                    editNoteViewModel.undo()
                },
                enabled = isUndoAvailable.value
            ){
                Icon(painter = painterResource(R.drawable.ic_undo_up),
                    contentDescription = null,
                    tint = undoArrowColor,
                    modifier = Modifier.size(25.dp)
                )
            }
            IconButton(
                onClick = {
                    editNoteViewModel.redo()
                },
                enabled = isRedoAvailable.value
            ){
                Icon(painter = painterResource(R.drawable.ic_redo_up),
                    contentDescription = null,
                    tint = redoArrowColor,
                    modifier = Modifier.size(25.dp)
                )
            }
        }
               Row(
                   Modifier.fillMaxWidth(),
                   verticalAlignment = Alignment.CenterVertically,
                   horizontalArrangement = Arrangement.End
               ) {
                   IconButton(onClick = { editNoteViewModel.dialogShowState.value = ShowDialogState.FileDialog }) {
                       Icon(
                           painter = painterResource(id = R.drawable.ic_attach),
                           contentDescription = "",
                           modifier = Modifier
                               .size(35.dp),
                           tint = PrimaryFontColor
                       )
                   }
                   IconButton(
                       onClick = {
                           isDropDownMenuShow.value = true
                       }) {
                       Icon(
                           painter = painterResource(id = R.drawable.ic_dots),
                           contentDescription = "Dots",
                           modifier = Modifier
                               .size(40.dp),
                           tint = PrimaryFontColor
                       )
                   }
                   Box(
                   ) {
                       DropdownMenu(expanded = isDropDownMenuShow.value,
                           onDismissRequest = { isDropDownMenuShow.value = false },
                           modifier = Modifier.background(DropDownMenuColor)
                       ) {
                           dropDownItemList.forEach {
                               if(it.isEnable) {
                                   DropdownMenuItem(onClick = { it.onClick.invoke() }) {
                                       Text(it.text,color = PrimaryFontColor)
                                   }
                               }
                           }
                       }
                   }
               }
    }
}

@Composable
fun TitleEditField(editNoteViewModel: editNoteViewModel, textFieldFocus: FocusRequester) {
    val isHideText = remember {
        editNoteViewModel.isHideText
    }
    val textInField = remember {
        editNoteViewModel.titleTextField
    }
    val stub = remember {
        mutableStateOf("")
    }
    val textState = if(!isHideText.value) textInField else stub
        TextField(value = textState.value,
            onValueChange = {textState.value = it;editNoteViewModel.checkChanges()},
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            singleLine = true,
            placeholder = {Text(stringResource(R.string.Title),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            )},
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White.copy(0f),
                focusedIndicatorColor = Color.White.copy(0f),
                unfocusedIndicatorColor = Color.White.copy(0f),
                cursorColor = CursorColor,
                placeholderColor = TitleHintColor.copy(0.6f)
            ),
            textStyle = TextStyle(
                color = PrimaryFontColor,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                ),
            keyboardActions = KeyboardActions(
                onDone = {
                    textFieldFocus.requestFocus()
                }
            )
        )
    }

@Composable
fun CryptDialog(editNoteViewModel: editNoteViewModel) {
    val passwordText = remember {
        mutableStateOf("")
    }
    val repitPasswordText = remember {
        mutableStateOf("")
    }
    val isEnabled = remember {
        mutableStateOf(false)
    }
    Dialog(onDismissRequest = {editNoteViewModel.dialogShowState.value = ShowDialogState.None },
        ) {
        Card(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            shape = RoundedCornerShape(10)
        ) {
            Column(
                Modifier.background(MainBackGroundColor)

            ) {
                PasswordTextEdit(editNoteViewModel,passwordText,repitPasswordText,isEnabled)
                RepitPasswordTextEdit(editNoteViewModel,repitPasswordText,passwordText,isEnabled)
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
                    onClick = { editNoteViewModel.changeStateToEncryptNote(passwordText.value) },
                    enabled = (isEnabled.value&&!passwordText.value.isEmpty()
                            &&!repitPasswordText.value.isEmpty()),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = PrimaryFontColor,
                        disabledContentColor = Color.Black.copy(0.3f),
                        disabledBackgroundColor = PrimaryFontColor.copy(0.3f)
                        )
                ){
                    Text(text = stringResource(R.string.Encrypt),
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                }
                WarmingText(stringResource(R.string.warming_remember_password))
            }
            }
        }
    }
@Composable
fun PasswordTextEdit(editNoteViewModel: editNoteViewModel, password: MutableState<String>,
                     repitPassword: MutableState<String>, isEnabled:MutableState<Boolean>) {
    OutlinedTextField(value = password.value,
        onValueChange = {password.value = it;isEnabled.value = repitPassword.value == password.value},
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(100))
            .padding(15.dp),
        label = { Text(text = stringResource(R.string.Enter_password),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = PrimaryFontColor,
            backgroundColor = SearchColor,
            placeholderColor = PrimaryFontColor.copy(0.7f),
            focusedBorderColor = SearchColor,
            focusedLabelColor = PrimaryFontColor.copy(alpha = 0.85f),
            cursorColor = CursorColor,
            unfocusedLabelColor = PrimaryFontColor.copy(0.6f)
        ),
        textStyle = TextStyle(fontSize = 16.sp),
        keyboardOptions = KeyboardOptions(autoCorrect = false,
        keyboardType = KeyboardType.Password,
            ),
            visualTransformation = PasswordVisualTransformation()
    )
}

@Composable
fun RepitPasswordTextEdit(editNoteViewModel: editNoteViewModel, repitPassword:MutableState<String>,
    password: MutableState<String>, isEnabled:MutableState<Boolean>) {

    OutlinedTextField(value = repitPassword.value,
        onValueChange = {repitPassword.value = it;isEnabled.value = repitPassword.value == password.value },
        singleLine = true,
        isError = (!isEnabled.value&&!repitPassword.value.isEmpty()),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(100))
            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
        label = { Text(text = stringResource(R.string.Repit_password),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = PrimaryFontColor,
            backgroundColor = SearchColor,
            placeholderColor = PrimaryFontColor.copy(0.7f),
            focusedBorderColor = SearchColor,
            focusedLabelColor = PrimaryFontColor.copy(alpha = 0.85f),
            cursorColor = CursorColor,
            unfocusedLabelColor = PrimaryFontColor.copy(0.6f)
        ),
        textStyle = TextStyle(fontSize = 16.sp),
        keyboardOptions = KeyboardOptions(autoCorrect = false,
            keyboardType = KeyboardType.Password,
        ),
        visualTransformation = PasswordVisualTransformation()
    )
}

@Composable
fun DecriptDialog(editNoteViewModel: editNoteViewModel,navController: NavController) {
    val passwordText = remember {
        mutableStateOf("")
    }
    val isLoad = remember {
        mutableStateOf(false)
    }
    val titleText = remember {
        mutableStateOf("")
    }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    titleText.value = stringResource(id = R.string.Enter_password)
    Dialog(onDismissRequest = {navController.navigateUp();editNoteViewModel.dialogShowState.value = ShowDialogState.None },
    ) {
        Card(
            Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10)
        ) {
            Column(
                Modifier.background(MainBackGroundColor)

            ) {
                PasswordEditText(titleText,passwordText)
                if(!isLoad.value) {
                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
                        onClick =  {
                            coroutineScope.launch{
                                isLoad.value = true
                                try {
                                    editNoteViewModel.decrypt(passwordText.value)
                                }catch (e:FailedDecryptException) {
                                    titleText.value = context.getString(R.string.Invalid_password)
                                }
                                isLoad.value = false
                            }
                        },
                        shape = RoundedCornerShape(50),
                        enabled = passwordText.value.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = PrimaryFontColor,
                            disabledContentColor = Color.Black.copy(0.3f),
                            disabledBackgroundColor = PrimaryFontColor.copy(0.3f)
                        )
                    ){
                        Text(text = stringResource(R.string.Decrypt),
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                    }
                }
                else {
                    CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                }
            }
        }
    }
}

@Composable
fun ExitDialog(editNoteViewModel: editNoteViewModel,navController: NavController) {
    Dialog(onDismissRequest = {
        editNoteViewModel.dialogShowState.value = ShowDialogState.None
    }) {
            Card(
               modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CardNoteColor),
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    Text(
                        text = stringResource(R.string.Save_changes_question),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(bottom = 25.dp, top = 25.dp)
                            .fillMaxWidth(),
                        fontSize = 17.sp,
                        color = PrimaryFontColor,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        Modifier,
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        OutlinedButton(
                            onClick = {
                                editNoteViewModel.notSaveChanges(navController)
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .padding(start = 5.dp, end = 5.dp),
                            shape = RoundedCornerShape(80),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = TitleHintColor,
                            )
                        ) {
                            Text(text = stringResource(R.string.Not_save),
                                color = PrimaryFontColor
                            )
                        }
                        OutlinedButton(
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = FloatingButtonColor,
                            ),
                            onClick = {
                                editNoteViewModel.dialogShowState.value = ShowDialogState.None
                                navController.navigateUp()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 5.dp, end = 5.dp),
                            shape = RoundedCornerShape(80),
                        ) {
                            Text(text = stringResource(R.string.Save),
                            color = PrimaryFontColor
                                )
                        }
                    }
                }
            }
    }
}
@Composable
fun CategorySelector(editNoteViewModel: editNoteViewModel) {
    val category = remember {
        editNoteViewModel.getCategory()
    }
    Row (
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val iconColor = category.value?.getColor() ?: PrimaryFontColor
        val icon = if(category.value != null) painterResource(R.drawable.ic_category_icon)
        else painterResource(R.drawable.ic_add_category)
        val categoryName = buildAnnotatedString {
            append(category.value?.categoryName ?: stringResource(R.string.Without_category))
            appendInlineContent("drop_down_triangle")
        }
        val inlineContentMap = mapOf(
            "drop_down_triangle" to InlineTextContent(
                Placeholder(50.sp, 50.sp, PlaceholderVerticalAlign.TextCenter)
            ) {
                Icon(painter = painterResource(R.drawable.ic_drop_down_triangle),
                    contentDescription = "",
                    tint = PrimaryFontColor,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        )
        Icon(painter = icon,
            contentDescription = "",
            tint = iconColor,
            modifier = Modifier
                .padding(start = 10.dp, end = 8.dp)
                .clickable {
                    editNoteViewModel.dialogShowState.value = ShowDialogState.EditCategoryDialog
                }
        )
        Text(text = categoryName,
            inlineContent = inlineContentMap,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = PrimaryFontColor.copy(0.75f),
            modifier = Modifier
                .padding(top = 15.dp, bottom = 15.dp)
                .clickable {
                    editNoteViewModel.dialogShowState.value = ShowDialogState.EditCategoryDialog
                }
        )
    }
}

@Composable
@MustBeLocalization
fun FilesDialog(editNoteViewModel: editNoteViewModel) {
    val context = LocalContext.current
    val images = editNoteViewModel.getNoteBitmap()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = CardNoteColor,
            shape = RoundedCornerShape(25.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Файлы",
                        fontSize = 24.sp,
                        color = PrimaryFontColor,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(start = 30.dp)
                    )
                    Box(modifier = Modifier.fillMaxWidth(),contentAlignment = Alignment.CenterEnd) {
                        IconButton(onClick = { editNoteViewModel.dialogShowState.value = ShowDialogState.None}) {
                            Icon(
                                painter = painterResource(R.drawable.ic_cancel),
                                contentDescription = "",
                                tint = PrimaryFontColor,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Изображения",
                        fontSize = 24.sp,
                        color = PrimaryFontColor,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(painter = painterResource(R.drawable.ic_plus),
                                contentDescription = "",
                                tint = PrimaryFontColor,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
                if(images.isNotEmpty()) {
                    LazyRow() {

                    }
                }
                else {
                    Text(
                        text = "Нет изображений",
                        fontSize = 26.sp,
                        color = SecondoryFontColor.copy(1f),
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    )
                }
            }
        }
    }
}