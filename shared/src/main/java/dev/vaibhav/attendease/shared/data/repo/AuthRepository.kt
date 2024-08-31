package dev.vaibhav.attendease.shared.data.repo

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dev.vaibhav.attendease.shared.data.models.Role
import dev.vaibhav.attendease.shared.data.models.User
import dev.vaibhav.attendease.shared.utils.auth.InvalidDomainException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository,
) {
    private val supportedDomains = listOf("rcciit.org.in")

    val user
        get() = auth.currentUser!!.let {
            User(
                id = it.email!!.substringBefore("@"),
                email = it.email!!,
                name = it.displayName ?: "Unknown",
                profilePic = it.photoUrl.toString()
            )
        }

    val userId
        get() = auth.currentUser?.uid ?: throw Exception("User not signed in")

    val email
        get() = auth.currentUser?.email ?: throw Exception("User not signed in")

    val isLoggedIn
        get() = auth.currentUser != null

    fun isValidEmail(email: String): Boolean {
        return true
        return supportedDomains.any(email::endsWith)
    }

    suspend fun signInUsingGoogle(
        account: GoogleSignInAccount,
        role: Role
    ) {
        val email = account.email ?: throw Exception("Email not found")
        if (!isValidEmail(email)) throw InvalidDomainException()

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        userRepository.saveUser(account, role)
        auth.signInWithCredential(credential).await()
    }
}