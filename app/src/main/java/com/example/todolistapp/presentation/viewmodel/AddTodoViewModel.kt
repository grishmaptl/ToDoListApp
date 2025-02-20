package com.example.todolistapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.common.UiState
import com.example.todolistapp.domain.model.TodoItem
import com.example.todolistapp.domain.usecase.AddTodoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTodoViewModel @Inject constructor(
    private val addTodoUseCase: AddTodoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<UiState>(UiState.Idle)
    val state = _state.asStateFlow()

    fun addTodo(text: String) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            delay(1000)
            try {
                addTodoUseCase(TodoItem(text = text))
                _state.value = UiState.Success
            } catch (e: Exception) {
                _state.value = UiState.Error("Failed to add TODO")
            }
        }
    }
}
