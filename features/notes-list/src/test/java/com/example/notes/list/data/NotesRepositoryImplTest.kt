package com.example.notes.list.data

import com.example.database.NoteEntity
import com.example.notes.api.NotesApi
import com.example.notes.api.model.NoteResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.objectbox.Box
import io.objectbox.query.Query
import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.jupiter.api.BeforeEach
import kotlin.coroutines.cancellation.CancellationException
import kotlin.test.Test
import kotlin.test.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
class NotesRepositoryImplTest {
    private lateinit var api: NotesApi
    private lateinit var noteBox: Box<NoteEntity>
    private lateinit var repository: NotesRepositoryImpl

    @Before
    fun setup() {
        api = mockk()
        noteBox = mockk(relaxed = true)
        repository = NotesRepositoryImpl(api, noteBox)
    }

    @Test
    fun `loadNotes should return notes from API`() = runTest {
        // given
        val notes = listOf(NoteResponse("1", "Test note"))
        coEvery { api.getNote() } returns notes

        // when
        val result = repository.loadNotes()

        // then
        assertEquals(notes, result)
        coVerify { api.getNote() }
    }

    @Test
    fun `addNote should put note into box`() {
        // given
        val note = NoteEntity(1, "Test")

        // when
        repository.addNote(note)

        // then
        verify { noteBox.put(note) }
    }

    @Test
    fun `getAllNotes should return all notes from box`() {
        // given
        val notes = listOf(NoteEntity(1, "Test"))
        every { noteBox.all } returns notes

        // when
        val result = repository.getAllNotes()

        // then
        assertEquals(notes, result)
        verify { noteBox.all }
    }

    @Test
    fun `deleteNote should remove note from box`() {
        // given
        val note = NoteEntity(1, "To delete")

        // when
        repository.deleteNote(note)

        // then
        verify { noteBox.remove(note) }
    }

    // Псевдо-тест на observeNotes (пример с Flow и cancel)
    @Test
    fun `observeNotes should emit data from observer`() = runTest {
        // Arrange
        val mockQuery: Query<NoteEntity> = mockk()
        val mockSubscription: DataSubscription = mockk(relaxed = true)
        val observerSlot = slot<DataObserver<List<NoteEntity>>>()
        val testData = listOf(NoteEntity(1, "Test note"))

        every { noteBox.query().build() } returns mockQuery
        every {
            mockQuery.subscribe().observer(capture(observerSlot))
        } returns mockSubscription

        val results = mutableListOf<List<NoteEntity>>()

        // Act
        val job = launch {
            repository.observeNotes().take(1).collect {
                results.add(it)
            }
        }

        waitUntil(condition = { observerSlot.isCaptured })

        observerSlot.captured.onData(testData)

        job.join()

        // Assert
        assertEquals(1, results.size)
        assertEquals(testData, results[0])
        verify { mockSubscription.cancel() }
    }

    private suspend fun waitUntil(
        condition: () -> Boolean,
        timeout: Long = 1000L,
        checkInterval: Long = 10L
    ) {
        val start = System.currentTimeMillis()
        while (!condition()) {
            if (System.currentTimeMillis() - start > timeout) {
                throw CancellationException("Condition not met within $timeout ms")
            }
            delay(checkInterval)
        }
    }
}
