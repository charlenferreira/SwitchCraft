package com.example.ui.keyboard

import androidx.compose.ui.graphics.Color
import com.example.model.RgbEffect
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

object RgbEngine {

    fun computeKeyColor(
        effect: RgbEffect,
        col: Int,
        totalCols: Int,
        row: Int,
        totalRows: Int,
        elapsedTimeMs: Long,
        accentColor: Color,
        lastPressedCol: Int,
        lastPressedRow: Int,
        timeSinceLastPressMs: Long
    ): Color {
        val t = (elapsedTimeMs % 100000L).toFloat() / 1000f

        return when (effect) {
            RgbEffect.NORMAL -> accentColor.copy(alpha = 0.55f)
            RgbEffect.ESTATICO -> accentColor
            RgbEffect.RESPIRACAO -> {
                val wave = (sin(t * 2.2) * 0.5 + 0.5).toFloat()
                accentColor.copy(alpha = 0.25f + wave * 0.75f)
            }
            RgbEffect.ONDA -> {
                val colRatio = col.toFloat() / totalCols.coerceAtLeast(1)
                val hue = ((colRatio * 360f - t * 90f) % 360f + 360f) % 360f
                colorFromHsv(hue, 0.95f, 1.0f)
            }
            RgbEffect.CASCATA -> {
                val rowRatio = row.toFloat() / totalRows.coerceAtLeast(1)
                val hue = ((rowRatio * 360f - t * 120f) % 360f + 360f) % 360f
                colorFromHsv(hue, 0.90f, 1.0f)
            }
            RgbEffect.CICLO_CORES -> {
                val hue = ((t * 60f) % 360f + 360f) % 360f
                colorFromHsv(hue, 0.90f, 1.0f)
            }
            RgbEffect.REATIVO -> {
                val dist = hypot((col - lastPressedCol).toFloat(), (row - lastPressedRow).toFloat())
                val waveProgress = (timeSinceLastPressMs.toFloat() / 600f) * 6.0f
                val delta = kotlin.math.abs(dist - waveProgress)
                if (delta < 1.4f && timeSinceLastPressMs < 750L) {
                    val fade = 1.0f - (timeSinceLastPressMs.toFloat() / 750f)
                    accentColor.copy(alpha = fade)
                } else {
                    accentColor.copy(alpha = 0.15f)
                }
            }
            RgbEffect.PINGO_DAGUA -> {
                val dist = hypot((col - lastPressedCol).toFloat(), (row - lastPressedRow).toFloat())
                val wave = sin(dist * 1.8 - (timeSinceLastPressMs.toFloat() / 150f))
                if (timeSinceLastPressMs < 1200L) {
                    val fade = 1.0f - (timeSinceLastPressMs.toFloat() / 1200f)
                    val intensity = ((wave * 0.5f + 0.5f) * fade).coerceIn(0f, 1f)
                    val hue = (accentColorHue(accentColor) + dist * 20f) % 360f
                    colorFromHsv(hue, 0.85f, intensity)
                } else {
                    accentColor.copy(alpha = 0.20f)
                }
            }
            RgbEffect.STARLIGHT -> {
                val keySeed = (col * 31 + row * 17)
                val starPhase = sin(t * 3.5 + keySeed)
                if (starPhase > 0.65) {
                    val alpha = ((starPhase - 0.65) / 0.35).toFloat()
                    Color.White.copy(alpha = alpha)
                } else {
                    accentColor.copy(alpha = 0.18f)
                }
            }
            RgbEffect.ESPIRAL -> {
                val centerX = totalCols / 2.0
                val centerY = totalRows / 2.0
                val angle = atan2(row - centerY, col - centerX)
                val dist = hypot(col - centerX, row - centerY)
                val hue = (((angle * 180.0 / Math.PI + dist * 35.0 - t * 140.0) % 360.0 + 360.0) % 360.0).toFloat()
                colorFromHsv(hue, 0.95f, 1.0f)
            }
            RgbEffect.CHUVA -> {
                val dropPos = ((t * 4.5f + col * 1.3f) % totalRows.toFloat())
                val diff = kotlin.math.abs(row.toFloat() - dropPos)
                if (diff < 1.0f) {
                    Color(0xFF38BDF8)
                } else if (diff < 2.0f) {
                    Color(0xFF0284C7).copy(alpha = 0.5f)
                } else {
                    accentColor.copy(alpha = 0.15f)
                }
            }
            RgbEffect.VISOR -> {
                val sweep = ((sin(t * 3.0) * 0.5 + 0.5) * totalCols).toFloat()
                val diff = kotlin.math.abs(col - sweep)
                if (diff < 1.5f) {
                    val glow = (1.0f - diff / 1.5f).coerceIn(0f, 1f)
                    Color(0xFF10B981).copy(alpha = 0.3f + glow * 0.7f)
                } else {
                    accentColor.copy(alpha = 0.15f)
                }
            }
            RgbEffect.FOGO -> {
                val rowFromBottom = (totalRows - 1 - row).toFloat()
                val flame = sin(t * 5.0 + col * 0.8) * 0.6 + cos(t * 3.2 + row * 1.1) * 0.4
                val heat = ((1.0f - (rowFromBottom / totalRows.coerceAtLeast(1))) + flame * 0.25f).coerceIn(0f, 1f)
                when {
                    heat > 0.7f -> Color(0xFFFDE047)
                    heat > 0.4f -> Color(0xFFF97316)
                    else -> Color(0xFFDC2626).copy(alpha = 0.5f + heat * 0.5f)
                }
            }
        }
    }

    private fun accentColorHue(color: Color): Float {
        val r = color.red
        val g = color.green
        val b = color.blue
        val max = maxOf(r, g, b)
        val min = minOf(r, g, b)
        val delta = max - min
        if (delta == 0f) return 180f
        val hue = when (max) {
            r -> 60f * (((g - b) / delta) % 6f)
            g -> 60f * (((b - r) / delta) + 2f)
            else -> 60f * (((r - g) / delta) + 4f)
        }
        return (hue + 360f) % 360f
    }

    private fun colorFromHsv(hue: Float, sat: Float, value: Float): Color {
        val c = value * sat
        val x = c * (1f - kotlin.math.abs((hue / 60f) % 2f - 1f))
        val m = value - c

        val (r, g, b) = when {
            hue < 60f -> Triple(c, x, 0f)
            hue < 120f -> Triple(x, c, 0f)
            hue < 180f -> Triple(0f, c, x)
            hue < 240f -> Triple(0f, x, c)
            hue < 300f -> Triple(x, 0f, c)
            else -> Triple(c, 0f, x)
        }

        return Color(
            red = (r + m).coerceIn(0f, 1f),
            green = (g + m).coerceIn(0f, 1f),
            blue = (b + m).coerceIn(0f, 1f),
            alpha = 1.0f
        )
    }
}
