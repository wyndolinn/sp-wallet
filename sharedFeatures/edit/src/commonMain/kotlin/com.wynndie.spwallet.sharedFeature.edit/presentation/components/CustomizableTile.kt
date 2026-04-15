package com.wynndie.spwallet.sharedFeature.edit.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons
import com.wynndie.spwallet.sharedCore.presentation.extensions.asColor
import com.wynndie.spwallet.sharedCore.presentation.extensions.asPainter
import com.wynndie.spwallet.sharedCore.presentation.extensions.cardColorGradient
import com.wynndie.spwallet.sharedCore.presentation.theme.AppTheme
import com.wynndie.spwallet.sharedCore.presentation.theme.sizes
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.edit
import com.wynndie.spwallet.sharedResources.ic_edit
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CustomizableTile(
    color: Color,
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .cardColorGradient(color)
            .clickable(onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(MaterialTheme.spacing.extraSmall)
        ) {
            Image(
                painter = icon,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                modifier = Modifier.size(MaterialTheme.sizes.extraLarge)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(
                space = MaterialTheme.spacing.small,
                alignment = Alignment.End
            ),
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .weight(1f)
                .padding(MaterialTheme.spacing.medium)
        ) {
            Text(
                text = stringResource(Res.string.edit),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Icon(
                painter = painterResource(Res.drawable.ic_edit),
                contentDescription = stringResource(Res.string.edit),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomizableTilePreview() {
    AppTheme {
        CustomizableTile(
            color = CardColors.LIGHT_BLUE.asColor(),
            icon = CardIcons.CASH.asPainter(),
            onClick = {},
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        )
    }
}