package com.example.quizapp.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.quizapp.ui.screens.home.HomeScreen
import com.example.quizapp.ui.screens.quiz.ChooseQuizTopicScreen
import com.example.quizapp.ui.screens.topics.TopicsScreen
import kotlinx.serialization.Serializable



@Serializable
object HomeRoute
@Serializable
object TopicsRoute
@Serializable
object QuizRoute

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: Any = HomeRoute
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<HomeRoute> { HomeScreen(home = HomeRoute) }
        composable<TopicsRoute> { TopicsScreen(topic = TopicsRoute) }
        composable<QuizRoute> { ChooseQuizTopicScreen(quiz = QuizRoute) }
    }
}