package com.xxmrk888ytxx.privatenote.Utils

import android.content.Context
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.xxmrk888ytxx.privatenote.DB.Entity.Category
import com.xxmrk888ytxx.privatenote.DB.Entity.Note
import com.xxmrk888ytxx.privatenote.DB.Entity.ToDoItem
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.Const.CHOSEN_ONLY
import com.xxmrk888ytxx.privatenote.Utils.Const.IGNORE_CATEGORY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.*

fun String.getFirstChars() : String {
    if(this.length <= 25) return this
    else if(this.isEmpty()) return ""
    return this.take(25) + "..."
    }
fun monthToString(month:Int,context: Context) : String {
    context.resources.apply {
        return when(month) {
            0 -> getString(R.string.January)
            1 -> getString(R.string.February)
            2 -> getString(R.string.March)
            3 -> getString(R.string.April)
            4 -> getString(R.string.May)
            5 -> getString(R.string.June)
            6 -> getString(R.string.July)
            7 -> getString(R.string.August)
            8 -> getString(R.string.September)
            9 -> getString(R.string.October)
            10 -> getString(R.string.November)
            11 -> getString(R.string.December)
            else -> return ""
        }
    }
}


fun Long.secondToData(context: Context) : String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    var minute = ""
    if(calendar.get(Calendar.MINUTE) < 10)
        minute = "0" + calendar.get(Calendar.MINUTE).toString()
    else minute = calendar.get(Calendar.MINUTE).toString()
    return "${calendar.get(Calendar.DAY_OF_MONTH)} ${monthToString(calendar.get(Calendar.MONTH),context)}" +
            " ${calendar.get(Calendar.YEAR)} " +
            "${calendar.get(Calendar.HOUR_OF_DAY)}:$minute"
}

fun <T> Flow<T>.getData() : T  = runBlocking {
    return@runBlocking this@getData.first()
}
object BackPressController {
    @Composable
    fun setHandler(enabled: Boolean = true, onBack: () -> Unit) {
        // Safely update the current `onBack` lambda when a new one is provided
        val currentOnBack by rememberUpdatedState(onBack)
        // Remember in Composition a back callback that calls the `onBack` lambda
        val backCallback = remember {
            object : OnBackPressedCallback(enabled) {
                override fun handleOnBackPressed() {
                    currentOnBack()
                }
            }
        }
        // On every successful composition, update the callback with the `enabled` value
        SideEffect {
            backCallback.isEnabled = enabled
        }
        val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
            "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
        }.onBackPressedDispatcher
        val lifecycleOwner = LocalLifecycleOwner.current
        DisposableEffect(lifecycleOwner, backDispatcher,) {
            // Add callback to the backDispatcher
            backDispatcher.addCallback(lifecycleOwner, backCallback)
            // When the effect leaves the Composition, remove the callback
            onDispose {
                backCallback.remove()
            }
        }
    }
}

fun <T> List<T>.fillList(element:T,count:Int) : List<T> {
    val list = mutableListOf<T>()
    repeat(count) {
        list.add(element)
    }
    return list.toList()
}

fun search(subString: String, note: Note) : Boolean {
    if(note.isEncrypted) return false
    if(subString.toLowerCase() in note.text.toLowerCase()) return true
    if(subString.toLowerCase() in note.title.toLowerCase()) return true
    return false
}

fun List<Note>.searchFilter(enable:Boolean, subString: String) : List<Note> {
    if(!enable) return this
    return this.filter { search(subString,it) }
}

fun List<Note>.sortNote() : List<Note> {
    val chosenNote = this.filter { it.isChosen }.sortedByDescending { it.created_at }
    val otherNote = this.filter { !it.isChosen }.sortedByDescending { it.created_at }
    val joinList = mutableListOf<Note>()
    joinList.addAll(chosenNote)
    joinList.addAll(otherNote)
    return joinList
}

fun Category.getColor() : Color {
    return Color(red,green,blue,1f)
}

fun List<Note>.sortedByCategory(categoryID: Int) : List<Note> {
     when(categoryID) {
         IGNORE_CATEGORY -> return this
         CHOSEN_ONLY -> {
             return this.filter { it.isChosen }
         }
         else -> {
             return this.filter { it.category == categoryID }
         }
    }
}

fun Context.setAppLocale(language: String): Context {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val config = resources.configuration
    config.setLocale(locale)
    config.setLayoutDirection(locale)
    return createConfigurationContext(config)
}
