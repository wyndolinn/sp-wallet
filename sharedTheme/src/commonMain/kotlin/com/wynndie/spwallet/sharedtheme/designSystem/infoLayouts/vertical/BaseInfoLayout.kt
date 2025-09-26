package com.wynndie.spwallet.sharedtheme.designSystem.infoLayouts.vertical

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BaseInfoLayout(
    modifier: Modifier = Modifier,
    image: @Composable (ColumnScope.() -> Unit)? = null,
    label: @Composable (ColumnScope.() -> Unit)? = null,
    title: @Composable (ColumnScope.() -> Unit)? = null,
    description: @Composable (ColumnScope.() -> Unit)? = null,
    action: @Composable (ColumnScope.() -> Unit)? = null
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        image?.let { it() }
        label?.let { it() }
        title?.let { it() }
        description?.let { it() }
        action?.let { it() }
    }
}