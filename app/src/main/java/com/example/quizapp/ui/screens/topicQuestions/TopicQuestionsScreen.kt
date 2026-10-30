package com.example.quizapp.ui.screens.topicQuestions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.text.input.setTextAndSelectAll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.quizapp.data.model.Question
import com.example.quizapp.data.model.QuestionType
import com.example.quizapp.database.QuizDatabase
import com.example.quizapp.navigation.TopicQuestionsRoute
import com.example.quizapp.viewmodel.QuizViewModel

@Composable
fun TopicQuestionsScreen(topicQuestions: TopicQuestionsRoute, viewModel: QuizViewModel, navController: NavController) {
    Scaffold(
        topBar = {
            TopicQuestionsTopAppBar(viewModel = viewModel, navController = navController)
        },
        floatingActionButton = {
        ExtendedFloatingActionButton(
            modifier = Modifier,
            onClick = { viewModel.isOpenNewTopicQuestionDialog.value = true },
            icon = { Icon(Icons.Filled.Add, "Add topic")},
            text = { Text("Add question") }
        )
    }) { padding ->
        QuestionList(viewModel, modifier = Modifier.padding(padding))
        if (viewModel.isOpenNewTopicQuestionDialog.value) {
            NewQuestionDialog(viewModel) {
                viewModel.isOpenNewTopicQuestionDialog.value = false
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicQuestionsTopAppBar(viewModel: QuizViewModel, navController: NavController) {
    CenterAlignedTopAppBar(
        modifier = Modifier.statusBarsPadding(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        title = {
            Text(text = "${viewModel.selectedTopic.value.name} topic")
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back button")
            }
        }
    )
}

@Composable
fun NewQuestionDialog(viewModel: QuizViewModel, onDismissRequest: () -> Unit) {
    val questionTextTextFieldState = rememberTextFieldState()
    val database = QuizDatabase(LocalContext.current)

    Dialog(onDismissRequest = {}) {
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
                    text = "Enter a new question for the ${viewModel.selectedTopic.value.name} topic",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                OutlinedTextField(
                    state = questionTextTextFieldState,
                    label = { Text("Question") },
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                QuestionTypeDropdownMenu(viewModel) {
                    when (viewModel.selectedQuestionType.value) {
                        QuestionType.FILL_IN -> FillInQuestion()
                        QuestionType.MULTIPLE_CHOICE -> MultipleChoiceQuestion()
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

                    }) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@Composable
fun MultipleChoiceQuestion() {
    val choices = rememberSaveable { mutableStateListOf<String>() }
    val newChoiceTextFieldState = rememberTextFieldState("")
    val questionAnswer = rememberSaveable { mutableStateOf("") }
    val questionAnswerTextFieldState = rememberTextFieldState()

    if (questionAnswer.value != "") {
        OutlinedTextField(
            state = questionAnswerTextFieldState,
            readOnly = true,
            label = { Text(text = "Answer", fontWeight = FontWeight.Bold) },
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            trailingIcon = {
                IconButton(onClick = {
                    choices.add(questionAnswer.value)
                    questionAnswer.value = ""
                }) {
                    Icon(imageVector = Icons.Default.RemoveCircle, contentDescription = "Delete choice")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier.requiredHeightIn(max = 225.dp)
    ) {
        itemsIndexed(choices, key = { _, choice -> choice}) { index, choice ->
            OutlinedTextField(
                state = rememberTextFieldState(choice),
                readOnly = true,
                label = { Text(text = "Choice ${index + 1}") },
                lineLimits = TextFieldLineLimits.SingleLine,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                trailingIcon = {
                    IconButton(onClick = { choices.removeAt(index) }) {
                        Icon(imageVector = Icons.Default.RemoveCircle, contentDescription = "Delete choice")
                    }
                },
                leadingIcon = {
                    IconButton(onClick = {
                        if (questionAnswer.value != "") { choices.add(questionAnswer.value) }
                        questionAnswer.value = choice
                        choices.removeAt(index)
                        questionAnswerTextFieldState.edit { replace(0, length, questionAnswer.value) }
                    }) {
                        Icon(imageVector = Icons.Default.QuestionAnswer, contentDescription = "Mark as question answer")
                    }
                }
            )
        }
    }

    OutlinedTextField(
        state = newChoiceTextFieldState,
        label = { Text(text = "New choice") },
        lineLimits = TextFieldLineLimits.SingleLine,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        onKeyboardAction = {
            val choiceText = newChoiceTextFieldState.text.trim().toString()
            if (choiceText.isBlank() || choiceText == "Enter a choice") {
                newChoiceTextFieldState.setTextAndSelectAll("Enter a choice")
            } else if (choices.contains(choiceText)) {
                newChoiceTextFieldState.setTextAndSelectAll("Already entered")
            }
            else {
                choices.add(choiceText)
                newChoiceTextFieldState.setTextAndPlaceCursorAtEnd("")
            }
        }
    )
}

@Composable
fun FillInQuestion() {
    val questionAnswerTextFieldState = rememberTextFieldState("")
    OutlinedTextField(
        state = questionAnswerTextFieldState,
        label = { Text("Question answer") },
        modifier = Modifier.padding(bottom = 8.dp),
        lineLimits = TextFieldLineLimits.SingleLine,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        onKeyboardAction = {
            val answerText = questionAnswerTextFieldState.text.trim().toString()
            if (answerText.isBlank() || answerText == "Enter an answer") {
                questionAnswerTextFieldState.setTextAndSelectAll("Enter an answer")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionTypeDropdownMenu(viewModel: QuizViewModel, input: @Composable (() -> Unit)) {
    val expanded = rememberSaveable { mutableStateOf(false) }
    val questionTypeTextFieldState = rememberTextFieldState(QuestionType.FILL_IN.text)

    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = { expanded.value = !expanded.value }
    ) {
        TextField(
            modifier = Modifier.menuAnchor(
                type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                enabled = true
            ),
            readOnly = true,
            state = questionTypeTextFieldState,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) }
        )
        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            QuestionType.entries.forEach { type ->
                DropdownMenuItem(
                    text = { Text(text = type.text) },
                    onClick = {
                        questionTypeTextFieldState.edit { replace(0, questionTypeTextFieldState.text.length, type.text) }
                        viewModel.selectedQuestionType.value = type
                        expanded.value = false
                    }
                )
            }
        }
    }

    input()
}

@Composable
fun QuestionList(viewModel: QuizViewModel, modifier: Modifier = Modifier) {
    LazyColumn() {
        items(viewModel.topicQuestions[viewModel.selectedTopic.value.id] ?: emptyList()) { question ->
            QuestionCard(question = question, viewModel = viewModel)
        }
    }
}

@Composable
fun QuestionCard(question: Question, viewModel: QuizViewModel) {
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
            Row() {
                Icon(
                    Icons.Filled.QuestionMark,
                    contentDescription = "Question"
                )
                Text("Qeastion: ")
            }
            Row() {
                Icon(
                    Icons.Filled.QuestionAnswer,
                    contentDescription = "Question"
                )
                Text("Qeastion asnwer: ")
            }
            Row() {
                Icon(
                    Icons.Filled.Category,
                    contentDescription = "Question type"
                )
                Text("Qeastion type: ")
            }
        }
    }
}