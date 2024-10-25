package dev.vaibhav.attendease.shared.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.vaibhav.attendease.shared.data.models.Role
import dev.vaibhav.attendease.shared.data.models.Role.*
import dev.vaibhav.attendease.shared.data.models.Subject
import dev.vaibhav.attendease.shared.data.models.label

@Composable
fun SubjectCard(
    subject: Subject,
    modifier: Modifier = Modifier,
    forRole: Role,
    onClick: () -> Unit,
) {
    val desc = when(forRole){
        STUDENT -> subject.creatorName
        TEACHER -> subject.department.label
    }
    val title = when(forRole){
        STUDENT -> subject.title
        TEACHER -> subject.title + " - " + subject.section.name
    }
    Surface(
        modifier = modifier,
        onClick = onClick,
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = desc,
                style = MaterialTheme.typography.bodyMedium,
                color = LocalContentColor.current.copy(alpha = 0.7f)
            )
        }
    }
}