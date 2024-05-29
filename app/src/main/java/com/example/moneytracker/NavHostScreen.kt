package com.example.moneytracker

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavHostScreen() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "/start_screen") {
        composable(route = "/home") {
            HomeScreen(navController)
        }

        composable(route = "/add") {
            AddExpense(navController)
        }

        composable(route ="/login"){
            LoginScreen(navController)
        }

        composable(route ="/registration"){
            RegistrationScreen(navController)
        }
        composable(route ="/start_screen"){
            StartScreen(navController = navController)
        }
    }
}