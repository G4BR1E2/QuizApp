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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.data.model.Question
import com.example.quizapp.ui.components.DeleteDialog
import com.example.quizapp.viewmodel.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicQuestionsScreen(
    topicId: Int,
    viewModel: QuizViewModel,
    onTopAppBarBackClick: () -> Unit,
    onNavigateToNewQuestionDialog: (topicId: Int) -> Unit,
    onNavigateToEditQuestionDialog: (questionId: Int) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val questions by viewModel.questions.collectAsState()
    val topics = viewModel.topics.collectAsState()
    val topic = topics.value.find { it.id == topicId }

    var isOpenDeleteQuestionDialog by rememberSaveable { mutableStateOf(false) }
    var selectedQuestionId = rememberSaveable { -1 }

    Scaffold(
        contentWindowInsets = WindowInsets(),
        topBar = {
            TopicQuestionsTopAppBar(
                topicName = topic?.name ?: "",
                scrollBehavior = scrollBehavior,
                onBackClick = onTopAppBarBackClick
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                modifier = Modifier,
                onClick = { onNavigateToNewQuestionDialog(topicId) },
                icon = { Icon(Icons.Filled.Add, "Add topic")},
                text = { Text("Add question") }
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { padding ->
        QuestionList(
            topicId = topicId,
            questions = questions,
            modifier = Modifier.padding(padding),
            onEditQuestionClick = { questionId -> onNavigateToEditQuestionDialog(questionId) },
            onDeleteQuestionClick = {
                questionId -> selectedQuestionId = questionId
                isOpenDeleteQuestionDialog = true
            }
        )

        if (isOpenDeleteQuestionDialog) {
            DeleteQuestionDialog(
                onDismissRequest = { isOpenDeleteQuestionDialog = false },
                onConfirmRequest = {
                    viewModel.deleteQuestion(questions.find { it.id == selectedQuestionId })
                    isOpenDeleteQuestionDialog = false
                }
            )
        }
    }
}

@Composable
fun DeleteQuestionDialog(
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit
) {
    DeleteDialog(
        dialogHeader = {
            Text(
                fontSize = 24.sp,
                text = "Are you sure you want to delete this question?",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        },
        onDismissRequest = onDismissRequest,
        onConfirmRequest = onConfirmRequest
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicQuestionsTopAppBar(
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
fun QuestionList(
    questions: List<Question>,
    modifier: Modifier = Modifier,
    topicId: Int,
    onEditQuestionClick: (Int) -> Unit,
    onDeleteQuestionClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(questions.filter { it.topicId == topicId }) { question ->
            QuestionCard(
                question = question,
                onEditClick = { onEditQuestionClick(question.id) },
                onDeleteClick = { onDeleteQuestionClick(question.id) }
            )
        }
    }
}

@Composable
fun QuestionCard(
    question: Question,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val arrowRoatation = animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
    )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {expanded = !expanded}),
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
                    visible = expanded,
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
                            Column(modifier = Modifier.padding(start = 20.dp)) {
                                question.options.forEach { choice ->
                                    Row(verticalAlignment = Alignment.CenterVertically) {
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