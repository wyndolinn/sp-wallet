package com.wynndie.spwallet.sharedCore.presentation.component.keyboard.calculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.presentation.model.KeyboardAction
import com.wynndie.spwallet.sharedtheme.theme.spacing

@Composable
fun CalculatorLayout(
    onAction: (KeyboardAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        modifier = modifier
    ) {
        CalculatorKeyboard(
            onAction = onAction
        )
    }
}