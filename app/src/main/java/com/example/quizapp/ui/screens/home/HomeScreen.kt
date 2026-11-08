package com.example.quizapp.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.quizapp.navigation.AppNavigation
import com.example.quizapp.navigation.Destination
import com.example.quizapp.viewmodel.QuizViewModel

@Composable
fun HomeScreen(viewModel: QuizViewModel, modifier: Modifier = Modifier) {

}

@Composable
fun NavBar(modifier: Modifier = Modifier, viewModel: QuizViewModel, navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination = backStackEntry?.destination
    val isVisibleBottomBar = rememberSaveable { mutableStateOf(true) }

    isVisibleBottomBar.value = Destination.entries.any { destination ->
        selectedDestination?.hierarchy?.any {
            it.hasRoute(destination.route::class)
        } == true
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            AnimatedVisibility(isVisibleBottomBar.value) {
                NavigationBar() {
                    Destination.entries.forEach { destination ->
                        val selected = selectedDestination?.hierarchy?.any {
                            it.hasRoute(destination.route::class)
                        } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(route = destination.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
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
        }
    ) { contentPadding ->
        AppNavigation(
            viewModel = viewModel,
            navController = navController,
            startDestination = Destination.HOME.route,
            modifier = Modifier.padding(bottom = contentPadding.calculateBottomPadding()))
    }
}