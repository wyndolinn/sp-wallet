package com.wynndie.spwallet.sharedCore.presentation.component.keyboard.button

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.wynndie.spwallet.sharedCore.presentation.model.input.KeyboardAction

@Composable
fun KeyboardImageButton(

) {
    KeyboardButton(
        symbol = {
            Text(
                text = KeyboardAction.OnClickOperation.ADD.symbol,
                style = MaterialTheme.typography.headlineMedium
            )
        },
        onClick = {

        }
    )
}