package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.quizapp.ui.screens.home.NavBar
import com.example.quizapp.ui.theme.QuizAppTheme
import com.example.quizapp.viewmodel.QuizViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: QuizViewModel = viewModel(factory = QuizViewModel.Factory)
            QuizAppTheme {
                QuizApp(viewModel)
            }
        }
    }
}

@Composable
fun QuizApp(viewModel: QuizViewModel) {
    val navController = rememberNavController()
    NavBar(viewModel = viewModel, navController = navController)
}