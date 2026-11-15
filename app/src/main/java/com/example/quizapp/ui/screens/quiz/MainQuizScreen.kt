package com.example.quizapp.ui.screens.quiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.example.quizapp.data.model.Topic
import com.example.quizapp.viewmodel.QuizViewModel

enum class QuizTab(val title: String) {
    CHOOSE_QUIZ_TOPIC("Quizzes"),
    QUIZ_ATTEMPTS("Attempts")
}

@Composable
fun MainQuizScreen(viewModel: QuizViewModel, onNavigateToQuizPlayScreen: (Topic) -> Unit) {
    val selectedDestination = rememberSaveable { mutableStateOf(QuizTab.CHOOSE_QUIZ_TOPIC) }
    val topics = viewModel.topics.collectAsState()
    val questions = viewModel.questions.collectAsState()
    val attempts = viewModel.attempts.collectAsState()

    Scaffold(

    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            SecondaryTabRow(selectedTabIndex = selectedDestination.value.ordinal) {
                QuizTab.entries.forEach { tab ->
                    Tab(
                        selected = tab.ordinal == selectedDestination.value.ordinal,
                        onClick = { selectedDestination.value = tab},
                        text = { Text(text = tab.title) }
                    )
                }
            }

            when (selectedDestination.value) {
                QuizTab.CHOOSE_QUIZ_TOPIC -> ChooseQuizTopicTab(
                    topics = topics.value.filter { topic -> questions.value.count { it.topicId == topic.id } > 0  },
                    onTopicPlay = {
                        topic -> onNavigateToQuizPlayScreen(topic)
                    },
                )
                QuizTab.QUIZ_ATTEMPTS -> QuizAttemptsTab(
                    attempts = attempts.value,
                    topics = topics.value
                )
            }
        }
    }
}