package dev.vaibhav.attendease.shared.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.vaibhav.attendease.shared.utils.safeCatch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun BaseScreenContent(
    viewModel: BaseViewModel,
    modifier: Modifier = Modifier,
    topAppBar: @Composable () -> Unit = {},
    fab: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val screenState by viewModel.screenState.collectAsState()
    val isNormalState by remember {
        derivedStateOf { screenState is ScreenState.Normal }
    }

    LaunchedEffect(Unit) {
        viewModel.snackBarEvent
            .onEach { snackBarHostState.showSnackbar(it) }
            .safeCatch()
            .launchIn(this)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) {
                Snackbar(snackbarData = it)
            }
        },
        topBar = { topAppBar() },
        floatingActionButton = {
            AnimatedVisibility(visible = isNormalState) {
                fab()
            }
        }
    ) {
        Crossfade(
            targetState = screenState,
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            label = "Main Content"
        ) {
            when (it) {
                is ScreenState.Error -> {
                    ErrorState(
                        modifier = Modifier.fillMaxSize(),
                        onRetry = viewModel::retry
                    )
                }

                ScreenState.Loading -> LoadingState(Modifier.fillMaxSize())
                ScreenState.Normal -> content()
            }
        }
    }
}

@Composable
private fun ErrorState(
    modifier: Modifier = Modifier,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Oops something went wrong!",
            textAlign = TextAlign.Center,
            fontSize = 17.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.size(16.dp))

        Text(
            text = "Please click the button below to try again.",
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.size(24.dp))

        OutlinedButton(
            onClick = onRetry,
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text(
                text = "Retry",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
            )
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}