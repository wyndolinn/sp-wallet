package com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.cash_card.component

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
import com.wynndie.spwallet.sharedCore.presentation.theme.radius
import com.wynndie.spwallet.sharedCore.presentation.theme.size
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedFeature.wallet.presentation.model.UiCard
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.edit
import com.wynndie.spwallet.sharedResources.no_name
import org.jetbrains.compose.resources.stringResource

@Composable
fun <T : UiCard> CustomizableCard(
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
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            Text(
                text = card.label.asString().ifBlank { stringResource(Res.string.no_name) },
                style = MaterialTheme.typography.titleMedium,
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