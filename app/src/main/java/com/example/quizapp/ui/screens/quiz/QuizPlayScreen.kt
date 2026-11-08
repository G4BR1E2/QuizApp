package com.example.quizapp.ui.screens.quiz

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
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.navigation.NavController
import com.example.quizapp.data.model.Question
import com.example.quizapp.data.model.QuestionType
import com.example.quizapp.viewmodel.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizPlayScreen(viewModel: QuizViewModel, navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        contentWindowInsets = WindowInsets(),
        topBar = {
            QuizPlayTopAppBar(
                topicName = viewModel.selectedTopic.value.name,
                scrollBehavior = scrollBehavior,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    ) { padding ->
        QuizQuestionList(
            modifier = Modifier.padding(padding),
            questions = viewModel.questions,
            onBackClick = {
                navController.popBackStack()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizPlayTopAppBar(topicName: String, scrollBehavior: TopAppBarScrollBehavior, onBackClick: () -> Unit) {
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
fun QuizQuestionList(questions: List<Question>, modifier: Modifier = Modifier, onBackClick: () -> Unit) {
    val fillInQuestionAnswerState = rememberTextFieldState("")
    val (selectedOption, onOptionSelected) = rememberSaveable { mutableStateOf("") }
    val userAnswers = rememberSaveable { mutableListOf<String>() }
    var correctAnswers = rememberSaveable { 0 }
    val questionIndex = rememberSaveable { mutableIntStateOf(0) }
    val numberOfQuestions = questions.count()

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
                        QuestionType.FILL_IN -> userAnswers.add(fillInQuestionAnswerState.text.toString().trim())
                        QuestionType.MULTIPLE_CHOICE -> userAnswers.add(selectedOption.trim())
                    }

                    questionIndex.intValue++
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
            userAnswers.forEachIndexed { index, answer ->
                if (answer == questions[index].correctAnswer)
                    correctAnswers++
            }

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
fun FillInQuestionAnswerForm(textFieldState: TextFieldState) {
    OutlinedTextField(
        state = textFieldState
    )
}

@Composable
fun MultipleChoiceQuestionAnswerForm(options: List<String>?, selectedOption: String, onOptionSelected: (String) -> Unit) {
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