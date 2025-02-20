package com.example.todolistapp.data.repository

import com.example.todolistapp.data.db.TodoDao
import com.example.todolistapp.data.db.TodoEntity
import com.example.todolistapp.domain.model.TodoItem
import com.example.todolistapp.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(private val dao: TodoDao) : TodoRepository {
    override fun getTodos(): Flow<List<TodoItem>> =
        dao.getTodos().map { list -> list.map { TodoItem(it.id, it.text) } }

    override suspend fun addTodo(todo: TodoItem) {
        dao.insertTodo(TodoEntity(text = todo.text))
    }
}
