package com.example.quizapp.ui.screens.topics

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.example.quizapp.data.model.Topic
import com.example.quizapp.database.QuizDatabase
import com.example.quizapp.navigation.TopicsRoute
import com.example.quizapp.viewmodel.QuizViewModel

@Composable
fun TopicsScreen(topic: TopicsRoute, viewModel: QuizViewModel) {
    val openNewTopicDialog = rememberSaveable { mutableStateOf(false) }

    Scaffold(floatingActionButton = {
        ExtendedFloatingActionButton(
            modifier = Modifier,
            onClick = { openNewTopicDialog.value = true },
            icon = { Icon(Icons.Filled.Add, "Add topic")},
            text = { Text("Add topic") }
        )
    }) { padding ->
        TopicList(viewModel = viewModel, modifier = Modifier.padding(padding))
        if (openNewTopicDialog.value) {
            NewTopicDialog(viewModel) { openNewTopicDialog.value = false }
        }
    }
}

@Composable
fun NewTopicDialog(viewModel: QuizViewModel, onDismissRequest: () -> Unit) {
    val topicNameTextFieldState = rememberTextFieldState()
    val topicDescriptionTextFieldState = rememberTextFieldState()
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
                    text = "Enter a new topic",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                OutlinedTextField(
                    state = topicNameTextFieldState,
                    label = { Text("Topic name") },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    state = topicDescriptionTextFieldState,
                    label = { Text("Topic description (optional)") },
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
                        val nextTopicId = if (database.getData().topics.isEmpty()) 1 else database.getData().topics.maxBy { topic -> topic.id }.id + 1
                        viewModel.addTopic(
                            Topic(
                                nextTopicId,
                                topicNameTextFieldState.text.toString(),
                                topicDescriptionTextFieldState.text.toString()
                            )
                        )
                        database.saveData(viewModel.topics, viewModel.topicQuestions)
                        onDismissRequest()
                    }) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@Composable
fun TopicList(viewModel: QuizViewModel, modifier: Modifier = Modifier) {
    LazyColumn() {
        items(viewModel.topics) { topic ->
            TopicCard(topic = topic)
        }
    }
}

@Composable
fun TopicCard(modifier: Modifier = Modifier, topic: Topic) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = topic.name,
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = topic.description,
            fontSize = 18.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}