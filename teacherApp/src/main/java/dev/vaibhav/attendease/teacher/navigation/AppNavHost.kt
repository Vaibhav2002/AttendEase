package dev.vaibhav.attendease.teacher.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.vaibhav.attendease.shared.data.models.Role
import dev.vaibhav.attendease.shared.ui.screens.auth.AuthScreen
import dev.vaibhav.attendease.shared.ui.screens.auth.AuthViewModel
import dev.vaibhav.attendease.shared.ui.screens.profile.ProfileScreen
import dev.vaibhav.attendease.shared.ui.screens.profile.ProfileViewModel
import dev.vaibhav.attendease.teacher.screens.attendance.AttendanceScreen
import dev.vaibhav.attendease.teacher.screens.attendance.AttendanceViewModel
import dev.vaibhav.attendease.teacher.screens.classes.ClassesScreen
import dev.vaibhav.attendease.teacher.screens.classes.ClassesViewModel
import dev.vaibhav.attendease.teacher.screens.home.HomeScreen
import dev.vaibhav.attendease.teacher.screens.home.HomeViewModel

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
                factory.create(Role.TEACHER)
            }
            AuthScreen(
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize(),
                onAuthSuccess = {
                    navController.navigate(Screens.Home) {
                        popUpTo(Screens.Auth) {
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
                onNavToClasses = { navController.navigate(Screens.Classes(it.id)) },
                onNavToProfile = { navController.navigate(Screens.Profile) }
            )
        }

        composable<Screens.Classes> {
            val subjectId = it.toRoute<Screens.Classes>().subjectId
            val viewModel =
                hiltViewModel<ClassesViewModel, ClassesViewModel.ClassesViewModelFactory> { factory ->
                    factory.create(subjectId)
                }

            ClassesScreen(
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize(),
                onBack = navController::popBackStack,
                onNavToAttendance = { classId ->
                    navController.navigate(Screens.Attendance(classId))
                }
            )
        }

        composable<Screens.Attendance> {
            val classId = it.toRoute<Screens.Attendance>().classId
            val viewModel =
                hiltViewModel<AttendanceViewModel, AttendanceViewModel.Factory> { factory ->
                    factory.create(classId)
                }
            AttendanceScreen(
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize(),
                onBack = navController::popBackStack
            )
        }

        composable<Screens.Profile> {
            val viewModel = hiltViewModel<ProfileViewModel>()
            ProfileScreen(
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize(),
                onLogout = {
                    navController.navigate(Screens.Auth) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
                onNavBack = { navController.popBackStack() }
            )
        }
    }
}