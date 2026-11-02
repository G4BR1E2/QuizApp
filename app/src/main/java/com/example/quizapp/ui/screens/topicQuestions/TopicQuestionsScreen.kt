package com.example.quizapp.ui.screens.topicQuestions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fitInside
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.text.input.setTextAndSelectAll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
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
import androidx.compose.material3.FabPosition
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
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.quizapp.data.model.Question
import com.example.quizapp.data.model.QuestionType
import com.example.quizapp.navigation.TopicQuestionsRoute
import com.example.quizapp.viewmodel.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicQuestionsScreen(topicQuestions: TopicQuestionsRoute, viewModel: QuizViewModel, navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        contentWindowInsets = WindowInsets(),
        topBar = {
            TopicQuestionsTopAppBar(viewModel = viewModel, navController = navController, scrollBehavior)
        },
        floatingActionButton = {
        ExtendedFloatingActionButton(
            modifier = Modifier,
            onClick = { viewModel.onOpenNewTopicQuestionDialog() },
            icon = { Icon(Icons.Filled.Add, "Add topic")},
            text = { Text("Add question") }
        )},
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { padding ->
        QuestionList(viewModel, modifier = Modifier.padding(padding))
        if (viewModel.isOpenNewTopicQuestionDialog.value) {
            NewQuestionDialog(viewModel) {
                viewModel.onCloseNewTopicQuestionDialog()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicQuestionsTopAppBar(viewModel: QuizViewModel, navController: NavController, scrollBehavior: TopAppBarScrollBehavior) {
    CenterAlignedTopAppBar(
        modifier = Modifier,
        scrollBehavior = scrollBehavior,
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
    val topicQuestion = Question(-1, -1, "", QuestionType.FILL_IN, emptyList(), "")

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
                        QuestionType.FILL_IN -> FillInQuestion(viewModel, questionTextTextFieldState.text.trim().toString())
                        QuestionType.MULTIPLE_CHOICE -> MultipleChoiceQuestion(viewModel, questionTextTextFieldState.text.trim().toString())
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
                        viewModel.addTopicQuestion(viewModel.question.value)
                        onDismissRequest()
                    }) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@Composable
fun MultipleChoiceQuestion(viewModel: QuizViewModel, questionText: String) {
    val choices = rememberSaveable { mutableStateListOf<String>() }
    val newChoiceTextFieldState = rememberTextFieldState("")
    val questionAnswer = rememberSaveable { mutableStateOf("") }
    val questionAnswerTextFieldState = rememberTextFieldState()

    val nextQuestionId = viewModel.questions.filter { it.id == viewModel.selectedTopic.value.id }.maxBy { it.id }.id + 1
    viewModel.setQuestion(
        Question(
            id = nextQuestionId,
            topicId = viewModel.selectedTopic.value.id,
            questionText = questionText,
            type = viewModel.selectedQuestionType.value,
            options = choices,
            correctAnswer = questionAnswer.value
        )
    )

    if (questionAnswer.value != "") {
        OutlinedTextField(
            state = questionAnswerTextFieldState,
            readOnly = true,
            label = { Text(text = "Answer", fontWeight = FontWeight.Bold) },
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            trailingIcon = {
                IconButton(onClick = {
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
                        questionAnswer.value = choice
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
fun FillInQuestion(viewModel: QuizViewModel, questionText: String) {
    val questionAnswerTextFieldState = rememberTextFieldState("")
    val nextQuestionId = if (viewModel.questions.isEmpty()) 1 else viewModel.questions.maxBy { it.topicId }.topicId + 1
    viewModel.setQuestion(
        Question(
            id = nextQuestionId,
            topicId = viewModel.selectedTopic.value.id,
            questionText = questionText,
            type = viewModel.selectedQuestionType.value,
            correctAnswer = questionAnswerTextFieldState.text.trim().toString()
        )
    )

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
fun QuestionTypeDropdownMenu(viewModel: QuizViewModel, inputForm: @Composable (() -> Unit)) {
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
                        viewModel.setSelectedQuestionType(type)
                        expanded.value = false
                    }
                )
            }
        }
    }

    inputForm()
}

@Composable
fun QuestionList(viewModel: QuizViewModel, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(viewModel.questions.filter { it.topicId == viewModel.selectedTopic.value.id }) { question ->
            QuestionCard(question = question, viewModel = viewModel)
        }
    }
}

@Composable
fun QuestionCard(question: Question, viewModel: QuizViewModel) {
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
                        onClick = {
                        },
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Edit",
                        )
                    }
                    IconButton(
                        onClick = {
                        },
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = "DeleteTopicIcon",
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