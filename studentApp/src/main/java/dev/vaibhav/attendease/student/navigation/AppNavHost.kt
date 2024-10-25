package dev.vaibhav.attendease.student.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import dev.vaibhav.attendease.student.screens.qr.QRScreen
import dev.vaibhav.attendease.student.screens.qr.QrViewModel

@Composable
fun AppNavHost(
    startDestination: Screens,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300, easing = EaseIn)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300, easing = EaseOut)
            )
        }
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
                onNavToQRScreen = { navController.navigate(Screens.QR) },
                onNavToProfile = { navController.navigate(Screens.Profile) }
            )
        }

        composable<Screens.QR> {
            val viewModel = hiltViewModel<QrViewModel>()
            QRScreen(
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize(),
                onNavBack = { navController.popBackStack() }
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