package com.example.quizapp.ui.screens.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.quizapp.data.model.Attempt
import com.example.quizapp.data.model.Question
import com.example.quizapp.data.model.Topic
import kotlin.math.roundToInt

@Composable
fun ChooseQuizTopicTab(
    topics: List<Topic>,
    questions: List<Question>,
    attempts: List<Attempt>,
    onTopicPlay: (Topic) -> Unit,
    onAddTopic: () -> Unit,
    onAddQuestion: (Topic) -> Unit
) {
    TopicList(
        topics = topics,
        questions = questions,
        attempts = attempts,
        onPlayTopicClick = onTopicPlay,
        onAddTopicClick = onAddTopic,
        onAddQuestionClick = onAddQuestion
    )
}

@Composable
fun TopicList(
    topics: List<Topic>,
    questions: List<Question>,
    attempts: List<Attempt>,
    modifier: Modifier = Modifier,
    onPlayTopicClick: (Topic) -> Unit,
    onAddTopicClick: () -> Unit,
    onAddQuestionClick: (Topic) -> Unit
) {
    if (topics.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = "No topics. Create a topic and its questions to play it."
                )

                Text(
                    modifier = Modifier.clickable(
                        onClick = onAddTopicClick
                    ),
                    text = "Add topic +",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
    else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(topics) { topic ->
                val numberOfQuestions = questions.count { it.topicId == topic.id }
                val bestAttemptPercent = attempts.filter { it.topicId == topic.id }.maxOfOrNull { it.score.toFloat() / it.totalQuestions.toFloat() }
                TopicCard(
                    topic = topic,
                    numberOfQuestions = numberOfQuestions,
                    bestAttemptPercent = bestAttemptPercent,
                    onPlayClick = { onPlayTopicClick(topic) },
                    onAddQuestionClick = { onAddQuestionClick(topic) }
                )
            }
        }
    }
}

@Composable
fun TopicTag(modifier: Modifier = Modifier, text: String) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun TopicCard(
    modifier: Modifier = Modifier,
    topic: Topic,
    bestAttemptPercent: Float?,
    numberOfQuestions: Int,
    onPlayClick: () -> Unit,
    onAddQuestionClick: () -> Unit
) {
    val hasQuestions = numberOfQuestions > 0

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f, fill = false)) {
                    Text(
                        text = topic.name,
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                ElevatedButton(
                    modifier = Modifier.align(Alignment.Top),
                    elevation = ButtonDefaults.elevatedButtonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 0.dp,
                    ),
                    enabled = hasQuestions,
                    onClick = onPlayClick
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Play topic quiz"
                    )
                    Text(text = "PLAY")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                TopicTag(
                    text =
                        when {
                            numberOfQuestions == 1 -> "$numberOfQuestions Question"
                            else -> "$numberOfQuestions Questions"
                        }
                )
                TopicTag(
                    text = if (bestAttemptPercent == null) "No attempts" else "Best: ${(bestAttemptPercent * 100).roundToInt()}%"
                )
            }

            if (!hasQuestions) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().background(color = Color.Yellow.copy(alpha = 0.4f)).padding(vertical = 4.dp, horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f, false)) {
                        Text(
                            text = "Add a question to play this topic.",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            modifier = Modifier.clickable(
                                onClick = onAddQuestionClick
                            ),
                            text = "Add question +",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.primary,
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.HelpOutline,
                        contentDescription = "Add questions to play this topic"
                    )
                }
            }

            if (!topic.description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = topic.description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}