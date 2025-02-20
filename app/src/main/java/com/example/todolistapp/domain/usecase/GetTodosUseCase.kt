package com.example.todolistapp.domain.usecase

import com.example.todolistapp.domain.model.TodoItem
import com.example.todolistapp.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodosUseCase @Inject constructor(private val repository: TodoRepository) {
    operator fun invoke(): Flow<List<TodoItem>> = repository.getTodos()
}