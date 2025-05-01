package com.example.notes.list.domain

import com.example.database.NoteEntity
import com.example.notes.list.data.Mapper
import com.example.notes.list.data.NoteModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoadNotesUseCaseTest {

    private val repository: NotesRepository = mockk()
    private lateinit var useCase: LoadNotesUseCase

    @Before
    fun setUp() {
        useCase = LoadNotesUseCase(repository)
    }

    @Test
    fun `execute should return mapped list of NoteModel`() = runTest {
        // Arrange
        val testEntities = listOf(NoteEntity(1, "Hello"), NoteEntity(2, "World"))
        val expectedModels = testEntities.map { Mapper.mapFromDbToModel(it) }

        every { repository.observeNotes() } returns flowOf(testEntities)

        val results = mutableListOf<List<NoteModel>>()

        // Act
        useCase.execute().toList(results)

        // Assert
        assertEquals(1, results.size)
        assertEquals(expectedModels, results[0])
    }
}
