package com.example.notes.edit.domain

import javax.inject.Inject

class EditNotesUseCase @Inject constructor(private val editNotesRepository: EditNotesRepository) {
   suspend fun execute() {
        editNotesRepository.editNote()
    }
}
