package com.wynndie.spwallet.sharedFeature.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.activate
import com.wynndie.spwallet.sharedResources.auth_card_to_get_benefits
import com.wynndie.spwallet.sharedResources.no_authed_cards
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.Button
import com.wynndie.spwallet.sharedCore.presentation.theme.AppTheme
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AuthCardOffer(
    title: String,
    description: String,
    onClickAuthCard: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(MaterialTheme.spacing.medium)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight(600)
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            text = stringResource(Res.string.activate),
            onClick = onClickAuthCard,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthCardOfferPreview() {
    AppTheme {
        AuthCardOffer(
            title = stringResource(Res.string.no_authed_cards),
            description = stringResource(Res.string.auth_card_to_get_benefits),
            onClickAuthCard = { },
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        )
    }
}