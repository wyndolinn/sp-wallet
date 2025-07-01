package com.wynndie.spwallet.sharedCore.presentation.component.carousel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.wynndie.spwallet.sharedCore.presentation.theme.radius
import com.wynndie.spwallet.sharedCore.presentation.theme.size
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing

@Composable
fun <T> CarouselScaffold(
    items: List<T>,
    page: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable (T) -> Unit
) {

    val pagerState = rememberPagerState(initialPage = page) { items.size }

    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = enabled
        ) {
            content(items[it])
        }

        if (items.size > 1) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
            ) {
                items.forEachIndexed { index, _ ->
                    Box(
                        modifier = Modifier
                            .size(MaterialTheme.size.scale(0.33f).extraSmall)
                            .clip(MaterialTheme.radius.circle)
                            .background(
                                color = if (pagerState.currentPage == index) {
                                    MaterialTheme.colorScheme.secondary
                                } else {
                                    MaterialTheme.colorScheme.surfaceContainerHighest
                                }
                            )
                    )
                }
            }
        }
    }
}