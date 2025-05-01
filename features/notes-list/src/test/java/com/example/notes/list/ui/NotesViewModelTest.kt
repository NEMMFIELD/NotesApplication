package com.example.notes.list.ui

import com.example.database.NoteEntity
import com.example.notes.list.data.Mapper
import com.example.notes.list.data.NoteModel
import com.example.notes.list.domain.AddNotesUseCase
import com.example.notes.list.domain.DeleteNoteUseCase
import com.example.notes.list.domain.LoadNotesUseCase
import com.example.utils.Logger
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.jupiter.api.Assertions.*
import com.example.state.State
import kotlinx.coroutines.Dispatchers
import kotlin.test.Test
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After


@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModelTest {
    private lateinit var viewModel: NotesViewModel
    private lateinit var loadNotesUseCase: LoadNotesUseCase
    private lateinit var addNotesUseCase: AddNotesUseCase
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase
    private lateinit var androidLogger: Logger

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        // Мокаем зависимости
        loadNotesUseCase = mockk()
        addNotesUseCase = mockk()
        deleteNoteUseCase = mockk()
        androidLogger = mockk(relaxed = true)

        // Инициализация ViewModel
        viewModel = NotesViewModel(loadNotesUseCase, addNotesUseCase, deleteNoteUseCase, androidLogger)
    }

    @Test
    fun `loadNotes should load and update notes state successfully`() = runTest {
        // Arrange
        val noteEntity = NoteEntity(id = 1, author = "Test Note", noteText = "Text")
        val noteModel = NoteModel(id = 1, author = "Test Note", noteText = "Text")

        val flowNotes: Flow<List<NoteModel>> = flow { emit(listOf(noteModel)) }

        // Мокаем поведение loadNotesUseCase.execute
        every { loadNotesUseCase.execute() } returns flowNotes

        // Act: запускаем loadNotes
        viewModel.loadNotes()

        // Добавим небольшую задержку для асинхронных операций
        advanceUntilIdle()

        // Assert
        val state = viewModel.notes.value
        assertTrue(state is State.Success, "Expected state to be Success, but was $state")
        assertEquals(listOf(noteModel), (state as State.Success<List<NoteModel>>).data)
    }

    @Test
    fun `addNote should add a note and reload notes`() = runTest {
        // Arrange
        val noteModel = NoteModel(id = 1, author = "Test Note", noteText = "Text")

        // Мокаем успешное выполнение addNotesUseCase.execute
        coEvery { addNotesUseCase.execute() } just Runs

        // Мокаем loadNotesUseCase.execute, чтобы при перезагрузке возвращались заметки
        val flowNotes: Flow<List<NoteModel>> = flow { emit(listOf(noteModel)) }
        every { loadNotesUseCase.execute() } returns flowNotes

        // Act: вызываем addNote
        viewModel.addNote()

        // Добавим небольшую задержку для асинхронных операций
        advanceUntilIdle()

        // Assert: проверяем, что загрузка заметок произошла снова
        val state = viewModel.notes.value
        assertTrue(state is State.Success, "Expected state to be Success, but was $state")
        assertEquals(listOf(noteModel), (state as State.Success<List<NoteModel>>).data)

        // Проверяем, что после добавления вызвалась загрузка заметок
        coVerify { loadNotesUseCase.execute() }
    }

    @Test
    fun `deleteNote should log error if exception occurs`() = runTest {
        // Arrange
        val noteModel = NoteModel(id = 1, author = "Test Note", noteText = "Text")
        val entity = Mapper.mapFromModelToDb(noteModel)

        val exception = Exception("Delete failed")

        // Мокаем вызов deleteNoteUseCase.execute, чтобы он выбросил исключение
        every { deleteNoteUseCase.execute(entity) } throws exception

        // Act: вызываем deleteNote
        viewModel.deleteNote(noteModel)

        // Добавим небольшую задержку для асинхронных операций
        advanceUntilIdle()

        // Assert: проверяем, что логирование ошибки произошло
        coVerify {
            androidLogger.d("Error during deleting", exception.toString())
        }
    }

    @After
    fun tearDown() {
        // Сбрасываем Main диспетчер после теста
        Dispatchers.resetMain()
    }

}
