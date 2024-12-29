package dev.vaibhav.attendease.student.screens.subject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.vaibhav.attendease.shared.data.models.createdAt
import dev.vaibhav.attendease.shared.ui.components.AttendEaseAppBar
import dev.vaibhav.attendease.shared.ui.components.ClassCard
import dev.vaibhav.attendease.shared.ui.screens.BaseScreenContent
import dev.vaibhav.attendease.shared.utils.DateHelpers
import kotlinx.coroutines.flow.mapLatest
import java.time.format.TextStyle
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectScreen(
    viewModel: SubjectViewModel,
    modifier: Modifier = Modifier,
    onBack:() -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val classes by viewModel.classes
        .mapLatest { it.groupBy { DateHelpers.toLocalDateTime(it.classData.createdAt).month } }
        .collectAsStateWithLifecycle(emptyMap())

    BaseScreenContent(
        viewModel = viewModel,
        modifier = modifier,
        topAppBar = {
            AttendEaseAppBar(
                title = viewModel.subjectName,
                onBack = onBack,
                scrollBehavior = scrollBehavior
            )
        },
    ) {
        LazyVerticalGrid(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            item(span = { GridItemSpan(3) }, key = "Stats", contentType = "Stats") {
                StatsSection(
                    classes = classes.values.flatten(),
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
            }

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
                    ClassCard(
                        classData = it.classData,
                        isPresent = it.present,
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                        onClick = {}
                    )
                }
            }
        }
    }
}

@Composable
fun StatsSection(
    classes: List<SubjectViewModel.ClassData>,
    modifier: Modifier = Modifier
) {
    val attended = remember(classes) {
        classes.count { it.present }
    }

    val percentage by remember(attended, classes) {
        derivedStateOf { abs(attended / classes.size.toFloat() * 100).toInt() }
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        StatsItem(
            title = "Attended",
            value = "$attended/${classes.size}"
        )

        StatsItem(
            title = "Attendance %",
            value = "${percentage}%"
        )
    }
}

@Composable
private fun StatsItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = LocalContentColor.current.copy(alpha = 0.6f)
        )
    }
}