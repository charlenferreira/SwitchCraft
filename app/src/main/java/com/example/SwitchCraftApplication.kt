package com.example

import android.app.Application
import com.example.audio.MechanicalSoundEngine
import com.example.data.KeyboardPreferences
import com.example.data.StatisticsRepository
import com.example.haptics.HapticFeedbackEngine

class SwitchCraftApplication : Application() {

    lateinit var preferences: KeyboardPreferences
        private set

    lateinit var statisticsRepository: StatisticsRepository
        private set

    lateinit var soundEngine: MechanicalSoundEngine
        private set

    lateinit var hapticEngine: HapticFeedbackEngine
        private set

    override fun onCreate() {
        super.onCreate()
        preferences = KeyboardPreferences(this)
        statisticsRepository = StatisticsRepository(this)
        soundEngine = MechanicalSoundEngine(this)
        hapticEngine = HapticFeedbackEngine(this)
    }
}
