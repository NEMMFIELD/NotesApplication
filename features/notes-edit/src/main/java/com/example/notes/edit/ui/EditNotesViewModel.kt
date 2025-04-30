package com.example.notes.edit.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.edit.domain.EditNotesUseCase
import com.example.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditNotesViewModel @Inject constructor(
    private val editNotesUseCase: EditNotesUseCase,
    private val androidLogger: Logger
) : ViewModel() {
    private val _editFinished = MutableLiveData<Boolean>()
    val editFinished: LiveData<Boolean> get() = _editFinished
    fun editNote() {
        viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
            try {
                editNotesUseCase.execute()
                delay(2000)
                _editFinished.postValue(true)
            } catch (e: Exception) {
                androidLogger.d("Edit note viewmodel", e.toString())
            }
        }
    }
}
