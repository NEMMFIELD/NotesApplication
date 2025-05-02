package com.example.notes.edit.data

import com.example.database.NoteEntity
import com.example.notes.api.NotesApi
import com.example.notes.api.model.NoteResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.objectbox.Box
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

class EditNotesRepositoryImplTest {
    private val api: NotesApi = mockk()
    private val noteBox: Box<NoteEntity> = mockk(relaxed = true)
    private lateinit var repository: EditNotesRepositoryImpl

    @Before
    fun setUp() {
        repository = EditNotesRepositoryImpl(api, noteBox)
    }

    @Test
    fun `editNote should fetch note from API and put it in the noteBox`() = runTest {
        // Arrange
        val noteDto = NoteResponse(  "Test", "Content") // пример DTO
        val noteEntity = NoteEntity( id = 1,  "Test",  "Content") // ожидаемый Entity

        // Мокаем вызов API
        coEvery { api.getNote() } returns listOf(noteDto)

        // Мокаем маппер (если это не extension, а статичный метод)
        mockkObject(EditNotesMapper)
        every { EditNotesMapper.mapFromDtoToEntity(noteDto) } returns noteEntity

        // Act
        repository.editNote()

        // Assert
        coVerify(exactly = 1) { api.getNote() }
        coVerify(exactly = 1) { noteBox.put(noteEntity) }

        // Очищаем статические моки
        unmockkObject(EditNotesMapper)
    }
}
