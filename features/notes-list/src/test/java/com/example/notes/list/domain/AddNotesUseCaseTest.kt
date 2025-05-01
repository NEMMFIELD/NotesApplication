package com.example.notes.list.domain

import com.example.notes.api.model.NoteResponse
import com.example.notes.list.data.Mapper
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddNotesUseCaseTest {
    private val repository: NotesRepository = mockk(relaxed = true)
    private lateinit var useCase: AddNotesUseCase

    @Before
    fun setUp() {
        useCase = AddNotesUseCase(repository)
    }

    @Test
    fun `execute should load notes and add the first mapped note`() = runTest {
        // Arrange
        val dtoList = listOf(NoteResponse("1", "From API"))
        val expectedEntity = Mapper.mapFromDtoToEntity(dtoList.first())

        coEvery { repository.loadNotes() } returns dtoList
        coEvery { repository.addNote(expectedEntity) } just Runs

        // Act
        useCase.execute()

        // Assert
        coVerify(exactly = 1) { repository.loadNotes() }
        coVerify(exactly = 1) { repository.addNote(expectedEntity) }
    }
}
