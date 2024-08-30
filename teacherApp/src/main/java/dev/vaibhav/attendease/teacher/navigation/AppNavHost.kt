package dev.vaibhav.attendease.teacher.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.vaibhav.attendease.shared.ui.screens.auth.AuthScreen
import dev.vaibhav.attendease.shared.ui.screens.auth.AuthViewModel
import dev.vaibhav.attendease.teacher.screens.home.HomeScreen

@Composable
fun AppNavHost(
    startDestination: Screens,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ){
        composable<Screens.Auth> {
            val viewModel = hiltViewModel<AuthViewModel>()
            AuthScreen(
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize(),
                onAuthSuccess = { navController.navigate(Screens.Home) }
            )
        }

        composable<Screens.Home> {
            HomeScreen(Modifier.fillMaxSize())
        }
    }
}