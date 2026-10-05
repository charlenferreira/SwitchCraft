package com.example.ui.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ViewSidebar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.model.KeyboardThemePalette

@Composable
fun KeyboardToolbar(
    palette: KeyboardThemePalette,
    isVoiceListening: Boolean,
    incognitoActive: Boolean,
    oneHandedMode: String,
    onVoiceClick: () -> Unit,
    onEmojiClick: () -> Unit,
    onClipboardClick: () -> Unit,
    onTextEditClick: () -> Unit,
    onOneHandedClick: () -> Unit,
    onIncognitoToggle: () -> Unit,
    onLanguageClick: () -> Unit,
    onSwitchKeyboardClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
            .background(palette.baseBackground)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (incognitoActive) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF3F3F46))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Anônimo",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = " Anônimo",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onVoiceClick,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isVoiceListening) Color(0xFFEF4444) else Color.Transparent)
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Ditado por Voz",
                    tint = if (isVoiceListening) Color.White else palette.accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            IconButton(onClick = onEmojiClick, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = Icons.Default.Mood,
                    contentDescription = "Emojis",
                    tint = palette.keyText,
                    modifier = Modifier.size(20.dp)
                )
            }

            IconButton(onClick = onClipboardClick, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = Icons.Default.ContentPaste,
                    contentDescription = "Área de Transferência",
                    tint = palette.keyText,
                    modifier = Modifier.size(20.dp)
                )
            }

            IconButton(onClick = onTextEditClick, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edição de Texto",
                    tint = palette.keyText,
                    modifier = Modifier.size(20.dp)
                )
            }

            IconButton(onClick = onOneHandedClick, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = Icons.Default.ViewSidebar,
                    contentDescription = "Modo Uma Mão",
                    tint = if (oneHandedMode != "OFF") palette.accentColor else palette.keyText,
                    modifier = Modifier.size(20.dp)
                )
            }

            IconButton(onClick = onIncognitoToggle, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "Modo Anônimo",
                    tint = if (incognitoActive) palette.accentColor else palette.keyText,
                    modifier = Modifier.size(20.dp)
                )
            }

            IconButton(onClick = onLanguageClick, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = "Idioma",
                    tint = palette.keyText,
                    modifier = Modifier.size(20.dp)
                )
            }

            IconButton(onClick = onSwitchKeyboardClick, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = Icons.Default.Keyboard,
                    contentDescription = "Trocar Teclado",
                    tint = palette.keyText,
                    modifier = Modifier.size(20.dp)
                )
            }

            IconButton(onClick = onSettingsClick, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Configurações",
                    tint = palette.keyText,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
