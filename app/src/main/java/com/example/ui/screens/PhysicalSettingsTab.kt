package com.example.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.KeyboardSettings

@Composable
fun PhysicalSettingsTab(
    settings: KeyboardSettings,
    onSelectLayout: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var physicalTestText by remember { mutableStateOf("") }

    val layouts = listOf(
        Triple(
            "ABNT2",
            "ABNT / ABNT2 (Brasil)",
            "Mapeamento direto da tecla 'Ç', vírgula decimal no teclado numérico e AltGr funcional (º, ª, §, ², ³, ¹, £, ¢, ¬, /, ?)."
        ),
        Triple(
            "US_INTL",
            "US-International (Dead Keys)",
            "Buffer de 1 estado para acentos: ' + c = ç, ' + a = á, ~ + a = ã, ^ + e = ê, ` + a = à. Espaço emite o acento isolado."
        ),
        Triple(
            "ISO_EUROPEAN",
            "Europeu / ISO (UK, ES, PT)",
            "Suporte à tecla física extra entre Shift esquerdo e Z (< e >), além de acentuação européia e tremas."
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Usb, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text(
                    text = " Suporte a Teclados Físicos (USB / Bluetooth)",
                    fontSize = 18.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = "Conecte qualquer teclado mecânico externo. O motor do SwitchCraft intercepta os eventos em baixo nível com som e vibração!",
                fontSize = 13.sp,
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        item {
            Text(
                text = "Layout Físico Ativo",
                fontSize = 16.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        items(layouts.size) { index ->
            val (id, title, desc) = layouts[index]
            val isSelected = settings.physicalLayout == id

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectLayout(id) }
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
                            text = title,
                            fontSize = 15.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = desc,
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
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Keyboard, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Text(
                            text = " Área de Teste de Teclado Físico",
                            fontSize = 15.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = "Foque no campo abaixo e digite com seu teclado USB ou Bluetooth para validar acentos, AltGr e 'Ç'.",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = physicalTestText,
                        onValueChange = { physicalTestText = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Digite com seu teclado físico aqui...") },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.border(0.5.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Memory, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Text(
                            text = " Tabela de Dead Keys Inteligentes",
                            fontSize = 14.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text("• Aspas simples + c = ç / Ç", fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                    Text("• Aspas simples + vogal = á, é, í, ó, ú", fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                    Text("• Til (~) + a/o = ã, õ", fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                    Text("• Circunflexo (^) + vogal = â, ê, î, ô, û", fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                    Text("• Crase (`) + a = à", fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                    Text("• Aspas duplas (\") + vogal = ä, ë, ï, ö, ü (Trema)", fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                    Text("• Tecla de acento + Espaço = caractere puro isolado", fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
