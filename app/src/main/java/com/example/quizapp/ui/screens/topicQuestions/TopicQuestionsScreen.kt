package com.example.quizapp.ui.screens.topicQuestions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.RadioButtonChecked
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quizapp.data.model.Question
import com.example.quizapp.data.model.QuestionType
import com.example.quizapp.navigation.TopicQuestionsRoute
import com.example.quizapp.ui.components.DeleteDialog
import com.example.quizapp.viewmodel.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicQuestionsScreen(topicQuestions: TopicQuestionsRoute, viewModel: QuizViewModel, navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        contentWindowInsets = WindowInsets(),
        topBar = {
            TopicQuestionsTopAppBar(topicName = viewModel.selectedTopic.value.name, navController = navController, scrollBehavior)
        },
        floatingActionButton = {
        ExtendedFloatingActionButton(
            modifier = Modifier,
            onClick = { viewModel.onOpenNewQuestionDialog() },
            icon = { Icon(Icons.Filled.Add, "Add topic")},
            text = { Text("Add question") }
        )},
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { padding ->
        QuestionList(
            topicId = viewModel.selectedTopic.value.id,
            questions = viewModel.questions,
            modifier = Modifier.padding(padding),
            onEditQuestionClick = { question -> viewModel.onOpenEditQuestionDialog(question) },
            onDeleteQuestionClick = { question -> viewModel.onOpenDeleteQuestionDialog(question) }
        )

        if (viewModel.isOpenNewQuestionDialog.value) {
            NewQuestionDialog(
                topicId = viewModel.selectedTopic.value.id,
                onDismissRequest = { viewModel.onCloseNewQuestionDialog() },
                onConfirmRequest = {
                    question -> viewModel.addTopicQuestion(question = question)
                    viewModel.onCloseNewQuestionDialog()
                }
            )
        }
        if (viewModel.isOpenDeleteQuestionDialog.value) {
            DeleteQuestionDialog(
                onDismissRequest = {
                    viewModel.onCloseDeleteQuestionDialog()
                },
                onConfirmRequest = {
                    viewModel.deleteTopicQuestion(viewModel.selectedQuestion.value)
                    viewModel.onCloseDeleteQuestionDialog()
                }
            )
        }
        if (viewModel.isOpenEditQuestionDialog.value) {
            EditTopicQuestionDialog(
                selectedQuestion = viewModel.selectedQuestion.value,
                onDismissRequest = { viewModel.onCloseEditQuestionDialog() },
                onConfirmRequest = {
                    question -> viewModel.updateQuestion(question)
                    viewModel.onCloseEditQuestionDialog()
                }
            )
        }
    }
}

@Composable
fun EditTopicQuestionDialog(selectedQuestion: Question, onDismissRequest: () -> Unit, onConfirmRequest: (Question) -> Unit) {
    TopicQuestionDialog(
        selectedQuestion = selectedQuestion,
        topicId = 0,
        onDismissRequest = onDismissRequest,
        onConfirmRequest = onConfirmRequest,
    )
}

@Composable
fun DeleteQuestionDialog(onDismissRequest: () -> Unit, onConfirmRequest: () -> Unit) {
    DeleteDialog(
        text = "Are you sure you want to delete this question?",
        onDismissRequest = onDismissRequest,
        onConfirmRequest = onConfirmRequest
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicQuestionsTopAppBar(topicName: String, navController: NavController, scrollBehavior: TopAppBarScrollBehavior) {
    CenterAlignedTopAppBar(
        modifier = Modifier,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        title = {
            Text(text = topicName)
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
fun NewQuestionDialog(topicId: Int, onDismissRequest: () -> Unit, onConfirmRequest: (Question) -> Unit) {
    TopicQuestionDialog(
        topicId = topicId,
        onDismissRequest = onDismissRequest,
        onConfirmRequest = onConfirmRequest,
    )
}

@Composable
fun MultipleChoiceQuestion(choices: SnapshotStateList<String>, answer: MutableState<String>) {
    val newChoiceTextFieldState = rememberTextFieldState("")
    val questionAnswerTextFieldState = rememberTextFieldState(answer.value)

    if (answer.value != "") {
        OutlinedTextField(
            state = questionAnswerTextFieldState,
            readOnly = true,
            label = { Text(text = "Answer", fontWeight = FontWeight.Bold) },
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            trailingIcon = {
                IconButton(onClick = {
                    answer.value = ""
                }) {
                    Icon(imageVector = Icons.Default.RemoveCircle, contentDescription = "Delete answer")
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
                        answer.value = choice
                        questionAnswerTextFieldState.edit { replace(0, length, answer.value) }
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
            if (choiceText.isBlank() || choiceText == "Enter a choice" || choiceText == "Already entered") {
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
fun FillInQuestion(answer: MutableState<String>) {
    val questionAnswerTextFieldState = rememberTextFieldState()
    answer.value = questionAnswerTextFieldState.text.trim().toString()

    OutlinedTextField(
        state = questionAnswerTextFieldState,
        label = { Text("Question answer") },
        modifier = Modifier.padding(bottom = 8.dp),
        lineLimits = TextFieldLineLimits.SingleLine,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        onKeyboardAction = {
            if (answer.value.isBlank() || answer.value == "Enter an answer") {
                questionAnswerTextFieldState.setTextAndSelectAll("Enter an answer")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionTypeDropdownMenu(selectedType: MutableState<QuestionType>, inputForm: @Composable (() -> Unit)) {
    val expanded = rememberSaveable { mutableStateOf(false) }
    val questionTypeTextFieldState = rememberTextFieldState(selectedType.value.text)

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
                        selectedType.value = type
                        expanded.value = false
                    }
                )
            }
        }
    }

    inputForm()
}

@Composable
fun QuestionList(
    questions: List<Question>,
    modifier: Modifier = Modifier,
    topicId: Int,
    onEditQuestionClick: (Question) -> Unit,
    onDeleteQuestionClick: (Question) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(questions.filter { it.topicId == topicId }) { question ->
            QuestionCard(
                question = question,
                onEditClick = { onEditQuestionClick(question) },
                onDeleteClick = { onDeleteQuestionClick(question) }
            )
        }
    }
}

@Composable
fun QuestionCard(question: Question, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    val expanded = rememberSaveable { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier
            .clickable(onClick = {expanded.value = !expanded.value})
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    Icons.Filled.QuestionMark,
                    contentDescription = "Question"
                )
                Row() {
                    IconButton(
                        onClick = onEditClick,
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "EditQuestionIcon",
                        )
                    }
                    IconButton(
                        onClick = onDeleteClick,
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = "DeleteQuestionIcon",
                        )
                    }
                }
            }
            Text(
                modifier = Modifier,
                text = question.questionText,
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
            )

            if (expanded.value) {
                if (!question.options.isNullOrEmpty()) {
                    Row(modifier = Modifier) {
                        Text(
                            text = "Choices:",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                    }
                    Column() {
                        question.options!!.forEach { choice ->
                            Row(
                                modifier = Modifier.padding(start = 20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    modifier = Modifier.size(height = 16.dp, width = 16.dp),
                                    imageVector = Icons.Filled.RadioButtonChecked,
                                    contentDescription = "Choice"
                                )
                                Text(modifier = Modifier.padding(start = 4.dp), text = choice)
                            }
                        }
                    }
                }

                Row(modifier = Modifier) {
                    Text(
                        text = "Answer: ",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                    Text(text = question.correctAnswer)
                }
                Row(modifier = Modifier) {
                    Text(
                        text = "Question type: ",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                    Text(text = question.type.text)
                }
            }
        }
    }
}