package com.example.ui.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.KeyboardThemePalette

data class EmojiCategory(
    val name: String,
    val icon: String,
    val emojis: List<String>
)

val EMOJI_CATEGORIES = listOf(
    EmojiCategory(
        name = "Sorrisos",
        icon = "😀",
        emojis = listOf("😀","😃","😄","😁","😆","😅","😂","🤣","🥲","🥹","😊","😇","🙂","🙃","😉","😌","😍","🥰","😘","😗","😙","😚","😋","😛","😝","😜","🤪","🤨","🧐","🤓","😎","🤩","🥳","😏","😒","😞","😔","😟","😕","🙁","☹️","😣","😖","😫","😩","🥺","😢","😭","😮‍💨","😤","😠","😡","🤬","🤯","😳","🥵","🥶","😱","😨","😰","😥","😓","🤗","🤔","🤫","🤥","😶","😐","😑","😬","🫠","🙄","😯","😦","😧","😮","😲","🥱","😴","🤤","😪","😵","🤐","🥴","🤢","🤮","🤧","😷","🤒","🤕","🤑","🤠")
    ),
    EmojiCategory(
        name = "Gestos",
        icon = "👍",
        emojis = listOf("👋","🤚","🖐️","✋","🖖","👌","🤌","🤏","✌️","🤞","🫰","🤟","🤘","🤙","👈","👉","👆","👇","☝️","👍","👎","✊","👊","🤛","🤜","👏","🙌","🫶","👐","🤲","🤝","🙏","✍️","💅","🤳","💪","👂","👃","👀","👁️","👅","👄")
    ),
    EmojiCategory(
        name = "Natureza",
        icon = "🌿",
        emojis = listOf("🐶","🐱","🐭","🐹","🐰","🦊","🐻","🐼","🐨","🐯","🦁","🐮","🐷","🐸","🐵","🐔","🐧","🐦","🐤","🦆","🦅","🦉","🦇","🐺","🐗","🐴","🦄","🐝","🐛","🦋","🐌","🐞","🐜","🪴","🌲","🌳","🌴","🌵","🌾","🌿","☘️","🍀","🍁","🍂","🍃","🍄","🌸","🌺","🌹","🌻","🌼")
    ),
    EmojiCategory(
        name = "Comida",
        icon = "🍔",
        emojis = listOf("🍏","🍎","🍐","🍊","🍋","🍌","🍉","🍇","🍓","🫐","🍈","🍒","🍑","🥭","🍍","🥥","🥝","🍅","🥑","🥦","🥬","🥒","🌶️","🌽","🥕","🥔","🥐","🥯","🍞","🥖","🥨","🧀","🥚","🍳","🧈","🥞","🧇","🥓","🥩","🍗","🍖","🌭","🍔","🍟","🍕","🥪","🌮","🌯","🥗","🥘","🍝","🍜","🍲","🍣","🍱","☕","🍵","🧋","🥤","🍺","🍻","🍷")
    ),
    EmojiCategory(
        name = "Objetos",
        icon = "💡",
        emojis = listOf("💻","🖥️","⌨️","🖱️","💽","💾","💿","📀","📱","📲","☎️","📞","📟","🔋","🔌","💡","🔦","🕯️","💸","💵","💰","💳","💎","⚖️","🪜","🧰","🪛","🔧","🔨","🛠️","⛏️","⚙️","⛓️","🧲","🔫","💣","🧨","🔪","🗡️","⚔️","🛡️")
    )
)

@Composable
fun EmojiPickerSheet(
    palette: KeyboardThemePalette,
    onEmojiSelected: (String) -> Unit,
    onBackspace: () -> Unit,
    onClose: () -> Unit
) {
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .background(palette.baseBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ScrollableTabRow(
                selectedTabIndex = selectedCategoryIndex,
                modifier = Modifier.weight(1f),
                containerColor = Color.Transparent,
                contentColor = palette.accentColor,
                edgePadding = 4.dp
            ) {
                EMOJI_CATEGORIES.forEachIndexed { index, category ->
                    Tab(
                        selected = selectedCategoryIndex == index,
                        onClick = { selectedCategoryIndex = index },
                        text = { Text(text = category.icon, fontSize = 18.sp) }
                    )
                }
            }

            IconButton(onClick = onBackspace) {
                Icon(Icons.Default.Backspace, contentDescription = "Apagar", tint = palette.keyText)
            }

            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Fechar", tint = palette.keyText)
            }
        }

        val currentCategory = EMOJI_CATEGORIES[selectedCategoryIndex]
        LazyVerticalGrid(
            columns = GridCells.Fixed(8),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            items(currentCategory.emojis) { emoji ->
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onEmojiSelected(emoji) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = emoji, fontSize = 22.sp, fontFamily = FontFamily.SansSerif)
                }
            }
        }
    }
}
