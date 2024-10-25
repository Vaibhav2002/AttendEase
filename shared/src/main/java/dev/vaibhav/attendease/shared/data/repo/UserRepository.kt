package dev.vaibhav.attendease.shared.data.repo

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.firestore.FirebaseFirestore
import dev.vaibhav.attendease.shared.data.datastore.Preferences
import dev.vaibhav.attendease.shared.data.models.Role
import dev.vaibhav.attendease.shared.data.models.User
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val preferences: Preferences
){
    private val collection = "users"

    val user = preferences.user
        .runCatching { Json.decodeFromString<User>(this) }
        .getOrDefault(null)

    suspend fun getUsers(ids: List<String>): List<User>{
        if(ids.isEmpty()) return emptyList()
        return firestore.collection(collection)
            .whereIn("id", ids)
            .get()
            .await()
            .takeIf { !it.isEmpty }
            ?.toObjects(User::class.java)
            ?: emptyList()
    }

    suspend fun getUser(email: String): User {
        return firestore.collection(collection)
            .whereEqualTo("email", email)
            .get()
            .await()
            .first()
            .toObject(User::class.java)
    }

    suspend fun saveUser(
        account: GoogleSignInAccount,
        role: Role
    ){
        val user = User(
            id = account.email?.substringBefore('@') ?: "",
            name = account.displayName ?: "",
            email = account.email ?: "",
            profilePic = account.photoUrl.toString(),
            role = role
        )
        firestore.collection(collection)
            .document(user.id)
            .set(user)
            .await()

        preferences.user = Json.encodeToString(user)
    }
}