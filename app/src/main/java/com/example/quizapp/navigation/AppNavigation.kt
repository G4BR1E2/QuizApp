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
import com.example.quizapp.ui.screens.home.HomeScreen
import com.example.quizapp.ui.screens.quiz.ChooseQuizTopicScreen
import com.example.quizapp.ui.screens.quiz.QuizPlayScreen
import com.example.quizapp.ui.screens.topicQuestions.TopicQuestionsScreen
import com.example.quizapp.ui.screens.topics.TopicsScreen
import com.example.quizapp.viewmodel.QuizViewModel
import kotlinx.serialization.Serializable

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
                onNavigateToTopicQuestions = { navController.navigate(route = TopicQuestionsRoute) }
            )
        }
        composable<QuizRoute> {
            ChooseQuizTopicScreen(
                viewModel = viewModel,
                onNavigateToQuizPlayScreen = { navController.navigate(route = QuizPlayRoute) }
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
                navController = navController
            )
        }
    }
}