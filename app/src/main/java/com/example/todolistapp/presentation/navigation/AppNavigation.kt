package com.example.todolistapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todolistapp.presentation.screens.add.AddTodoScreen
import com.example.todolistapp.presentation.screens.main.MainScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = "main") {
        composable("main") { MainScreen(navController) }
        composable("add") { AddTodoScreen(navController) }
    }
}
