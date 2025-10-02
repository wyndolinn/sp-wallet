package com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.searchRecipient.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.ic_arrow_right
import com.wynndie.spwallet.sharedResources.ic_person
import com.wynndie.spwallet.sharedResources.recipient
import com.wynndie.spwallet.sharedtheme.designSystem.tiles.horizontal.HorizontalTileMedium
import com.wynndie.spwallet.sharedtheme.theme.AppTheme
import com.wynndie.spwallet.sharedtheme.theme.spacing
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun RecipientTransparentTile(
    icon: Painter,
    color: Color,
    cardNumber: String,
    cardOwner: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HorizontalTileMedium(
        leadingContent = {
            Image(
                painter = icon,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                contentDescription = null
            )
        },
        leadingContentBackground = color,
        leadingContentShape = CircleShape,
        title = cardOwner,
        description = cardNumber,
        trailingContent = {
            Image(
                painter = painterResource(Res.drawable.ic_arrow_right),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                contentDescription = null
            )
        },
        onClick = onClick,
        contentPadding = PaddingValues(
            start = MaterialTheme.spacing.medium,
            top = MaterialTheme.spacing.small,
            end = MaterialTheme.spacing.extraSmall,
            bottom = MaterialTheme.spacing.small
        ),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun RecipientTransparentTilePreview() {
    AppTheme {
        RecipientTransparentTile(
            icon = painterResource(Res.drawable.ic_person),
            color = MaterialTheme.colorScheme.primary,
            cardNumber = "55555",
            cardOwner = stringResource(Res.string.recipient),
            onClick = {},
            modifier = Modifier
        )
    }
}