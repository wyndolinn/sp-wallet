package com.wynndie.spwallet.sharedCore.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.auth_card
import com.wynndie.spwallet.sharedCore.auth_card_info
import com.wynndie.spwallet.sharedCore.presentation.extensions.module
import com.wynndie.spwallet.sharedCore.presentation.theme.AppTheme
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import org.jetbrains.compose.resources.stringResource

@Composable
fun InformationModule(
    title: String,
    modifier: Modifier = Modifier,
    actions: @Composable (() -> Unit)? = null,
    content: @Composable (ColumnScope.() -> Unit)? = null
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        modifier = modifier
            .module()
            .padding(MaterialTheme.spacing.medium),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )

            content?.let {
                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraExtraSmall),
                    content = it,
                )
            }
        }

        actions?.let { it() }
    }
}

@Preview(showBackground = true)
@Composable
private fun InformationModulePreview() {
    AppTheme {
        InformationModule(
            title = stringResource(Res.string.auth_card),
            content = {
                Text(
                    text = stringResource(Res.string.auth_card_info),
                )
            },
            modifier = Modifier.padding(MaterialTheme.spacing.medium),
        )
    }
}