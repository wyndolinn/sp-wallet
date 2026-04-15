package com.wynndie.spwallet.sharedCore.presentation.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
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
    icon: Painter? = null
) {
    SegmentedButton(
        checked = selected,
        shape = RectangleShape,
        border = BorderStroke(0.dp, Color.Transparent),
        onCheckedChange = onClick,
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.spacing.extraSmall,
            vertical = MaterialTheme.spacing.extraSmall
        ),
        colors = SegmentedButtonDefaults.colors().copy(
            activeContainerColor = MaterialTheme.colorScheme.primaryContainer,
            activeContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            inactiveContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            inactiveContentColor = MaterialTheme.colorScheme.secondary
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
                style = MaterialTheme.typography.labelLarge
            )
        },
        modifier = modifier
            .height(MaterialTheme.sizes.large)
    )
}