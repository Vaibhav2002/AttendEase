package dev.vaibhav.attendease.teacher.screens.attendance

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import dev.vaibhav.attendease.shared.data.models.User
import dev.vaibhav.attendease.shared.data.models.createdAt
import dev.vaibhav.attendease.shared.ui.components.AttendEaseAppBar
import dev.vaibhav.attendease.shared.ui.screens.BaseScreenContent
import dev.vaibhav.attendease.shared.utils.DateHelpers
import qrscanner.QrScanner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    viewModel: AttendanceViewModel,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    val subject by viewModel.subject.collectAsStateWithLifecycle()
    val classData by viewModel.classData.collectAsStateWithLifecycle()
    val attendees by viewModel.attendees.collectAsStateWithLifecycle()
    val canTakeAttendance by viewModel.canTakeAttendance.collectAsStateWithLifecycle()

    val attendeeCount by remember {
        derivedStateOf { attendees.size }
    }

    val appBarTitle by remember {
        derivedStateOf {
            if (subject != null && classData != null)
                DateHelpers.fullDate(classData!!.createdAt, false)
                    .let { "${subject!!.title} - $it" }
            else "Attendance"
        }
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    BaseScreenContent(
        viewModel = viewModel,
        modifier = modifier,
        topAppBar = {
            AttendEaseAppBar(
                title = appBarTitle,
                onBack = onBack,
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            if (canTakeAttendance) {
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        QrScanner(
                            modifier = Modifier
                                .fillMaxSize()
                                .clipToBounds(),
                            flashlightOn = false,
                            openImagePicker = false,
                            onCompletion = viewModel::takeAttendance,
                            imagePickerHandler = {},
                            onFailure = {
                                if (it.isEmpty()) viewModel.showSnackBar("Invalid QR code")
                                else viewModel.showSnackBar(it)
                            }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }

            }

            item {
                Text(
                    text = "Attendees ($attendeeCount)",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(attendees) {
                AttendeeItem(user = it, Modifier.fillParentMaxWidth())
            }

        }
    }
}

@Composable
fun AttendeeItem(
    user: User,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = user.profilePic,
                contentDescription = user.name,
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
            )

            Text(
                text = user.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .basicMarquee()
            )
        }
    }
}