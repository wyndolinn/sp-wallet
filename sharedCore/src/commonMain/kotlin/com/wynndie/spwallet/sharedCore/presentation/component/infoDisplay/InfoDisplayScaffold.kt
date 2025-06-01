package com.wynndie.spwallet.sharedCore.presentation.component.infoDisplay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing

@Composable
fun InfoDisplayScaffold(
    modifier: Modifier = Modifier,
    topContent: @Composable (ColumnScope.() -> Unit)? = null,
    label: @Composable (ColumnScope.() -> Unit)? = null,
    title: @Composable (ColumnScope.() -> Unit)? = null,
    description: @Composable (ColumnScope.() -> Unit)? = null
) {
    Column(
        modifier = modifier
    ) {
        topContent?.let {
            it()

            Spacer(Modifier.height(MaterialTheme.spacing.medium))
        }

        label?.let {
            it()

            Spacer(Modifier.height(MaterialTheme.spacing.extraSmall))
        }

        title?.let { it() }

        description?.let { it() }
    }
}