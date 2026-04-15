package com.wynndie.spwallet.sharedCore.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.wynndie.spwallet.sharedCore.presentation.formatters.DisplayableOreValue
import com.wynndie.spwallet.sharedCore.presentation.formatters.asFormattedAmount
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.total_balance
import com.wynndie.spwallet.sharedResources.x_of_ore
import com.wynndie.spwallet.sharedCore.presentation.theme.AppTheme
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import org.jetbrains.compose.resources.stringResource

@Composable
fun BalanceComponent(
    balance: DisplayableOreValue,
    modifier: Modifier = Modifier,
    title: String = stringResource(Res.string.total_balance)
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium
        )

        Spacer(Modifier.height(MaterialTheme.spacing.extraSmall))

        Text(
            text = stringResource(Res.string.x_of_ore, balance.value)
                .uppercase()
                .asFormattedAmount(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight(600)
        )

        Text(
            text = if (balance.value != 0L) {
                balance.formatted.asFormattedAmount()
            } else "",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BalanceComponentPreview() {
    AppTheme {
        BalanceComponent(
            balance = DisplayableOreValue.of(123412432134),
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        )
    }
}