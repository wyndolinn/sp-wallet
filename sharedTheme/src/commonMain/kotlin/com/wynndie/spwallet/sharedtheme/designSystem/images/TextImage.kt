package com.wynndie.spwallet.sharedtheme.designSystem.images

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wynndie.spwallet.sharedtheme.designSystem.text.DynamicText

@Composable
fun TextImage(
    text: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = style.color,
    background: Color = Color.Transparent,
    shape: CornerBasedShape = RoundedCornerShape(0.dp)
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(background)
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        DynamicText(
            text = text,
            textAlign = TextAlign.Center,
            style = style,
            color = color
        )
    }
}