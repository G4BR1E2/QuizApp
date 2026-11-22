package com.example.quizapp.ui.screens.quiz

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.quizapp.data.model.Attempt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizAttemptsDetailsScreen(
    attempt: Attempt?,
    topicName: String?,
    onTopAppBarBackClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        contentWindowInsets = WindowInsets(),
        topBar = {
            AttemptDetailsTopAppBar(
                title = { Text(text = "$topicName topic - Attempt ${attempt?.attemptNumber}") },
                scrollBehavior = scrollBehavior,
                onBackClick = onTopAppBarBackClick
            )
        }
    ) { padding ->
        QuestionDetailsList(modifier = Modifier.padding(padding))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttemptDetailsTopAppBar(
    title: @Composable () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    onBackClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        title = title,
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back button")
            }
        }
    )
}

@Composable
fun QuestionDetailsList(modifier: Modifier = Modifier) {

}

@Composable
fun QuestionDetailsCard() {

}

