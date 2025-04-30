package com.example.notes.list.domain

import com.example.notes.list.data.Mapper
import com.example.notes.list.data.NoteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoadNotesUseCase @Inject constructor(private val repository: NotesRepository) {
    fun execute(): Flow<List<NoteModel>> = repository.observeNotes()
        .map { notes ->
            // Преобразование данных из сущностей в модели
            notes.map { Mapper.mapFromDbToModel(it) }
        }
        .flowOn(Dispatchers.IO)
}
