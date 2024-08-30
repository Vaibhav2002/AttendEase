package dev.vaibhav.attendease.teacher.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.vaibhav.attendease.shared.data.models.Subject
import dev.vaibhav.attendease.shared.data.models.label
import dev.vaibhav.attendease.shared.ui.screens.BaseScreenContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val subjects by viewModel.subjects.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    BaseScreenContent(
        modifier = modifier,
        viewModel = viewModel
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                CreateSubjectFab(onClick = viewModel::onCreateSubjectClick)
            },
            contentWindowInsets = WindowInsets(0.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    Text(
                        text = "Subjects",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }
                items(
                    items = subjects,
                    key = Subject::id
                ){
                    SubjectCard(subject = it, modifier = Modifier.fillMaxWidth())
                }
            }

            if(viewModel.subjectCreationSheet){
                val state = rememberModalBottomSheetState()
                val isValid by viewModel.isValid.collectAsState()

                CreateSubjectSheet(
                    state = state,
                    subject = viewModel.subject,
                    dept = viewModel.department,
                    departments = viewModel.departments,
                    onDepartmentChange = viewModel::onDepartmentChange,
                    onSubjectChange = viewModel::onSubjectChange,
                    onDismiss = {
                        scope.launch { state.hide() }
                            .invokeOnCompletion { viewModel.subjectCreationSheet = false }
                    },
                    isValid = isValid,
                    onSaveClick = viewModel::onSavePress
                )
            }

        }
    }
}


@Composable
fun SubjectCard(
    subject: Subject,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        onClick = {},
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {

            Text(
                text = subject.title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subject.department.label,
                style = MaterialTheme.typography.bodyMedium,
                color = LocalContentColor.current.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun CreateSubjectFab(
    modifier: Modifier = Modifier,
    onClick:() -> Unit
) {
    FloatingActionButton(modifier = modifier, onClick = onClick) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = "Add Subject"
        )
    }
}