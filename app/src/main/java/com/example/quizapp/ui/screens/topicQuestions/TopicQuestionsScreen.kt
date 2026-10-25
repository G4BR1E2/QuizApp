package com.example.quizapp.ui.screens.topicQuestions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quizapp.data.model.Question
import com.example.quizapp.navigation.TopicQuestionsRoute
import com.example.quizapp.viewmodel.QuizViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.ui.draw.paint
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.quizapp.data.model.Topic
import com.example.quizapp.database.QuizDatabase

@Composable
fun TopicQuestionsScreen(topicQuestions: TopicQuestionsRoute, viewModel: QuizViewModel) {
    Scaffold(floatingActionButton = {
        ExtendedFloatingActionButton(
            modifier = Modifier,
            onClick = { viewModel.isOpenNewTopicQuestionDialog.value = true },
            icon = { Icon(Icons.Filled.Add, "Add topic")},
            text = { Text("Add question") }
        )
    }) { padding ->
        QuestionList(viewModel, modifier = Modifier.padding(padding))
        if (viewModel.isOpenNewTopicQuestionDialog.value) {
            NewQuestionDialog(viewModel) {
                viewModel.isOpenNewTopicQuestionDialog.value = false
            }
        }
    }
}

@Composable
fun NewQuestionDialog(viewModel: QuizViewModel, onDismissRequest: () -> Unit) {
    val questionTextTextFieldState = rememberTextFieldState()
    val questionAnswerTextFieldState = rememberTextFieldState()
    val database = QuizDatabase(LocalContext.current)

    Dialog(onDismissRequest = {}) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    fontSize = 24.sp,
                    text = "Enter a new question",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                OutlinedTextField(
                    state = questionTextTextFieldState,
                    label = { Text("Question") },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    state = questionAnswerTextFieldState,
                    label = { Text("Question answer") },
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }

                    TextButton(onClick = {

                    }) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionList(viewModel: QuizViewModel, modifier: Modifier = Modifier) {
    LazyColumn() {
        items(viewModel.topicQuestions[viewModel.selectedTopic.value.id] ?: emptyList()) { question ->
            QuestionCard(question = question, viewModel = viewModel)
        }
    }
}

@Composable
fun QuestionCard(question: Question, viewModel: QuizViewModel) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Column() {
            Row() {
                Icon(
                    Icons.Filled.QuestionMark,
                    contentDescription = "Question"
                )
                Text("Qeastion: ")
            }
            Row() {
                Icon(
                    Icons.Filled.QuestionAnswer,
                    contentDescription = "Question"
                )
                Text("Qeastion asnwer: ")
            }
            Row() {
                Icon(
                    Icons.Filled.Category,
                    contentDescription = "Question type"
                )
                Text("Qeastion type: ")
            }
        }
    }
}