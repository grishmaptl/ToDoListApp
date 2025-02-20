package com.example.todolistapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.domain.model.TodoItem
import com.example.todolistapp.domain.usecase.GetTodosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTodosUseCase: GetTodosUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _todos = MutableStateFlow<List<TodoItem>>(emptyList())

    @OptIn(FlowPreview::class)
    val filteredTodos: StateFlow<List<TodoItem>> = _searchQuery
        .debounce(2000) // Delay filtering by 2 seconds
        .combine(_todos) { query, todos ->
            if (query.isEmpty()) todos else todos.filter {
                it.text.contains(
                    query,
                    ignoreCase = true
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Combine search query and todos to filter todos for unit testing
    init {
        viewModelScope.launch {
            getTodosUseCase().collect { todos ->
                println("getTodosUseCase() emitted: ${todos.size} items") // Debug log
                _todos.value = todos //  Ensure _todos updates
            }
        }

        viewModelScope.launch {
            filteredTodos.collect { todos ->
                println("filteredTodos updated: ${todos.size} items") // Debug log
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}
