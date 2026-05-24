package com.wynndie.spwallet.sharedFeature.edit.presentation.screens.recipients.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.card_name
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.Button
import com.wynndie.spwallet.sharedCore.presentation.components.inputField.InputField
import com.wynndie.spwallet.sharedCore.presentation.components.overlays.BottomSheet
import com.wynndie.spwallet.sharedCore.presentation.formatters.InputFieldState
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedCore.save
import com.wynndie.spwallet.sharedCore.token
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecipientSheet(
    onDismiss: () -> Unit,
    nameFieldState: InputFieldState,
    onNameChange: (TextFieldValue) -> Unit,
    onClearNameFocus: () -> Unit,
    numberFieldState: InputFieldState,
    onNumberChange: (TextFieldValue) -> Unit,
    onClearNumberFocus: () -> Unit,
    onSaveRecipient: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomSheet(
        onDismiss = onDismiss,
        modifier = Modifier
    ) {
        val focusManager = LocalFocusManager.current
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures { focusManager.clearFocus(true) }
            }.then(modifier)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
            ) {
                InputField(
                    value = nameFieldState.value,
                    onValueChange = onNameChange,
                    label = stringResource(Res.string.card_name),
                    supportingText = nameFieldState.supportingText?.asString(),
                    hasError = nameFieldState.hasError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    modifier = Modifier.onFocusChanged {
                        if (!it.isFocused) onClearNameFocus()
                    }
                )

                InputField(
                    value = numberFieldState.value,
                    onValueChange = onNumberChange,
                    label = stringResource(Res.string.token),
                    supportingText = numberFieldState.supportingText?.asString(),
                    hasError = numberFieldState.hasError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus(true) }
                    ),
                    modifier = Modifier.onFocusChanged {
                        if (!it.isFocused) onClearNumberFocus()
                    }
                )
            }

            Button(
                text = stringResource(Res.string.save),
                onClick = onSaveRecipient,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}