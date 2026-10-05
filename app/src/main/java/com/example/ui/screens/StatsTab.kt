package com.example.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TypingStats

@Composable
fun StatsTab(
    stats: TypingStats,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedFilterIndex by remember { mutableIntStateOf(0) }
    val filterLabels = listOf("Semana", "Mês", "Ano", "Sempre")

    val displayedWords = when (selectedFilterIndex) {
        0 -> stats.weeklyWords
        1 -> stats.monthlyWords
        2 -> stats.yearlyWords
        else -> stats.totalWords
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoGraph, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text(
                        text = " Estatísticas Privadas",
                        fontSize = 18.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(16.dp))
                    Text(
                        text = " 100% Local",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = Color(0xFF10B981),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Text(
                text = "Seus hábitos de digitação permanecem exclusivamente na memória do aparelho.",
                fontSize = 13.sp,
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filterLabels.forEachIndexed { idx, label ->
                    FilterChip(
                        selected = selectedFilterIndex == idx,
                        onClick = { selectedFilterIndex = idx },
                        label = { Text(label, fontFamily = FontFamily.SansSerif, fontSize = 12.sp) }
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    title = "Palavras",
                    value = "$displayedWords",
                    icon = Icons.Default.Bolt,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Toques",
                    value = "${stats.totalKeystrokes}",
                    icon = Icons.Default.AutoGraph,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "PPM Médio",
                    value = "${stats.estimatedWpm}",
                    icon = Icons.Default.Speed,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Recordes e Hábitos",
                        fontSize = 15.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.SemiBold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Melhor dia registrado:", fontSize = 13.sp, fontFamily = FontFamily.SansSerif)
                        Text("${stats.bestDayWords} palavras", fontSize = 13.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Sequência de dias ativos:", fontSize = 13.sp, fontFamily = FontFamily.SansSerif)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color(0xFFF97316), modifier = Modifier.size(16.dp))
                            Text(" ${stats.activeDaysStreak} dias", fontSize = 13.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = Color(0xFFF97316))
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Correções aceitas:", fontSize = 13.sp, fontFamily = FontFamily.SansSerif)
                        Text("${stats.acceptedCorrections} vezes", fontSize = 13.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.border(0.5.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Mapa de Calor de Toques (Heatmap)",
                        fontSize = 15.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Distribuição visual das teclas mais acionadas no layout.",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    HeatmapKeyboardCanvas(heatmap = stats.keyHeatmap)
                }
            }
        }

        item {
            Button(
                onClick = { shareStatsCard(context, stats) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Text(
                    text = "  Compartilhar Cartão de Digitação",
                    fontSize = 15.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = title,
                fontSize = 11.sp,
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun HeatmapKeyboardCanvas(heatmap: Map<String, Int>) {
    val maxCount = (heatmap.values.maxOrNull() ?: 1).coerceAtLeast(1)

    val rows = listOf(
        listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"),
        listOf("A", "S", "D", "F", "G", "H", "J", "K", "L", "Ç"),
        listOf("Z", "X", "C", "V", "B", "N", "M")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF18181B))
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        rows.forEach { rowLetters ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                rowLetters.forEach { letter ->
                    val count = heatmap[letter] ?: 0
                    val intensity = (count.toFloat() / maxCount).coerceIn(0.1f, 1.0f)

                    val heatColor = when {
                        intensity > 0.75f -> Color(0xFFEF4444)
                        intensity > 0.50f -> Color(0xFFF59E0B)
                        intensity > 0.25f -> Color(0xFF10B981)
                        else -> Color(0xFF0284C7)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(28.dp)
                            .padding(2.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(heatColor.copy(alpha = 0.25f + intensity * 0.75f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = letter,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

private fun shareStatsCard(context: Context, stats: TypingStats) {
    val text = """
        ⌨️ Minhas Estatísticas no SwitchCraft Keyboard
        --------------------------------------------
        ⚡ Velocidade: ${stats.estimatedWpm} PPM
        📝 Total de Palavras: ${stats.totalWords}
        🔥 Sequência de Dias Ativos: ${stats.activeDaysStreak} dias
        🏆 Recorde Diário: ${stats.bestDayWords} palavras
        🔒 100% Privado e Seguro no Android!
    """.trimIndent()

    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Compartilhar Estatísticas do SwitchCraft")
    context.startActivity(shareIntent)
}
