package dev.vaibhav.attendease.shared.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun ProgressOutlinedButton(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    borderStroke: BorderStroke = ButtonDefaults.outlinedButtonBorder,
    shape: Shape = ButtonDefaults.shape,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    icon: @Composable () -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        colors = colors,
        enabled = enabled,
        border = borderStroke,
        contentPadding = contentPadding
    ) {
        Crossfade(
            targetState = isLoading,
            label = "Icon"
        ) {
            if (it) CircularProgressIndicator(
                Modifier.size(24.dp),
                color = LocalContentColor.current
            )
            else icon()
        }
        Spacer(modifier = Modifier.width(12.dp))
        content()
    }
}