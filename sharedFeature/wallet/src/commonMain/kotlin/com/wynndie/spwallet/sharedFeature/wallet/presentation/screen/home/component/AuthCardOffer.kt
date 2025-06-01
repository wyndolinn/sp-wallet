package com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.wynndie.spwallet.sharedCore.presentation.component.button.UiButton
import com.wynndie.spwallet.sharedCore.presentation.component.infoDisplay.LargeInfoDisplay
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.auth
import com.wynndie.spwallet.sharedResources.auth_card_to_get_benefits
import com.wynndie.spwallet.sharedResources.no_authed_cards
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AuthCardOffer(
    onClickAuthCard: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {

        LargeInfoDisplay(
            title = stringResource(Res.string.no_authed_cards),
            description = stringResource(Res.string.auth_card_to_get_benefits),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(MaterialTheme.spacing.medium))

        UiButton(
            text = stringResource(Res.string.auth),
            onClick = onClickAuthCard,
            modifier = Modifier.fillMaxWidth()
        )
    }
}