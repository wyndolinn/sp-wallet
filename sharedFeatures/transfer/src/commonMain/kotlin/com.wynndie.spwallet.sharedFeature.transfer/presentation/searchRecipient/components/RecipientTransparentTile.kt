package com.wynndie.spwallet.sharedFeature.transfer.presentation.searchRecipient.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.ic_arrow_right
import com.wynndie.spwallet.sharedResources.ic_person
import com.wynndie.spwallet.sharedResources.recipient
import com.wynndie.spwallet.sharedtheme.extensions.thenIfNotNull
import com.wynndie.spwallet.sharedtheme.theme.AppTheme
import com.wynndie.spwallet.sharedtheme.theme.sizing
import com.wynndie.spwallet.sharedtheme.theme.spacing
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun RecipientTransparentTile(
    icon: Painter,
    color: Color,
    cardNumber: String,
    cardOwner: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconColor = MaterialTheme.colorScheme.primary
    val minContentHeight = MaterialTheme.sizing.medium
    Row(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        modifier = modifier
            .thenIfNotNull(onClick) { Modifier.clickable(onClick = it) }
            .padding(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.small
            )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(minContentHeight)
                .clip(CircleShape)
                .background(iconColor.copy(alpha = 0.2f))
        ) {
            Image(
                painter = icon,
                colorFilter = ColorFilter.tint(iconColor),
                contentDescription = null
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(
                space = 0.dp,
                alignment = Alignment.CenterVertically
            ),
            modifier = Modifier
                .heightIn(min = minContentHeight)
                .weight(1f)
        ) {
            Text(
                text = cardOwner,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = cardNumber,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight(600)
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(minContentHeight)
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_arrow_right),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
                contentDescription = null
            )
        }
    }
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