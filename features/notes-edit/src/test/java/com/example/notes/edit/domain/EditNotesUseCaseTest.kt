package com.example.notes.edit.domain

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class EditNotesUseCaseTest {
    private val repository: EditNotesRepository = mockk()
    private lateinit var useCase: EditNotesUseCase

    @Before
    fun setUp() {
        useCase = EditNotesUseCase(repository)
    }

    @Test
    fun `execute should call editNote on repository`() = runTest {
        // Arrange
        coEvery { repository.editNote() } just Runs

        // Act
        useCase.execute()

        // Assert
        coVerify(exactly = 1) { repository.editNote() }
    }
}
