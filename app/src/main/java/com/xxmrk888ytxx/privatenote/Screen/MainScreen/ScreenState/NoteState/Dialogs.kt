package com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.NoteState

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.xxmrk888ytxx.privatenote.DB.Entity.Category
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.getColor
import com.xxmrk888ytxx.privatenote.ui.theme.CardNoteColor
import com.xxmrk888ytxx.privatenote.ui.theme.MainBackGroundColor
import com.xxmrk888ytxx.privatenote.ui.theme.PrimaryFontColor

@Composable
fun OrderCategoryDialog(noteStateViewModel: NoteStateViewModel) {
    Dialog(onDismissRequest = {
        noteStateViewModel.dialogState.value =  NoteDialogState.None
    }) {
        //val category = noteStateViewModel.getAllCategory().collectAsState(listOf())
        listOf(Category(categoryName = "test"))
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .background(MainBackGroundColor)
        ) {
            items(listOf(Category(categoryName = "test"), Category(
                categoryName = "Белый цвет",
                red = 255,
                green = 255,
                blue = 255
            )
            )) {
                Card(
                    Modifier
                        .padding(10.dp)
                        .fillMaxSize(),
                    backgroundColor = it.getColor().copy(0.3f)
                ) {
                    Row(
                        Modifier
                            .padding(top = 10.dp, bottom = 10.dp)
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                        ) {
                        Icon(painterResource(R.drawable.ic_category_icon),
                            "",
                            tint = it.getColor()
                        )
                            Text(it.categoryName,
                                color = PrimaryFontColor,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(start = 10.dp),
                                fontWeight = FontWeight.Medium,
                            )

                    }
                }
            }
        }
    }
}