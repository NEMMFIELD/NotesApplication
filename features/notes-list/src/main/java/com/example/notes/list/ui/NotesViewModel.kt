package com.example.notes.list.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.list.data.Mapper
import com.example.notes.list.data.NoteModel
import com.example.notes.list.domain.AddNotesUseCase
import com.example.notes.list.domain.DeleteNoteUseCase
import com.example.notes.list.domain.LoadNotesUseCase
import com.example.state.State
import com.example.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val loadNotesUseCase: LoadNotesUseCase,
    private val addNotesUseCase: AddNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val androidLogger:Logger,
) : ViewModel() {
    private val _notes = MutableStateFlow<State<List<NoteModel>>>(State.Empty)
    val notes: StateFlow<State<List<NoteModel>>> get() = _notes

    init {
        addNote()  // Добавляем заметку при старте приложения
        loadNotes()
    }

    fun loadNotes() {
        viewModelScope.launch {
            loadNotesUseCase.execute()
                .onStart {
                    _notes.value = State.Empty
                }
                .catch { e ->
                    _notes.value = State.Failure(e)
                }
                .collect { result ->
                    _notes.value = State.Success(result)
                }
        }
    }

    fun addNote() {
        viewModelScope.launch {
            try {
                addNotesUseCase.execute()
                loadNotes() // Можно перезагрузить список заметок после добавления новой
            } catch (e: Exception) {
                _notes.value = State.Failure(e)
            }
        }
    }

    fun deleteNote(note: NoteModel) {
        viewModelScope.launch {
            try {
                val entity = Mapper.mapFromModelToDb(note)
                deleteNoteUseCase.execute(entity)
            } catch (e: Exception) {
               androidLogger.d("Error during deleting", e.toString())
            }
        }
    }
}
