package com.xxmrk888ytxx.privatenote

import com.xxmrk888ytxx.privatenote.domain.InputHistoryManager.InputHistoryManager
import org.junit.Assert.assertEquals
import org.junit.Test

class InputHistoryManagerTest {
    @Test
    fun OutRedoTest() {
        val textManager = InputHistoryManager()
        repeat(10) {
            textManager.addInHistory(it.toString())
        }
        textManager.getUndo()
        textManager.getRedo()
        assertEquals(false,textManager.isHaveRedo())
    }
    @Test()
    fun OutUndoTest() {
        val textManager = InputHistoryManager()
        repeat(10) {
            textManager.addInHistory(it.toString())
        }
        repeat(10) {
            textManager.getUndo()
        }
        assertEquals(false,textManager.isHaveUndo())
    }

}