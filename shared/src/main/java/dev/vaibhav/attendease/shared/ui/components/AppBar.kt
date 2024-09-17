package dev.vaibhav.attendease.shared.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendEaseAppBar(
    title: String,
    onBack: (() -> Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    actions: @Composable RowScope.() -> Unit = {},
    modifier: Modifier = Modifier
) {
    LargeTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge
            )
        },
        windowInsets = WindowInsets(0.dp),
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        actions = actions,
        navigationIcon = {
            AnimatedVisibility(
                visible = onBack != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(onClick = { onBack?.invoke() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendEaseAppBar(
    title: AnnotatedString,
    onBack: (() -> Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    actions: @Composable RowScope.() -> Unit = {},
    modifier: Modifier = Modifier
) {
    LargeTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge
            )
        },
        windowInsets = WindowInsets(0.dp),
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        actions = actions,
        navigationIcon = {
            AnimatedVisibility(
                visible = onBack != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(onClick = { onBack?.invoke() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendEaseSmallAppBar(
    title: String,
    onBack: (() -> Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    actions: @Composable RowScope.() -> Unit = {},
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = title) },
        windowInsets = WindowInsets(0.dp),
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        actions = actions,
        navigationIcon = {
            AnimatedVisibility(
                visible = onBack != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(onClick = { onBack?.invoke() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendEaseSmallAppBar(
    title: AnnotatedString,
    onBack: (() -> Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    actions: @Composable RowScope.() -> Unit = {},
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = title) },
        windowInsets = WindowInsets(0.dp),
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        actions = actions,
        navigationIcon = {
            AnimatedVisibility(
                visible = onBack != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(onClick = { onBack?.invoke() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        }
    )
}