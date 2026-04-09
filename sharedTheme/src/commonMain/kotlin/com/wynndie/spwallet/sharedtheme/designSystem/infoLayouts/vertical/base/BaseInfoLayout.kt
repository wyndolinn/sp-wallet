package com.wynndie.spwallet.sharedtheme.designSystem.infoLayouts.vertical.base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun BaseInfoLayout(
    modifier: Modifier = Modifier,
    image: @Composable (ColumnScope.() -> Unit)? = null,
    label: @Composable (ColumnScope.() -> Unit)? = null,
    title: @Composable (ColumnScope.() -> Unit)? = null,
    description: @Composable (ColumnScope.() -> Unit)? = null,
    action: @Composable (ColumnScope.() -> Unit)? = null,
    contentVerticalArrangement: Arrangement.Vertical,
    topContentArrangement: Arrangement.Vertical,
    bodyContentArrangement: Arrangement.Vertical,
    trailingContentArrangement: Arrangement.Vertical
) {
    Column(
        modifier = modifier,
        verticalArrangement = contentVerticalArrangement
    ) {
        Column(
            verticalArrangement = topContentArrangement
        ) {
            image?.let { it() }
            label?.let { it() }
        }

        Column(
            verticalArrangement = bodyContentArrangement
        ) {
            title?.let { it() }
            description?.let { it() }
        }

        Column(
            verticalArrangement = trailingContentArrangement
        ) {
            action?.let { it() }
        }
    }
}