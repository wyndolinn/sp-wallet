package com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.empty_profile
import com.wynndie.spwallet.sharedCore.presentation.extensions.asFormattedAmount
import com.wynndie.spwallet.sharedCore.presentation.formatters.DisplayableOreValue
import com.wynndie.spwallet.sharedCore.presentation.theme.AppTheme
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedCore.x_of_ore
import org.jetbrains.compose.resources.stringResource

@Composable
fun BalanceComponent(
    balance: DisplayableOreValue,
    hasCards: Boolean,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start
) {
    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
    ) {
        Text(
            text = if (hasCards) {
                stringResource(Res.string.x_of_ore, balance.value)
                    .asFormattedAmount().uppercase()
            } else {
                stringResource(Res.string.empty_profile)
            },
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        if (balance.formatted.isNotBlank()) {
            Text(
                text = balance.formatted.asFormattedAmount(),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BalanceComponentPreview() {
    AppTheme {
        BalanceComponent(
            balance = DisplayableOreValue.of(10),
            hasCards = true,
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        )
    }
}