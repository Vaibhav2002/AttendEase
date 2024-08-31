package dev.vaibhav.attendease.shared.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import dev.vaibhav.attendease.shared.data.models.Class
import dev.vaibhav.attendease.shared.utils.DateHelpers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ClassesRepository @Inject constructor(
    private val firestore: FirebaseFirestore
){
    private val collection = "classes"

    fun getClasses(subjectId: String) = callbackFlow {
        val listener = firestore.collection(collection)
            .whereEqualTo("subjectId", subjectId)
            .addSnapshotListener { value, error ->
                if(error != null) throw error
                value?.toObjects(Class::class.java)?.toList()?.let(::trySend)
            }

        awaitClose { listener.remove() }
    }

    suspend fun createClass(subjectId: String):String {
        val currentDay = DateHelpers.toLocalDateTime(DateHelpers.now).dayOfYear
        val id = "$subjectId-$currentDay"

        val classData = Class(
            id = id,
            createdOn = DateHelpers.nowInMillis,
            subjectId = subjectId
        )
        firestore.collection(collection).document(id).set(classData).await()
        return classData.id
    }
}