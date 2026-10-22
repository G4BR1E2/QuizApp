package com.example.quizapp.navigation

import android.graphics.drawable.Icon
import android.media.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Topic
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

class NavDestination(val title: String, val route: String, val icon: ImageVector) {
    companion object {
        val Home = NavDestination(title = "Home", route = "home_screen", icon = Icons.Filled.Home)
        val Topics = NavDestination(title = "Topics", route = "topics_screen", icon = Icons.Filled.Topic)
        val Quiz = NavDestination(title = "Quiz", route = "quiz_screen", icon = Icons.Filled.Quiz)
    }
}