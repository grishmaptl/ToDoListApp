package com.example.todolistapp.presentation.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.todolistapp.presentation.viewmodel.MainViewModel

@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    val todos by viewModel.filteredTodos.collectAsState()
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val listState = rememberLazyListState() // Track scrolling state
    val showFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            modifier = Modifier.systemBarsPadding(), title = {
                Text(
                    text = "TODO List", fontSize = 20.sp, fontWeight = FontWeight.Bold
                )
            }, backgroundColor = Color.White, contentColor = Color.Black, elevation = 4.dp
        )
    }, floatingActionButton = {
        AnimatedVisibility(
            visible = showFab, enter = fadeIn() + scaleIn(), exit = fadeOut() + scaleOut()
        ) {
            FloatingActionButton(
                onClick = { navController.navigate("add") },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add TODO")
            }
        }
    }) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OutlinedTextField(value = searchQuery,
                onValueChange = { newQuery ->
                    searchQuery = newQuery
                    viewModel.onSearchQueryChanged(newQuery)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            if (todos.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Press the + button to add a TODO item")
                }
            } else {
                LazyColumn(state = listState) {
                    items(todos, key = { it.id }) { todo ->
                        TodoItemCard(todo.text)
                    }
                }
            }
        }
    }
}

@Composable
fun TodoItemCard(todo: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp),
        elevation = 2.dp
    ) {
        Text(
            text = todo, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.body1
        )
    }
}
