package com.wynndie.spwallet.sharedtheme.designSystem.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.wynndie.spwallet.sharedtheme.theme.sizing
import com.wynndie.spwallet.sharedtheme.theme.spacing

@Composable
fun IconButton(
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    enabled: Boolean = true,
    loading: Boolean = false
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        IconButton(
            onClick = onClick,
            enabled = enabled && !loading
        ) {
            Image(
                painter = icon,
                contentDescription = null
            )
        }

        label?.let {
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