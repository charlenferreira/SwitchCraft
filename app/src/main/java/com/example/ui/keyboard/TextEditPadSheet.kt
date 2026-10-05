package com.example.ui.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.KeyboardThemePalette

@Composable
fun TextEditPadSheet(
    palette: KeyboardThemePalette,
    onMoveCursor: (direction: String) -> Unit,
    onSelectAll: () -> Unit,
    onCut: () -> Unit,
    onCopy: () -> Unit,
    onPaste: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
            .background(palette.baseBackground)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Edição e Navegação de Texto",
                fontSize = 14.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.SemiBold,
                color = palette.keyText
            )
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Fechar", tint = palette.keyText)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilledTonalButton(
                        onClick = onSelectAll,
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = palette.keyBackground),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.SelectAll, contentDescription = "Tudo", tint = palette.keyText, modifier = Modifier.size(18.dp))
                        Text(" Tudo", fontSize = 12.sp, color = palette.keyText, fontFamily = FontFamily.SansSerif)
                    }
                    FilledTonalButton(
                        onClick = onCopy,
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = palette.keyBackground),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copiar", tint = palette.keyText, modifier = Modifier.size(18.dp))
                        Text(" Copiar", fontSize = 12.sp, color = palette.keyText, fontFamily = FontFamily.SansSerif)
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilledTonalButton(
                        onClick = onCut,
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = palette.keyBackground),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.ContentCut, contentDescription = "Recortar", tint = palette.keyText, modifier = Modifier.size(18.dp))
                        Text(" Cortar", fontSize = 12.sp, color = palette.keyText, fontFamily = FontFamily.SansSerif)
                    }
                    FilledTonalButton(
                        onClick = onPaste,
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = palette.keyBackground),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.ContentPaste, contentDescription = "Colar", tint = palette.accentColor, modifier = Modifier.size(18.dp))
                        Text(" Colar", fontSize = 12.sp, color = palette.accentColor, fontFamily = FontFamily.SansSerif)
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { onMoveCursor("UP") },
                    modifier = Modifier.size(40.dp).background(palette.keyBackground, RoundedCornerShape(8.dp))
                ) {
                    Icon(Icons.Default.ArrowUpward, contentDescription = "Cima", tint = palette.keyText)
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    IconButton(
                        onClick = { onMoveCursor("LEFT") },
                        modifier = Modifier.size(40.dp).background(palette.keyBackground, RoundedCornerShape(8.dp))
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Esquerda", tint = palette.keyText)
                    }
                    IconButton(
                        onClick = { onMoveCursor("RIGHT") },
                        modifier = Modifier.size(40.dp).background(palette.keyBackground, RoundedCornerShape(8.dp))
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Direita", tint = palette.keyText)
                    }
                }

                IconButton(
                    onClick = { onMoveCursor("DOWN") },
                    modifier = Modifier.size(40.dp).background(palette.keyBackground, RoundedCornerShape(8.dp))
                ) {
                    Icon(Icons.Default.ArrowDownward, contentDescription = "Baixo", tint = palette.keyText)
                }
            }
        }
    }
}
