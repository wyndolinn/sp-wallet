package com.wynndie.spwallet.sharedFeature.wallet.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.presentation.component.designSystem.list.VerticalTitledList
import com.wynndie.spwallet.sharedFeature.wallet.presentation.model.UiCard

@Composable
fun <T : UiCard> UiCardList(
    listTitle: String,
    cards: List<T>,
    onClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    trailingContent: @Composable (() -> Unit)? = null
) {
    VerticalTitledList(
        title = listTitle,
        items = cards,
        trailingContent = trailingContent,
        modifier = modifier
    ) { card ->
        UiCardItem(
            icon = card.icon.value,
            iconBackground = card.iconBackground.value,
            label = card.label.asString(),
            title = card.title.asString(),
            description = card.description?.asString(),
            onClick = { onClick(card) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}