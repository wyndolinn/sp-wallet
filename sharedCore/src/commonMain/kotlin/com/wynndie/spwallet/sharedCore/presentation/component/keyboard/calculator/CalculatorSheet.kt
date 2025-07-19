package com.wynndie.spwallet.sharedCore.presentation.component.keyboard.calculator

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.presentation.component.baseDesignSystem.BaseBottomSheetLayout
import com.wynndie.spwallet.sharedCore.presentation.model.input.InputFieldState
import com.wynndie.spwallet.sharedCore.presentation.model.KeyboardAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorSheet(
    onDismiss: () -> Unit,
    value: InputFieldState,
    onAction: (KeyboardAction) -> Unit,
    modifier: Modifier = Modifier
) {
    BaseBottomSheetLayout(
        onDismiss = onDismiss
    ) {
        CalculatorSheetContent(
            value = value,
            onAction = onAction,
            modifier = modifier
        )
    }
}

@Composable
private fun CalculatorSheetContent(
    value: InputFieldState,
    onAction: (KeyboardAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        CalculatorLayout(
            onAction = onAction,
            modifier = modifier
        )
    }
}