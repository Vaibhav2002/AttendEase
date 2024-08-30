package dev.vaibhav.attendease.shared.data.repo

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dev.vaibhav.attendease.shared.utils.auth.InvalidDomainException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(private val auth: FirebaseAuth) {
    private val supportedDomains = listOf("rcciit.org.in")

    val currentUserId
        get() = auth.currentUser?.uid

    val currentUser
        get() = auth.currentUser


    suspend fun signInUsingGoogle(account: GoogleSignInAccount) {
        val email = account.email ?: throw Exception("Email not found")
        if (supportedDomains.none(email::endsWith)) throw InvalidDomainException()

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).await()
    }
}