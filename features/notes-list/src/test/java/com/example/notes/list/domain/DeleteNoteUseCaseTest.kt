package com.example.notes.list.domain

import com.example.database.NoteEntity
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Before
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class DeleteNoteUseCaseTest {
    private val repository: NotesRepository = mockk(relaxed = true)
    private lateinit var useCase: DeleteNoteUseCase

    @Before
    fun setUp() {
        useCase = DeleteNoteUseCase(repository)
    }

    @Test
    fun `execute should call deleteNote on repository`() {
        // Arrange
        val note = NoteEntity(1, "Test note")

        // Act
        useCase.execute(note)

        // Assert
        coVerify(exactly = 1) { repository.deleteNote(note) }
    }
}
