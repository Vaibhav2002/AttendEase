package dev.vaibhav.attendease.shared.data.repo

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dev.vaibhav.attendease.shared.data.models.Class
import dev.vaibhav.attendease.shared.utils.DateHelpers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class ClassesRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val collection = "classes"

    fun observeClasses(subjectId: String) = callbackFlow {
        val listener = firestore.collection(collection)
            .whereEqualTo("subjectId", subjectId)
            .orderBy("createdOn", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) throw error
                value?.toObjects(Class::class.java)
                    ?.toList()
                    ?.map { it as Class }
                    ?.let(::trySend)
            }

        awaitClose { listener.remove() }
    }

    suspend fun getClasses(subjectId: String) = firestore
        .collection(collection)
        .whereEqualTo("subjectId", subjectId)
        .orderBy("createdOn", Query.Direction.DESCENDING)
        .get()
        .await()
        ?.toObjects(Class::class.java)
        .orEmpty()
        .toList()
        .map { it as Class }

    fun getClass(classId: String) = callbackFlow {
        val listener = firestore.collection(collection)
            .document(classId)
            .addSnapshotListener { value, error ->
                if (error != null) throw error
                value?.toObject(Class::class.java)?.let(::trySend)
            }

        awaitClose { listener.remove() }
    }

    suspend fun createClass(subjectId: String): String {
        val classData = Class(
            id = UUID.randomUUID().toString(),
            createdOn = DateHelpers.nowInMillis,
            subjectId = subjectId
        )
        firestore.collection(collection).document(classData.id).set(classData).await()
        return classData.id
    }

    suspend fun addAttendee(classId: String, userId: String) {
        firestore.collection(collection)
            .document(classId)
            .update("attendees", FieldValue.arrayUnion(userId))
            .await()
    }
}