package com.example.todolistapp.presentation.screens.add

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.todolistapp.common.UiState
import com.example.todolistapp.presentation.viewmodel.AddTodoViewModel

@Composable
fun AddTodoScreen(navController: NavController, viewModel: AddTodoViewModel = hiltViewModel()) {
    var text by rememberSaveable { mutableStateOf("") }
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state) {
        if (state is UiState.Success) {
            navController.popBackStack()
        } else if (state is UiState.Error) {
            Toast.makeText(context, "Failed to add TODO", Toast.LENGTH_LONG).show()
            navController.popBackStack()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Add TODO", fontSize = 20.sp, fontWeight = FontWeight.Bold
                )
            }, backgroundColor = Color.White, contentColor = Color.Black, elevation = 4.dp
        )

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Enter TODO") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 16.dp, horizontal = 16.dp
                )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.addTodo(text) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE)),
            enabled = state !is UiState.Loading
        ) {
            Text("Add TODO", color = Color.White)
        }

        if (state is UiState.Loading) {
            CircularProgressIndicator()
        }
    }
}
