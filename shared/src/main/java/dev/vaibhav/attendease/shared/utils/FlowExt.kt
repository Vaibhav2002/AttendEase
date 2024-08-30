package dev.vaibhav.attendease.shared.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlin.coroutines.cancellation.CancellationException

fun <T> Flow<T>.toStateFlow(
    coroutineScope: CoroutineScope,
    defaultValue: T
) = stateIn(coroutineScope, SharingStarted.WhileSubscribed(5000L), defaultValue)

fun <T> Flow<T>.safeCatch(block: suspend FlowCollector<T>.(Throwable) -> Unit = {}) = catch { e ->
    block(e)
    if (e is CancellationException) throw e
}

fun <T> Flow<T>.onIO() = flowOn(Dispatchers.IO)

fun <T> Flow<T>.onMain() = flowOn(Dispatchers.Main)