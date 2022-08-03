package com.xxmrk888ytxx.privatenote.InputHistoryManager

class InputHistoryManager(
    private var maxBufferSize:Int = 20
) {
    private val historyBuffer:MutableList<String> = mutableListOf()
    var currentPos = 0
    get() = field
    private set

    fun addInHistory(text: String) {
        historyBuffer.add(text)
        if(historyBuffer.size > maxBufferSize) historyBuffer.removeFirst()
        currentPos = historyBuffer.lastIndex
    }

    fun getMaxBufferSize() = maxBufferSize

    fun setMaxBufferSize(size:Int) {
        maxBufferSize = size
    }

    fun isHaveUndo():Boolean {
        if(historyBuffer.isEmpty()) return false
        return try {
            historyBuffer[currentPos--]
            true
        }catch (e:Exception) {
            false
        }
    }

    fun isHaveRedo():Boolean {
        if(historyBuffer.isEmpty()) return false
        return try {
            historyBuffer[currentPos++]
            true
        }catch (e:Exception) {
            false
        }
    }

    fun getUndo() : String {
        try {
            val text = historyBuffer[currentPos]
            currentPos = currentPos--
            return text
        }catch (e:Exception) {
            throw IndexOutOfBoundsException()
        }
    }

    fun getRedo() : String {
        try {
            val text = historyBuffer[currentPos]
            currentPos = currentPos--
            return text
        }catch (e:Exception) {
            throw IndexOutOfBoundsException()
        }
    }
}