package com.wynndie.spwallet.sharedFeature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.auth_card_to_get_benefits
import com.wynndie.spwallet.sharedCore.no_authed_cards
import com.wynndie.spwallet.sharedCore.presentation.theme.AppTheme
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AuthCardOffer(
    title: String,
    description: String,
    content: @Composable (() -> Unit)? = null,
    shape: CornerBasedShape = MaterialTheme.shapes.medium,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(MaterialTheme.spacing.medium)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraExtraSmall)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall
            )
        }

        content?.let {it ()}
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthCardOfferPreview() {
    AppTheme {
        AuthCardOffer(
            title = stringResource(Res.string.no_authed_cards),
            description = stringResource(Res.string.auth_card_to_get_benefits),
            content = { },
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        )
    }
}