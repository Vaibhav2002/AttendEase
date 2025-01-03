package dev.vaibhav.attendease.shared.utils.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.vaibhav.attendease.shared.BuildConfig
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GoogleSignInClient @Inject constructor(@ApplicationContext private val context: Context) {

    suspend fun handleResult(result: ActivityResult): GoogleSignInAccount = when {
        result.resultCode == Activity.RESULT_OK && result.data != null -> {
            getGoogleAccount(result.data!!).also {
                runCatching { client.signOut().await() }
            }
        }

        result.resultCode == Activity.RESULT_CANCELED -> throw AuthCancellationException()
        else -> throw Exception("Error: ${result.resultCode}")
    }

    private val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
        .requestProfile()
        .build()

    private val client = GoogleSignIn.getClient(context, signInOptions)

    val signInIntent = client.signInIntent

    private suspend fun getGoogleAccount(intent: Intent): GoogleSignInAccount {
        return GoogleSignIn.getSignedInAccountFromIntent(intent).await()
    }
}