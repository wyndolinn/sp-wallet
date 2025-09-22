package com.wynndie.spwallet.sharedFeature.home.presentation.screen.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.activate
import com.wynndie.spwallet.sharedResources.auth_card_to_get_benefits
import com.wynndie.spwallet.sharedResources.no_authed_cards
import com.wynndie.spwallet.sharedtheme.designSystem.button.BaseButton
import com.wynndie.spwallet.sharedtheme.designSystem.infoPanel.BaseInfoPanelLarge
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

        BaseInfoPanelLarge(
            title = stringResource(Res.string.no_authed_cards),
            description = stringResource(Res.string.auth_card_to_get_benefits),
            action = {
                BaseButton(
                    text = stringResource(Res.string.activate),
                    onClick = onClickAuthCard,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}