package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import com.example.model.SwitchSoundProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin
import kotlin.random.Random

class MechanicalSoundEngine(private val context: Context) {

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val pcmCache = ConcurrentHashMap<SwitchSoundProfile, ShortArray>()

    var currentProfile: SwitchSoundProfile = SwitchSoundProfile.GRAVE
    var volume: Float = 0.85f
    var muteInSilentMode: Boolean = true

    init {
        scope.launch {
            for (profile in SwitchSoundProfile.entries) {
                pcmCache[profile] = synthesizeSwitchClick(profile, 1.0f)
            }
        }
    }

    fun playKeystroke(isSpacebar: Boolean = false) {
        if (muteInSilentMode && isDeviceMuted()) return
        if (volume <= 0.01f) return

        val profile = currentProfile
        val pitchVariance = 1.0f + (Random.nextFloat() * 0.06f - 0.03f) - (if (isSpacebar) 0.15f else 0f)

        scope.launch {
            try {
                val samples = if (isSpacebar || kotlin.math.abs(pitchVariance - 1.0f) > 0.02f) {
                    synthesizeSwitchClick(profile, pitchVariance, isSpacebar = isSpacebar)
                } else {
                    pcmCache[profile] ?: synthesizeSwitchClick(profile, 1.0f)
                }
                playPcmDirect(samples, volume)
            } catch (_: Exception) {}
        }
    }

    private fun isDeviceMuted(): Boolean {
        return try {
            val ringerMode = audioManager?.ringerMode ?: AudioManager.RINGER_MODE_NORMAL
            ringerMode != AudioManager.RINGER_MODE_NORMAL
        } catch (_: Exception) {
            false
        }
    }

    private fun synthesizeSwitchClick(
        profile: SwitchSoundProfile,
        pitchFactor: Float,
        isSpacebar: Boolean = false
    ): ShortArray {
        val sampleRate = 44100
        val durationMs = (profile.attackMs + profile.decayMs) * (if (isSpacebar) 1.4f else 1.0f)
        val numSamples = ((sampleRate * durationMs) / 1000f).toInt().coerceAtLeast(200)
        val buffer = ShortArray(numSamples)

        val baseFreq = profile.baseFrequency * pitchFactor * (if (isSpacebar) 0.75f else 1.0f)
        val pingFreq = profile.pingFrequency * pitchFactor
        val attackSamples = ((sampleRate * profile.attackMs) / 1000f).coerceAtLeast(10f)
        val decaySamples = (numSamples - attackSamples).coerceAtLeast(50f)

        var lastNoise = 0f

        for (i in 0 until numSamples) {
            val t = i.toFloat() / sampleRate.toFloat()

            val envelope = if (i < attackSamples) {
                i.toFloat() / attackSamples
            } else {
                val progress = (i - attackSamples) / decaySamples
                exp(-progress * 4.5f)
            }

            val instantBaseFreq = baseFreq * (1.0f + 0.15f * envelope)
            val baseWave = sin(2.0 * PI * instantBaseFreq * t).toFloat()
            val harmonicsWave = sin(2.0 * PI * pingFreq * t).toFloat() * profile.harmonicsRatio

            val rawNoise = (Random.nextFloat() * 2f - 1f)
            lastNoise = lastNoise * 0.7f + rawNoise * 0.3f
            val noiseWave = lastNoise * profile.noiseRatio

            val composite = (baseWave + harmonicsWave + noiseWave) * envelope
            val sampleInt = (composite * 28000f).toInt().coerceIn(-32767, 32767)
            buffer[i] = sampleInt.toShort()
        }

        return buffer
    }

    private fun playPcmDirect(samples: ShortArray, vol: Float) {
        val sampleRate = 44100
        val bufferSize = samples.size * 2

        val audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(bufferSize)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        audioTrack.write(samples, 0, samples.size)
        audioTrack.setVolume(vol.coerceIn(0f, 1f))
        audioTrack.play()

        scope.launch {
            try {
                val durationMs = (samples.size * 1000L) / sampleRate + 25L
                kotlinx.coroutines.delay(durationMs)
                audioTrack.stop()
                audioTrack.release()
            } catch (_: Exception) {}
        }
    }
}
