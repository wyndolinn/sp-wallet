package com.wynndie.spwallet.sharedFeature.home.presentation.screen.transfer_by_card.component

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.wynndie.spwallet.sharedCore.presentation.model.input.InputFieldState
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.apply
import com.wynndie.spwallet.sharedResources.card_number
import com.wynndie.spwallet.sharedResources.enter_card_number
import com.wynndie.spwallet.sharedtheme.designSystem.BaseBottomSheetLayout
import com.wynndie.spwallet.sharedtheme.designSystem.button.BaseButton
import com.wynndie.spwallet.sharedtheme.designSystem.inputField.TitledInputField
import com.wynndie.spwallet.sharedtheme.theme.spacing
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipientSheet(
    onDismiss: () -> Unit,
    receiverInputFieldState: InputFieldState,
    onChangeRecipientValue: (TextFieldValue) -> Unit,
    onClickRecipient: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BaseBottomSheetLayout(
        onDismiss = onDismiss
    ) {
        RecipientSheetContent(
            receiverInputFieldState = receiverInputFieldState,
            onChangeReceiverValue = onChangeRecipientValue,
            onClickRecipient = onClickRecipient,
            modifier = modifier
        )
    }
}

@Composable
private fun RecipientSheetContent(
    receiverInputFieldState: InputFieldState,
    onChangeReceiverValue: (TextFieldValue) -> Unit,
    onClickRecipient: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus(true) }
                )
            }
    ) {
        TitledInputField(
            value = receiverInputFieldState.value,
            onValueChange = { onChangeReceiverValue(it) },
            label = stringResource(Res.string.enter_card_number),
            placeholder = stringResource(Res.string.card_number),
            supportingText = receiverInputFieldState.supportingText?.asString(),
            isError = receiverInputFieldState.hasError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus(true)
                    onClickRecipient(receiverInputFieldState.value.text)
                }
            )
        )

        BaseButton(
            text = stringResource(Res.string.apply),
            onClick = { onClickRecipient(receiverInputFieldState.value.text) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}