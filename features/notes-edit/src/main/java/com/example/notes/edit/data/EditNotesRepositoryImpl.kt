package com.example.notes.edit.data

import com.example.database.NoteEntity
import com.example.notes.api.NotesApi
import com.example.notes.edit.domain.EditNotesRepository
import io.objectbox.Box
import javax.inject.Inject

class EditNotesRepositoryImpl @Inject constructor(
    private val api: NotesApi,
    private val noteBox: Box<NoteEntity>
) : EditNotesRepository {
    override suspend fun editNote() {
        val newResponse = api.getNote()
        noteBox.put(EditNotesMapper.mapFromDtoToEntity(newResponse.first()))
    }
}
