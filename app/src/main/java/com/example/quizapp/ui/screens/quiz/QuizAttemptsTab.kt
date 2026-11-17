package com.example.quizapp.ui.screens.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.GridTrackSize
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.data.model.Attempt
import com.example.quizapp.data.model.Topic
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

private val dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault())

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
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(attempts) { attempt ->
            AttemptCard(
                attempt = attempt,
                topicName = topics.find { it.id == attempt.topicId }!!.name
            )
        }
    }
}

@OptIn(ExperimentalGridApi::class)
@Composable
fun AttemptCard(attempt: Attempt, topicName: String) {
    val scorePercentage: Float = attempt.score.toFloat()/attempt.totalQuestions.toFloat() * 100

    val scoreColor = when {
        scorePercentage >= 80f -> Color(0xff62a865)
        scorePercentage >= 50f -> Color(0xffe8a13c)
        else -> Color(0xfffa5f5f)
    }

    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f, false)
                ) {
                    Text(
                        modifier = Modifier,
                        text = "Attempt ${attempt.attemptNumber}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        modifier = Modifier,
                        text = "$topicName topic",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Surface(
                    modifier = Modifier.align(Alignment.Top),
                    shape = RoundedCornerShape(12.dp),
                    color = scoreColor.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, scoreColor)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                        text = "${scorePercentage.roundToInt()}%",
                        fontWeight = FontWeight.ExtraBold,
                        color = scoreColor,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(6.dp))

            Grid(
                config = {
                    repeat(2) {
                        column(GridTrackSize.Auto)
                    }
                    repeat(4) {
                        row(GridTrackSize.Auto)
                    }
                    columnGap(8.dp)
                },
            ) {
                Text(text = "Score:")
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    Text(text = "${attempt.score}/${attempt.totalQuestions}")
                }

                Text(text = "Start time:")
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    Text(text = "${dateTimeFormatter.format(Instant.ofEpochMilli(attempt.start))}")
                }

                Text(text = "Finish time:")
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    Text(text = "${dateTimeFormatter.format(Instant.ofEpochMilli(attempt.end))}")
                }

                Text(text = "Total time:")
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    Text(text = "${(attempt.end - attempt.start).milliseconds.inWholeSeconds}s")
                }
            }
        }
    }
}