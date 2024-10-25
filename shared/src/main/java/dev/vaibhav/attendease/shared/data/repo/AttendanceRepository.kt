package dev.vaibhav.attendease.shared.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import dev.vaibhav.attendease.shared.data.models.exceptions.AttendanceException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AttendanceRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val collection = "attendance"


}