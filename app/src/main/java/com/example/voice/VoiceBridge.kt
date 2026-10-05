package com.example.voice

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object VoiceBridge {
    private val _voiceTextFlow = MutableSharedFlow<String>(extraBufferCapacity = 10)
    val voiceTextFlow: SharedFlow<String> = _voiceTextFlow.asSharedFlow()

    private val _isListeningFlow = MutableSharedFlow<Boolean>(replay = 1, extraBufferCapacity = 2)
    val isListeningFlow: SharedFlow<Boolean> = _isListeningFlow.asSharedFlow()

    fun emitResult(text: String) {
        _voiceTextFlow.tryEmit(text)
    }

    fun setListeningState(isListening: Boolean) {
        _isListeningFlow.tryEmit(isListening)
    }
}
