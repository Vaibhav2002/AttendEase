package dev.vaibhav.attendease.shared.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import dev.vaibhav.attendease.shared.data.models.Attendance
import dev.vaibhav.attendease.shared.data.models.exceptions.AttendanceException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AttendanceRepository @Inject constructor(
    private val userRepo: UserRepository,
    private val firestore: FirebaseFirestore
) {
    private val collection = "attendance"

    suspend fun addAttendee(classId: String, userId: String) {
        val attendance = Attendance(userId, classId)

        if(firestore.collection(collection).document(attendance.id).get().await().exists())
            throw AttendanceException.AlreadyMarked

        firestore.collection(collection)
            .document(attendance.id)
            .set(attendance)
            .await()
    }

    fun getAttendance(classId: String) = callbackFlow {
        val listener = firestore.collection(collection)
            .whereEqualTo("classId", classId)
            .addSnapshotListener { value, error ->
                if (error != null) return@addSnapshotListener
                value?.takeIf { !it.isEmpty }
                    ?.toObjects(Attendance::class.java)
                    ?.toList()
                    ?.map(Attendance::studentId)
                    ?.let(::trySend)
            }

        awaitClose { listener.remove() }
    }.map { userRepo.getUsers(it) }
}