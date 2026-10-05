package com.example.haptics

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class HapticFeedbackEngine(context: Context) {

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    var generalIntensity: Float = 0.60f
    var spacebarIntensity: Float = 0.85f

    fun vibrateKey() {
        if (generalIntensity <= 0.02f || vibrator == null || !vibrator.hasVibrator()) return

        val duration = (12 * generalIntensity).toLong().coerceIn(4, 25)
        val amplitude = (255 * generalIntensity).toInt().coerceIn(1, 255)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                if (vibrator.hasAmplitudeControl()) {
                    vibrator.vibrate(VibrationEffect.createOneShot(duration, amplitude))
                } else {
                    vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
                }
            } catch (_: Exception) {}
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(duration)
        }
    }

    fun vibrateSpacebar() {
        if (spacebarIntensity <= 0.02f || vibrator == null || !vibrator.hasVibrator()) return

        val amplitude = (255 * spacebarIntensity).toInt().coerceIn(1, 255)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                if (vibrator.hasAmplitudeControl()) {
                    val timings = longArrayOf(0, 14, 8, 18)
                    val amplitudes = intArrayOf(
                        0,
                        amplitude,
                        (amplitude * 0.4f).toInt().coerceIn(1, 255),
                        (amplitude * 0.75f).toInt().coerceIn(1, 255)
                    )
                    vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
                } else {
                    vibrator.vibrate(VibrationEffect.createOneShot(28, VibrationEffect.DEFAULT_AMPLITUDE))
                }
            } catch (_: Exception) {}
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(28)
        }
    }
}
