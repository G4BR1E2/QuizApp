package com.example.quizapp.ui.screens.topicQuestions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.quizapp.data.model.Question
import com.example.quizapp.data.model.QuestionType

data class InvalidQuestionError(
    val questionTextError: String? = null,
    val questionOptionsError: String? = null,
    val questionAnswerError: String? = null
) {
    val hasError: Boolean get() = questionTextError != null || questionOptionsError != null || questionAnswerError != null
}

fun Question.validate(): InvalidQuestionError {
    val textError = if (questionText.isBlank()) "Please enter the question text" else null

    when (type) {
        QuestionType.FILL_IN -> {
            val answerError = if (correctAnswer.isBlank()) "Please enter the question answer" else null
            return InvalidQuestionError(questionTextError = textError, questionAnswerError = answerError)
        }
        QuestionType.MULTIPLE_CHOICE -> {
            val choices = options
            val optionsError = if (choices.isNullOrEmpty() || choices.count() < 2) "Please enter at least 2 choices" else null
            val answerError = if (correctAnswer.isBlank()) "Please select a question answer" else null
            return InvalidQuestionError(questionTextError = textError, questionOptionsError = optionsError, questionAnswerError = answerError)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicQuestionDialog(text: String, selectedQuestion: Question? = null, topicId: Int? = null, onDismissRequest: () -> Unit, onConfirmRequest: (Question) -> Unit) {
    val questionTextTextFieldState = rememberTextFieldState(selectedQuestion?.questionText ?: "")
    val fillInQuestionTextFieldState = rememberTextFieldState(selectedQuestion?.correctAnswer ?: "")
    val (selectedType, setSelectedType) = rememberSaveable { mutableStateOf(selectedQuestion?.type ?: QuestionType.FILL_IN) }
    val options = rememberSaveable { selectedQuestion?.options?.toMutableStateList() ?: mutableStateListOf() }
    var correctAnswerMultipleChoice by rememberSaveable { mutableStateOf(selectedQuestion?.correctAnswer ?: "") }

    var errors by remember { mutableStateOf(InvalidQuestionError()) }

    val confirm = {
        val question = Question(
            id = selectedQuestion?.id ?: 0,
            topicId = selectedQuestion?.topicId ?: topicId ?: -1,
            questionText = questionTextTextFieldState.text.toString().trim(),
            type = selectedType,
            options = if (selectedType == QuestionType.FILL_IN) null else options.toList(),
            correctAnswer = if (selectedType == QuestionType.FILL_IN) fillInQuestionTextFieldState.text.toString().trim() else correctAnswerMultipleChoice.trim()
        )

        errors = question.validate()

        if (!errors.hasError)
            onConfirmRequest(question)
    }

    BasicAlertDialog(
        modifier = Modifier,
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = true)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .padding(20.dp),
            ) {
                Text(
                    fontSize = 24.sp,
                    text = text,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = questionTextTextFieldState,
                    isError = errors.questionTextError != null,
                    label = { Text("Question") },
                    supportingText = { errors.questionTextError?.let { Text(text = it) } },
                )

                QuestionTypeDropdownMenu(
                    selectedType = selectedType,
                    onTypeSelected = {
                        type -> setSelectedType(type)
                        errors = InvalidQuestionError()
                    }
                )

                when (selectedType) {
                    QuestionType.FILL_IN -> {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            state = fillInQuestionTextFieldState,
                            isError = errors.questionAnswerError != null,
                            label = { Text("Question answer") },
                            supportingText = { errors.questionAnswerError?.let { Text(text = it) } },
                            lineLimits = TextFieldLineLimits.SingleLine,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            onKeyboardAction = { confirm() }
                        )
                    }
                    QuestionType.MULTIPLE_CHOICE -> MultipleChoiceQuestion(
                        choices = options,
                        answer = correctAnswerMultipleChoice,
                        errors = errors,
                        onChoiceAdded = { choice -> options.add(choice) },
                        onChoiceRemoved = { choice -> options.remove(choice) },
                        onAnswerMarked = { answer -> correctAnswerMultipleChoice = answer }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }

                    TextButton(onClick = { confirm() }) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionTypeDropdownMenu(selectedType: QuestionType, onTypeSelected: (QuestionType) -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val questionTypeTextFieldState = rememberTextFieldState(selectedType.text)

    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth(),
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            modifier = Modifier.menuAnchor(
                type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                enabled = true
            ),
            label = { Text(text = "Choose a question type:", style = MaterialTheme.typography.titleMedium) },
            labelPosition = TextFieldLabelPosition.Above(),
            readOnly = true,
            state = questionTypeTextFieldState,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            QuestionType.entries.forEach { type ->
                DropdownMenuItem(
                    text = { Text(text = type.text) },
                    onClick = {
                        questionTypeTextFieldState.edit { replace(0, questionTypeTextFieldState.text.length, type.text) }
                        onTypeSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun MultipleChoiceQuestion(
    choices: List<String>,
    answer: String,
    errors: InvalidQuestionError,
    onChoiceAdded: (String) -> Unit,
    onChoiceRemoved: (String) -> Unit,
    onAnswerMarked: (String) -> Unit
) {
    val newChoiceTextFieldState = rememberTextFieldState("")
    var newChoiceErrorText by rememberSaveable { mutableStateOf<String?>(null) }

    val errorHandleAddChoice = {
        val choice = newChoiceTextFieldState.text.toString().trim()
        when {
            choice.isBlank() -> { newChoiceErrorText = "Please enter a choice" }
            choices.contains(choice) -> { newChoiceErrorText = "Already entered" }
            else -> {
                onChoiceAdded(choice)
                newChoiceTextFieldState.clearText()
                newChoiceErrorText = null
            }
        }
    }

    val errorHandleRemoveChoice: (String) -> Unit =  { choice ->
        when {
            choice == answer.trim() -> {
                onAnswerMarked("")
                onChoiceRemoved(choice)
            }
            else -> onChoiceRemoved(choice)
        }
    }

    Column(
        modifier = Modifier.selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        choices.forEach{ choice ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (choice == answer),
                        onClick = { onAnswerMarked(choice) },
                        role = Role.RadioButton
                    ),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (choice == answer),
                            onClick = null
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            modifier = Modifier.weight(1f, false),
                            text = choice
                        )
                    }

                    IconButton(onClick = { errorHandleRemoveChoice(choice) }) {
                        Icon(Icons.Filled.RemoveCircle, contentDescription = "Remove from choices")
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        state = newChoiceTextFieldState,
        isError = newChoiceErrorText != null,
        label = { Text(text = "New choice") },
        supportingText = { newChoiceErrorText?.let { Text(text = it) } },
        lineLimits = TextFieldLineLimits.SingleLine,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        trailingIcon = {
            IconButton(onClick = { errorHandleAddChoice() }) {
                Icon(imageVector = Icons.Filled.AddCircle, contentDescription = "Add choice")
            }
        },
        onKeyboardAction = { errorHandleAddChoice() }
    )

    val choiceError = errors.questionOptionsError ?: errors.questionAnswerError
    if (choiceError != null)
    Text(
        text = choiceError,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodyLarge
    )
}