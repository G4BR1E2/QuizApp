package com.example.quizapp.ui.screens.home

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.quizapp.navigation.NavDestination

val destinations = listOf(
    NavDestination.Home,
    NavDestination.Topics,
    NavDestination.Quiz)


@Composable
fun NavBar(modifier: Modifier = Modifier) {

}