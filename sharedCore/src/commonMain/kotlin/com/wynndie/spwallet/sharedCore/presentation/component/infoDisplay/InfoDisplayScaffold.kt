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
    label: @Composable (ColumnScope.() -> Unit)? = null,
    title: @Composable (ColumnScope.() -> Unit)? = null,
    description: @Composable (ColumnScope.() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        label?.let {
            it()

            Spacer(Modifier.height(MaterialTheme.spacing.extraSmall))
        }

        title?.let { it() }

        description?.let { it() }
    }
}