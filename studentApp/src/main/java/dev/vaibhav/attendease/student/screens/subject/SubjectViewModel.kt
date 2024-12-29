package dev.vaibhav.attendease.student.screens.subject

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vaibhav.attendease.shared.data.models.Class
import dev.vaibhav.attendease.shared.data.repo.ClassesRepository
import dev.vaibhav.attendease.shared.data.repo.SubjectsRepository
import dev.vaibhav.attendease.shared.data.repo.UserRepository
import dev.vaibhav.attendease.shared.ui.screens.BaseViewModel
import dev.vaibhav.attendease.shared.ui.screens.ScreenState
import dev.vaibhav.attendease.shared.utils.onIO
import dev.vaibhav.attendease.shared.utils.safeCatch
import dev.vaibhav.attendease.shared.utils.toStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

@HiltViewModel(assistedFactory = SubjectViewModel.Factory::class)
class SubjectViewModel @AssistedInject constructor(
    @Assisted("id") val id: String,
    @Assisted("name") val name: String,
    private val subjectRepo: SubjectsRepository,
    private val classRepo: ClassesRepository,
    private val userRepo: UserRepository
) : BaseViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("id") id: String,
            @Assisted("name") name: String
        ): SubjectViewModel
    }

    data class ClassData(
        val classData: Class,
        val present: Boolean
    )

    var subjectName by mutableStateOf(name)
        private set

    private val subject = flow { emit(subjectRepo.getSubject(id)!!) }
        .onStart { setScreenState(ScreenState.Loading) }
        .onEach { setScreenState(ScreenState.Normal) }
        .onEach { subjectName = it.title }
        .safeCatch { setErrorState(it) }
        .onIO()
        .toStateFlow(viewModelScope, null)

    val classes = subject
        .mapNotNull { it?.id }
        .distinctUntilChanged()
        .onEach { setScreenState(ScreenState.Loading) }
        .mapLatest { classRepo.getClasses(it) }
        .mapLatest {
            val userId = userRepo.userId
            it.map {
                ClassData(classData = it, present = it.attendees.contains(userId))
            }
        }
        .onEach { setScreenState(ScreenState.Normal) }
        .onIO()
        .toStateFlow(viewModelScope, emptyList())
}