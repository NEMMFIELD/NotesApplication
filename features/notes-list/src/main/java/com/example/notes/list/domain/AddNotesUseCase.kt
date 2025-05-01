package com.example.notes.list.domain

import com.example.notes.list.data.Mapper
import javax.inject.Inject

class AddNotesUseCase @Inject constructor(private val repository: NotesRepository) {
    suspend fun execute() {
        val noteFromDto = repository.loadNotes()
        repository.addNote(Mapper.mapFromDtoToEntity(noteFromDto.first()))
    }
}
