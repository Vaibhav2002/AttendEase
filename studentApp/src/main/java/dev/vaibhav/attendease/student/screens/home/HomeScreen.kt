package dev.vaibhav.attendease.student.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import dev.vaibhav.attendease.shared.data.models.Subject
import dev.vaibhav.attendease.shared.ui.components.AttendEaseAppBar
import dev.vaibhav.attendease.shared.ui.components.SubjectCard
import dev.vaibhav.attendease.student.navigation.Screens

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    onNavToQRScreen: () -> Unit,
    onNavToProfile: () -> Unit,
    onNavToSubject: (Subject) -> Unit
) {
    val subjects by viewModel.subjects.collectAsStateWithLifecycle()
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = onNavToQRScreen) {
                Icon(imageVector = Icons.Filled.QrCode, contentDescription = "Refresh QR")
            }
        },
        topBar = { AppBar(viewModel, onNavToProfile) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(subjects) {
                SubjectCard(
                    subject = it,
                    modifier = Modifier.fillParentMaxWidth(),
                    onClick = { onNavToSubject(it) },
                    forRole = viewModel.user.role
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    viewModel: HomeViewModel,
    onNavToProfile: () -> Unit
) {
    val name = buildAnnotatedString {
        append("Attend")
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        ) {
            append("Ease")
        }
    }
    AttendEaseAppBar(
        title = name,
        actions = {
            IconButton(onClick = onNavToProfile) {
                AsyncImage(
                    model = viewModel.user.profilePic,
                    contentDescription = "Profile",
                    placeholder = rememberVectorPainter(Icons.Default.Person),
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                )
            }
        }
    )
}