package com.example.quizapp.ui.screens.topics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.data.model.Topic
import com.example.quizapp.ui.components.DeleteDialog
import com.example.quizapp.viewmodel.QuizViewModel

@Composable
fun TopicsScreen(
    viewModel: QuizViewModel,
    onNavigateToTopicQuestions: (Int) -> Unit,
    onNavigateToNewTopicDialog: () -> Unit,
    onNavigateToEditTopicDialog: (Int) -> Unit
) {
    val topics = viewModel.topics.collectAsState()
    var selectedTopicId = rememberSaveable { -1 }
    var isOpenDeleteTopicDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        contentWindowInsets = WindowInsets.statusBars,
        floatingActionButton = {
        ExtendedFloatingActionButton(
            modifier = Modifier,
            onClick = onNavigateToNewTopicDialog,
            icon = { Icon(Icons.Filled.Add, "Add topic")},
            text = { Text("Add topic") }
        )
    }) { padding ->
        if (topics.value.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.padding(20.dp),
                    text = "No topics yet. Create a topic to get started.",
                    textAlign = TextAlign.Center
                )
            }
        }
        else {
            TopicList(
                modifier = Modifier.padding(padding),
                topics = topics.value,
                onTopicClick = onNavigateToTopicQuestions,
                onEditTopicClick = onNavigateToEditTopicDialog,
                onDeleteTopicClick = {
                    topicId ->  selectedTopicId = topicId
                    isOpenDeleteTopicDialog = true
                }
            )
        }

        if (isOpenDeleteTopicDialog) {
            DeleteTopicDialog(
                topicName = topics.value.find { it.id == selectedTopicId }?.name ?: "",
                onDismissRequest = { isOpenDeleteTopicDialog = false },
                onConfirmRequest = {
                    viewModel.deleteTopic(topics.value.find { it.id == selectedTopicId })
                    isOpenDeleteTopicDialog = false
                }
            )
        }
    }
}

@Composable
fun DeleteTopicDialog(topicName: String, onDismissRequest: () -> Unit, onConfirmRequest: () -> Unit) {
    DeleteDialog(
        dialogHeader = {
            Text(
                fontSize = 24.sp,
                text = "Are you sure you want to delete the $topicName topic?",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        },
        onDismissRequest = onDismissRequest,
        onConfirmRequest = onConfirmRequest
    )
}

@Composable
fun TopicList(
    topics: List<Topic>,
    modifier: Modifier = Modifier,
    onTopicClick: (Int) -> Unit,
    onEditTopicClick: (Int) -> Unit,
    onDeleteTopicClick: (Int) -> Unit)
{
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(12.dp)
    ) {
        items(topics) { topic ->
            TopicCard(
                topic = topic,
                onClick = { onTopicClick(topic.id) },
                onEditClick = { onEditTopicClick(topic.id) },
                onDeleteClick = { onDeleteTopicClick(topic.id) }
            )
        }
    }
}

@Composable
fun TopicCard(
    modifier: Modifier = Modifier,
    topic: Topic, onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth(),
        onClick = {
            onClick()
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically).weight(1f, fill = false),
                    text = topic.name,
                    fontSize = 24.sp,
                )

                Row(modifier = Modifier.align(Alignment.Top)) {
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

            if (!topic.description.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = topic.description,
                    fontSize = 18.sp,
                )
            }
        }
    }
}