package com.xxmrk888ytxx.privatenote.domain.InputHistoryManager

import javax.inject.Inject
import kotlin.random.Random

class InputHistoryManagerImpl @Inject constructor() : InputHistoryManager {
    private val historyBuffer:MutableList<String> = mutableListOf()
    var currentPos = -1
    get() = field
    private set

    private var primaryVersion = ""


    override fun setPrimaryVersion(text: String) {
        primaryVersion = text
    }

    override fun addInHistory(text: String) {
        historyBuffer.add(text)
        currentPos = historyBuffer.lastIndex
    }

    override fun isHaveUndo():Boolean {
        if(historyBuffer.isEmpty()||currentPos == -1) return false
        return try {
            val undoPos = if(currentPos != 0) currentPos - 1 else currentPos
            historyBuffer[undoPos]
            true
        }catch (e:Exception) {
            false
        }
    }

    override fun isHaveRedo():Boolean {
        if(historyBuffer.isEmpty()) return false
        return try {
            val repoPos = currentPos + 1
            historyBuffer[repoPos]
            true
        }catch (e:Exception) {
            false
        }
    }

    override fun getUndo() : String {
        try {
            currentPos--
            if(currentPos == -1) return primaryVersion
            val text = historyBuffer[currentPos]
            return text
        }catch (e:Exception) {
            throw IndexOutOfBoundsException()
        }
    }

    override fun clearBuffer() {
        historyBuffer.clear()
    }

    override fun getRedo() : String {
        try {
            currentPos++
            val text = historyBuffer[currentPos]
            return text
        }catch (e:Exception) {
            throw IndexOutOfBoundsException()
        }
    }
    private var hashcode:Int = Random(System.currentTimeMillis()).nextInt(100000,999999)
    override fun hashCode(): Int {
        return hashcode
    }
}