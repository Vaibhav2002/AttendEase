package dev.vaibhav.attendease.shared.ui.components.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.vaibhav.attendease.shared.R
import dev.vaibhav.attendease.shared.ui.components.ProgressOutlinedButton
import dev.vaibhav.attendease.shared.utils.auth.GoogleSignInClient
import kotlinx.coroutines.launch

@Composable
fun GoogleLoginButton(
    googleClient: GoogleSignInClient,
    modifier: Modifier = Modifier,
    onStateChange: (AuthState) -> Unit
) {
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            scope.launch {
                runCatching { googleClient.handleResult(it) }
                    .onSuccess { onStateChange(AuthState.Success(it)) }
                    .onFailure { onStateChange(AuthState.Error(it)) }
                    .also { isLoading = false }
            }
        }
    )

    val onClick:() -> Unit = remember {
        {
            scope.launch {
                isLoading = true
                onStateChange(AuthState.Loading)

                runCatching {
                    googleLauncher.launch(googleClient.signInIntent)
                }.onFailure {
                    onStateChange(AuthState.Error(it))
                    isLoading = false
                }
            }
        }
    }


    ProgressOutlinedButton(
        isLoading = isLoading,
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = LocalContentColor.current.copy(alpha = 0.7f)
        ),
        borderStroke = BorderStroke(1.dp, LocalContentColor.current.copy(alpha = 0.4f)),
        enabled = !isLoading,
        contentPadding = PaddingValues(vertical = 12.dp),
        icon = {
            Image(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Google Login",
                modifier = Modifier.size(24.dp),
            )
        }
    ) {
        Text(
            text = "Continue with Google",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Normal
        )
    }

}