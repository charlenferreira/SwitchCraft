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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
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
import com.example.model.KeyboardKey
import com.example.model.KeyboardThemePalette
import com.example.model.KeycapProfile
import com.example.ui.keyboard.KeycapComposable

@Composable
fun VisualSettingsTab(
    settings: KeyboardSettings,
    onSelectProfile: (KeycapProfile) -> Unit,
    onSpringPhysicsChange: (Float) -> Unit,
    onSelectPalette: (String) -> Unit,
    onUppercaseToggle: (Boolean) -> Unit,
    onBlankKeycapsToggle: (Boolean) -> Unit,
    onNumberRowToggle: (Boolean) -> Unit,
    onKeyPreviewToggle: (Boolean) -> Unit,
    onSecondarySymbolsToggle: (Boolean) -> Unit,
    onHeightScaleChange: (Float) -> Unit,
    onBottomPaddingChange: (Int) -> Unit,
    onKeySpacingChange: (Int) -> Unit,
    onRowSpacingChange: (Int) -> Unit,
    onCornerRadiusChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentPalette = KeyboardThemePalette.fromId(settings.paletteId)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Layers, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text(
                    text = " 17 Perfis de Keycaps",
                    fontSize = 18.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = "Renderização procedural em tempo real com curvatura e profundidade de topo.",
                fontSize = 13.sp,
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(KeycapProfile.entries) { profile ->
                    val isSelected = settings.keycapProfile == profile

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .width(105.dp)
                            .clickable { onSelectProfile(profile) }
                            .border(
                                width = if (isSelected) 2.dp else 0.5.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp, 58.dp)
                                    .padding(4.dp)
                            ) {
                                KeycapComposable(
                                    key = KeyboardKey("A", "A", "@"),
                                    settings = settings.copy(keycapProfile = profile),
                                    palette = currentPalette,
                                    rgbColor = Color(settings.rgbAccentColorHex),
                                    isShiftActive = false,
                                    onTap = { onSelectProfile(profile) }
                                )
                            }

                            Text(
                                text = profile.displayName,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                            )
                        }
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Mola da Tecla (Física Elástica)",
                            fontSize = 15.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${(settings.springPhysics * 100).toInt()}%",
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = "Ajusta rigidez (stiffness) e amortecimento (damping) no afundamento e recuo das teclas.",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Slider(
                        value = settings.springPhysics,
                        onValueChange = onSpringPhysicsChange,
                        valueRange = 0f..1f,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Palette, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text(
                    text = " Paletas de Cores Oficiais",
                    fontSize = 18.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = "Tonalidades autênticas inspiradas na cultura de teclados mecânicos customizados.",
                fontSize = 13.sp,
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                KeyboardThemePalette.entries.forEach { paletteItem ->
                    val isSelected = settings.paletteId == paletteItem.id

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectPalette(paletteItem.id) }
                            .border(
                                width = if (isSelected) 2.dp else 0.5.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(1.dp, Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            ) {
                                Box(modifier = Modifier.size(18.dp, 28.dp).background(paletteItem.baseBackground))
                                Box(modifier = Modifier.size(18.dp, 28.dp).background(paletteItem.keyBackground))
                                Box(modifier = Modifier.size(18.dp, 28.dp).background(paletteItem.keySurfaceTop))
                                Box(modifier = Modifier.size(18.dp, 28.dp).background(paletteItem.accentColor))
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 12.dp)
                            ) {
                                Text(
                                    text = paletteItem.displayName,
                                    fontSize = 15.sp,
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = paletteItem.description,
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
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Opções de Estilização",
                        fontSize = 15.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.SemiBold
                    )

                    ToggleItem(
                        title = "Letras maiúsculas nas teclas",
                        subtitle = "Exibe sempre caracteres maiúsculos nas legendas",
                        checked = settings.uppercaseOnKeys,
                        onCheckedChange = onUppercaseToggle
                    )

                    ToggleItem(
                        title = "Teclas sem legenda (Blank Keycaps)",
                        subtitle = "Esconde letras e números para visual limpo de entusiasta",
                        checked = settings.blankKeycaps,
                        onCheckedChange = onBlankKeycapsToggle
                    )

                    ToggleItem(
                        title = "Linha de números fixa no topo",
                        subtitle = "Adiciona a fileira de 1 a 0 acima das letras",
                        checked = settings.numberRow,
                        onCheckedChange = onNumberRowToggle
                    )

                    ToggleItem(
                        title = "Prévia da tecla flutuante",
                        subtitle = "Exibe balão 3D ampliado sobre a tecla ao tocar",
                        checked = settings.keyPreview,
                        onCheckedChange = onKeyPreviewToggle
                    )

                    ToggleItem(
                        title = "Mostrar símbolos secundários",
                        subtitle = "Acessíveis por toque longo no canto da tecla",
                        checked = settings.showSecondarySymbols,
                        onCheckedChange = onSecondarySymbolsToggle
                    )
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SquareFoot, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Text(
                            text = " Geometria e Espaçamento",
                            fontSize = 15.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    SliderItem(
                        title = "Altura do Teclado",
                        valueText = "${(settings.keyboardHeightScale * 100).toInt()}%",
                        value = settings.keyboardHeightScale,
                        range = 0.8f..1.3f,
                        onValueChange = onHeightScaleChange
                    )

                    SliderItem(
                        title = "Espaço Abaixo do Teclado",
                        valueText = "${settings.bottomPaddingDp} dp",
                        value = settings.bottomPaddingDp.toFloat(),
                        range = 0f..40f,
                        onValueChange = { onBottomPaddingChange(it.toInt()) }
                    )

                    SliderItem(
                        title = "Espaçamento Entre Teclas",
                        valueText = "${settings.keySpacingDp} dp",
                        value = settings.keySpacingDp.toFloat(),
                        range = 0f..8f,
                        onValueChange = { onKeySpacingChange(it.toInt()) }
                    )

                    SliderItem(
                        title = "Espaçamento Entre Fileiras",
                        valueText = "${settings.rowSpacingDp} dp",
                        value = settings.rowSpacingDp.toFloat(),
                        range = 0f..8f,
                        onValueChange = { onRowSpacingChange(it.toInt()) }
                    )

                    SliderItem(
                        title = "Cantos Arredondados",
                        valueText = "${(settings.cornerRadiusPercent * 100).toInt()}%",
                        value = settings.cornerRadiusPercent,
                        range = 0f..1f,
                        onValueChange = onCornerRadiusChange
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ToggleItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 14.sp, fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium)
            Text(text = subtitle, fontSize = 12.sp, fontFamily = FontFamily.SansSerif, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun SliderItem(
    title: String,
    valueText: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, fontSize = 13.sp, fontFamily = FontFamily.SansSerif)
            Text(text = valueText, fontSize = 13.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
        Slider(value = value, onValueChange = onValueChange, valueRange = range)
    }
}
