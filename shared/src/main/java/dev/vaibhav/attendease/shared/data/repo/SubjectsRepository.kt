package dev.vaibhav.attendease.shared.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dev.vaibhav.attendease.shared.data.datastore.Preferences
import dev.vaibhav.attendease.shared.data.models.Department
import dev.vaibhav.attendease.shared.data.models.Section
import dev.vaibhav.attendease.shared.data.models.Subject
import dev.vaibhav.attendease.shared.utils.DateHelpers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SubjectsRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authRepo: AuthRepository,
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

    suspend fun createSubject(
        title: String,
        dept: Department,
        section: Section
    ) {
        val id = "$title-${dept.name}"
        val subject = Subject(
            id = id,
            createdBy = authRepo.userId,
            created = DateHelpers.nowInMillis,
            title = title,
            department = dept,
            section = section
        )
        firestore.collection(collection).document(id).set(subject).await()
    }
}