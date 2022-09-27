package com.xxmrk888ytxx.privatenote.presentation.Screen.BackupSettingsScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.MustBeLocalization
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsBackupRepository.BackupSettings
import com.xxmrk888ytxx.privatenote.presentation.Activity.MainActivity.ActivityController
import com.xxmrk888ytxx.privatenote.presentation.Screen.ThemeSettingsScreen.TopBar
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager

@Composable
fun BackupSettingsScreen(
    backupSettingsViewModel: BackupSettingsViewModel = hiltViewModel(),
    navController: NavController,
    activityController: ActivityController
) {
    val settings = backupSettingsViewModel.getBackupSettings().collectAsState(BackupSettings())
    LaunchedEffect(key1 = activityController, block = {
        backupSettingsViewModel.initActivityController(activityController)
    })
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        TopBar(navController)
        Text(text = stringResource(R.string.Backup),
            fontWeight = FontWeight.W800,
            fontSize = 30.sp,
            color = ThemeManager.PrimaryFontColor,
            modifier = Modifier.padding(start = 20.dp,bottom = 15.dp)
        )
        MainBackupSettings(backupSettingsViewModel,settings)
        ParamsBackupList(backupSettingsViewModel,settings)
    }

}

@Composable
fun getParamsList(
    backupSettingsViewModel: BackupSettingsViewModel,
    settings: State<BackupSettings>,
): List<BackupParams> {
    return listOf(
        BackupParams(
            title = stringResource(R.string.Not_copy_encrypt_note),
            settingsState = settings.value.isBackupNotEncryptedNote,
            isEnable = settings.value.isEnableBackup,
            updateState = {
                backupSettingsViewModel.updateIsBackupNotEncryptedNote(it)
            }
        ),
        BackupParams(
            title = stringResource(R.string.Copy_encrypt_note),
            settingsState = settings.value.isBackupEncryptedNote,
            isEnable = settings.value.isEnableBackup,
            updateState = {
                backupSettingsViewModel.updateIsBackupEncryptedNote(it)
            }
        ),
        BackupParams(
            title = stringResource(R.string.Copy_note_images),
            settingsState = settings.value.isBackupNoteImages,
            isEnable = settings.value.isEnableBackup,
            updateState = {
                backupSettingsViewModel.updateIsBackupNoteImages(it)
            }
        ),
        BackupParams(
            title = stringResource(R.string.Copy_notes_audio),
            settingsState = settings.value.isBackupNoteAudio,
            isEnable = settings.value.isEnableBackup,
            updateState = {
                backupSettingsViewModel.updateIsBackupNoteAudio(it)
            }
        ),
        BackupParams(
            title = stringResource(R.string.Copy_note_category),
            settingsState = settings.value.isBackupNoteCategory,
            isEnable = settings.value.isEnableBackup,
            updateState = {
                backupSettingsViewModel.updateIsBackupNoteCategory(it)
            }
        ),
        BackupParams(
            title = stringResource(R.string.Copy_not_сompleted_todo),
            settingsState = settings.value.isBackupNotCompletedTodo,
            isEnable = settings.value.isEnableBackup,
            updateState = {
                backupSettingsViewModel.updateIsBackupNotCompletedTodo(it)
            }
        ),
        BackupParams(
            title = stringResource(R.string.Copy_сompleted_todo),
            settingsState = settings.value.isBackupCompletedTodo,
            isEnable = settings.value.isEnableBackup,
            updateState = {
                backupSettingsViewModel.updateIsBackupCompletedTodo(it)
            }
        ),
    )
}

@Composable
fun ParamsBackupList(
    backupSettingsViewModel: BackupSettingsViewModel,
    settings: State<BackupSettings>,
) {
    val paramsList: List<BackupParams> = getParamsList(backupSettingsViewModel, settings)
    Column(
        modifier = Modifier.padding(top = 10.dp)
    ) {
        Text(
            text = stringResource(R.string.Backup_params),
            fontWeight = FontWeight.W800,
            fontSize = 22.sp,
            color = ThemeManager.PrimaryFontColor,
            modifier = Modifier.padding(start = 10.dp)
        )
        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp), color = ThemeManager.PrimaryFontColor)
    }
    LazyColumn() {
        items(paramsList) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = it.title,
                    fontWeight = FontWeight.W800,
                    fontSize = 16.sp,
                    color = ThemeManager.PrimaryFontColor,
                    modifier = Modifier.padding(start = 10.dp, bottom = 10.dp)
                )
                Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.fillMaxWidth()) {
                    Switch(
                        checked = it.settingsState,
                        onCheckedChange = { newState ->
                            it.updateState(newState)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = ThemeManager.SecondaryColor,
                            uncheckedThumbColor = ThemeManager.SecondoryFontColor
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
    settings: State<BackupSettings>
) {
    Column(

    ) {
        Text(
            text = stringResource(R.string.Main_settings),
            fontWeight = FontWeight.W800,
            fontSize = 22.sp,
            color = ThemeManager.PrimaryFontColor,
            modifier = Modifier.padding(start = 10.dp)
        )
        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp), color = ThemeManager.PrimaryFontColor)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.Backup_is_active),
            fontWeight = FontWeight.W800,
            fontSize = 20.sp,
            color = ThemeManager.PrimaryFontColor,
            modifier = Modifier.padding(start = 10.dp, bottom = 10.dp)
        )
        Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.fillMaxWidth()) {
            Switch(
                checked = settings.value.isEnableBackup,
                onCheckedChange = {
                    backupSettingsViewModel.updateBackupState(it)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = ThemeManager.SecondaryColor,
                    uncheckedThumbColor = ThemeManager.SecondoryFontColor
                ),
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, start = 10.dp, bottom = 10.dp)
            .clickable {
                backupSettingsViewModel.selectBackupFile()
            },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.Backup_path),
                fontWeight = FontWeight.W800,
                fontSize = 20.sp,
                color = ThemeManager.PrimaryFontColor,
                modifier = Modifier
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp),
                contentAlignment = Alignment.CenterEnd) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = "",
                    tint = ThemeManager.PrimaryFontColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        if(settings.value.backupPath == null) {
            Text(
                text = stringResource(R.string.Not_set),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = ThemeManager.ErrorColor,
                modifier = Modifier
            )
        }
        else {
            Text(
                text = "${stringResource(R.string.Path_set)}",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = ThemeManager.Green,
                modifier = Modifier
            )
        }
    }
}
