package com.xxmrk888ytxx.privatenote.domain.InputHistoryManager

interface InputHistoryManager {
    fun setPrimaryVersion(text: String)
    fun addInHistory(text: String)
    fun isHaveUndo():Boolean
    fun isHaveRedo():Boolean
    fun getUndo() : String
    fun clearBuffer()
    fun getRedo() : String
}