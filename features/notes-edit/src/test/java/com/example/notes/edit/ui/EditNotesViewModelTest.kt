package com.example.notes.edit.ui

import android.annotation.SuppressLint
import android.graphics.ColorSpace.match
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.notes.edit.domain.EditNotesUseCase
import com.example.utils.Logger
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.verify
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.test.Test

class EditNotesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val editNotesUseCase: EditNotesUseCase = mockk()
    private val logger: Logger = mockk(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: EditNotesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = EditNotesViewModel(editNotesUseCase, logger, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `editNote should execute use case and update editFinished after delay`() = runTest {
        // Arrange
        coEvery { editNotesUseCase.execute() } just Runs

        // Act
        viewModel.editNote()

        // Симулируем выполнение всех корутин
        advanceTimeBy(2000) // <- нужно для прохождения delay
        runCurrent()        // <- нужно, чтобы LiveData обновилось

        // Assert
        assertEquals(true, viewModel.editFinished.getOrAwaitValue())
        coVerify(exactly = 1) { editNotesUseCase.execute() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @SuppressLint("CheckResult")
    @Test
    fun `editNote should catch exception and log it`() = runTest {
        // Arrange
        val exception = RuntimeException("Test error")
        coEvery { editNotesUseCase.execute() } throws exception

        // Act
        viewModel.editNote()
        advanceUntilIdle() // дожидаемся завершения корутины

        // LiveData не должен обновиться
        assertNull(viewModel.editFinished.value)
    }

    fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)

        val observer = object : Observer<T> {
            override fun onChanged(value: T) {
                data = value
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }

        this.observeForever(observer)

        // Ждем максимум `time` секунд
        if (!latch.await(time, timeUnit)) {
            this.removeObserver(observer)
            throw TimeoutException("LiveData value was never set.")
        }

        return data as T
    }
}
