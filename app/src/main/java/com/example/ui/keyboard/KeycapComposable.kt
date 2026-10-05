package com.example.ui.keyboard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.KeyboardSettings
import com.example.model.KeyType
import com.example.model.KeyboardKey
import com.example.model.KeyboardThemePalette
import com.example.model.KeycapProfile

@Composable
fun KeycapComposable(
    key: KeyboardKey,
    settings: KeyboardSettings,
    palette: KeyboardThemePalette,
    rgbColor: Color,
    isShiftActive: Boolean,
    modifier: Modifier = Modifier,
    onTap: () -> Unit,
    onLongPress: (() -> Unit)? = null
) {
    var isPressed by remember { mutableStateOf(false) }

    val springValue = settings.springPhysics.coerceIn(0.05f, 1.0f)
    val damping = 0.50f + (1.0f - springValue) * 0.45f
    val stiffness = 1200f - (springValue * 800f)

    val pressOffset by animateFloatAsState(
        targetValue = if (isPressed) 1.0f else 0.0f,
        animationSpec = spring(dampingRatio = damping, stiffness = stiffness),
        label = "keycap_spring_press"
    )

    val profile = settings.keycapProfile
    val isFunctionKey = key.type != KeyType.CHARACTER && key.type != KeyType.SPACE

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(key) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onTap() },
                    onLongPress = { onLongPress?.invoke() ?: onTap() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            if (w <= 0f || h <= 0f) return@Canvas

            if (settings.rgbBrightness > 0.05f) {
                val underglowAlpha = settings.rgbBrightness * 0.70f * (if (isPressed) 1.0f else 0.65f)
                drawRoundRect(
                    color = rgbColor.copy(alpha = underglowAlpha),
                    topLeft = Offset(-1f, 1f),
                    size = Size(w + 2f, h + 2f),
                    cornerRadius = CornerRadius(16f, 16f)
                )
            }

            val sinkDepth = 6.0f * (1.0f - pressOffset) * profile.heightRatio
            val baseCornerRadius = (h * 0.22f * settings.cornerRadiusPercent).coerceIn(4f, w / 2f)

            drawRoundRect(
                color = Color.Black.copy(alpha = 0.45f * (1.0f - pressOffset * 0.5f)),
                topLeft = Offset(0f, 4f + sinkDepth * 0.5f),
                size = Size(w, h - 4f),
                cornerRadius = CornerRadius(baseCornerRadius, baseCornerRadius)
            )

            val bodyColor = if (isFunctionKey) palette.functionKeyBackground else palette.keyBackground
            val pressedBodyColor = Color(
                red = (bodyColor.red * 0.82f).coerceIn(0f, 1f),
                green = (bodyColor.green * 0.82f).coerceIn(0f, 1f),
                blue = (bodyColor.blue * 0.82f).coerceIn(0f, 1f),
                alpha = bodyColor.alpha
            )

            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        if (isPressed) pressedBodyColor else bodyColor,
                        bodyColor.copy(alpha = 0.95f),
                        Color.Black.copy(alpha = 0.25f)
                    ),
                    startY = 0f,
                    endY = h
                ),
                topLeft = Offset(0f, sinkDepth),
                size = Size(w, h - sinkDepth),
                cornerRadius = CornerRadius(baseCornerRadius, baseCornerRadius)
            )

            val bevelInsetX = (w * profile.bevelRatio).coerceIn(2f, w * 0.35f)
            val bevelInsetY = (h * profile.bevelRatio).coerceIn(2f, h * 0.35f)
            val topWidth = (w - bevelInsetX * 2).coerceAtLeast(4f)
            val topHeight = (h - bevelInsetY * 2 - sinkDepth).coerceAtLeast(4f)
            val topTopY = bevelInsetY + sinkDepth * 0.85f

            val topCorner = if (profile == KeycapProfile.REDONDA) {
                topWidth / 2f
            } else if (profile == KeycapProfile.PILULA) {
                topHeight / 2f
            } else {
                (baseCornerRadius * (1f - profile.bevelRatio)).coerceAtLeast(2f)
            }

            val topSurfaceColor = if (isFunctionKey) palette.functionKeyBackground else palette.keySurfaceTop

            if (profile.isPudding) {
                drawRoundRect(
                    color = rgbColor.copy(alpha = 0.35f),
                    topLeft = Offset(0f, h * 0.45f),
                    size = Size(w, h * 0.55f),
                    cornerRadius = CornerRadius(baseCornerRadius, baseCornerRadius)
                )
            }

            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        topSurfaceColor,
                        if (profile.isSpherical) topSurfaceColor.copy(alpha = 0.88f) else topSurfaceColor
                    ),
                    startY = topTopY,
                    endY = topTopY + topHeight
                ),
                topLeft = Offset(bevelInsetX, topTopY),
                size = Size(topWidth, topHeight),
                cornerRadius = CornerRadius(topCorner, topCorner)
            )

            if (profile.topCurvature > 0.05f) {
                drawLine(
                    color = Color.White.copy(alpha = 0.22f * (1f - pressOffset)),
                    start = Offset(bevelInsetX + topCorner * 0.5f, topTopY + 1f),
                    end = Offset(w - bevelInsetX - topCorner * 0.5f, topTopY + 1f),
                    strokeWidth = 1.2f
                )
            }

            drawRoundRect(
                color = palette.previewBorderColor.copy(alpha = 0.30f),
                topLeft = Offset(bevelInsetX, topTopY),
                size = Size(topWidth, topHeight),
                cornerRadius = CornerRadius(topCorner, topCorner),
                style = Stroke(width = 0.8f)
            )
        }

        if (!settings.blankKeycaps || isFunctionKey) {
            val labelText = when {
                key.type == KeyType.SPACE -> ""
                key.type == KeyType.CHARACTER -> {
                    if (isShiftActive || settings.uppercaseOnKeys) {
                        key.primaryLabel.uppercase()
                    } else {
                        key.primaryLabel.lowercase()
                    }
                }
                else -> key.primaryLabel
            }

            val baseTextColor = if (isFunctionKey) {
                val tint = settings.functionKeyTint
                Color(
                    red = (palette.functionKeyText.red * (1f - tint) + palette.accentColor.red * tint),
                    green = (palette.functionKeyText.green * (1f - tint) + palette.accentColor.green * tint),
                    blue = (palette.functionKeyText.blue * (1f - tint) + palette.accentColor.blue * tint),
                    alpha = 1f
                )
            } else {
                palette.keyText
            }

            if (settings.showSecondarySymbols && key.secondaryLabel != null && key.type == KeyType.CHARACTER) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 4.dp, end = 6.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Text(
                        text = key.secondaryLabel,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        color = baseTextColor.copy(alpha = 0.55f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (labelText.isNotEmpty()) {
                Text(
                    text = labelText,
                    fontSize = if (labelText.length > 2) 11.sp else 16.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = if (isFunctionKey) FontWeight.SemiBold else FontWeight.Normal,
                    color = baseTextColor
                )
            }
        }
    }
}
