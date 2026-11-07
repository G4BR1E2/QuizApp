package com.example.quizapp.ui.screens.topicQuestions

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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.quizapp.data.model.Question
import com.example.quizapp.data.model.QuestionType

@Composable
fun TopicQuestionDialog(selectedQuestion: Question? = null, topicId: Int, onDismissRequest: () -> Unit, onConfirmRequest: (Question) -> Unit) {
    val questionTextTextFieldState = rememberTextFieldState(selectedQuestion?.questionText ?: "")
    val selectedType = rememberSaveable { mutableStateOf(selectedQuestion?.type ?: QuestionType.FILL_IN) }
    val options = rememberSaveable { selectedQuestion?.options?.toMutableStateList() ?: mutableStateListOf() }
    val correctAnswer = rememberSaveable { mutableStateOf(selectedQuestion?.correctAnswer ?: "") }

    Dialog(
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
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    fontSize = 24.sp,
                    text = "Edit the current question",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                OutlinedTextField(
                    state = questionTextTextFieldState,
                    label = { Text("Question") },
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                QuestionTypeDropdownMenu(selectedType) {
                    when (selectedType.value) {
                        QuestionType.FILL_IN -> FillInQuestion(correctAnswer)
                        QuestionType.MULTIPLE_CHOICE -> MultipleChoiceQuestion(options, correctAnswer)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }

                    TextButton(onClick = {
                        val question = selectedQuestion?.copy(
                            questionText = questionTextTextFieldState.text.trim().toString(),
                            type = selectedType.value,
                            options = if (options.isEmpty() || selectedType.value == QuestionType.FILL_IN) null else options,
                            correctAnswer = correctAnswer.value
                        ) ?: Question(
                            0,
                            topicId = topicId,
                            questionText = questionTextTextFieldState.text.trim().toString(),
                            type = selectedType.value,
                            options = if (options.isEmpty()) null else options,
                            correctAnswer = correctAnswer.value
                        )
                        onConfirmRequest(question)
                    }) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}