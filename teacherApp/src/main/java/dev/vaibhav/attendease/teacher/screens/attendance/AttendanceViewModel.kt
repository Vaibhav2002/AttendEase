package dev.vaibhav.attendease.teacher.screens.attendance

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vaibhav.attendease.shared.data.models.QrModel
import dev.vaibhav.attendease.shared.data.models.User
import dev.vaibhav.attendease.shared.data.models.createdAt
import dev.vaibhav.attendease.shared.data.models.exceptions.AttendanceException
import dev.vaibhav.attendease.shared.data.repo.AttendanceRepository
import dev.vaibhav.attendease.shared.data.repo.AuthRepository
import dev.vaibhav.attendease.shared.data.repo.ClassesRepository
import dev.vaibhav.attendease.shared.data.repo.SubjectsRepository
import dev.vaibhav.attendease.shared.ui.screens.BaseViewModel
import dev.vaibhav.attendease.shared.ui.screens.ScreenState
import dev.vaibhav.attendease.shared.utils.DateHelpers
import dev.vaibhav.attendease.shared.utils.onIO
import dev.vaibhav.attendease.shared.utils.safeCatch
import dev.vaibhav.attendease.shared.utils.toStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@HiltViewModel(assistedFactory = AttendanceViewModel.Factory::class)
class AttendanceViewModel @AssistedInject constructor(
    @Assisted private val classId: String,
    private val authRepo: AuthRepository,
    private val classRepo: ClassesRepository,
    private val attendanceRepo: AttendanceRepository,
    private val subjectRepo: SubjectsRepository
) : BaseViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(classId: String): AttendanceViewModel
    }

    private val qrData = MutableSharedFlow<String>()

    val classData = retry.onStart { emit(Unit) }
        .onEach { setScreenState(ScreenState.Loading) }
        .mapLatest { classRepo.getClass(classId) }
        .onEach { setScreenState(ScreenState.Normal) }
        .onIO()
        .safeCatch { setScreenState(ScreenState.Error(it)) }
        .toStateFlow(viewModelScope, null)

    val subject = classData.mapNotNull { it?.subjectId }
        .onEach { setScreenState(ScreenState.Loading) }
        .mapLatest { subjectRepo.getSubject(it) }
        .onEach { setScreenState(ScreenState.Normal) }
        .onIO()
        .safeCatch { setScreenState(ScreenState.Error(it)) }
        .toStateFlow(viewModelScope, null)

    val attendees = classData.mapNotNull { it?.id }
        .distinctUntilChanged()
        .flatMapLatest { attendanceRepo.getAttendance(it) }
        .onEach { Log.d("Attendance", it.toString()) }
        .onIO()
        .safeCatch { showSnackBar("Failed to fetch attendees") }
        .toStateFlow(viewModelScope, emptyList())

    val canTakeAttendance = classData.mapNotNull { it?.createdAt }
        .mapLatest { DateHelpers.daysBetween(it, DateHelpers.now) == 0 }
        .toStateFlow(viewModelScope, false)

    init {
        saveAttendance()
    }

    fun takeAttendance(data: String) = viewModelScope.launch {
        qrData.emit(data)
    }

    private fun saveAttendance() = qrData
        .map { Json.decodeFromString<QrModel>(it) }
        .filter { validateQR(it) }
        .distinctUntilChangedBy { it.studentId }
        .map { attendanceRepo.addAttendee(classId, it.studentId) }
        .safeCatch {
            if(it is AttendanceException)
                showSnackBar(it.message ?: "Failed to capture attendance")
            else showSnackBar("Failed to capture attendance")
        }
        .onIO()
        .launchIn(viewModelScope)

    private fun validateQR(model: QrModel): Boolean {
        val classCreationTime = classData.value?.createdAt ?: return false

        val isOfCorrectDate = (DateHelpers.toInstant(model.createdAt) > classCreationTime)
            .also { if (!it) showSnackBar("Please generate a new QR") }

        val isValidEmail = authRepo.isValidEmail(model.email)
            .also { if (!it) showSnackBar("Only rcciit.org.in emails are allowed") }

        return isOfCorrectDate && isValidEmail
    }
}