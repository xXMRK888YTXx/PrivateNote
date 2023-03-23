package com.xxmrk888ytxx.privatenote.presentation.Screen.EditNoteScreen

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.xxmrk888ytxx.privatenote.BuildConfig
import com.xxmrk888ytxx.privatenote.presentation.Activity.MainActivity.ActivityController
import com.xxmrk888ytxx.privatenote.domain.RecordManager.RecorderState
import com.xxmrk888ytxx.privatenote.Utils.Exception.FailedDecryptException
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.PasswordEditText.PasswordEditText
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.Player.PlayerDialog
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.SelectionCategoryDialog
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.WarmingText.WarmingText
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.YesNoDialog.YesNoDialog
import com.xxmrk888ytxx.privatenote.presentation.Screen.EditNoteScreen.States.ShowDialogState
import com.xxmrk888ytxx.privatenote.Utils.*
import com.xxmrk888ytxx.privatenote.Utils.Const.getNoteId
import com.xxmrk888ytxx.privatenote.presentation.Activity.MainActivity.WakeLockController
import com.xxmrk888ytxx.privatenote.presentation.ActivityLaunchContacts.PickImageContract
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.AdMobBanner.AdMobBanner
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EditNoteScreen(
    editNoteViewModel: EditNoteViewModel = hiltViewModel(),
    navController: NavController,
    activityController: ActivityController,
    wakeLockController: WakeLockController
) {
    val dialogState = remember {
        editNoteViewModel.dialogShowState
    }
    val removeImageDialogState = remember {
        editNoteViewModel.getShowRemoveImageState()
    }
    val changeCategoryDialogStatus = remember {
        editNoteViewModel.getChangeCategoryDialogStatus()
    }
    val currentSelectedItem = remember {
        editNoteViewModel.getCurrentSelectedCategory()
    }
    val isAudioRecordDialogShow = remember {
        editNoteViewModel.isAudioRecorderDialogShow()
    }
    val playerDialogState = editNoteViewModel.getPlayerDialogState().Remember()
    val removeAudioDialogState = editNoteViewModel.getAudioRemoveDialogState().Remember()
    val isNeedShowAd = editNoteViewModel.isNeedShowAd().collectAsState(true)

    LaunchedEffect(key1 = editNoteViewModel, block = {
        editNoteViewModel.updateImagesCount()
        editNoteViewModel.updateAudiosCount()
        editNoteViewModel.getNote(NavArguments.bundle.getInt(getNoteId))
    })
    val textFieldFocus = remember { FocusRequester() }
    ProvideWindowInsets(consumeWindowInsets = true) {
        Column(
            Modifier
                .fillMaxSize()
                .height(LocalConfiguration.current.screenHeightDp.dp)
                .background(themeColors.mainBackGroundColor)
                .statusBarsPadding()
                .navigationBarsWithImePadding()
                .verticalScroll(rememberScrollState())
        ) {
            Toolbar(editNoteViewModel,navController)
            TitleEditField(editNoteViewModel,textFieldFocus)
            TimeCreated(editNoteViewModel)
            LazySpacer(5)
            CategorySelector(editNoteViewModel)
            if(isNeedShowAd.value) {
                AdMobBanner(
                    if(BuildConfig.DEBUG) stringResource(R.string.TestBannerKey)
                    else stringResource(R.string.EditScreenBannerKey)
                )
            }
            NoteTextEdit(editNoteViewModel,textFieldFocus)
        }
    }
    when(dialogState.value) {
       is ShowDialogState.EncryptDialog -> {CryptDialog(editNoteViewModel)}
        is ShowDialogState.DecryptDialog -> {DecriptDialog(editNoteViewModel,navController)}
        is ShowDialogState.ExitDialog -> { ExitDialog(editNoteViewModel,navController)}
        is ShowDialogState.EditCategoryDialog -> {SelectionCategoryDialog(currentSelected = currentSelectedItem,
            dialogController = editNoteViewModel.getDialogDispatcher())}
        is ShowDialogState.FileDialog -> {
            FilesDialog(editNoteViewModel,activityController,navController)
        }
        is ShowDialogState.None -> {}
    }
    val isHaveChanges = remember {
        editNoteViewModel.isHaveChanges
    }
    if(isAudioRecordDialogShow.value) {
        AudioRecordDialog(editNoteViewModel)
    }
    BackPressController.setHandler(isHaveChanges.value&&editNoteViewModel.isHavePrimaryVersion()) {
        editNoteViewModel.dialogShowState.value = ShowDialogState.ExitDialog
    }
    if(removeImageDialogState.value.first) {
        YesNoDialog(title = stringResource(R.string.Remove_image),
            onCancel = { editNoteViewModel.hideRemoveImageDialog() }) {
            removeImageDialogState.value.second()
            editNoteViewModel.hideRemoveImageDialog()
        }
    }
    if(playerDialogState.value.first&&playerDialogState.value.second != null) {
        PlayerDialog(
            controller = editNoteViewModel.getPlayerController(),
            audio = playerDialogState.value.second!!,
            onHideDialog = {editNoteViewModel.hidePlayerDialog()}
        )
    }
    if(removeAudioDialogState.value.first) {
        YesNoDialog(title = stringResource(R.string.Remove_audio),
            onCancel = { editNoteViewModel.hideAudioRemoveDialog() },
            onConfirm = {
                removeAudioDialogState.value.second()
                editNoteViewModel.hideAudioRemoveDialog()
            }
        )
    }
    LaunchedEffect(key1 = activityController, block = {
        editNoteViewModel.initActivityController(activityController)
        editNoteViewModel.initWakeLockController(wakeLockController)
    })

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AudioRecordDialog(editNoteViewModel: EditNoteViewModel) {
    val scope = rememberCoroutineScope()
    val recordState = editNoteViewModel.getAudioRecorderState()
        .collectAsState(RecorderState.RecordDisable,scope.coroutineContext)
    LaunchedEffect(key1 = recordState.value) {
        if(recordState.value == RecorderState.RecordDisable) editNoteViewModel.stopRecordStopWatch()
        else editNoteViewModel.startRecordStopWatch((recordState.value as RecorderState.RecordingNow).startRecordNow)
    }
//    val recordVolume = if(recordState.value is RecorderState.RecordingNow)
//            (recordState.value as RecorderState.RecordingNow).volume % 150 else null
    Dialog(onDismissRequest = { editNoteViewModel.hideAudioRecorderDialog() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = themeColors.cardColor,
            shape = RoundedCornerShape(20.dp)
        )
        {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                RecordTimer(editNoteViewModel,recordState.value)
                Row(
                    modifier = Modifier.padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(contentAlignment = Alignment.CenterEnd) {
                        IconButton(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(60.dp)
                                .background(Color.Red),
                            onClick = {
                                if(recordState.value == RecorderState.RecordDisable) editNoteViewModel.startRecord()
                                else editNoteViewModel.stopRecord()
                            },
                        ) {
                            Icon(painter =
                            if(recordState.value == RecorderState.RecordDisable) painterResource(R.drawable.ic_record)
                            else painterResource(R.drawable.ic_stop),
                                contentDescription = "",
                                tint = themeColors.primaryFontColor
                            )
                        }

                    }
                    }
                }
        }
    }
}

@Composable
fun RecordTimer(editNoteViewModel: EditNoteViewModel,recorderState: RecorderState) {
    val currentRecordTime = remember {
        editNoteViewModel.getCurrentRecordTime()
    }
    Text(
        text = currentRecordTime.value,
        fontSize = 30.sp,
        color = themeColors.primaryFontColor,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(bottom = 10.dp)
    )
}

@Composable
fun TimeCreated(editNoteViewModel: EditNoteViewModel) {
    if(editNoteViewModel.currentTime.value == 0L) return
    Text(text = editNoteViewModel.currentTime.value.secondToData(LocalContext.current),
        color = themeColors.primaryFontColor.copy(0.5f),
        fontSize = 14.sp,
        fontWeight = FontWeight.Light,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 13.dp)
    )
}

@Composable
fun NoteTextEdit(editNoteViewModel: EditNoteViewModel, textFieldFocus: FocusRequester) {
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
                cursorColor = themeColors.cursorColor,
                placeholderColor = themeColors.titleHintColor.copy(0.6f)
            ),
            textStyle = TextStyle(
                color = themeColors.primaryFontColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
            )

        )
    }

@Composable
fun Toolbar(editNoteViewModel: EditNoteViewModel, navController: NavController) {
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
            if(editNoteViewModel.textField.value.isEmpty()&&
                editNoteViewModel.titleTextField.value.isEmpty()&&!editNoteViewModel.isHaveImages()) {
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
    val undoArrowColor = if(isUndoAvailable.value) themeColors.primaryFontColor else themeColors.primaryFontColor.copy(0.4f)
    val redoArrowColor = if(isRedoAvailable.value) themeColors.primaryFontColor else themeColors.primaryFontColor.copy(0.4f)
    Row(
        Modifier
            .fillMaxWidth()
            .background(themeColors.mainBackGroundColor),
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
                tint = themeColors.primaryFontColor
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
                           tint = themeColors.primaryFontColor
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
                           tint = themeColors.primaryFontColor
                       )
                   }
                   Box(
                   ) {
                       DropdownMenu(expanded = isDropDownMenuShow.value,
                           onDismissRequest = { isDropDownMenuShow.value = false },
                           modifier = Modifier.background(themeColors.dropDownMenuColor)
                       ) {
                           dropDownItemList.forEach {
                               if(it.isEnable) {
                                   DropdownMenuItem(onClick = { it.onClick.invoke() }) {
                                       Text(it.text,color = themeColors.primaryFontColor)
                                   }
                               }
                           }
                       }
                   }
               }
    }
}

@Composable
fun TitleEditField(editNoteViewModel: EditNoteViewModel, textFieldFocus: FocusRequester) {
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
                .padding(top = 10.dp),
            singleLine = true,
            placeholder = {Text(stringResource(R.string.Title),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            )},
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White.copy(0f),
                focusedIndicatorColor = Color.White.copy(0f),
                unfocusedIndicatorColor = Color.White.copy(0f),
                cursorColor = themeColors.cursorColor,
                placeholderColor = themeColors.titleHintColor.copy(0.6f)
            ),
            textStyle = TextStyle(
                color = themeColors.primaryFontColor,
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
fun CryptDialog(editNoteViewModel: EditNoteViewModel) {
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
                Modifier.background(themeColors.mainBackGroundColor)

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
                        backgroundColor = themeColors.largeButtonColor,
                        disabledContentColor = themeColors.largeButtonColor.copy(0.3f),
                        disabledBackgroundColor = themeColors.largeButtonColor.copy(0.3f)
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
fun PasswordTextEdit(editNoteViewModel: EditNoteViewModel, password: MutableState<String>,
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
            textColor = themeColors.primaryFontColor,
            backgroundColor = themeColors.searchColor,
            placeholderColor = themeColors.primaryFontColor.copy(0.7f),
            focusedBorderColor = themeColors.searchColor,
            focusedLabelColor = themeColors.primaryFontColor.copy(alpha = 0.85f),
            cursorColor = themeColors.cursorColor,
            unfocusedLabelColor = themeColors.primaryFontColor.copy(0.6f)
        ),
        textStyle = TextStyle(fontSize = 16.sp),
        keyboardOptions = KeyboardOptions(autoCorrect = false,
        keyboardType = KeyboardType.Password,
            ),
            visualTransformation = PasswordVisualTransformation()
    )
}

@Composable
fun RepitPasswordTextEdit(editNoteViewModel: EditNoteViewModel, repitPassword:MutableState<String>,
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
            textColor = themeColors.primaryFontColor,
            backgroundColor = themeColors.searchColor,
            placeholderColor = themeColors.primaryFontColor.copy(0.7f),
            focusedBorderColor = themeColors.searchColor,
            focusedLabelColor = themeColors.primaryFontColor.copy(alpha = 0.85f),
            cursorColor = themeColors.cursorColor,
            unfocusedLabelColor = themeColors.primaryFontColor.copy(0.6f)
        ),
        textStyle = TextStyle(fontSize = 16.sp),
        keyboardOptions = KeyboardOptions(autoCorrect = false,
            keyboardType = KeyboardType.Password,
        ),
        visualTransformation = PasswordVisualTransformation()
    )
}

@Composable
fun DecriptDialog(editNoteViewModel: EditNoteViewModel, navController: NavController) {
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
                Modifier.background(themeColors.mainBackGroundColor)

            ) {
                PasswordEditText(titleText,passwordText) {
                    coroutineScope.launch{
                        isLoad.value = true
                        try {
                            editNoteViewModel.decrypt(passwordText.value)
                        }catch (e:FailedDecryptException) {
                            titleText.value = context.getString(R.string.Invalid_password)
                        }
                        isLoad.value = false
                    }
                }

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
                            backgroundColor = themeColors.largeButtonColor,
                            disabledContentColor = themeColors.largeButtonColor.copy(0.3f),
                            disabledBackgroundColor = themeColors.largeButtonColor.copy(0.3f)
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
fun ExitDialog(editNoteViewModel: EditNoteViewModel, navController: NavController) {
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
                        .background(themeColors.cardColor),
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    Text(
                        text = stringResource(R.string.Save_changes_question),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(bottom = 25.dp, top = 25.dp)
                            .fillMaxWidth(),
                        fontSize = 17.sp,
                        color = themeColors.primaryFontColor,
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
                                backgroundColor = themeColors.titleHintColor,
                            )
                        ) {
                            Text(text = stringResource(R.string.Not_save),
                                color = themeColors.primaryFontColor
                            )
                        }
                        OutlinedButton(
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = themeColors.secondaryColor,
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
                            color = themeColors.primaryFontColor
                                )
                        }
                    }
                }
            }
    }
}
@Composable
fun CategorySelector(editNoteViewModel: EditNoteViewModel) {
    val category = remember {
        editNoteViewModel.getCategory()
    }
    Row (
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val iconColor = category.value?.getColor() ?: themeColors.primaryFontColor
        val icon = if(category.value != null) painterResource(R.drawable.ic_category_icon)
        else painterResource(R.drawable.ic_add_category)
        val categoryName = buildAnnotatedString {
            append(category.value?.categoryName ?: stringResource(R.string.Without_category))
            appendInlineContent("drop_down_triangle")
        }
        val inlineContentMap = mapOf(
            "drop_down_triangle" to InlineTextContent(
                Placeholder(30.sp, 30.sp, PlaceholderVerticalAlign.TextCenter)
            ) {
                Icon(painter = painterResource(R.drawable.ic_drop_down_triangle),
                    contentDescription = "",
                    tint = themeColors.primaryFontColor,
                    modifier = Modifier.padding(top = 1.dp)
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
            color = themeColors.primaryFontColor.copy(0.75f),
            modifier = Modifier
                .padding(top = 10.dp, bottom = 15.dp)
                .clickable {
                    editNoteViewModel.dialogShowState.value = ShowDialogState.EditCategoryDialog
                }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun FilesDialog(
    editNoteViewModel: EditNoteViewModel,
    activityController: ActivityController,
    navController: NavController
) {
    val permission = rememberPermissionState(permission = Manifest.permission.RECORD_AUDIO)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val images = editNoteViewModel.getNoteImage().collectAsState(listOf(),scope.coroutineContext)
    val audios = editNoteViewModel.getAudioFiles().collectAsState(listOf(),scope.coroutineContext)
    val imageLoadState = editNoteViewModel.getImageRepositoryLoadState().collectAsState()
    val audioLoadState = editNoteViewModel.getAudioRepositoryLoadState().collectAsState()

    val pickImageContract = rememberLauncherForActivityResult(
        contract = PickImageContract(),
        onResult = editNoteViewModel::onImagePicked
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = themeColors.cardColor,
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
                        text = stringResource(R.string.Files),
                        fontSize = 24.sp,
                        color = themeColors.primaryFontColor,
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
                                tint = themeColors.primaryFontColor,
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
                            text = stringResource(R.string.Audio_records),
                        fontSize = 24.sp,
                        color = themeColors.primaryFontColor,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                        Row {
                            IconButton(onClick = {
                                editNoteViewModel.showAddAudioDropDown()
                            }) {
                                if(audioLoadState.value is LoadRepositoryState.Loaded) {
                                    Icon(painter = painterResource(R.drawable.ic_plus),
                                        contentDescription = "",
                                        tint = themeColors.primaryFontColor,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                                else {
                                    Box(modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.CenterEnd) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }
                                }
                            }
                        }
                        AddAudioDropDown(editNoteViewModel = editNoteViewModel)
                    }

                }
                if(audios.value.isNotEmpty()) {
                    LazyRow(Modifier.padding(start = 5.dp)) {
                            items(audios.value) {
                                val isOptionDropDownVisible = remember {
                                    mutableStateOf(false)
                                }
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .combinedClickable(
                                            onClick = {
                                                editNoteViewModel.showPlayerDialog(it)
                                            },
                                            onLongClick = {
                                                isOptionDropDownVisible.value = true
                                            }
                                        )
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(themeColors.secondaryColor)
                                            .size(40.dp),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Icon(painter = painterResource(R.drawable.ic_play),
                                            contentDescription = "",
                                            tint = themeColors.primaryFontColor,
                                            modifier = Modifier.align(Alignment.Center)
                                        )
                                    }
                                    Text(
                                        text = it.duration.milliSecondToSecond(),
                                        color = themeColors.primaryFontColor,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                    )
                                }
                                FileOptionsDropBox(isVisible = isOptionDropDownVisible.value,
                                    onHide = { isOptionDropDownVisible.value = false },
                                    onDelete = { editNoteViewModel.showAudioRemoveDialogState(it.id) },
                                    onExportFile = {
                                        editNoteViewModel.exportAudio(it)
                                    })
                            }
                    }
                }
                else {
                    Text(
                        text = stringResource(R.string.No_Audio),
                        fontSize = 26.sp,
                        color = themeColors.secondaryFontColor.copy(1f),
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 5.dp,
                                bottom = 5.dp
                            )
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.Images),
                        fontSize = 24.sp,
                        color = themeColors.primaryFontColor,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                        Row {
                            IconButton(onClick = { editNoteViewModel.toDrawScreen(navController) }) {
                                Icon(painter = painterResource(R.drawable.ic_baseline_draw_24),
                                    contentDescription = "",
                                    tint = themeColors.primaryFontColor,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            if(imageLoadState.value is LoadRepositoryState.Loaded) {
                                IconButton(onClick = { editNoteViewModel.addImage(pickImageContract) }) {
                                    Icon(painter = painterResource(R.drawable.ic_plus),
                                        contentDescription = "",
                                        tint = themeColors.primaryFontColor,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }else {
                                Box(modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.CenterEnd) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }

                        }
                    }
                }
                if(images.value.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier.padding(7.dp)
                    ) {
                        items(images.value) {
                            val isOptionDropDownVisible = remember {
                                mutableStateOf(false)
                            }
                            AsyncImage(model = editNoteViewModel
                                .getImageRequest(context,it.image.getBytes()),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .combinedClickable(
                                        onClick = {
                                            editNoteViewModel
                                                .openImageInImageViewer(
                                                    it.image,
                                                    activityController
                                                )
                                        },
                                        onLongClick = {
                                            //editNoteViewModel.showRemoveImageDialog(it.id)
                                            isOptionDropDownVisible.value = true
                                        }
                                    )
                                    .padding(end = 10.dp)
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(10))
                            )
                            FileOptionsDropBox(isVisible = isOptionDropDownVisible.value,
                                onHide = { isOptionDropDownVisible.value = false },
                                onDelete = { editNoteViewModel.showRemoveImageDialog(it.id) },
                                onExportFile = {
                                    editNoteViewModel.exportImage(it)
                                }
                            )
                          }
                    }
                }
                else {
                    Text(
                        text = stringResource(R.string.No_Images),
                        fontSize = 26.sp,
                        color = themeColors.secondaryFontColor.copy(1f),
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddAudioDropDown(editNoteViewModel: EditNoteViewModel) {
    val state = editNoteViewModel.addAudioDropDownState().Remember()
    val permission = rememberPermissionState(permission = Manifest.permission.RECORD_AUDIO)
    val items = listOf(
        DropDownItem(
            stringResource(R.string.Record),
            onClick = {
                editNoteViewModel.requestAudioPermission(permission)
                editNoteViewModel.hideAddAudioDropDown()
            }
        ),
        DropDownItem(
            stringResource(R.string.From_device),
            onClick = {
                editNoteViewModel.selectAudioFromExternalStorage()
                editNoteViewModel.hideAddAudioDropDown()
            }
        )
    )
   Box() {
       DropdownMenu(
           expanded = state.value,
           onDismissRequest = { editNoteViewModel.hideAddAudioDropDown() },
           modifier = Modifier
               .background(themeColors.dropDownMenuColor)
               .heightIn(max = 200.dp)
       ) {
           items.forEach {
               DropdownMenuItem(onClick = { it.onClick() }) {
                   Text(text = it.text,
                       fontSize = 16.sp,
                       fontWeight = FontWeight.Medium,
                       color = themeColors.primaryFontColor
                   )
               }
           }
       }
   }
}

@Composable
fun FileOptionsDropBox(
    isVisible:Boolean,
    onHide:() -> Unit,
    onDelete:() -> Unit,
    onExportFile:() -> Unit
) {
    val items = listOf(
        DropDownItem(
            text = stringResource(R.string.Delete),
            onClick = {
                onDelete()
                onHide()
            }
        ),
        DropDownItem(
            text = stringResource(R.string.Export),
            onClick = {
                onExportFile()
                onHide()
            }
        )
    )
    Box() {
        DropdownMenu(
            expanded = isVisible,
            onDismissRequest = { onHide() },
            modifier = Modifier
                .background(themeColors.dropDownMenuColor)
                .heightIn(max = 200.dp)
        ) {
            items.forEach {
                DropdownMenuItem(onClick = { it.onClick() }) {
                    Text(text = it.text,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = themeColors.primaryFontColor
                    )
                }
            }
        }
    }

}
