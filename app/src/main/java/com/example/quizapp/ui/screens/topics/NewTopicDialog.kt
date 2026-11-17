package com.example.quizapp.ui.screens.topics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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