package com.wynndie.spwallet.sharedtheme.designSystem.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.wynndie.spwallet.sharedtheme.theme.sizing
import com.wynndie.spwallet.sharedtheme.theme.spacing

@Composable
fun TonalIconButton(
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        FilledTonalButton(
            onClick = onClick,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.sizing.medium)
        ) {
            Image(
                painter = icon,
                contentDescription = text,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                modifier = Modifier.height(MaterialTheme.sizing.small)
            )
        }

        text?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}