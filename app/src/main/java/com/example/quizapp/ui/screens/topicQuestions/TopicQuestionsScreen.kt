package com.example.quizapp.ui.screens.topicQuestions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quizapp.data.model.Question
import com.example.quizapp.ui.components.DeleteDialog
import com.example.quizapp.viewmodel.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicQuestionsScreen(viewModel: QuizViewModel, navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val questions = viewModel.questions.collectAsState()

    Scaffold(
        contentWindowInsets = WindowInsets(),
        topBar = {
            TopicQuestionsTopAppBar(
                topicName = viewModel.selectedTopic.value.name,
                scrollBehavior = scrollBehavior,
                onBackClick = {
                    navController.popBackStack()
                }
            )
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
            questions = questions.value,
            modifier = Modifier.padding(padding),
            onEditQuestionClick = { question -> viewModel.onOpenEditQuestionDialog(question) },
            onDeleteQuestionClick = { question -> viewModel.onOpenDeleteQuestionDialog(question) }
        )

        if (viewModel.isOpenNewQuestionDialog.value) {
            NewQuestionDialog(
                topicId = viewModel.selectedTopic.value.id,
                onDismissRequest = { viewModel.onCloseNewQuestionDialog() },
                onConfirmRequest = {
                    question -> viewModel.addQuestion(question = question)
                    viewModel.onCloseNewQuestionDialog()
                }
            )
        }
        if (viewModel.isOpenDeleteQuestionDialog.value) {
            DeleteQuestionDialog(
                onDismissRequest = { viewModel.onCloseDeleteQuestionDialog() },
                onConfirmRequest = {
                    viewModel.deleteQuestion(viewModel.selectedQuestion.value)
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
        text = "Edit question",
        selectedQuestion = selectedQuestion,
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
fun TopicQuestionsTopAppBar(topicName: String, scrollBehavior: TopAppBarScrollBehavior, onBackClick: () -> Unit) {
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
                onClick = onBackClick
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back button")
            }
        }
    )
}

@Composable
fun NewQuestionDialog(topicId: Int, onDismissRequest: () -> Unit, onConfirmRequest: (Question) -> Unit) {
    TopicQuestionDialog(
        text = "Enter a new question",
        topicId = topicId,
        onDismissRequest = onDismissRequest,
        onConfirmRequest = onConfirmRequest,
    )
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
    val arrowRoatation = animateFloatAsState(
        targetValue = if (expanded.value) 180f else 0f,
    )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {expanded.value = !expanded.value}),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Box() {
            Column(
                modifier = Modifier.padding(top = 4.dp, bottom = 10.dp, start = 10.dp, end = 10.dp),
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

                AnimatedVisibility(
                    visible = expanded.value,
                    enter = expandVertically(expandFrom = Alignment.Top),
                    exit = shrinkVertically(shrinkTowards = Alignment.Top)
                ) {
                    Column() {
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
                Spacer(modifier = Modifier.height(8.dp))
            }

            Icon(
                modifier = Modifier.align(Alignment.BottomCenter).rotate(arrowRoatation.value),
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Drop down arrow"
            )
        }

    }
}