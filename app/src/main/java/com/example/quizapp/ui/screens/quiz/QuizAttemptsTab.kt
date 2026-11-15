package com.example.quizapp.ui.screens.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.data.model.Attempt
import com.example.quizapp.data.model.Topic
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun QuizAttemptsTab(attempts: List<Attempt>, topics: List<Topic>) {
    AttemptList(
        modifier = Modifier,
        attempts = attempts,
        topics = topics
    )
}

@Composable
fun AttemptList(modifier: Modifier = Modifier, attempts: List<Attempt>, topics: List<Topic>) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(attempts) { attempt ->
            AttemptCard(
                attempt = attempt,
                topicName = topics.find { it.id == attempt.topicId }!!.name
            )
        }
    }
}

@Composable
fun AttemptCard(attempt: Attempt, topicName: String) {
    val scorePercentage: Float = attempt.score.toFloat()/attempt.totalQuestions.toFloat() * 100
    val dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault())

    OutlinedCard(
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.outline),
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = "$topicName topic - Attempt ${attempt.attemptNumber}",
                fontSize = 24.sp
            )
            Text(
                text = buildAnnotatedString {
                    append("Score: ${attempt.score}/${attempt.totalQuestions} ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("(${String.format("%.2f", scorePercentage)}%)")
                    }
                }
            )
            Text(
                text = "Start time: ${dateTimeFormatter.format(Instant.ofEpochMilli(attempt.start))}"
            )
            Text(
                text = "Finish time: ${dateTimeFormatter.format(Instant.ofEpochMilli(attempt.end))}"
            )
            Text(
                text = "Total time: ${(attempt.end-attempt.start).milliseconds.inWholeSeconds} seconds"
            )
        }
    }
}