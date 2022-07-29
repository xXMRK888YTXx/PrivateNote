package com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.Exception.FailedDecryptException
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen.States.ShowDialogState
import com.xxmrk888ytxx.privatenote.Utils.Const.getNoteId
import com.xxmrk888ytxx.privatenote.Utils.NavArguments
import com.xxmrk888ytxx.privatenote.Utils.secondToData
import com.xxmrk888ytxx.privatenote.ui.theme.*
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EditNoteScreen(editNoteViewModel: editNoteViewModel = hiltViewModel(), navController: NavController) {
    val dialogState = remember {
        editNoteViewModel.dialogShowState
    }
    LaunchedEffect(key1 = editNoteViewModel, block = {
        editNoteViewModel.getNote(NavArguments.bundle.getInt(getNoteId))
    })
        Column(
            Modifier
                .fillMaxSize()
                .background(MainBackGroundColor)
        ) {
            Toolbar(editNoteViewModel,navController)
            TitleEditField(editNoteViewModel)
            TimeCreated(editNoteViewModel)
            NoteTextEdit(editNoteViewModel)
        }
    when(dialogState.value) {
       is ShowDialogState.EncryptDialog -> {CryptDialog(editNoteViewModel)}
        is ShowDialogState.DecryptDialog -> {DecriptDialog(editNoteViewModel,navController)}
        is ShowDialogState.None -> {}
    }
}

@Composable
fun TimeCreated(editNoteViewModel: editNoteViewModel) {
    if(editNoteViewModel.currentTime.value == 0L) return
    Text(text = editNoteViewModel.currentTime.value.secondToData(LocalContext.current),
        color = Color.White.copy(0.5f),
        fontSize = 14.sp,
        fontWeight = FontWeight.Light,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 13.dp)
    )
}

@Composable
fun NoteTextEdit(editNoteViewModel: editNoteViewModel) {
    val text = remember {
        editNoteViewModel.textField
    }
    val isHideText = remember {
        editNoteViewModel.isHideText
    }
    if(!isHideText.value) {
        TextField(
            value = text.value,
            onValueChange = {text.value = it},
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 0.dp),
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
                color = Color.White.copy(0.9f),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
            )

        )
    }
    else {
        TextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 0.dp),
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
                color = Color.White.copy(0.9f),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
            )

        )
    }
}

@Composable
fun Toolbar(editNoteViewModel: editNoteViewModel,navController: NavController) {
    val isDropDownMenuShow = remember {
        editNoteViewModel.isDropDownMenuShow
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
        DropDownItem(stringResource(R.string.cancel_changes),editNoteViewModel.isHavePrimaryVersion()) {
            editNoteViewModel.notSaveChanges(navController)
        }
    )
    Row(
        Modifier
            .fillMaxWidth()
            .background(MainBackGroundColor),
        verticalAlignment = Alignment.CenterVertically,
        ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back_arrow),
            contentDescription = "Back Arrow",
            modifier = Modifier
                .background(MainBackGroundColor)
                .clickable { editNoteViewModel.backToMainScreen(navController) }
                .size(40.dp)
                .padding(top = 10.dp, start = 15.dp),
            tint = Color.White
            )
               Row(
                   Modifier.fillMaxWidth(),
                   verticalAlignment = Alignment.CenterVertically,
                   horizontalArrangement = Arrangement.End
               ) {
                   Icon(
                       painter = painterResource(id = R.drawable.ic_dots),
                       contentDescription = "Dots",
                       modifier = Modifier
                           .background(MainBackGroundColor)
                           .clickable { isDropDownMenuShow.value = true }
                           .size(40.dp)
                           .padding(top = 10.dp),
                       tint = Color.White
                   )
                   Box(
                       modifier = Modifier
                   ) {
                       DropdownMenu(expanded = isDropDownMenuShow.value,
                           onDismissRequest = { isDropDownMenuShow.value = false },
                           modifier = Modifier.background(DropDownMenuColor)
                       ) {
                           dropDownItemList.forEach {
                               if(it.isEnable) {
                                   DropdownMenuItem(onClick = { it.onClick.invoke() }) {
                                       Text(it.text,color = Color.White.copy(0.9f))
                                   }
                               }
                           }
                       }
                   }
               }
    }
}

@Composable
fun TitleEditField(editNoteViewModel: editNoteViewModel) {
    val isHideText = remember {
        editNoteViewModel.isHideText
    }
    val text = remember {
        editNoteViewModel.titleTextField
    }
    if(!isHideText.value) {
        TextField(value = text.value,
            onValueChange = {text.value = it},
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
                color = Color.White.copy(0.9f),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,

                )
        )
    }
    else {
        TextField(value = "",
            onValueChange = {},
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
                color = Color.White.copy(0.9f),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,

                )
        )
    }
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
            Modifier.fillMaxWidth(),
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
                        backgroundColor = Color.White.copy(0.9f),
                        disabledContentColor = Color.Black.copy(0.3f),
                        disabledBackgroundColor = Color.White.copy(0.3f)
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
            textColor = Color.White,
            backgroundColor = SearchColor,
            placeholderColor = Color.White.copy(0.7f),
            focusedBorderColor = SearchColor,
            focusedLabelColor = Color.White.copy(alpha = 0.85f),
            cursorColor = CursorColor,
            unfocusedLabelColor = Color.White.copy(0.6f)
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
            textColor = Color.White,
            backgroundColor = SearchColor,
            placeholderColor = Color.White.copy(0.7f),
            focusedBorderColor = SearchColor,
            focusedLabelColor = Color.White.copy(alpha = 0.85f),
            cursorColor = CursorColor,
            unfocusedLabelColor = Color.White.copy(0.6f)
        ),
        textStyle = TextStyle(fontSize = 16.sp),
        keyboardOptions = KeyboardOptions(autoCorrect = false,
            keyboardType = KeyboardType.Password,
        ),
        visualTransformation = PasswordVisualTransformation()
    )
}
@Composable
fun WarmingText(text:String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painter = painterResource(id = R.drawable.ic_error),
            contentDescription = "Error",
            tint = Color.Red,
            modifier = Modifier.padding(end = 10.dp)
            )
        Text(text = text,
                color = Color.White.copy(0.9f),
            fontSize = 18.sp,
            fontStyle = FontStyle.Italic,
            )

    }
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
                PasswordTextEdit(titleText,passwordText,)
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
                            backgroundColor = Color.White.copy(0.9f),
                            disabledContentColor = Color.Black.copy(0.3f),
                            disabledBackgroundColor = Color.White.copy(0.3f)
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
//use in decrypt dialog
@Composable
fun PasswordTextEdit(titleText:MutableState<String>, password: MutableState<String>) {
    val context = LocalContext.current
    OutlinedTextField(value = password.value,
        onValueChange = {password.value = it;
                      if(titleText.value == context.getString(R.string.Invalid_password))
                          titleText.value = context.getString(R.string.Enter_password)
                        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(100))
            .padding(15.dp),
        label = { Text(text = titleText.value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        },
        isError = titleText.value == stringResource(id = R.string.Invalid_password),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.White,
            backgroundColor = SearchColor,
            placeholderColor = Color.White.copy(0.7f),
            focusedBorderColor = SearchColor,
            focusedLabelColor = Color.White.copy(alpha = 0.85f),
            cursorColor = CursorColor,
            unfocusedLabelColor = Color.White.copy(0.6f)
        ),
        textStyle = TextStyle(fontSize = 16.sp),
        keyboardOptions = KeyboardOptions(autoCorrect = false,
            keyboardType = KeyboardType.Password,
        ),
        visualTransformation = PasswordVisualTransformation()
    )
}
