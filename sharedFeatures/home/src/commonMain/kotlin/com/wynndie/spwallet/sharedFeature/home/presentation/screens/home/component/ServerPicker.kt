package com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.wynndie.spwallet.sharedCore.domain.models.Servers
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.SegmentedButton
import com.wynndie.spwallet.sharedCore.presentation.extensions.then
import com.wynndie.spwallet.sharedCore.presentation.theme.sizes
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing

@Composable
fun ServerPicker(
    onClick: (Servers) -> Unit,
    selectedServer: Servers,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    MultiChoiceSegmentedButtonRow(
        space = 0.dp,
        modifier = modifier
            .height(MaterialTheme.sizes.small)
            .padding(horizontal = MaterialTheme.spacing.medium)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .then {
                val color = if (!enabled) {
                    MaterialTheme.colorScheme.primaryContainer
                } else MaterialTheme.colorScheme.primary

                Modifier.border(
                    width = 1.dp,
                    color = color,
                    shape = CircleShape,
                )
            },
    ) {
        Servers.entries.forEach { server ->
            SegmentedButton(
                label = server.label,
                selected = server == selectedServer,
                onClick = { onClick(server) },
                enabled = enabled,
                modifier = Modifier.fillMaxHeight(),
            )
        }
    }
}