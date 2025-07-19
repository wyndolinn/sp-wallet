package com.wynndie.spwallet.sharedCore.presentation.component.keyboard.calculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.presentation.component.keyboard.button.KeyboardTextButton
import com.wynndie.spwallet.sharedCore.presentation.model.KeyboardAction
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing

@Composable
fun CalculatorKeyboard(
    onAction: (KeyboardAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardButtonSpacing = MaterialTheme.spacing.small

    Column(
        verticalArrangement = Arrangement.spacedBy(keyboardButtonSpacing),
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(keyboardButtonSpacing),
            modifier = Modifier.fillMaxWidth()
        ) {
            KeyboardTextButton(
                symbol = KeyboardAction.OnClickOperation.ADD.symbol,
                onClick = { onAction(KeyboardAction.OnClickOperation.ADD) },
                modifier = Modifier.weight(1f)
            )

            KeyboardTextButton(
                symbol = KeyboardAction.OnClickNumber(symbol = "1").symbol,
                onClick = { onAction(KeyboardAction.OnClickNumber(symbol = "1")) },
                modifier = Modifier.weight(1f)
            )

            KeyboardTextButton(
                symbol = KeyboardAction.OnClickNumber(symbol = "2").symbol,
                onClick = { onAction(KeyboardAction.OnClickNumber(symbol = "2")) },
                modifier = Modifier.weight(1f)
            )

            KeyboardTextButton(
                symbol = KeyboardAction.OnClickNumber(symbol = "3").symbol,
                onClick = { onAction(KeyboardAction.OnClickNumber(symbol = "3")) },
                modifier = Modifier.weight(1f)
            )

            KeyboardTextButton(
                symbol = KeyboardAction.OnClickModifier.OPEN_PARENTHESES.symbol,
                onClick = { onAction(KeyboardAction.OnClickModifier.OPEN_PARENTHESES) },
                modifier = Modifier.weight(1f)
            )
        }


        Row(
            horizontalArrangement = Arrangement.spacedBy(keyboardButtonSpacing),
            modifier = Modifier.fillMaxWidth()
        ) {
            KeyboardTextButton(
                symbol = KeyboardAction.OnClickOperation.SUBTRACT.symbol,
                onClick = { onAction(KeyboardAction.OnClickOperation.SUBTRACT) },
                modifier = Modifier.weight(1f)
            )

            KeyboardTextButton(
                symbol = KeyboardAction.OnClickNumber(symbol = "4").symbol,
                onClick = { onAction(KeyboardAction.OnClickNumber(symbol = "4")) },
                modifier = Modifier.weight(1f)
            )

            KeyboardTextButton(
                symbol = KeyboardAction.OnClickNumber(symbol = "5").symbol,
                onClick = { onAction(KeyboardAction.OnClickNumber(symbol = "5")) },
                modifier = Modifier.weight(1f)
            )

            KeyboardTextButton(
                symbol = KeyboardAction.OnClickNumber(symbol = "6").symbol,
                onClick = { onAction(KeyboardAction.OnClickNumber(symbol = "6")) },
                modifier = Modifier.weight(1f)
            )

            KeyboardTextButton(
                symbol = KeyboardAction.OnClickModifier.CLOSE_PARENTHESES.symbol,
                onClick = { onAction(KeyboardAction.OnClickModifier.CLOSE_PARENTHESES) },
                modifier = Modifier.weight(1f)
            )
        }


        Row(
            horizontalArrangement = Arrangement.spacedBy(keyboardButtonSpacing),
            modifier = Modifier.fillMaxWidth()
        ) {
            KeyboardTextButton(
                symbol = KeyboardAction.OnClickOperation.MULTIPLY.symbol,
                onClick = { onAction(KeyboardAction.OnClickOperation.MULTIPLY) },
                modifier = Modifier.weight(1f)
            )

            KeyboardTextButton(
                symbol = KeyboardAction.OnClickNumber(symbol = "7").symbol,
                onClick = { onAction(KeyboardAction.OnClickNumber(symbol = "7")) },
                modifier = Modifier.weight(1f)
            )

            KeyboardTextButton(
                symbol = KeyboardAction.OnClickNumber(symbol = "8").symbol,
                onClick = { onAction(KeyboardAction.OnClickNumber(symbol = "8")) },
                modifier = Modifier.weight(1f)
            )

            KeyboardTextButton(
                symbol = KeyboardAction.OnClickNumber(symbol = "9").symbol,
                onClick = { onAction(KeyboardAction.OnClickNumber(symbol = "9")) },
                modifier = Modifier.weight(1f)
            )

            KeyboardTextButton(
                symbol = KeyboardAction.OnClickClearAll(symbol = "<-").symbol,
                onClick = { onAction(KeyboardAction.OnClickClearAll(symbol = "<-")) },
                modifier = Modifier.weight(1f)
            )
        }


        Row(
            horizontalArrangement = Arrangement.spacedBy(keyboardButtonSpacing),
            modifier = Modifier.fillMaxWidth()
        ) {
            KeyboardTextButton(
                symbol = KeyboardAction.OnClickOperation.DIVIDE.symbol,
                onClick = { onAction(KeyboardAction.OnClickOperation.DIVIDE) },
                modifier = Modifier.weight(1f)
            )

            KeyboardTextButton(
                symbol = KeyboardAction.OnClickNumber(symbol = "0").symbol,
                onClick = { onAction(KeyboardAction.OnClickNumber(symbol = "0")) },
                modifier = Modifier.weight(3f)
            )

            KeyboardTextButton(
                symbol = KeyboardAction.OnClickModifier.CALCULATE.symbol,
                onClick = { onAction(KeyboardAction.OnClickModifier.CALCULATE) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}