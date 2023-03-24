package com.xxmrk888ytxx.privatenote.presentation.Screen.BackupSettingsScreen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.Remember
import com.xxmrk888ytxx.privatenote.Utils.themeColors
import com.xxmrk888ytxx.privatenote.domain.BackupManager.isAllFalse
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsAutoBackupRepository.BackupSettings
import com.xxmrk888ytxx.privatenote.presentation.Activity.MainActivity.ActivityController
import com.xxmrk888ytxx.privatenote.presentation.ActivityLaunchContacts.CreateExternalFileContract
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.DontKillMyAppDialog.DontKillMyAppDialog
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.YesNoButtons.YesNoButton
import com.xxmrk888ytxx.privatenote.presentation.Screen.ThemeSettingsScreen.TopBar

@Composable
fun BackupSettingsScreen(
    backupSettingsViewModel: BackupSettingsViewModel = hiltViewModel(),
    navController: NavController,
    activityController: ActivityController,
) {
    val settings = backupSettingsViewModel.getBackupSettings().collectAsState(BackupSettings())
    val restoreBackupDialogState = backupSettingsViewModel.getRestoreBackupDialogState().Remember()
    val createBackupDialogState = backupSettingsViewModel.getCreateBackupDialogState().Remember()
    val dontKillMyAppDialogState = backupSettingsViewModel.getDontKillMyAppDialogState().Remember()
    val isLoadDialogShow = backupSettingsViewModel.isShowLoadDialog().Remember()
    LaunchedEffect(key1 = activityController, block = {
        backupSettingsViewModel.initActivityController(activityController)
    })
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        TopBar(navController)
        Text(
            text = stringResource(R.string.Backup),
            fontWeight = FontWeight.W800,
            fontSize = 30.sp,
            color = themeColors.primaryFontColor,
            modifier = Modifier.padding(start = 20.dp, bottom = 15.dp)
        )
        AutoBackupSettingsList(backupSettingsViewModel, settings)
    }
    if (restoreBackupDialogState.value) {
        RestoreBackupDialog(backupSettingsViewModel)
    }
    if (createBackupDialogState.value) {
        CreateBackupDialog(backupSettingsViewModel)
    }
    if (isLoadDialogShow.value) {
        LoadDialog()
    }
    if (dontKillMyAppDialogState.value.first) {
        DontKillMyAppDialog(
            onExecuteAfterCloseDialog = {
                dontKillMyAppDialogState.value.second?.invoke()
            },
            onDismissRequest = {
                backupSettingsViewModel.hideDontKillMyAppDialog()
            },
            onHideDialogForever = {
                backupSettingsViewModel.hideDontKillMyAppDialogForever()
            }
        )
    }
}

@Composable
fun getParamsList(
    backupSettingsViewModel: BackupSettingsViewModel,
    settings: BackupSettings,
): List<BackupParams> {
    return listOf(
        BackupParams(
            title = stringResource(R.string.Not_copy_encrypt_note),
            settingsState = settings.isBackupNotEncryptedNote,
            isEnable = settings.isEnableLocalBackup || settings.isEnableGDriveBackup,
            updateStateInAutoBackupParams = {
                backupSettingsViewModel.updateAutoBackupParamsIsBackupNotEncryptedNote(it)
            },
            updateStateInDialog = { state ->
                backupSettingsViewModel.changeBackupParamsInDialog {
                    it.copy(isBackupNotEncryptedNote = state)
                }
            }
        ),
        BackupParams(
            title = stringResource(R.string.Copy_encrypt_note),
            settingsState = settings.isBackupEncryptedNote,
            isEnable = settings.isEnableLocalBackup || settings.isEnableGDriveBackup,
            updateStateInAutoBackupParams = {
                backupSettingsViewModel.updateAutoBackupParamsIsBackupEncryptedNote(it)
            },
            updateStateInDialog = { state ->
                backupSettingsViewModel.changeBackupParamsInDialog {
                    it.copy(isBackupEncryptedNote = state)
                }
            }
        ),
        BackupParams(
            title = stringResource(R.string.Copy_note_images),
            settingsState = settings.isBackupNoteImages,
            isEnable = settings.isEnableLocalBackup || settings.isEnableGDriveBackup,
            updateStateInAutoBackupParams = {
                backupSettingsViewModel.updateAutoBackupParamsIsBackupNoteImages(it)
            },
            updateStateInDialog = { state ->
                backupSettingsViewModel.changeBackupParamsInDialog {
                    it.copy(isBackupNoteImages = state)
                }
            }
        ),
        BackupParams(
            title = stringResource(R.string.Copy_notes_audio),
            settingsState = settings.isBackupNoteAudio,
            isEnable = settings.isEnableLocalBackup || settings.isEnableGDriveBackup,
            updateStateInAutoBackupParams = {
                backupSettingsViewModel.updateAutoBackupParamsIsBackupNoteAudio(it)
            },
            updateStateInDialog = { state ->
                backupSettingsViewModel.changeBackupParamsInDialog {
                    it.copy(isBackupNoteAudio = state)
                }
            }
        ),
        BackupParams(
            title = stringResource(R.string.Copy_note_category),
            settingsState = settings.isBackupNoteCategory,
            isEnable = settings.isEnableLocalBackup || settings.isEnableGDriveBackup,
            updateStateInAutoBackupParams = {
                backupSettingsViewModel.updateAutoBackupParamsIsBackupNoteCategory(it)
            },
            updateStateInDialog = { state ->
                backupSettingsViewModel.changeBackupParamsInDialog {
                    it.copy(isBackupNoteCategory = state)
                }
            }
        ),
        BackupParams(
            title = stringResource(R.string.Copy_not_сompleted_todo),
            settingsState = settings.isBackupNotCompletedTodo,
            isEnable = settings.isEnableLocalBackup || settings.isEnableGDriveBackup,
            updateStateInAutoBackupParams = {
                backupSettingsViewModel.updateAutoBackupParamsIsBackupNotCompletedTodo(it)
            },
            updateStateInDialog = { state ->
                backupSettingsViewModel.changeBackupParamsInDialog {
                    it.copy(isBackupNotCompletedTodo = state)
                }
            }
        ),
        BackupParams(
            title = stringResource(R.string.Copy_сompleted_todo),
            settingsState = settings.isBackupCompletedTodo,
            isEnable = settings.isEnableLocalBackup || settings.isEnableGDriveBackup,
            updateStateInAutoBackupParams = {
                backupSettingsViewModel.updateAutoBackupParamsIsBackupCompletedTodo(it)
            },
            updateStateInDialog = { state ->
                backupSettingsViewModel.changeBackupParamsInDialog {
                    it.copy(isBackupCompletedTodo = state)
                }
            }
        ),
    )
}

@Composable
fun AutoBackupSettingsList(
    backupSettingsViewModel: BackupSettingsViewModel,
    settings: State<BackupSettings>,
) {
    val paramsList: List<BackupParams> = getParamsList(backupSettingsViewModel, settings.value)
    val localAutoBackupDropDownState =
        backupSettingsViewModel.isRepeatLocalAutoBackupTimeDropDownVisible().Remember()
    val gDriveAutoBackupDropDownState = backupSettingsViewModel
        .isRepeatGDriveAutoBackupTimeDropDownVisible().Remember()
    val googleAccount = backupSettingsViewModel.getGoogleAccount().Remember()

    val context = LocalContext.current

    val selectFileForLocalAutoBackupContract = rememberLauncherForActivityResult(
        contract = CreateExternalFileContract(context),
        onResult = backupSettingsViewModel::onFileForLocalAutoBackupSelected
    )

    Column(
        modifier = Modifier.padding(top = 10.dp)
    ) {
    }
    LazyColumn() {
        item {
            MainBackupSettings(backupSettingsViewModel)
            Text(
                text = stringResource(R.string.Auto_Backup_Settings_Local),
                fontWeight = FontWeight.W800,
                fontSize = 20.sp,
                color = themeColors.primaryFontColor,
                modifier = Modifier.padding(start = 10.dp)
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp), color = themeColors.primaryFontColor
            )
        }
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.Backup_is_active),
                    fontWeight = FontWeight.W800,
                    fontSize = 18.sp,
                    color = themeColors.primaryFontColor,
                    modifier = Modifier.padding(start = 10.dp, bottom = 0.dp)
                )
                Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.fillMaxWidth()) {
                    Switch(
                        checked = settings.value.isEnableLocalBackup,
                        onCheckedChange = {
                            if (it) {
                                backupSettingsViewModel.showDontKillMyAppDialog {
                                    backupSettingsViewModel.updateBackupState(it)
                                }
                            } else {
                                backupSettingsViewModel.updateBackupState(it)
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = themeColors.secondaryColor,
                            uncheckedThumbColor = themeColors.secondaryFontColor
                        ),
                    )
                }
            }
        }
        item {
            SelectBackupPathButton(isPathSelected = settings.value.backupPath != null,
                onClick = {
                    backupSettingsViewModel.selectFileForLocalAutoBackup(
                        selectFileForLocalAutoBackupContract
                    )
                })
        }
        item {
            SelectRepeatBackupButton(
                getCurrentTime = {
                    settings.value.repeatLocalAutoBackupTimeAtHours
                },
                isVisible = localAutoBackupDropDownState.value,
                onChange = {
                    backupSettingsViewModel.hideRepeatLocalAutoBackupTimeDropDown()
                    backupSettingsViewModel.updateLocalAutoBackupTime(it)
                },
                onShow = {
                    backupSettingsViewModel.showRepeatLocalAutoBackupTimeDropDown()
                },
                onHide = {
                    backupSettingsViewModel.hideRepeatLocalAutoBackupTimeDropDown()
                }
            )
        }
        item {
//            Divider(modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 10.dp), color = ThemeManager.PrimaryFontColor)
            Text(
                text = stringResource(R.string.Auto_Backup_Settings_GDrive),
                fontWeight = FontWeight.W800,
                fontSize = 20.sp,
                color = themeColors.primaryFontColor,
                modifier = Modifier.padding(start = 10.dp, top = 10.dp)
            )
        }
        item {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp), color = themeColors.primaryFontColor
            )
        }
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.Backup_is_active),
                    fontWeight = FontWeight.W800,
                    fontSize = 18.sp,
                    color = themeColors.primaryFontColor,
                    modifier = Modifier.padding(start = 10.dp, bottom = 0.dp)
                )
                Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.fillMaxWidth()) {
                    Switch(
                        checked = settings.value.isEnableGDriveBackup,
                        onCheckedChange = {
                            if (it) {
                                backupSettingsViewModel.showDontKillMyAppDialog {
                                    backupSettingsViewModel.updateIsEnableGDriveBackup(it)
                                }
                            } else {
                                backupSettingsViewModel.updateIsEnableGDriveBackup(it)
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = themeColors.secondaryColor,
                            uncheckedThumbColor = themeColors.secondaryFontColor
                        ),
                    )
                }
            }
        }
        item {
            SettingsRadioButton(
                text = stringResource(R.string.Load_only_from_WIFI),
                state = settings.value.isUploadToGDriveOnlyForWiFi,
                onChange = {
                    backupSettingsViewModel.updateUploadToGDriveOnlyForWiFi(it)
                })
        }
        if (googleAccount.value == null) {
            item {
                GoogleSingInButton(
                    onAuth = {
                        backupSettingsViewModel.sendGoogleAuthRequest()
                    }

                )
            }
        }
        if (googleAccount.value != null) {
            item {
                SettingsButton(text = stringResource(R.string.Login_out_from_account)) {
                    backupSettingsViewModel.loginOutGoogleAccount()
                }
            }
        }
        item {
            SelectRepeatBackupButton(
                getCurrentTime = {
                    settings.value.repeatGDriveAutoBackupTimeAtHours
                },
                isVisible = gDriveAutoBackupDropDownState.value,
                onChange = {
                    backupSettingsViewModel.hideRepeatGDriveAutoBackupTimeDropDownVisible()
                    backupSettingsViewModel.updateGDriveAutoBackupTime(it)
                },
                onShow = {
                    backupSettingsViewModel.showRepeatGDriveAutoBackupTimeDropDownVisible()
                },
                onHide = {
                    backupSettingsViewModel.hideRepeatGDriveAutoBackupTimeDropDownVisible()
                }
            )
        }
        item {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp), color = themeColors.primaryFontColor
            )
        }
        items(paramsList) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = it.title,
                    fontWeight = FontWeight.W800,
                    fontSize = 16.sp,
                    color = themeColors.primaryFontColor,
                    modifier = Modifier.padding(start = 10.dp, bottom = 10.dp)
                )
                Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.fillMaxWidth()) {
                    Switch(
                        checked = it.settingsState,
                        onCheckedChange = { newState ->
                            it.updateStateInAutoBackupParams(newState)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = themeColors.secondaryColor,
                            uncheckedThumbColor = themeColors.secondaryFontColor
                        ),
                        enabled = it.isEnable
                    )
                }
            }
        }
    }
}

@Composable
fun MainBackupSettings(
    backupSettingsViewModel: BackupSettingsViewModel,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable {
                backupSettingsViewModel.showCreateBackupDialogState()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.Create_backup_now),
            fontWeight = FontWeight.W800,
            fontSize = 18.sp,
            color = themeColors.primaryFontColor,
            modifier = Modifier.padding(start = 10.dp, bottom = 15.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = "",
                tint = themeColors.primaryFontColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }

    Row(
        Modifier
            .fillMaxWidth()
            .clickable {
                backupSettingsViewModel.showRestoreBackupDialog()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.Restore_backup),
            fontWeight = FontWeight.W800,
            fontSize = 18.sp,
            color = themeColors.primaryFontColor,
            modifier = Modifier.padding(start = 10.dp, bottom = 10.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = "",
                tint = themeColors.primaryFontColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }

}

@Composable
fun CreateBackupDialog(backupSettingsViewModel: BackupSettingsViewModel) {
    val currentBackupSettings = backupSettingsViewModel.getBackupSettingsInDialog().Remember()
    val backupParamsList =
        getParamsList(backupSettingsViewModel, currentBackupSettings.value ?: BackupSettings())
    Dialog(onDismissRequest = { backupSettingsViewModel.hideCreateBackupDialogState() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            backgroundColor = themeColors.cardColor,
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                LazyColumn(Modifier.fillMaxWidth()) {
                    item {
                        SelectBackupPathButton(
                            isPathSelected = currentBackupSettings.value?.backupPath != null,
                            onClick = {
                                backupSettingsViewModel.createBackupFile()
                            })
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth(),
                            color = themeColors.primaryFontColor
                        )
                    }
                    items(backupParamsList) {
                        BackupItemCheckBox(
                            title = it.title,
                            state = it.settingsState,
                            onChange = { newState ->
                                it.updateStateInDialog(newState)
                            }
                        )
                    }
                    item {
                        YesNoButton(
                            onConfirm = {
                                backupSettingsViewModel.startBackup()
                                backupSettingsViewModel.hideCreateBackupDialogState()
                            },
                            onCancel = {
                                backupSettingsViewModel.hideCreateBackupDialogState()
                            },
                            isOkButtonEnable = currentBackupSettings.value?.backupPath != null
                        )
                    }

                }
            }
        }
    }
}


@Composable
fun RestoreBackupDialog(backupSettingsViewModel: BackupSettingsViewModel) {
    val backupParams = backupSettingsViewModel.getRestoreParamsInDialog().Remember()
    val restoreBackupFile = backupSettingsViewModel.getCurrentBackupFileForRestore().Remember()
    val userConfirmRemoveOldDataState =
        backupSettingsViewModel.getUserConfirmRemoveOldDataState().Remember()
    val checkBoxBackupParams = listOf(
        CheckBoxBackupParams(
            stringResource(R.string.Restore_notes),
            state = backupParams.value?.restoreNotes ?: false,
            onChange = { newState ->
                backupSettingsViewModel.updateRestoreParams {
                    it.copy(restoreNotes = newState)
                }
            }
        ),
        CheckBoxBackupParams(
            stringResource(R.string.Restore_category),
            state = backupParams.value?.restoreCategory ?: false,
            onChange = { newState ->
                backupSettingsViewModel.updateRestoreParams {
                    it.copy(restoreCategory = newState)
                }

            }
        ),
        CheckBoxBackupParams(
            stringResource(R.string.Restore_todo),
            state = backupParams.value?.restoreTodo ?: false,
            onChange = { newState ->
                backupSettingsViewModel.updateRestoreParams {
                    it.copy(restoreTodo = newState)
                }

            }
        ),
    )
    Dialog(onDismissRequest = { backupSettingsViewModel.hideRestoreBackupDialog() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = themeColors.cardColor,
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(Modifier.fillMaxWidth()) {
                LazyColumn() {
                    itemsIndexed(checkBoxBackupParams) { index, it ->
                        if (index == 0) {
                            Spacer(modifier = Modifier.height(5.dp))
                            SelectBackupPathButton(
                                isPathSelected = restoreBackupFile.value != null,
                                onClick = {
                                    backupSettingsViewModel.selectFileForRestoreBackup()
                                }
                            )
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                color = themeColors.primaryFontColor
                            )
                        }
                        BackupItemCheckBox(
                            title = it.title,
                            state = it.state,
                            onChange = it.onChange
                        )
                        if (index == checkBoxBackupParams.lastIndex) {
                            Spacer(modifier = Modifier.height(7.dp))
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                color = themeColors.primaryFontColor
                            )
                            Spacer(modifier = Modifier.height(7.dp))
                            BackupItemCheckBox(title = stringResource(R.string.Restore_backup_warming),
                                userConfirmRemoveOldDataState.value,
                                onChange = {
                                    backupSettingsViewModel.updateUserConfirmRemoveOldDataState(it)
                                }
                            )
                            YesNoButton(
                                onConfirm = {
                                    backupSettingsViewModel.startRestoreBackup()
                                    backupSettingsViewModel.hideRestoreBackupDialog()
                                },
                                onCancel = {
                                    backupSettingsViewModel.hideRestoreBackupDialog()
                                },
                                isOkButtonEnable = !backupParams.value.isAllFalse()
                                        && userConfirmRemoveOldDataState.value
                                        && restoreBackupFile.value != null
                            )
                        }
                    }
                }

            }

        }
    }
}


@Composable
fun BackupItemCheckBox(title: String, state: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable {
                onChange(!state)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = state,
            onCheckedChange = { onChange(it) },
            colors = CheckboxDefaults.colors(
                checkedColor = themeColors.secondaryColor,
                themeColors.secondaryColor,
            ),
            modifier = Modifier.padding(end = 10.dp)
        )
        Text(
            text = title,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            color = themeColors.primaryFontColor,
            modifier = Modifier.padding(start = 10.dp, bottom = 10.dp),
        )
    }
}

@Composable
fun RepeatAutoBackupTimeDropDownList(
    onChange: (Long) -> Unit,
    isVisible: Boolean,
    onHide: () -> Unit,
) {
    val dropDownItem = RepeatAutoBackupTimeItem.getDropDownList()
    DropdownMenu(
        expanded = isVisible,
        onDismissRequest = {
            onHide()
        },
        modifier = Modifier
            .background(themeColors.dropDownMenuColor)
            .heightIn(max = 200.dp)
    ) {
        dropDownItem.forEach {
            DropdownMenuItem(onClick = {
                onChange(it.timeAtHours)
            }) {
                Row {
                    Text(
                        text = stringResource(it.title),
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
fun SelectBackupPathButton(
    isPathSelected: Boolean,
    onClick: () -> Unit,
    title: String = stringResource(R.string.Backup_path),
    textIfPathNotSelected: String = stringResource(R.string.Not_set),
    textIfPathSelected: String = stringResource(R.string.Path_set),
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, start = 10.dp, bottom = 10.dp)
            .clickable {
                onClick()
            },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.W800,
                fontSize = 18.sp,
                color = themeColors.primaryFontColor,
                modifier = Modifier
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = "",
                    tint = themeColors.primaryFontColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        if (!isPathSelected) {
            Text(
                text = textIfPathNotSelected,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = themeColors.errorColor,
                modifier = Modifier
            )
        } else {
            Text(
                text = textIfPathSelected,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = themeColors.green,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun SelectRepeatBackupButton(
    getCurrentTime: () -> Long,
    isVisible: Boolean,
    onChange: (Long) -> Unit,
    onShow: () -> Unit,
    onHide: () -> Unit,
) {
    val annotatedLabelString = buildAnnotatedString {
        append(
            stringResource(
                RepeatAutoBackupTimeItem
                    .getDropDownItemByTime(getCurrentTime())
                    .title
            )
        )
        appendInlineContent("drop_down_triangle")
    }
    val inlineContentMap = mapOf(
        "drop_down_triangle" to InlineTextContent(
            Placeholder(20.sp, 20.sp, PlaceholderVerticalAlign.TextCenter)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_drop_down_triangle),
                contentDescription = "",
                tint = themeColors.secondaryFontColor,
                modifier = Modifier.padding(top = 0.dp)
            )
        }
    )
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, bottom = 10.dp)
            .clickable {
                onShow()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.Repeat_every),
            fontWeight = FontWeight.W800,
            fontSize = 18.sp,
            color = themeColors.primaryFontColor,
            modifier = Modifier
        )
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            Text(
                text = annotatedLabelString,
                inlineContent = inlineContentMap,
                fontWeight = FontWeight.W800,
                fontSize = 16.sp,
                color = themeColors.secondaryFontColor
            )
            RepeatAutoBackupTimeDropDownList(onChange, isVisible, onHide)
        }
    }
}

@Composable
fun GoogleSingInButton(
    onAuth: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, start = 10.dp, bottom = 10.dp)
            .clickable {
                onAuth()
            },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            Text(
                text = stringResource(R.string.Authorization_google),
                fontWeight = FontWeight.W800,
                fontSize = 18.sp,
                color = themeColors.primaryFontColor,
                modifier = Modifier
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Image(
                    painter = painterResource(R.drawable.g_button),
                    contentDescription = "",
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoadDialog() {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = true
        )
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20),
            backgroundColor = themeColors.cardColor
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.padding(end = 20.dp))
                Text(
                    text = stringResource(R.string.Wait_please),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = themeColors.primaryFontColor
                )
            }
        }
    }
}

@Composable
fun SettingsRadioButton(
    text: String,
    state: Boolean,
    onChange: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.W800,
            fontSize = 18.sp,
            color = themeColors.primaryFontColor,
            modifier = Modifier.padding(start = 10.dp, bottom = 0.dp)
        )
        Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.fillMaxWidth()) {
            Switch(
                checked = state,
                onCheckedChange = {
                    onChange(it)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = themeColors.secondaryColor,
                    uncheckedThumbColor = themeColors.secondaryFontColor
                ),
            )
        }
    }
}

@Composable
fun SettingsButton(
    text: String,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, bottom = 15.dp)
            .clickable {
                onClick()
            }
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.W800,
            fontSize = 18.sp,
            color = themeColors.primaryFontColor,
            modifier = Modifier
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = "",
                tint = themeColors.primaryFontColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}