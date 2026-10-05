package com.example.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.KeyboardSettings

@Composable
fun TypingTestTab(
    settings: KeyboardSettings,
    isImeEnabled: Boolean,
    isImeSelected: Boolean,
    onEnableImeClick: () -> Unit,
    onSelectImeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var sandboxText by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "SwitchCraft Keyboard",
                fontSize = 22.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Ativação rápida e área de testes para calibrar a resposta mecânica.",
                fontSize = 13.sp,
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isImeSelected) Color(0xFF064E3B).copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.border(
                    width = 1.dp,
                    color = if (isImeSelected) Color(0xFF10B981) else MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(16.dp)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isImeSelected) Icons.Default.CheckCircle else Icons.Default.Warning,
                                contentDescription = null,
                                tint = if (isImeSelected) Color(0xFF10B981) else Color(0xFFF59E0B)
                            )
                            Text(
                                text = if (isImeSelected) " SwitchCraft Ativo e Padrão" else " Configuração Necessária",
                                fontSize = 16.sp,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                color = if (isImeSelected) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Text(
                        text = if (isImeSelected) {
                            "O SwitchCraft está totalmente ativado como seu método de entrada principal. Qualquer campo de texto usará o teclado mecânico!"
                        } else {
                            "Para começar a usar o SwitchCraft no seu Android, conclua as duas etapas abaixo:"
                        },
                        fontSize = 13.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onEnableImeClick,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isImeEnabled) Color(0xFF15803D) else MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                imageVector = if (isImeEnabled) Icons.Default.CheckCircleOutline else Icons.Default.Settings,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (isImeEnabled) " 1. Habilitado" else " 1. Habilitar",
                                fontSize = 12.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                        }

                        Button(
                            onClick = onSelectImeClick,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            enabled = isImeEnabled,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isImeSelected) Color(0xFF15803D) else MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                imageVector = if (isImeSelected) Icons.Default.CheckCircleOutline else Icons.Default.Keyboard,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (isImeSelected) " 2. Selecionado" else " 2. Selecionar",
                                fontSize = 12.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "Setup Atual do Teclado",
                fontSize = 15.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                SuggestionChip(
                    onClick = {},
                    label = { Text("Switch: ${settings.soundProfile.displayName}", fontSize = 11.sp, fontFamily = FontFamily.Monospace) }
                )
                SuggestionChip(
                    onClick = {},
                    label = { Text("Keycap: ${settings.keycapProfile.displayName}", fontSize = 11.sp, fontFamily = FontFamily.Monospace) }
                )
                SuggestionChip(
                    onClick = {},
                    label = { Text("RGB: ${settings.rgbEffect.displayName}", fontSize = 11.sp, fontFamily = FontFamily.Monospace) }
                )
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Text(
                            text = " Área de Teste & Digitação ao Vivo",
                            fontSize = 15.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Text(
                        text = "Toque dentro da caixa de texto abaixo para abrir o SwitchCraft e experimentar a sensação dos switches mecânicos!",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedTextField(
                        value = sandboxText,
                        onValueChange = { sandboxText = it },
                        modifier = Modifier.fillMaxWidth().height(130.dp),
                        placeholder = { Text("Toque aqui para digitar com o SwitchCraft...") },
                        shape = RoundedCornerShape(12.dp)
                    )

                    if (sandboxText.isNotEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            OutlinedButton(
                                onClick = { sandboxText = "" },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Limpar Texto", fontSize = 12.sp, fontFamily = FontFamily.SansSerif)
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
