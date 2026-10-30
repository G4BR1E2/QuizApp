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
        composable<HomeRoute> { HomeScreen(home = HomeRoute, viewModel = viewModel) }
        composable<TopicsRoute> {
            TopicsScreen(
                topic = TopicsRoute,
                viewModel = viewModel,
                onNavigateToTopicQuestions = { navController.navigate(route = TopicQuestionsRoute) }
            )
        }
        composable<QuizRoute> { ChooseQuizTopicScreen(quiz = QuizRoute, viewModel = viewModel) }
        composable<TopicQuestionsRoute> {
            TopicQuestionsScreen(
                topicQuestions = TopicQuestionsRoute,
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}