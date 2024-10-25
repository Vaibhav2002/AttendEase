package dev.vaibhav.attendease.student.screens.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vaibhav.attendease.shared.data.models.QrModel
import dev.vaibhav.attendease.shared.data.repo.AuthRepository
import dev.vaibhav.attendease.shared.data.repo.SubjectsRepository
import dev.vaibhav.attendease.shared.ui.screens.BaseViewModel
import dev.vaibhav.attendease.shared.utils.DateHelpers
import dev.vaibhav.attendease.shared.utils.onIO
import dev.vaibhav.attendease.shared.utils.safeCatch
import dev.vaibhav.attendease.shared.utils.toStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    subjectRepo: SubjectsRepository
): BaseViewModel() {

    val user = authRepo.user

    val subjects = subjectRepo.subjectsEnrolledIn
        .onIO()
        .safeCatch()
        .toStateFlow(viewModelScope, emptyList())
}