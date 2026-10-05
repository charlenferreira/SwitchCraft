package com.example.ui.preview

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.model.SwitchSoundProfile

@Composable
fun ExposedSwitchesCanvas(
    currentProfile: SwitchSoundProfile,
    rgbColor: Color,
    modifier: Modifier = Modifier,
    onTestClick: (isSpacebar: Boolean) -> Unit
) {
    var activeTestSwitchIndex by remember { mutableIntStateOf(-1) }

    val pressSink by animateFloatAsState(
        targetValue = if (activeTestSwitchIndex >= 0) 1.0f else 0.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "switch_sink"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
            .shadow(6.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        val isSpace = offset.y > size.height * 0.55f
                        activeTestSwitchIndex = if (isSpace) 4 else (offset.x / (size.width / 4)).toInt().coerceIn(0, 3)
                        onTestClick(isSpace)
                        tryAwaitRelease()
                        activeTestSwitchIndex = -1
                    }
                )
            }
    ) {
        val stemColor = Color(currentProfile.stemColorHex)

        Canvas(modifier = Modifier.fillMaxWidth().height(130.dp)) {
            val w = size.width
            val h = size.height

            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1E2024), Color(0xFF121417), Color(0xFF090A0C))
                )
            )

            listOf(Offset(14f, 14f), Offset(w - 14f, 14f), Offset(14f, h - 14f), Offset(w - 14f, h - 14f)).forEach { screwPos ->
                drawCircle(color = Color(0xFF3F3F46), radius = 5f, center = screwPos)
                drawCircle(color = Color(0xFF71717A), radius = 3.5f, center = screwPos)
                drawLine(color = Color(0xFF18181B), start = Offset(screwPos.x - 3f, screwPos.y), end = Offset(screwPos.x + 3f, screwPos.y), strokeWidth = 1f)
            }

            val switchSize = 44.dp.toPx()
            val rowY = 16.dp.toPx()
            val spacing = (w - (switchSize * 4)) / 5f

            for (i in 0 until 4) {
                val switchX = spacing + i * (switchSize + spacing)
                val isThisPressed = activeTestSwitchIndex == i
                val currentSink = if (isThisPressed) pressSink * 7f else 0f

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(rgbColor.copy(alpha = 0.6f), Color.Transparent),
                        center = Offset(switchX + switchSize / 2f, rowY + switchSize / 2f),
                        radius = switchSize * 0.75f
                    ),
                    radius = switchSize * 0.75f,
                    center = Offset(switchX + switchSize / 2f, rowY + switchSize / 2f)
                )

                drawRoundRect(
                    color = Color(0xFF18181B),
                    topLeft = Offset(switchX, rowY),
                    size = Size(switchSize, switchSize),
                    cornerRadius = CornerRadius(6f, 6f)
                )
                drawRoundRect(
                    color = Color(0xFF27272A),
                    topLeft = Offset(switchX + 2f, rowY + 2f),
                    size = Size(switchSize - 4f, switchSize - 4f),
                    cornerRadius = CornerRadius(4f, 4f),
                    style = Stroke(width = 1.2f)
                )

                drawCircle(
                    color = Color(0xFF09090B),
                    radius = switchSize * 0.30f,
                    center = Offset(switchX + switchSize / 2f, rowY + switchSize / 2f)
                )

                val centerX = switchX + switchSize / 2f
                val centerY = rowY + switchSize / 2f + currentSink
                val barThickness = 4.dp.toPx()
                val barLength = 16.dp.toPx()

                drawRoundRect(
                    color = stemColor,
                    topLeft = Offset(centerX - barThickness / 2f, centerY - barLength / 2f),
                    size = Size(barThickness, barLength),
                    cornerRadius = CornerRadius(2f, 2f)
                )
                drawRoundRect(
                    color = stemColor,
                    topLeft = Offset(centerX - barLength / 2f, centerY - barThickness / 2f),
                    size = Size(barLength, barThickness),
                    cornerRadius = CornerRadius(2f, 2f)
                )
                drawRect(
                    color = Color.White.copy(alpha = 0.25f),
                    topLeft = Offset(centerX - barThickness / 2f, centerY - barThickness / 2f),
                    size = Size(barThickness, barThickness)
                )
            }

            val spaceY = 72.dp.toPx()
            val spaceW = w * 0.65f
            val spaceX = (w - spaceW) / 2f
            val isSpacePressed = activeTestSwitchIndex == 4
            val spaceSink = if (isSpacePressed) pressSink * 8f else 0f

            val wireY = spaceY + 18.dp.toPx()
            drawLine(
                color = Color(0xFFFBBF24),
                start = Offset(spaceX + 16f, wireY),
                end = Offset(spaceX + spaceW - 16f, wireY),
                strokeWidth = 2.5f
            )

            val centerSpaceX = spaceX + spaceW / 2f - switchSize / 2f
            drawRoundRect(
                color = Color(0xFF18181B),
                topLeft = Offset(centerSpaceX, spaceY),
                size = Size(switchSize, switchSize * 0.85f),
                cornerRadius = CornerRadius(6f, 6f)
            )

            val spaceCenterX = centerSpaceX + switchSize / 2f
            val spaceCenterY = spaceY + (switchSize * 0.85f) / 2f + spaceSink
            drawRoundRect(
                color = stemColor,
                topLeft = Offset(spaceCenterX - 2.5.dp.toPx(), spaceCenterY - 7.dp.toPx()),
                size = Size(5.dp.toPx(), 14.dp.toPx()),
                cornerRadius = CornerRadius(2f, 2f)
            )
            drawRoundRect(
                color = stemColor,
                topLeft = Offset(spaceCenterX - 7.dp.toPx(), spaceCenterY - 2.5.dp.toPx()),
                size = Size(14.dp.toPx(), 5.dp.toPx()),
                cornerRadius = CornerRadius(2f, 2f)
            )

            val stabW = 18.dp.toPx()
            val stabH = 24.dp.toPx()
            drawRoundRect(
                color = Color(0xFF18181B),
                topLeft = Offset(spaceX + 8f, spaceY + 4f),
                size = Size(stabW, stabH),
                cornerRadius = CornerRadius(4f, 4f)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.9f),
                radius = 3.5.dp.toPx(),
                center = Offset(spaceX + 8f + stabW / 2f, spaceY + 4f + stabH / 2f + spaceSink)
            )

            drawRoundRect(
                color = Color(0xFF18181B),
                topLeft = Offset(spaceX + spaceW - 8f - stabW, spaceY + 4f),
                size = Size(stabW, stabH),
                cornerRadius = CornerRadius(4f, 4f)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.9f),
                radius = 3.5.dp.toPx(),
                center = Offset(spaceX + spaceW - 8f - stabW / 2f, spaceY + 4f + stabH / 2f + spaceSink)
            )
        }
    }
}
