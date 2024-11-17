package dev.vaibhav.attendease.shared.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.vaibhav.attendease.shared.data.models.Class
import dev.vaibhav.attendease.shared.data.models.createdAt
import dev.vaibhav.attendease.shared.utils.DateHelpers
import java.time.format.TextStyle

@Composable
fun ClassCard(
    classData: Class,
    modifier: Modifier = Modifier,
    isPresent: Boolean = false,
    onClick: () -> Unit,
) {
    val bgColor = if (isPresent)
        MaterialTheme.colorScheme.primaryContainer
    else MaterialTheme.colorScheme.surface

    val contentColor = if (isPresent)
        MaterialTheme.colorScheme.onPrimaryContainer
    else MaterialTheme.colorScheme.primary

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier,
        color = bgColor,
        contentColor = contentColor,
        tonalElevation = if(isPresent) 0.dp else 1.dp,
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
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = date.month.getDisplayName(TextStyle.SHORT, java.util.Locale.ENGLISH),
                style = MaterialTheme.typography.bodyLarge,
                color = LocalContentColor.current.copy(alpha = 0.6f)
            )
        }
    }
}