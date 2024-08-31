package dev.vaibhav.attendease.student.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.vaibhav.attendease.shared.data.models.Role
import dev.vaibhav.attendease.shared.ui.screens.auth.AuthScreen
import dev.vaibhav.attendease.shared.ui.screens.auth.AuthViewModel
import dev.vaibhav.attendease.shared.ui.screens.profile.ProfileScreen
import dev.vaibhav.attendease.shared.ui.screens.profile.ProfileViewModel
import dev.vaibhav.attendease.student.screens.home.HomeScreen
import dev.vaibhav.attendease.student.screens.home.HomeViewModel

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
    ) {
        composable<Screens.Auth> {
            val viewModel = hiltViewModel<AuthViewModel, AuthViewModel.Factory> { factory ->
                factory.create(Role.STUDENT)
            }
            AuthScreen(
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize(),
                onAuthSuccess = {
                    navController.navigate(Screens.Home) {
                        popUpTo(Screens.Auth){
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Screens.Home> {
            val viewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize(),
                onNavToProfile = { navController.navigate(Screens.Profile) }
            )
        }

        composable<Screens.Profile> {
            val viewModel = hiltViewModel<ProfileViewModel>()
            ProfileScreen(
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize(),
                onLogout = {
                    navController.navigate(Screens.Auth){
                        popUpTo(navController.graph.id){
                            inclusive = true
                        }
                    }
                },
                onNavBack = { navController.popBackStack() }
            )
        }
    }
}