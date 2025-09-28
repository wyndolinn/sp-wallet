package com.wynndie.spwallet.sharedtheme.designSystem.infoLayouts.vertical

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedtheme.theme.spacing

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
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            image?.let { it() }
            label?.let { it() }
        }

        Column {
            title?.let { it() }
            description?.let { it() }
        }

        action?.let { it() }
    }
}