package com.wynndie.spwallet.sharedCore.presentation.component.tile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.presentation.component.designSystem.carousel.CarouselScaffold
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedCore.presentation.model.UiCard

@Composable
fun <T : UiCard> UiCardCarousel(
    items: List<T>,
    page: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: ((T) -> Unit)? = null
) {
    CarouselScaffold(
        items = items,
        page = page,
        enabled = enabled,
        modifier = modifier
    ) { card ->
        Box(
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
        ) {
            UiCardItem(
                icon = card.icon.value,
                iconBackground = card.iconBackground.value,
                label = card.label.asString(),
                title = card.title.asString(),
                description = card.description?.asString(),
                onClick = onClick?.let { { it(card) } },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}