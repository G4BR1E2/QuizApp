package com.example.quizapp.ui.screens.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.quizapp.data.model.Question
import com.example.quizapp.data.model.QuestionType
import com.example.quizapp.viewmodel.QuizViewModel
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizPlayScreen(
    topicId: Int,
    viewModel: QuizViewModel,
    onBackClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val questions by viewModel.questions.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val topic = topics.find { it.id == topicId }
    val topicQuestions = questions.filter { it.topicId == topicId }

    val start = rememberSaveable { Clock.System.now().toEpochMilliseconds() }

    var isOpenQuitQuizDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        contentWindowInsets = WindowInsets(),
        topBar = {
            QuizPlayTopAppBar(
                topicName = topic?.name ?: "",
                scrollBehavior = scrollBehavior,
                onBackClick = { isOpenQuitQuizDialog =true }
            )
        }
    ) { padding ->
        QuizQuestionList(
            modifier = Modifier.padding(padding),
            questions = topicQuestions,
            onBackClick = onBackClick,
            onFinishClick = {
                questionToAttempt, quizEndTime -> viewModel.addAttempt(
                    topicId = topic?.id ?: -1,
                    questionToAttempt = questionToAttempt,
                    questions = topicQuestions,
                    start = start,
                    end = quizEndTime
                )
            }
        )

        if (isOpenQuitQuizDialog) {
            QuitQuizDialog(
                topicName = topic?.name ?: "",
                onDismissRequest = { isOpenQuitQuizDialog = false },
                onConfirmRequest = {
                    onBackClick()
                }
            )
        }
    }

    BackHandler() {
        isOpenQuitQuizDialog = true
    }
}

@Composable
fun QuitQuizDialog(
    topicName: String,
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit
) {
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
                    .padding(20.dp)
            ) {
                Icon(
                    Icons.Filled.Warning,
                    contentDescription = "QuitQuizWarning"
                )
                Text(
                    fontSize = 24.sp,
                    text = "Are you sure you want to quit playing the $topicName quiz?",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }

                    TextButton(onClick = onConfirmRequest ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizPlayTopAppBar(
    topicName: String,
    scrollBehavior: TopAppBarScrollBehavior,
    onBackClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = Modifier,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        title = {
            Text(text = "$topicName quiz")
        },
        navigationIcon = {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back button")
            }
        }
    )
}

@Composable
fun QuizQuestionList(
    questions: List<Question>,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onFinishClick: (Map<Int, String>, Long) -> Unit
) {
    val fillInQuestionAnswerState = rememberTextFieldState("")
    val (selectedOption, onOptionSelected) = rememberSaveable { mutableStateOf("") }
    val questionToAttempt = rememberSaveable { mutableMapOf<Int, String>() }
    val questionIndex = rememberSaveable { mutableIntStateOf(0) }
    val numberOfQuestions = questions.count()

    val correctAnswers = rememberSaveable(questionIndex.intValue) {
        questionToAttempt.count { item ->
            questions.find { it.id == item.key }?.correctAnswer == item.value
        }
    }

    Column(modifier = modifier.padding(10.dp).fillMaxWidth()) {
        if (questionIndex.intValue <= numberOfQuestions - 1) {
            QuizQuestionCard(
                question = questions[questionIndex.intValue],
                fillInQuestionTextFieldState = fillInQuestionAnswerState,
                multipleChoiceQuestionSelectedOption = selectedOption,
                multipleChoiceQuestionOnOptionSelected = onOptionSelected
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedButton(
                modifier = Modifier.align(Alignment.End),
                onClick = {
                    when (questions[questionIndex.intValue].type) {
                        QuestionType.FILL_IN -> questionToAttempt[questions[questionIndex.intValue].id] = fillInQuestionAnswerState.text.toString().trim()
                        QuestionType.MULTIPLE_CHOICE -> questionToAttempt[questions[questionIndex.intValue].id] = selectedOption.trim()
                    }

                    fillInQuestionAnswerState.setTextAndPlaceCursorAtEnd("")
                    onOptionSelected("")
                    questionIndex.intValue++

                    if (numberOfQuestions == questionIndex.intValue) {
                        onFinishClick(questionToAttempt, Clock.System.now().toEpochMilliseconds())
                    }
                }
            ) {
                Text(
                    text = if (numberOfQuestions - 1 == questionIndex.intValue) "Finish" else "Next question",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector =
                        if (numberOfQuestions - 1 == questionIndex.intValue) Icons.Filled.Check
                        else Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next question"
                )
            }
        }
        else {
            Text(
                text = buildAnnotatedString {
                    append("You have ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold, fontStyle = FontStyle.Italic)) {
                        append("$correctAnswers")
                    }
                    append(" out of ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold, fontStyle = FontStyle.Italic)) {
                        append("$numberOfQuestions")
                    }
                    append(" correct answers")
                },
                fontSize = 20.sp
            )
            OutlinedButton(
                modifier = Modifier.align(Alignment.Start),
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to quizzes"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Back to quizzes",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun QuizQuestionCard(
    question: Question,
    fillInQuestionTextFieldState: TextFieldState,
    multipleChoiceQuestionSelectedOption: String,
    multipleChoiceQuestionOnOptionSelected: (String) -> Unit
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                modifier = Modifier,
                text = question.questionText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(12.dp))
            when (question.type) {
                QuestionType.FILL_IN -> FillInQuestionAnswerForm(textFieldState = fillInQuestionTextFieldState)
                QuestionType.MULTIPLE_CHOICE -> MultipleChoiceQuestionAnswerForm(
                    options = question.options,
                    selectedOption = multipleChoiceQuestionSelectedOption,
                    onOptionSelected = multipleChoiceQuestionOnOptionSelected
                )
            }
        }
    }
}

@Composable
fun FillInQuestionAnswerForm(
    textFieldState: TextFieldState
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        state = textFieldState
    )
}

@Composable
fun MultipleChoiceQuestionAnswerForm(
    options: List<String>?,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        options!!.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (option == selectedOption),
                        onClick = { onOptionSelected(option) },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = null
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = option
                )
            }
        }
    }
}