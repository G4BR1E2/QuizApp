package com.example.quizapp.ui.screens.topics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.quizapp.data.model.Topic
import com.example.quizapp.ui.components.DeleteDialog
import com.example.quizapp.viewmodel.QuizViewModel

@Composable
fun TopicsScreen(viewModel: QuizViewModel, onNavigateToTopicQuestions: () -> Unit) {
    Scaffold(
        contentWindowInsets = WindowInsets.statusBars,
        floatingActionButton = {
        ExtendedFloatingActionButton(
            modifier = Modifier,
            onClick = { viewModel.onOpenNewTopicDialog() },
            icon = { Icon(Icons.Filled.Add, "Add topic")},
            text = { Text("Add topic") }
        )
    }) { padding ->
        TopicList(
            modifier = Modifier.padding(padding),
            topics = viewModel.topics,
            onTopicClick = {
                topic -> viewModel.setSelectedTopic(topic)
                onNavigateToTopicQuestions()
            },
            onEditTopicClick = { topic -> viewModel.onOpenEditTopicDialog(topic) },
            onDeleteTopicClick = { topic -> viewModel.onOpenDeleteTopicDialog(topic) }
        )

        if (viewModel.isOpenNewTopicDialog.value) {
            NewTopicDialog(
                onDismissRequest = { viewModel.onCloseNewTopicDialog() },
                onConfirmRequest = {
                    topic -> viewModel.addTopic(topic)
                    viewModel.onCloseNewTopicDialog()
                }
            )
        }
        if (viewModel.isOpenDeleteTopicDialog.value) {
            DeleteTopicDialog(
                topicName = viewModel.selectedTopic.value.name,
                onDismissRequest = { viewModel.onCloseDeleteTopicDialog() },
                onConfirmRequest = {
                    viewModel.deleteTopic(viewModel.selectedTopic.value)
                    viewModel.onCloseDeleteTopicDialog()
                }
            )
        }
        if (viewModel.isOpenEditTopicDialog.value) {
            EditTopicDialog(
                selectedTopic = viewModel.selectedTopic.value,
                onDismissRequest = { viewModel.onCloseEditTopicDialog() },
                onConfirmRequest = {
                    updatedTopic -> viewModel.updateTopic(updatedTopic)
                    viewModel.onCloseEditTopicDialog()
                }
            )
        }
    }
}

@Composable
fun DeleteTopicDialog(topicName: String, onDismissRequest: () -> Unit, onConfirmRequest: () -> Unit) {
    DeleteDialog(
        text = "Are you sure you want to delete the $topicName topic?",
        onDismissRequest = onDismissRequest,
        onConfirmRequest = onConfirmRequest
    )
}

@Composable
fun EditTopicDialog(selectedTopic: Topic, onDismissRequest: () -> Unit, onConfirmRequest: (Topic) -> Unit) {
    val topicNameTextFieldState = rememberTextFieldState(selectedTopic.name)
    val topicDescriptionTextFieldState = rememberTextFieldState(selectedTopic.description ?: "")

    Dialog(onDismissRequest = onDismissRequest) {
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
                        val updatedTopic = selectedTopic.copy(
                            name = topicNameTextFieldState.text.toString(),
                            description = topicDescriptionTextFieldState.text.toString()
                        )
                        onConfirmRequest(updatedTopic)
                    }) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@Composable
fun NewTopicDialog(onDismissRequest: () -> Unit, onConfirmRequest: (Topic) -> Unit) {
    val topicNameTextFieldState = rememberTextFieldState()
    val topicDescriptionTextFieldState = rememberTextFieldState()

    Dialog(onDismissRequest = onDismissRequest) {
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
                    modifier = Modifier.padding(bottom = 8.dp),
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
                        val newTopic = Topic(
                            id = 0,
                            name = topicNameTextFieldState.text.trim().toString(),
                            description = if (topicDescriptionTextFieldState.text.isBlank()) null else topicDescriptionTextFieldState.text.trim().toString()
                        )
                        onConfirmRequest(newTopic)
                    }) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@Composable
fun TopicList(
    topics: List<Topic>,
    modifier: Modifier = Modifier,
    onTopicClick: (Topic) -> Unit,
    onEditTopicClick: (Topic) -> Unit,
    onDeleteTopicClick: (Topic) -> Unit)
{
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(topics) { topic ->
            TopicCard(
                topic = topic,
                onClick = { onTopicClick(topic) },
                onEditClick = { onEditTopicClick(topic) },
                onDeleteClick = { onDeleteTopicClick(topic) }
            )
        }
    }
}

@Composable
fun TopicCard(modifier: Modifier = Modifier, topic: Topic, onClick: () -> Unit, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    ElevatedCard(
        onClick = {
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
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f, fill = false)
                )

                Row() {
                    IconButton(
                        onClick = onEditClick,
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Edit",
                        )
                    }
                    IconButton(
                        onClick = onDeleteClick,
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