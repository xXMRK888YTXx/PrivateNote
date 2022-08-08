package com.xxmrk888ytxx.privatenote.DB.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xxmrk888ytxx.privatenote.DB.Entity.Category
import com.xxmrk888ytxx.privatenote.DB.Entity.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM NOTE")
    fun getAllNote() : Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note)

    @Query("SELECT * FROM NOTE WHERE id = :id")
    fun getNoteById(id:Int) : Flow<Note>

    @Query("DELETE FROM Note WHERE id = :id")
    fun removeNote(id:Int)

    @Query("UPDATE Note SET isChosen=:isChosen WHERE id=:id")
    fun changeChosenStatus(isChosen:Boolean,id: Int)

    @Query("UPDATE NOTE SET category= :categoryId WHERE id = :noteId")
    fun changeCurrentCategory(noteId:Int,categoryId:Int?)
}