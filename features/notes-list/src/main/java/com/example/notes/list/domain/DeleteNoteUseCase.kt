package com.example.notes.list.domain

import com.example.database.NoteEntity
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(private val repository: NotesRepository) {
    fun execute(note: NoteEntity) {
        repository.deleteNote(note)
    }
}
