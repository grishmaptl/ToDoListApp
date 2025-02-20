package com.example.todolistapp.domain.repository

import com.example.todolistapp.domain.model.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getTodos(): Flow<List<TodoItem>>
    suspend fun addTodo(todo: TodoItem)
}
