package com.example.quizapp.ui.screens.topics

import android.os.Parcelable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.quizapp.data.model.Topic
import kotlinx.parcelize.Parcelize

@Parcelize
data class InvalidTopicError(
    val topicNameError: String? = null,
) : Parcelable {
    val hasError: Boolean get() = topicNameError != null
}

fun Topic.validate(): InvalidTopicError {
    val nameError = if (this.name.isBlank()) "Please enter the topic name" else null
    return InvalidTopicError(topicNameError = nameError)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicDialog(dialogHeader: @Composable () -> Unit, selectedTopic: Topic? = null, onDismissRequest: () -> Unit, onConfirmRequest: (Topic) -> Unit) {
    val topicNameTextFieldState = rememberTextFieldState(selectedTopic?.name ?: "")
    val topicDescriptionTextFieldState = rememberTextFieldState(selectedTopic?.description ?: "")

    var errors by rememberSaveable { mutableStateOf(InvalidTopicError()) }

    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = true)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 8.dp)
            ) {
                dialogHeader()

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    state = topicNameTextFieldState,
                    isError = errors.topicNameError != null,
                    label = { Text("Topic name") },
                    supportingText = { errors.topicNameError?.let { Text(text = it) } },
                    lineLimits = TextFieldLineLimits.SingleLine
                )

                Spacer(modifier = Modifier.height(8.dp))

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
                        val name = topicNameTextFieldState.text.toString().trim()
                        val description = if (topicDescriptionTextFieldState.text.isBlank()) null else topicDescriptionTextFieldState.text.toString().trim()

                        val topic = Topic(
                            id = selectedTopic?.id ?: 0,
                            name = name,
                            description = description
                        )

                        errors = topic.validate()

                        if (!errors.hasError)
                            onConfirmRequest(topic)
                    }) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}