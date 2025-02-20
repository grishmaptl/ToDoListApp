package com.example.todolistapp.domain.usecase

import com.example.todolistapp.domain.model.TodoItem
import com.example.todolistapp.domain.repository.TodoRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class AddTodoUseCaseTest {

    private lateinit var addTodoUseCase: AddTodoUseCase
    private val repository: TodoRepository = mockk(relaxed = true)

    @Before
    fun setUp() {
        addTodoUseCase = AddTodoUseCase(repository)
    }

    @Test
    fun `add todo successfully`() = runBlocking {
        val todo = TodoItem(text = "My Task")

        addTodoUseCase(todo)

        coVerify { repository.addTodo(todo) } //Verify repository method was called
    }

    @Test
    fun `add todo with Error text should throw exception`() {
        val todo = TodoItem(text = "Error")

        assertThrows(Exception::class.java) {
            runBlocking {
                addTodoUseCase(todo) //Should throw exception
            }
        }
    }
}
