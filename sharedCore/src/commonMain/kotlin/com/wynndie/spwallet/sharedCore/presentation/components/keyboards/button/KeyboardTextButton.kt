package com.wynndie.spwallet.sharedCore.presentation.components.keyboards.button

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedtheme.theme.radius

@Composable
fun KeyboardTextButton(
    symbol: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: CornerBasedShape = MaterialTheme.radius.default
) {
    KeyboardButton(
        symbol = {
            Text(
                text = symbol,
                style = MaterialTheme.typography.headlineMedium
            )
        },
        onClick = onClick,
        shape = shape,
        modifier = modifier
    )
}