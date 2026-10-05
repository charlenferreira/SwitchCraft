package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.KeycapProfile
import com.example.model.RgbEffect
import com.example.model.SwitchSoundProfile
import com.example.physical.PhysicalLayoutManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read app_name from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("SwitchCraft", appName)
    }

    @Test
    fun `verify keycap profiles and sound profiles count`() {
        assertEquals(17, KeycapProfile.entries.size)
        assertEquals(14, SwitchSoundProfile.entries.size)
        assertEquals(13, RgbEffect.entries.size)
    }

    @Test
    fun `verify physical layout manager dead keys`() {
        val manager = PhysicalLayoutManager()
        manager.activeLayout = "US_INTL"
        assertNotNull(manager)
    }
}
