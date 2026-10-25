package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quizapp.database.QuizDatabase
import com.example.quizapp.navigation.AppNavigation
import com.example.quizapp.ui.screens.home.NavBar
import com.example.quizapp.ui.theme.QuizAppTheme
import com.example.quizapp.viewmodel.QuizViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: QuizViewModel = viewModel()
            QuizAppTheme {
                QuizApp(viewModel)
            }
        }
    }
}

@Composable
fun QuizApp(viewModel: QuizViewModel) {
    val database = QuizDatabase(LocalContext.current)
    val data = database.getData()
    viewModel.fillTopics(data.topics)
    viewModel.fillTopicQuestions(data.questions)
    NavBar()
}