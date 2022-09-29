package com.xxmrk888ytxx.privatenote.presentation.MultiUse.Player

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.domain.Repositories.AudioRepository.Audio
import com.xxmrk888ytxx.privatenote.domain.PlayerManager.PlayerState
import com.xxmrk888ytxx.privatenote.Utils.milliSecondToSecond
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.CardColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.SecondaryColor
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.PrimaryFontColor

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
    val playerButtons = listOf<PlayerButton>(
        PlayerButton(
            icon = R.drawable.ic_fast_rewind,
            onClick = {
                controller.seekTo(playerState.value.currentPos-5000)
            }
        ),
        PlayerButton(
            icon = if(playerState.value is PlayerState.Play) R.drawable.ic_pause
            else R.drawable.ic_play,
            onClick = {
                if(playerState.value is PlayerState.Play) controller.pause()
                else controller.play(audio)
            }
        ),
        PlayerButton(
            icon = R.drawable.ic_fast_forward,
            onClick = {controller.seekTo(playerState.value.currentPos+5000)}
        ),

    )
    Dialog(onDismissRequest = { onHideDialog();controller.reset() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = CardColor,
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
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
                        valueRange = 0f..audio.duration.toFloat(),
                        colors = SliderDefaults.colors(
                            thumbColor = SecondaryColor,
                            activeTrackColor = SecondaryColor
                        )
                    )
                    Text(
                        text = audio.duration.milliSecondToSecond(),
                        fontWeight = FontWeight.Medium,
                        color = PrimaryFontColor,
                        modifier = Modifier.padding(start = 5.dp),
                        fontSize = 14.sp
                    )
                }
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    LazyRow(
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    ) {
                        items(playerButtons) {
                            Box(modifier = Modifier.padding(start = 5.dp, end = 5.dp)) {
                                IconButton(onClick = { it.onClick() }) {
                                    Icon(painter = painterResource(it.icon),
                                        contentDescription = "",
                                        tint = PrimaryFontColor,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}