package com.wynndie.spwallet.sharedtheme.designSystem.titledContent.base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedtheme.theme.spacing

@Composable
internal fun BaseTitledContent(
    title: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    titlePadding: PaddingValues = PaddingValues(),
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(titlePadding)
        ) {
            title()
        }

        Box {
            content()
        }
    }
}