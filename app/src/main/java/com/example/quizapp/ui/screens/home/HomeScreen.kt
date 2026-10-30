package com.example.quizapp.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.quizapp.navigation.AppNavigation
import com.example.quizapp.navigation.Destination
import com.example.quizapp.navigation.HomeRoute
import com.example.quizapp.viewmodel.QuizViewModel

@Composable
fun HomeScreen(home: HomeRoute, viewModel: QuizViewModel, modifier: Modifier = Modifier) {

}

@Composable
fun NavBar(modifier: Modifier = Modifier, viewModel: QuizViewModel, navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination = backStackEntry?.destination

    Scaffold(
        modifier = modifier,
        bottomBar = {
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
    ) { contentPadding ->
        AppNavigation(
            viewModel = viewModel,
            navController = navController,
            startDestination = Destination.HOME.route,
            modifier = modifier.padding(contentPadding))
    }
}