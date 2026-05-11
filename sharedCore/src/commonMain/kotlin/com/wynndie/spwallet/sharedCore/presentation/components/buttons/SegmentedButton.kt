package com.wynndie.spwallet.sharedCore.presentation.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRowScope
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wynndie.spwallet.sharedCore.presentation.theme.RectangleShape
import com.wynndie.spwallet.sharedCore.presentation.theme.sizes
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing

@Composable
fun MultiChoiceSegmentedButtonRowScope.SegmentedButton(
    label: String,
    selected: Boolean,
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    enabled: Boolean = true
) {
    SegmentedButton(
        checked = selected,
        onCheckedChange = onClick,
        enabled = enabled,
        shape = RectangleShape,
        border = BorderStroke(0.dp, Color.Transparent),
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.spacing.extraExtraSmall,
            vertical = MaterialTheme.spacing.extraExtraSmall
        ),
        colors = SegmentedButtonDefaults.colors().copy(
            activeContainerColor = MaterialTheme.colorScheme.secondary,
            activeContentColor = MaterialTheme.colorScheme.onSecondary,
            inactiveContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            inactiveContentColor = MaterialTheme.colorScheme.onSurface
        ),
        icon = icon?.let {
            {
                Icon(
                    painter = icon,
                    contentDescription = label
                )
            }
        } ?: {},
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(MaterialTheme.sizes.small)
            )
        },
        modifier = modifier
            .height(MaterialTheme.sizes.large)
    )
}