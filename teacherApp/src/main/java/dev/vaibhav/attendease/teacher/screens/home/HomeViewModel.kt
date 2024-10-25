package dev.vaibhav.attendease.teacher.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vaibhav.attendease.shared.data.models.Department
import dev.vaibhav.attendease.shared.data.repo.AuthRepository
import dev.vaibhav.attendease.shared.data.repo.SubjectsRepository
import dev.vaibhav.attendease.shared.ui.screens.BaseViewModel
import dev.vaibhav.attendease.shared.ui.screens.ScreenState
import dev.vaibhav.attendease.shared.utils.onIO
import dev.vaibhav.attendease.shared.utils.safeCatch
import dev.vaibhav.attendease.shared.utils.toStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val subjectsRepo: SubjectsRepository,
    private val authRepo: AuthRepository
) : BaseViewModel(ScreenState.Loading) {

    var subjectCreationSheet by mutableStateOf(false)

    var subject by mutableStateOf("")
        private set

    var department by mutableStateOf(Department.IT)
        private set

    val isValid = snapshotFlow { subject }
        .mapLatest { it.isNotBlank() }
        .toStateFlow(viewModelScope, false)

    val departments = Department.entries.toList()

    var isSaving by mutableStateOf(false)

    val user = authRepo.user

    val subjects = subjectsRepo.subjects
        .onStart { setScreenState(ScreenState.Loading) }
        .onEach { setScreenState(ScreenState.Normal) }
        .safeCatch { showSnackBar(); setScreenState(ScreenState.Normal) }
        .onIO()
        .toStateFlow(viewModelScope, emptyList())


    fun onCreateSubjectClick() {
        subjectCreationSheet = true
    }

    fun onSubjectChange(subject: String) {
        this.subject = subject
    }

    fun onDepartmentChange(department: Department) {
        this.department = department
    }
}