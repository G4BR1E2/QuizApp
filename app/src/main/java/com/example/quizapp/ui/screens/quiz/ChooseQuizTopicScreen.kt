package com.example.quizapp.ui.screens.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.data.model.Topic
import com.example.quizapp.viewmodel.QuizViewModel

@Composable
fun ChooseQuizTopicScreen(viewModel: QuizViewModel, onNavigateToQuizPlayScreen: () -> Unit) {
    Scaffold(

    ){ padding ->
        TopicList(
            modifier = Modifier.padding(padding),
            topics = viewModel.topics,
            onPlayTopicClick = {
                topic -> viewModel.setSelectedTopic(topic)
                onNavigateToQuizPlayScreen()
            }
        )
    }
}

@Composable
fun TopicList(
    topics: List<Topic>,
    modifier: Modifier = Modifier,
    onPlayTopicClick: (Topic) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(topics) { topic ->
            TopicCard(
                topic = topic,
                onClick = { onPlayTopicClick(topic) },
            )
        }
    }
}

@Composable
fun TopicCard(modifier: Modifier = Modifier, topic: Topic, onClick: () -> Unit) {
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
                ElevatedButton(
                    modifier = Modifier.padding(6.dp),
                    elevation = ButtonDefaults.elevatedButtonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 0.dp
                    ),
                    onClick = onClick
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Play topic quiz"
                    )
                    Text(text = "PLAY")
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