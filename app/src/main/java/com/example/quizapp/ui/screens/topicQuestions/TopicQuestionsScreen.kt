package com.example.quizapp.ui.screens.topicQuestions

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FlexibleBottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    val questions by viewModel.questions.collectAsStateWithLifecycle()
    val topics by viewModel.topics.collectAsStateWithLifecycle()

    val topic = remember(topics, topicId) { topics.find { it.id == topicId } }
    val topicQuestions = remember(questions, topicId) { questions.filter { it.topicId == topicId } }

    var selectedQuestionId by rememberSaveable { mutableStateOf<Int?>(null) }

    var isEnabledEditMode by rememberSaveable { mutableStateOf(false) }
    val selectedQuestionIds = rememberSaveable { mutableStateSetOf<Int>() }
    var isOpenDeleteQuestionsDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        contentWindowInsets = WindowInsets(),
        topBar = {
            TopicQuestionsTopAppBar(
                title = {
                    Text(
                        text =
                            if (isEnabledEditMode)
                                when {
                                    selectedQuestionIds.isEmpty() -> "Select questions"
                                    else -> "Selected questions: ${selectedQuestionIds.count()}"
                                }
                            else { topic?.name ?: "" }
                    )
                },
                scrollBehavior = scrollBehavior,
                onBackClick = onTopAppBarBackClick,
                isEnabledEditMode = isEnabledEditMode,
                onCancelEditClick = {
                    selectedQuestionIds.clear()
                    isEnabledEditMode = false
                }
            )
        },
        bottomBar = {
            AnimatedContent(
                targetState = isEnabledEditMode,
            ) { isEditMode ->
                when (isEditMode) {
                    true -> {
                        TopicQuestionsBottomAppBar(
                            buttonEnabled = selectedQuestionIds.isNotEmpty(),
                            onDeleteClick = { isOpenDeleteQuestionsDialog = true }
                        )
                    }
                    false -> {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(end = 20.dp, bottom = 20.dp),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            ExtendedFloatingActionButton(
                                onClick = { onNavigateToNewQuestionDialog(topicId) },
                                icon = { Icon(Icons.Filled.Add, "Add topic")},
                                text = { Text("Add question") }
                            )
                        }
                    }
                }
            }
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { padding ->
        if (topicQuestions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.padding(20.dp),
                    text = "No questions yet. Add a question to get started.",
                    textAlign = TextAlign.Center
                )
            }
        }
        else {
            QuestionList(
                selectedQuestionIds = selectedQuestionIds.toSet(),
                questions = topicQuestions,
                modifier = Modifier.padding(padding),
                onEditQuestionClick = { questionId -> onNavigateToEditQuestionDialog(questionId) },
                onDeleteQuestionClick = {
                        questionId -> selectedQuestionId = questionId
                },
                onLongQuestionClick = { questionId ->
                    if (isEnabledEditMode) { selectedQuestionIds.clear() }
                    else { selectedQuestionIds.add(questionId) }
                    isEnabledEditMode = !isEnabledEditMode
                },
                isEditMode = isEnabledEditMode,
                onCheckedChanged = { questionId ->
                    if (questionId in selectedQuestionIds) { selectedQuestionIds.remove(questionId) }
                    else { selectedQuestionIds.add(questionId) }
                }
            )
        }


        if (selectedQuestionId != null) {
            DeleteDialog(
                dialogHeader = {
                    Text(
                        fontSize = 24.sp,
                        text = "Are you sure you want to delete this question?",
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                },
                onDismissRequest = { selectedQuestionId = null},
                onConfirmRequest = {
                    viewModel.deleteQuestion(topicQuestions.find { it.id == selectedQuestionId })
                    selectedQuestionId = null
                }
            )
        }

        if (isOpenDeleteQuestionsDialog) {
            DeleteDialog(
                dialogHeader = {
                    Text(
                        text = "Are you sure you want to delete ${selectedQuestionIds.count()} ${if (selectedQuestionIds.count() == 1) "question" else "Questions"}?"
                    )
                },
                onDismissRequest = { isOpenDeleteQuestionsDialog = false },
                onConfirmRequest = {
                    viewModel.deleteQuestions(selectedQuestionIds.toList())
                    selectedQuestionIds.clear()
                    isOpenDeleteQuestionsDialog = false
                    isEnabledEditMode = false
                }
            )
        }
    }

    BackHandler(enabled = isEnabledEditMode) {
        selectedQuestionIds.clear()
        isEnabledEditMode = false
    }
}

@Composable
fun TopicQuestionsBottomAppBar(
    buttonEnabled: Boolean,
    onDeleteClick: () -> Unit
) {
    FlexibleBottomAppBar(
        horizontalArrangement = BottomAppBarDefaults.FlexibleFixedHorizontalArrangement,
        windowInsets = WindowInsets()
    ) {
        TextButton(
            enabled = buttonEnabled,
            onClick = onDeleteClick
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete selected questions")
                Text("Delete")
            }
        }
    }
}

@Composable
fun TopicQuestionsTopAppBar(
    title: @Composable () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    onBackClick: () -> Unit,
    isEnabledEditMode: Boolean,
    onCancelEditClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = Modifier,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        title = title,
        navigationIcon = {
            AnimatedVisibility(
                visible = !isEnabledEditMode,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back button")
                }
            }
            AnimatedVisibility(
                visible = isEnabledEditMode,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(onClick = onCancelEditClick) {
                    Icon(Icons.Filled.Cancel, contentDescription = "Quiz edit mode button")
                }
            }
        },
    )
}

@Composable
fun QuestionList(
    questions: List<Question>,
    modifier: Modifier = Modifier,
    selectedQuestionIds: Set<Int>,
    onEditQuestionClick: (Int) -> Unit,
    onDeleteQuestionClick: (Int) -> Unit,
    onLongQuestionClick: (Int) -> Unit,
    isEditMode: Boolean,
    onCheckedChanged: (Int) -> Unit
) {
    val listState = rememberLazyListState()
    LaunchedEffect(questions.maxOf { it.id }) {
        listState.animateScrollToItem(0)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(questions) { question ->
            QuestionCard(
                question = question,
                onEditClick = { onEditQuestionClick(question.id) },
                onDeleteClick = { onDeleteQuestionClick(question.id) },
                onLongClick = { onLongQuestionClick(question.id) },
                isEditMode = isEditMode,
                isChecked = question.id in selectedQuestionIds,
                onCheckChanged = { onCheckedChanged(question.id) }
            )
        }
    }
}

@Composable
fun QuestionCard(
    question: Question,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onLongClick: () -> Unit,
    isEditMode: Boolean,
    isChecked: Boolean,
    onCheckChanged: () -> Unit
) {
    var expanded by rememberSaveable(isEditMode) { mutableStateOf(false) }
    val arrowRotation = animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
    )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    if (!isEditMode) expanded = !expanded else onCheckChanged()
                },
                onLongClick = onLongClick
            ),
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AnimatedVisibility(isEditMode) {
                            Checkbox(
                                modifier = Modifier.requiredSize(24.dp),
                                checked = isChecked,
                                onCheckedChange = { onCheckChanged() }
                            )
                        }
                        Icon(
                            imageVector = Icons.Filled.QuestionMark,
                            contentDescription = "Question"
                        )
                    }

                    AnimatedVisibility(!isEditMode) {
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
                }
                Text(
                    modifier = Modifier,
                    text = question.questionText,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                )

                AnimatedVisibility(
                    visible = expanded && !isEditMode,
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
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .rotate(arrowRotation.value),
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Drop down arrow"
            )
        }

    }
}