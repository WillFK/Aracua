package com.example.aracua.data.db

import androidx.room.*
import com.example.aracua.data.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note WHERE id = :id")
    fun getNote(id: Long): Flow<Note>

    @Query("SELECT * FROM note ORDER BY timestamp DESC")
    fun getNotes(): Flow<List<Note>>

    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)
}