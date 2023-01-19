package com.example.aracua.data.repository

import com.example.aracua.data.Note
import com.example.aracua.data.db.NoteDao
import kotlinx.coroutines.flow.Flow

class NotesRoomRepository(private val dao: NoteDao) : NotesRepository {

    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes()
    }

    override fun getNote(id: Long): Flow<Note?> {
        return dao.getNote(id)
    }

    override suspend fun update(note: Note) {
        dao.update(note)
    }

    override suspend fun insert(note: Note) {
        dao.insert(note)
    }

    override suspend fun delete(note: Note) {
        dao.delete(note)
    }

    /*override suspend fun insertTODO() {
        dao.insert(Note(
            title = "Canção do Exílio",
            body = """
                    Minha terra tem palmeiras
                    Onde canta o Sabiá,
                    As aves, que aqui gorjeiam,
                    Não gorjeiam como lá.
                """.trimIndent(),
            timestamp = System.currentTimeMillis()
        ))
    }*/
}