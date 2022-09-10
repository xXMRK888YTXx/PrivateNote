package com.xxmrk888ytxx.privatenote.MultiUse.Player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.xxmrk888ytxx.privatenote.AudioManager.Audio
import com.xxmrk888ytxx.privatenote.AudioManager.PlayerState
import com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen.EditNoteViewModel
import com.xxmrk888ytxx.privatenote.Utils.milliSecondToSecond
import com.xxmrk888ytxx.privatenote.ui.theme.CardNoteColor
import com.xxmrk888ytxx.privatenote.ui.theme.PrimaryFontColor

@Composable
fun PlayerDialog(
    controller: PlayerController,
    audio: Audio,
    onHideDialog:() -> Unit,
) {
    val scope = rememberCoroutineScope()
    val playerState = controller.getPlayerState().collectAsState(PlayerState.Disable,scope.coroutineContext)
    LaunchedEffect(key1 = Unit, block = {
        controller.play(audio)
    })
    Dialog(onDismissRequest = { onHideDialog();controller.reset() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = CardNoteColor,
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = playerState.value.currentPos.milliSecondToSecond(),
                        fontWeight = FontWeight.Medium,
                        color = PrimaryFontColor,
                        modifier = Modifier.padding(end = 5.dp),
                        fontSize = 14.sp
                    )
                    Slider(playerState.value.currentPos.toFloat(), onValueChange = {
                        controller.seekTo(it.toLong())
                    },
                        modifier = Modifier.fillMaxWidth(0.8f),
                        valueRange = 0f..audio.duration.toFloat()
                    )
                    Text(
                        text = audio.duration.milliSecondToSecond(),
                        fontWeight = FontWeight.Medium,
                        color = PrimaryFontColor,
                        modifier = Modifier.padding(start = 5.dp),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}