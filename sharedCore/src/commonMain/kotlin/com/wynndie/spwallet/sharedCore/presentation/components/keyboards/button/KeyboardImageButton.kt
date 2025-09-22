package com.wynndie.spwallet.sharedCore.presentation.components.keyboards.button

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.wynndie.spwallet.sharedCore.presentation.models.KeyboardAction

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