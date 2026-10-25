package com.example.quizapp.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Topic
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.rememberNavController
import com.example.quizapp.navigation.AppNavigation
import com.example.quizapp.navigation.Destination
import com.example.quizapp.navigation.HomeRoute
import com.example.quizapp.navigation.QuizRoute
import com.example.quizapp.navigation.TopicsRoute

@Composable
fun HomeScreen(home: HomeRoute, modifier: Modifier = Modifier) {

}

@Composable
fun NavBar(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    var selectedDestination by rememberSaveable { mutableIntStateOf(Destination.HOME.id) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar() {
                Destination.entries.forEach { destination ->
                    NavigationBarItem(
                        selected = destination.id == selectedDestination,
                        onClick = {
                            navController.navigate(route = destination.route)
                            selectedDestination = destination.id
                        },
                        icon = {
                            Icon(
                                destination.icon,
                                contentDescription = null
                            )
                        },
                        label = { Text(destination.title) }
                    )
                }
            }
        }
    ) { contentPadding ->
        AppNavigation(
            navController = navController,
            startDestination = Destination.HOME.route,
            modifier = modifier.padding(contentPadding))
    }
}