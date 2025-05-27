package com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.home.component.dialog

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import com.wynndie.spwallet.sharedCore.presentation.component.button.UiButton
import com.wynndie.spwallet.sharedCore.presentation.component.dialog.BottomSheetScaffold
import com.wynndie.spwallet.sharedCore.presentation.component.inputField.UiOutlinedInputField
import com.wynndie.spwallet.sharedCore.presentation.model.InputField
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedFeature.wallet.presentation.component.UiCardCarousel
import com.wynndie.spwallet.sharedFeature.wallet.presentation.model.UiUnauthedCard
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.auth
import com.wynndie.spwallet.sharedResources.auth_instruction
import com.wynndie.spwallet.sharedResources.enter_id
import com.wynndie.spwallet.sharedResources.enter_token
import com.wynndie.spwallet.sharedResources.id
import com.wynndie.spwallet.sharedResources.token
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthCardSheet(
    onDismiss: () -> Unit,
    cards: List<UiUnauthedCard>,
    initialPage: Int,
    tokenInputField: InputField,
    onChangeTokenValue: (TextFieldValue) -> Unit,
    idInputField: InputField,
    onChangeIdValue: (TextFieldValue) -> Unit,
    onClickAuthButton: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomSheetScaffold(
        onDismiss = onDismiss
    ) {
        AuthCardSheetContent(
            cards = cards,
            page = initialPage,
            tokenInputField = tokenInputField,
            onTokenValueChange = onChangeTokenValue,
            idInputField = idInputField,
            onChangeIdValue = onChangeIdValue,
            onClickAuthButton = onClickAuthButton,
            modifier = modifier
        )
    }
}

@Composable
private fun AuthCardSheetContent(
    cards: List<UiUnauthedCard>,
    page: Int,
    tokenInputField: InputField,
    onTokenValueChange: (TextFieldValue) -> Unit,
    idInputField: InputField,
    onChangeIdValue: (TextFieldValue) -> Unit,
    onClickAuthButton: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus(true) }
                )
            }
    ) {
        if (cards.isNotEmpty()) {
            UiCardCarousel(
                items = cards,
                page = page,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
        ) {

            if (cards.isEmpty()) {
                UiOutlinedInputField(
                    value = idInputField.value,
                    onValueChange = { onChangeIdValue(it) },
                    label = stringResource(Res.string.enter_id),
                    placeholder = stringResource(Res.string.id),
                    supportingText = idInputField.supportingText.asString(),
                    isError = idInputField.supportingText.asString().isNotBlank(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    )
                )
            }

            UiOutlinedInputField(
                value = tokenInputField.value,
                onValueChange = { onTokenValueChange(it) },
                label = stringResource(Res.string.enter_token),
                placeholder = stringResource(Res.string.token),
                supportingText = tokenInputField.supportingText.asString(),
                isError = tokenInputField.supportingText.asString().isNotBlank(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus(true)
                        onClickAuthButton(cards[page].id, tokenInputField.value.text)
                    }
                )
            )

            Text(
                text = stringResource(Res.string.auth_instruction),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }

        val cardId = if (cards.isNotEmpty()) cards[page].id else idInputField.value.text
        UiButton(
            text = stringResource(Res.string.auth),
            onClick = { onClickAuthButton(cardId, tokenInputField.value.text) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.medium)
        )
    }
}