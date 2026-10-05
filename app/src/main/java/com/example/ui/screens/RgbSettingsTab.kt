package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.KeyboardSettings
import com.example.model.RgbEffect

val ACCENT_PALETTE_COLORS = listOf(
    Pair("Ciano Neon", 0xFF00E5FF),
    Pair("Verde Esmeralda", 0xFF10B981),
    Pair("Branco Estelar", 0xFFFFFFFF),
    Pair("Vermelho Fogo", 0xFFEF4444),
    Pair("Laranja Chama", 0xFFF97316),
    Pair("Amarelo Ouro", 0xFFFACC15),
    Pair("Azul Deep", 0xFF3B82F6),
    Pair("Roxo Cibernético", 0xFFA855F7),
    Pair("Rosa Choque", 0xFFEC4899),
    Pair("Lima Tech", 0xFF84CC16),
    Pair("Âmbar Quente", 0xFFF59E0B),
    Pair("Índigo Escuro", 0xFF6366F1)
)

@Composable
fun RgbSettingsTab(
    settings: KeyboardSettings,
    onRgbEffectSelect: (RgbEffect) -> Unit,
    onBrightnessChange: (Float) -> Unit,
    onFunctionKeyTintChange: (Float) -> Unit,
    onAccentColorSelect: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Flare, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text(
                    text = " Iluminação RGB Mecânica",
                    fontSize = 18.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = "13 modos de iluminação dinâmica com underglow sob a base e legendas pulsantes.",
                fontSize = 13.sp,
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Brilho da Iluminação", fontSize = 14.sp, fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold)
                            Text(text = "${(settings.rgbBrightness * 100).toInt()}%", fontSize = 14.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                        Slider(value = settings.rgbBrightness, onValueChange = onBrightnessChange, valueRange = 0f..1f)
                    }

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Pintura das Teclas de Função", fontSize = 14.sp, fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold)
                            Text(text = "${(settings.functionKeyTint * 100).toInt()}%", fontSize = 14.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                        Text(
                            text = "Intensidade de cor aplicada nas legendas de Shift, Backspace, Enter e Space",
                            fontSize = 12.sp,
                            fontFamily = FontFamily.SansSerif,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Slider(value = settings.functionKeyTint, onValueChange = onFunctionKeyTintChange, valueRange = 0f..1f)
                    }
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.InvertColors, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Text(
                            text = " Cor de Acento e Modos Reativos",
                            fontSize = 15.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = "Cor base utilizada nos modos Estático, Reativo, Pingo d'água e Respiração.",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ACCENT_PALETTE_COLORS.take(6).forEach { (_, hex) ->
                            val isSelected = settings.rgbAccentColorHex == hex
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(hex))
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.onSurface else Color.Black.copy(alpha = 0.3f),
                                        shape = CircleShape
                                    )
                                    .clickable { onAccentColorSelect(hex) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ACCENT_PALETTE_COLORS.drop(6).forEach { (_, hex) ->
                            val isSelected = settings.rgbAccentColorHex == hex
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(hex))
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.onSurface else Color.Black.copy(alpha = 0.3f),
                                        shape = CircleShape
                                    )
                                    .clickable { onAccentColorSelect(hex) }
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "13 Modos de Iluminação RGB",
                fontSize = 18.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        items(RgbEffect.entries) { effect ->
            val isSelected = settings.rgbEffect == effect

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onRgbEffectSelect(effect) }
                    .border(
                        width = if (isSelected) 2.dp else 0.5.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = effect.displayName,
                            fontSize = 15.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = effect.description,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.SansSerif,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (isSelected) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
