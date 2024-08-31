package dev.vaibhav.attendease.teacher.screens.classes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vaibhav.attendease.shared.data.models.Subject
import dev.vaibhav.attendease.shared.data.repo.ClassesRepository
import dev.vaibhav.attendease.shared.data.repo.SubjectsRepository
import dev.vaibhav.attendease.shared.ui.screens.BaseViewModel
import dev.vaibhav.attendease.shared.ui.screens.ScreenState
import dev.vaibhav.attendease.shared.utils.onIO
import dev.vaibhav.attendease.shared.utils.safeCatch
import dev.vaibhav.attendease.shared.utils.toStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

@HiltViewModel(assistedFactory = ClassesViewModel.ClassesViewModelFactory::class)
class ClassesViewModel @AssistedInject constructor(
    @Assisted val subjectId: String,
    private val subjectRepo: SubjectsRepository,
    private val classesRepo: ClassesRepository
) : BaseViewModel() {

    @AssistedFactory
    interface ClassesViewModelFactory {
        fun create(subjectId: String): ClassesViewModel
    }

    val subject = retry
        .onStart { emit(Unit) }
        .mapLatest { subjectId }
        .onEach { setScreenState(ScreenState.Loading) }
        .mapLatest { subjectRepo.getSubject(it) }
        .onIO()
        .safeCatch { setErrorState(it) }
        .toStateFlow(viewModelScope, null)

    val classes = subject
        .filterNotNull()
        .distinctUntilChangedBy { it.id }
        .onEach { setScreenState(ScreenState.Loading) }
        .flatMapLatest { classesRepo.getClasses(it.id) }
        .onEach { setScreenState(ScreenState.Normal) }
        .safeCatch { showSnackBar(); setScreenState(ScreenState.Normal) }
        .onIO()
        .toStateFlow(viewModelScope, emptyList())

    var isCreatingClass by mutableStateOf(false)
        private set

    private val _classCreated = MutableSharedFlow<String>()
    val classCreated = _classCreated.asSharedFlow()

    fun onCreateClass() = flow { emit(subject.value?.id) }
        .filterNotNull()
        .mapLatest { classesRepo.createClass(it) }
        .onStart { isCreatingClass = true }
        .onEach { isCreatingClass = false }
        .onEach { _classCreated.emit(it) }
        .safeCatch { showSnackBar(); isCreatingClass = false }
        .onIO()
        .launchIn(viewModelScope)

}