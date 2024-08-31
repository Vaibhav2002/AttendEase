package dev.vaibhav.attendease.student.screens.home

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vaibhav.attendease.shared.data.models.QrModel
import dev.vaibhav.attendease.shared.data.repo.AuthRepository
import dev.vaibhav.attendease.shared.ui.screens.BaseViewModel
import dev.vaibhav.attendease.shared.utils.DateHelpers
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepo: AuthRepository,
): BaseViewModel() {

    fun getQRData() = QrModel(
        studentId = authRepo.email.substringBefore("@"),
        email = authRepo.email,
        createdAt = DateHelpers.nowInMillis,
    ).let { Json.encodeToString(it) }
}