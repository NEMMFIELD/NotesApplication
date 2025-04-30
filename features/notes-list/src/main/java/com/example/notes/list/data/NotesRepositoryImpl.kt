package com.example.notes.list.data

import com.example.database.NoteEntity
import com.example.notes.api.NotesApi
import com.example.notes.api.model.NoteResponse
import com.example.notes.list.domain.NotesRepository
import io.objectbox.Box
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    private val api: NotesApi,
    private val noteBox: Box<NoteEntity>
) : NotesRepository {
    override suspend fun loadNotes(): List<NoteResponse> {
        return api.getNote()
    }

    override fun addNote(note: NoteEntity) {
        noteBox.put(note)
    }

    override fun getAllNotes(): List<NoteEntity> {
        return noteBox.all
    }

    override fun deleteNote(note: NoteEntity) {
        noteBox.remove(note)
    }

    override fun observeNotes(): Flow<List<NoteEntity>> = callbackFlow {
        val subscription = noteBox.query().build().subscribe().observer { data ->
            trySend(data)
        }
        // Закрытие при отмене потока
        awaitClose { subscription.cancel() }
    }
}

