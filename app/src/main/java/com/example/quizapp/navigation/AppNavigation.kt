package com.example.quizapp.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Topic
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import com.example.quizapp.ui.screens.home.HomeScreen
import com.example.quizapp.ui.screens.quiz.MainQuizScreen
import com.example.quizapp.ui.screens.quiz.QuizAttemptsDetailsScreen
import com.example.quizapp.ui.screens.quiz.QuizPlayScreen
import com.example.quizapp.ui.screens.topicQuestions.TopicQuestionDialog
import com.example.quizapp.ui.screens.topicQuestions.TopicQuestionsScreen
import com.example.quizapp.ui.screens.topics.TopicDialog
import com.example.quizapp.ui.screens.topics.TopicsScreen
import com.example.quizapp.viewmodel.QuizViewModel
import kotlinx.serialization.Serializable

enum class Destination(val title: String, val route: Any, val icon: ImageVector) {
    HOME(title = "Home", route = HomeRoute, icon = Icons.Filled.Home),
    TOPICS(title = "Topics", route = TopicsRoute, icon = Icons.Filled.Topic),
    QUIZ(title = "Quiz", route = QuizRoute, icon = Icons.Filled.Quiz)
}

@Serializable
object HomeRoute
@Serializable
object TopicsRoute
@Serializable
object QuizRoute
@Serializable
data class TopicQuestionsRoute(val topicId: Int)
@Serializable
data class NewQuestionRoute(val topicId: Int)
@Serializable
data class EditQuestionRoute(val questionId: Int)
@Serializable
data class QuizPlayRoute(val topicId: Int)
@Serializable
object NewTopicRoute
@Serializable
data class EditTopicRoute(val topicId: Int)
@Serializable
data class AttemptDetailsRoute(val attemptId: Int)

@Composable
fun AppNavigation(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: Any = HomeRoute
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<HomeRoute> { HomeScreen(viewModel = viewModel) }
        composable<TopicsRoute> {
            TopicsScreen(
                viewModel = viewModel,
                onNavigateToTopicQuestions = {
                    topicId -> navController.navigate(route = TopicQuestionsRoute(topicId))
                },
                onNavigateToNewTopicDialog = {
                    navController.navigate(route = NewTopicRoute)
                },
                onNavigateToEditTopicDialog = { topicId ->
                    navController.navigate(route = EditTopicRoute(topicId))
                }
            )
        }
        composable<QuizRoute> {
            MainQuizScreen(
                viewModel = viewModel,
                onNavigateToQuizPlayScreen = { topicId ->
                    navController.navigate(route = QuizPlayRoute(topicId))
                },
                onNavigateToNewTopicDialog = {
                    navController.navigate(route = NewTopicRoute)
                },
                onNavigateToNewQuestionDialog = { topicId ->
                    navController.navigate(TopicQuestionsRoute(topicId))
                    navController.navigate(NewQuestionRoute(topicId))
                },
                onNavigateToAttemptDetailsScreen = { attemptId ->
                    navController.navigate(AttemptDetailsRoute(attemptId))
                }
            )
        }
        composable<TopicQuestionsRoute> { navBackStackEntry ->
            val route: TopicQuestionsRoute = navBackStackEntry.toRoute()
            TopicQuestionsScreen(
                topicId = route.topicId,
                viewModel = viewModel,
                onTopAppBarBackClick =  { navController.popBackStack() },
                onNavigateToNewQuestionDialog = { topicId ->
                    navController.navigate(NewQuestionRoute(topicId))
                },
                onNavigateToEditQuestionDialog = { questionId ->
                    navController.navigate(EditQuestionRoute(questionId))
                }
            )
        }
        composable<QuizPlayRoute> { navBackStackEntry ->
            val route: QuizPlayRoute = navBackStackEntry.toRoute()
            QuizPlayScreen(
                topicId = route.topicId,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable<AttemptDetailsRoute> { navBackStackEntry ->
            val route: AttemptDetailsRoute = navBackStackEntry.toRoute()
            val attempts by viewModel.attempts.collectAsStateWithLifecycle()
            val topics by viewModel.topics.collectAsStateWithLifecycle()
            val attempt = attempts.find { it.id == route.attemptId }

            QuizAttemptsDetailsScreen(
                attempt = attempt,
                topicName = topics.find { it.id == attempt?.topicId }?.name,
                onTopAppBarBackClick = { navController.popBackStack() }
            )
        }
        dialog<NewTopicRoute> {
            TopicDialog(
                dialogHeader = {
                    Text(
                        text = "New topic",
                        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    )
                },
                onDismissRequest = {
                    navController.popBackStack()
                },
                onConfirmRequest = {
                    topic -> viewModel.addTopic(topic)
                    navController.popBackStack()
                }
            )
        }
        dialog<EditTopicRoute> { navBackStackEntry ->
            val route: EditTopicRoute = navBackStackEntry.toRoute()
            val topics = viewModel.topics.collectAsState()

            TopicDialog(
                dialogHeader = {
                    Text(
                        text = "Edit topic",
                        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    )
                },
                selectedTopic = topics.value.find { it.id == route.topicId },
                onDismissRequest = {
                    navController.popBackStack()
                },
                onConfirmRequest = {
                    topic -> viewModel.updateTopic(topic)
                    navController.popBackStack()
                }
            )
        }
        dialog<NewQuestionRoute> { navBackStackEntry ->
            val route: NewQuestionRoute = navBackStackEntry.toRoute()

            TopicQuestionDialog(
                dialogHeader = {
                    Text(
                        fontSize = 24.sp,
                        text = "New question",
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                topicId = route.topicId,
                onDismissRequest = { navController.popBackStack() },
                onConfirmRequest = {
                    question -> viewModel.addQuestion(question)
                    navController.popBackStack()
                }
            )
        }
        dialog<EditQuestionRoute>{ navBackStackEntry ->
            val route: EditQuestionRoute = navBackStackEntry.toRoute()
            val questions = viewModel.questions.collectAsState()

            TopicQuestionDialog(
                dialogHeader = {
                    Text(
                        fontSize = 24.sp,
                        text = "Edit topic",
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                selectedQuestion = questions.value.find { it.id == route.questionId },
                onDismissRequest = { navController.popBackStack() },
                onConfirmRequest = {
                    question -> viewModel.updateQuestion(question)
                    navController.popBackStack()
                }
            )
        }
    }
}