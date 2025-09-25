package com.wynndie.spwallet.sharedFeature.edit.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DriveFileRenameOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.wynndie.spwallet.sharedCore.presentation.models.cards.CardUi
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.edit
import com.wynndie.spwallet.sharedtheme.theme.radius
import com.wynndie.spwallet.sharedtheme.theme.size
import com.wynndie.spwallet.sharedtheme.theme.spacing
import org.jetbrains.compose.resources.stringResource

@Composable
fun <T : CardUi> CustomizableTile(
    card: T,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .clip(MaterialTheme.radius.default)
            .background(card.iconBackground.value)
            .clickable(onClick = onClick)
            .padding(MaterialTheme.spacing.small)
            .then(modifier)
    ) {
        Icon(
            imageVector = card.icon.value,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(MaterialTheme.size.extraLarge)
        )

        Spacer(Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            Text(
                text = stringResource(Res.string.edit),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Icon(
                imageVector = Icons.Outlined.DriveFileRenameOutline,
                contentDescription = stringResource(Res.string.edit),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}