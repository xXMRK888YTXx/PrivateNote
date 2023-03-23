package com.xxmrk888ytxx.privatenote.Utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.security.crypto.EncryptedFile
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Category
import com.xxmrk888ytxx.privatenote.data.Database.Entity.Note
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.Const.CHOSEN_ONLY
import com.xxmrk888ytxx.privatenote.Utils.Const.IGNORE_CATEGORY
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.models.SortNoteState
import com.xxmrk888ytxx.privatenote.presentation.theme.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File
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

fun List<Note>.sortNote(sortNoteState: SortNoteState) : List<Note> {
    val chosenNote = this.filter { it.isChosen }
    val otherNote = this.filter { !it.isChosen }
    val joinList = mutableListOf<Note>()
    joinList.addAll(
       if(sortNoteState is SortNoteState.ByDescending)
           chosenNote.sortedByDescending { it.created_at }
       else
           chosenNote.sortedBy { it.created_at }
    )
    joinList.addAll(
        if(sortNoteState is SortNoteState.ByDescending)
            otherNote.sortedByDescending { it.created_at }
        else
            otherNote.sortedBy { it.created_at }
    )
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

fun File.fileNameToLong() : Long {
    if(this.name.isDigitsOnly()) return this.name.toLong()
    var finalString = ""
    this.name.forEach {
        if(it.isDigit()) {
            finalString += it
        }else {
            return finalString.toLong()
        }
    }
    return 0
}

fun EncryptedFile.getBytes() : ByteArray? {
    try {
        val stream = this.openFileInput()
        val bytes = stream.readBytes()
        stream.close()
        return bytes
    }catch (e:Exception) {
        return null
    }
}

fun <T> T?.ifNotNull(Runnable:(T) -> Unit) {
    if(this != null) {
        Runnable(this)
    }
}
suspend  fun <T> T?.asyncIfNotNull(Runnable:suspend (T) -> Unit) {
    if(this != null) {
        Runnable(this)
    }
}

fun Long.milliSecondToSecond() : String {
    if(this == 0L) return "00:00"
    var finalString = ""
    val minute = this / 60000
    val second = (this - minute * 60000) / 1000
    finalString += if(minute in 0..9) "0$minute" else minute.toString()
    finalString += ":"
    finalString += if(second in 0..9) "0$second" else second.toString()
    return finalString
}

@Composable
fun <T> State<T>.Remember() = remember {this}

fun Any?.runOnMainThread(Runnable: () -> Unit) {
    val handler = Handler(Looper.getMainLooper())
    handler.post { Runnable() }
}

fun <T> MutableState<T>.toState():State<T> = this

@Composable
fun LazySpacer(height:Int = 0,width:Int = 0) {
    Spacer(Modifier.height(height.dp).width(width.dp))
}

@Composable
fun ComposeContext() = LocalContext.current

fun Int.isEvenNumber() : Boolean = this % 2 == 0

fun Modifier.borderIf(isNeedBorder:Boolean) : Modifier {
    if(!isNeedBorder) return this
    return this.border(
        width =  2.dp,
        color = ThemeManager.SecondaryColor,
        shape = RoundedCornerShape(10.dp),
    )
}
val themeColors : com.xxmrk888ytxx.privatenote.presentation.theme.Colors
    @Composable get() = Theme.LocalColors.current

val themeValues : com.xxmrk888ytxx.privatenote.presentation.theme.Values
    @Composable get() = Theme.LocalValues.current
