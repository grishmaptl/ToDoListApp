package com.example.todolistapp.domain.usecase

import com.example.todolistapp.domain.model.TodoItem
import com.example.todolistapp.domain.repository.TodoRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetTodosUseCaseTest {

    private lateinit var getTodosUseCase: GetTodosUseCase
    private val repository: TodoRepository = mockk()

    @Before
    fun setUp() {
        getTodosUseCase = GetTodosUseCase(repository)
    }

    @Test
    fun `get todos returns list`() = runBlocking {
        val mockTodos = listOf(TodoItem(1, "Task 1"), TodoItem(2, "Task 2"))
        every { repository.getTodos() } returns flowOf(mockTodos)

        val result = getTodosUseCase()

        result.collect { todos ->
            assertEquals(mockTodos, todos) //Compare expected and actual lists
        }
    }
}
