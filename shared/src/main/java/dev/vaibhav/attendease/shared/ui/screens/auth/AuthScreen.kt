package dev.vaibhav.attendease.shared.ui.screens.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.vaibhav.attendease.shared.ui.components.auth.GoogleLoginButton
import dev.vaibhav.attendease.shared.ui.screens.BaseScreenContent
import dev.vaibhav.attendease.shared.utils.safeCatch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier,
    onAuthSuccess: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.authSuccess
            .onEach { onAuthSuccess() }
            .safeCatch()
            .launchIn(this)
    }

    BaseScreenContent(viewModel = viewModel, modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {

            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                GoogleLoginButton(
                    googleClient = viewModel.signInClient,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    onStateChange = viewModel::handleAuthStateChange
                )
            }
        }
    }

}