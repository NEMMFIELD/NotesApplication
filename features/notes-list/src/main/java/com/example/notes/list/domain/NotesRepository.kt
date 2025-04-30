package com.example.notes.list.domain

import com.example.database.NoteEntity
import com.example.notes.api.model.NoteResponse
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    suspend fun loadNotes():List<NoteResponse>
    fun addNote(note: NoteEntity)
    fun getAllNotes():List<NoteEntity>
    fun deleteNote(note: NoteEntity)
    fun observeNotes():Flow<List<NoteEntity>>
}
