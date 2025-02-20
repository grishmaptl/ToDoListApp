package com.example.todolistapp.domain.usecase

import com.example.todolistapp.domain.model.TodoItem
import com.example.todolistapp.domain.repository.TodoRepository
import javax.inject.Inject

class AddTodoUseCase @Inject constructor(private val repository: TodoRepository) {
    suspend operator fun invoke(todo: TodoItem) {
        if (todo.text.equals("error", ignoreCase = true)) throw Exception("Failed to add TODO")
        repository.addTodo(todo)
    }
}