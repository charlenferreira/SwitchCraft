package com.example.voice

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import java.util.Locale

class VoiceInputActivity : ComponentActivity() {

    private var speechRecognizer: SpeechRecognizer? = null
    private val handler = Handler(Looper.getMainLooper())
    private var silenceRunnable: Runnable? = null
    private var silenceTimeoutMs = 1500L
    private var accumulatedText: String = ""

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startSpeechRecognition()
        } else {
            Toast.makeText(this, "Permissão de microfone necessária para ditado", Toast.LENGTH_SHORT).show()
            finishClean()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        silenceTimeoutMs = (intent.getFloatExtra(EXTRA_SILENCE_TIMEOUT, 1.5f) * 1000f).toLong().coerceIn(1000L, 5000L)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            startSpeechRecognition()
        } else {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    private fun startSpeechRecognition() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Toast.makeText(this, "Google Speech Services não disponível no dispositivo", Toast.LENGTH_SHORT).show()
            finishClean()
            return
        }

        VoiceBridge.setListeningState(true)

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this).apply {
            setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    resetSilenceTimer()
                }

                override fun onBeginningOfSpeech() {
                    cancelSilenceTimer()
                }

                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {
                    resetSilenceTimer()
                }

                override fun onError(error: Int) {
                    if (accumulatedText.isNotBlank()) {
                        VoiceBridge.emitResult(accumulatedText)
                    }
                    finishClean()
                }

                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val text = matches?.firstOrNull() ?: accumulatedText
                    if (text.isNotBlank()) {
                        VoiceBridge.emitResult(text)
                    }
                    finishClean()
                }

                override fun onPartialResults(partialResults: Bundle?) {
                    val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val partial = matches?.firstOrNull()
                    if (!partial.isNullOrBlank()) {
                        accumulatedText = partial
                        resetSilenceTimer()
                    }
                }

                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }

        try {
            speechRecognizer?.startListening(intent)
        } catch (_: Exception) {
            finishClean()
        }
    }

    private fun resetSilenceTimer() {
        cancelSilenceTimer()
        silenceRunnable = Runnable {
            if (accumulatedText.isNotBlank()) {
                VoiceBridge.emitResult(accumulatedText)
            }
            try {
                speechRecognizer?.stopListening()
            } catch (_: Exception) {}
            finishClean()
        }
        handler.postDelayed(silenceRunnable!!, silenceTimeoutMs)
    }

    private fun cancelSilenceTimer() {
        silenceRunnable?.let { handler.removeCallbacks(it) }
        silenceRunnable = null
    }

    private fun finishClean() {
        cancelSilenceTimer()
        VoiceBridge.setListeningState(false)
        try {
            speechRecognizer?.destroy()
        } catch (_: Exception) {}
        speechRecognizer = null
        finish()
        overridePendingTransition(0, 0)
    }

    override fun onDestroy() {
        finishClean()
        super.onDestroy()
    }

    companion object {
        const val EXTRA_SILENCE_TIMEOUT = "extra_silence_timeout"
    }
}
