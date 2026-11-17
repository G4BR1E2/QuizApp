package com.example.quizapp.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Topic
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.example.quizapp.ui.screens.home.HomeScreen
import com.example.quizapp.ui.screens.quiz.MainQuizScreen
import com.example.quizapp.ui.screens.quiz.QuizPlayScreen
import com.example.quizapp.ui.screens.topicQuestions.TopicQuestionsScreen
import com.example.quizapp.ui.screens.topics.NewTopicDialog
import com.example.quizapp.ui.screens.topics.TopicsScreen
import com.example.quizapp.viewmodel.QuizViewModel
import kotlinx.serialization.Serializable
import kotlin.time.Clock

enum class Destination(val id: Int, val title: String, val route: Any, val icon: ImageVector) {
    HOME(title = "Home", route = HomeRoute, icon = Icons.Filled.Home, id = 1),
    TOPICS(title = "Topics", route = TopicsRoute, icon = Icons.Filled.Topic, id = 2),
    QUIZ(title = "Quiz", route = QuizRoute, icon = Icons.Filled.Quiz, id = 3)
}

@Serializable
object HomeRoute
@Serializable
object TopicsRoute
@Serializable
object QuizRoute
@Serializable
object TopicQuestionsRoute
@Serializable
object QuizPlayRoute
@Serializable
object NewTopicRoute

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
                    topic -> viewModel.setSelectedTopic(topic)
                    navController.navigate(route = TopicQuestionsRoute)
                },
                onNavigateToNewTopicDialog = { navController.navigate(route = NewTopicRoute) }
            )
        }
        composable<QuizRoute> {
            MainQuizScreen(
                viewModel = viewModel,
                onNavigateToQuizPlayScreen = {
                    topic -> viewModel.setSelectedTopic(topic)
                    navController.navigate(route = QuizPlayRoute)
                },
                onNavigateToNewTopicDialog = { navController.navigate(route = NewTopicRoute) },
                onNavigateToNewQuestionDialog = {
                    topic -> viewModel.setSelectedTopic(topic)
                    navController.navigate(route = TopicQuestionsRoute)
                    viewModel.onOpenNewQuestionDialog()
                }
            )
        }
        composable<TopicQuestionsRoute> {
            TopicQuestionsScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable<QuizPlayRoute> {
            QuizPlayScreen(
                viewModel = viewModel,
                navController = navController,
                quizStartTime = Clock.System.now().toEpochMilliseconds()
            )
        }
        dialog<NewTopicRoute> {
            NewTopicDialog(
                onDismissRequest = {
                    viewModel.onCloseNewTopicDialog()
                    navController.popBackStack()
                },
                onConfirmRequest = {
                    topic -> viewModel.addTopic(topic)
                    viewModel.onCloseNewTopicDialog()
                    navController.popBackStack()
                }
            )
        }
    }
}