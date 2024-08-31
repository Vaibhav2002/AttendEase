package dev.vaibhav.attendease.shared.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val text = buildAnnotatedString {
                    append("Attend")
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("Ease")
                    }
                }

                Text(
                    text = text,
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Attendance made easy",
                    style = MaterialTheme.typography.bodyLarge,
                    color = LocalContentColor.current.copy(alpha = 0.6f)
                )
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