package com.wynndie.spwallet.sharedtheme.designSystem.lists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wynndie.spwallet.sharedtheme.extensions.factor
import com.wynndie.spwallet.sharedtheme.theme.sizing
import com.wynndie.spwallet.sharedtheme.theme.spacing

@Composable
fun <T> BaseCarousel(
    items: List<T>,
    page: Int,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    pageSpacing: Dp = 0.dp,
    enabled: Boolean = true,
    onSwipePage: (Int) -> Unit = {},
    content: @Composable (T) -> Unit
) {

    val pagerState = rememberPagerState(initialPage = page) { items.size }

    LaunchedEffect(pagerState.currentPage) {
        onSwipePage(pagerState.currentPage)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = enabled,
            contentPadding = contentPadding,
            pageSpacing = pageSpacing
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
                            .size(MaterialTheme.sizing.extraSmall.factor(1 / 3f))
                            .clip(CircleShape)
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