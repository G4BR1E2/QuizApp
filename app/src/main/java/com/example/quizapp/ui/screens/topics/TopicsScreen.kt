package com.example.quizapp.ui.screens.topics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.quizapp.data.model.Topic
import com.example.quizapp.database.QuizDatabase
import com.example.quizapp.navigation.AppNavigation
import com.example.quizapp.navigation.TopicsRoute
import com.example.quizapp.viewmodel.QuizViewModel

@Composable
fun TopicsScreen(topic: TopicsRoute, viewModel: QuizViewModel, onNavigateToTopicQuestions: () -> Unit) {
    Scaffold(floatingActionButton = {
        ExtendedFloatingActionButton(
            modifier = Modifier,
            onClick = { viewModel.isOpenNewTopicDialog.value = true },
            icon = { Icon(Icons.Filled.Add, "Add topic")},
            text = { Text("Add topic") }
        )
    }) { padding ->
        TopicList(viewModel = viewModel, modifier = Modifier.padding(padding), onTopicClick = onNavigateToTopicQuestions)
        if (viewModel.isOpenNewTopicDialog.value) {
            NewTopicDialog(viewModel) { viewModel.isOpenNewTopicDialog.value = false }
        }
        if (viewModel.isOpenDeleteTopicDialog.value) {
            DeleteTopicDialog(viewModel) { viewModel.isOpenDeleteTopicDialog.value = false }
        }
        if (viewModel.isOpenEditTopicDialog.value) {
            EditTopicDialog(viewModel) { viewModel.isOpenEditTopicDialog.value = false }
        }
    }
}

@Composable
fun DeleteTopicDialog(viewModel: QuizViewModel, onDismissRequest: () -> Unit) {
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
                Icon(
                    Icons.Filled.Warning,
                    contentDescription = "DeleteTopicWarning"
                )
                Text(
                    fontSize = 24.sp,
                    text = "Are you sure you want to delete the ${viewModel.selectedTopic.value.name} topic?",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row() {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }

                    TextButton(onClick = {
                        viewModel.deleteTopic(viewModel.selectedTopic.value)
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
fun EditTopicDialog(viewModel: QuizViewModel, onDismissRequest: () -> Unit) {
    val topicNameTextFieldState = rememberTextFieldState(viewModel.selectedTopic.value.name)
    val topicDescriptionTextFieldState = rememberTextFieldState(viewModel.selectedTopic.value.description ?: "")
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
                        val updatedTopic = viewModel.selectedTopic.value.copy(
                            name = topicNameTextFieldState.text.toString(),
                            description = topicDescriptionTextFieldState.text.toString()
                        )
                        viewModel.updateTopic(updatedTopic)
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
fun TopicList(viewModel: QuizViewModel, modifier: Modifier = Modifier, onTopicClick: () -> Unit) {
    LazyColumn() {
        items(viewModel.topics) { topic ->
            TopicCard(topic = topic, viewModel = viewModel, onClick = onTopicClick)
        }
    }
}

@Composable
fun TopicCard(modifier: Modifier = Modifier, topic: Topic, viewModel: QuizViewModel, onClick: () -> Unit) {
    ElevatedCard(
        onClick = {
            viewModel.selectedTopic.value = topic
            onClick()
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Column() {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = topic.name,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(16.dp)
                )

                Row() {
                    IconButton(
                        onClick = {
                            viewModel.isOpenEditTopicDialog.value = true
                            viewModel.selectedTopic.value = topic
                        },
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Edit",
                        )
                    }
                    IconButton(
                        onClick = {
                            viewModel.isOpenDeleteTopicDialog.value = true
                            viewModel.selectedTopic.value = topic
                        },
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = "DeleteTopicIcon",
                        )
                    }
                }
            }

            Text(
                text = topic.description ?: "",
                fontSize = 18.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}