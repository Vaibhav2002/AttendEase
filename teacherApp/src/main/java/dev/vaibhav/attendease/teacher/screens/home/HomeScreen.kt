package dev.vaibhav.attendease.teacher.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import dev.vaibhav.attendease.shared.data.models.Subject
import dev.vaibhav.attendease.shared.ui.components.AttendEaseAppBar
import dev.vaibhav.attendease.shared.ui.components.SubjectCard
import dev.vaibhav.attendease.shared.ui.screens.BaseScreenContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    onNavToClasses:(Subject) -> Unit,
    onNavToProfile:() -> Unit
) {
    val subjects by viewModel.subjects.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    BaseScreenContent(
        modifier = modifier,
        viewModel = viewModel,
        topAppBar = {
            AttendEaseAppBar(
                title = "Subjects",
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(onClick = onNavToProfile) {
                        AsyncImage(
                            model = viewModel.user.profilePic,
                            contentDescription = "Profile",
                            placeholder = rememberVectorPainter(Icons.Default.Person),
                            modifier = Modifier.size(32.dp).clip(CircleShape)
                        )
                    }
                }
            )
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = subjects,
                    key = Subject::id
                ){
                    SubjectCard(
                        subject = it,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onNavToClasses(it) },
                        forRole = viewModel.user.role
                    )
                }
            }
        }
    }
}