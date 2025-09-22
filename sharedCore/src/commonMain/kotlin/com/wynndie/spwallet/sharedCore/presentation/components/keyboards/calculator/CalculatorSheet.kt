package com.wynndie.spwallet.sharedCore.presentation.components.keyboards.calculator

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.presentation.models.KeyboardAction
import com.wynndie.spwallet.sharedCore.presentation.models.InputField
import com.wynndie.spwallet.sharedtheme.designSystem.BaseBottomSheetLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorSheet(
    onDismiss: () -> Unit,
    value: InputField,
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
    value: InputField,
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