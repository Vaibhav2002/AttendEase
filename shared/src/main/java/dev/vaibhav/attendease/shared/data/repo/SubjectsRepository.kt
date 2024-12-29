package dev.vaibhav.attendease.shared.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dev.vaibhav.attendease.shared.data.models.Subject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SubjectsRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userRepo: UserRepository
) {

    private val collection = "subjects"

    val subjectsCreatedByMe = callbackFlow {
        val listener = firestore.collection(collection)
            .whereEqualTo("createdBy", userRepo.user!!.id)
            .orderBy("created", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) throw error
                value?.toObjects(Subject::class.java)?.toList()?.let(::trySend)
            }

        awaitClose { listener.remove() }
    }

    val subjectsEnrolledIn = callbackFlow {
        val listener = firestore.collection(collection)
            .whereArrayContains("studentsEnrolled", userRepo.user!!.id)
            .orderBy("created", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) throw error
                value?.toObjects(Subject::class.java)?.toList()?.let(::trySend)
            }

        awaitClose { listener.remove() }
    }

    suspend fun getSubject(id: String) =
        firestore.collection(collection).document(id).get().await().toObject(Subject::class.java)
}