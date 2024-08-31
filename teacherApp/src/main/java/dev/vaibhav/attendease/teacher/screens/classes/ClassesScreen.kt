package dev.vaibhav.attendease.teacher.screens.classes

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.vaibhav.attendease.shared.data.models.Class
import dev.vaibhav.attendease.shared.data.models.Subject
import dev.vaibhav.attendease.shared.data.models.createdAt
import dev.vaibhav.attendease.shared.ui.components.AttendEaseAppBar
import dev.vaibhav.attendease.shared.ui.screens.BaseScreenContent
import dev.vaibhav.attendease.shared.utils.DateHelpers
import dev.vaibhav.attendease.shared.utils.safeCatch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import java.time.format.TextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassesScreen(
    viewModel: ClassesViewModel,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onNavToAttendance: (String) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val subject by viewModel.subject.collectAsStateWithLifecycle()

    val classes by viewModel.classes
        .mapLatest { it.groupBy { DateHelpers.toLocalDateTime(it.createdAt).month } }
        .collectAsStateWithLifecycle(emptyMap())

    LaunchedEffect(Unit) {
        viewModel.classCreated
            .onEach { onNavToAttendance(it) }
            .safeCatch()
            .launchIn(this)
    }

    BaseScreenContent(
        viewModel = viewModel,
        modifier = modifier,
        topAppBar = {
            subject?.let {
                AttendEaseAppBar(
                    title = it.title,
                    onBack = onBack,
                    scrollBehavior = scrollBehavior
                )
            }
        },
        fab = {
            CreateClassFab(
                isCreating = viewModel.isCreatingClass,
                onClick = viewModel::onCreateClass
            )
        }
    ) {
        LazyVerticalGrid(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            classes.forEach { (month, classes) ->
                item(span = { GridItemSpan(3) }) {
                    Text(
                        text = month.getDisplayName(TextStyle.FULL, java.util.Locale.ENGLISH),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                items(classes) {
                    ClassItem(
                        classData = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        onClick = { onNavToAttendance(it.id) }
                    )
                }
            }

        }
    }
}


@Composable
fun ClassItem(
    classData: Class,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier,
        tonalElevation = 1.dp,

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            val date = remember(classData.createdAt) {
                DateHelpers.toLocalDateTime(classData.createdAt)
            }

            Text(
                text = date.dayOfMonth.toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = date.month.getDisplayName(TextStyle.SHORT, java.util.Locale.ENGLISH),
                style = MaterialTheme.typography.bodyMedium,
                color = LocalContentColor.current.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun CreateClassFab(
    isCreating: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = { if (!isCreating) onClick() },
    ) {
        Crossfade(targetState = isCreating, label = "Create class fab") {
            if (it) CircularProgressIndicator()
            else Icon(imageVector = Icons.Outlined.Add, contentDescription = "Create class")
        }
    }
}
