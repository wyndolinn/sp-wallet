package com.wynndie.spwallet.sharedtheme.designSystem.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.isUnspecified

@Composable
fun DynamicText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = style.color
) {

    val defaultFontSize = MaterialTheme.typography.bodyMedium.fontSize
    var resizedTextStyle by remember { mutableStateOf(style) }
    var shouldDraw by remember { mutableStateOf(false) }

    Text(
        text = text,
        color = color,
        textAlign = textAlign,
        softWrap = false,
        maxLines = maxLines,
        style = resizedTextStyle,
        onTextLayout = { result ->
            if (result.didOverflowWidth || result.didOverflowHeight) {
                shouldDraw = false
                if (style.fontSize.isUnspecified) resizedTextStyle.copy(fontSize = defaultFontSize)
                resizedTextStyle.copy(fontSize = resizedTextStyle.fontSize * 0.9)
            } else {
                shouldDraw = true
            }
        },
        modifier = modifier.drawWithContent {
            if (shouldDraw) drawContent()
        }
    )
}