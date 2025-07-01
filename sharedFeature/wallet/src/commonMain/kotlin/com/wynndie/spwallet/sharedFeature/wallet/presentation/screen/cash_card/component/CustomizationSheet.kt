package com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.cash_card.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.wynndie.spwallet.sharedCore.presentation.component.button.UiButton
import com.wynndie.spwallet.sharedCore.presentation.component.dialog.BottomSheetScaffold
import com.wynndie.spwallet.sharedCore.presentation.component.inputField.UiOutlinedInputField
import com.wynndie.spwallet.sharedCore.presentation.model.input.InputFieldState
import com.wynndie.spwallet.sharedCore.presentation.theme.size
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedFeature.wallet.presentation.model.CardColor
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.apply
import com.wynndie.spwallet.sharedResources.card_name
import com.wynndie.spwallet.sharedResources.enter_card_name
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizationSheet(
    onDismiss: () -> Unit,
    idInputFieldState: InputFieldState,
    onIdValueChange: (TextFieldValue) -> Unit,
    selectedColorChip: Int,
    onColorChipClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomSheetScaffold(
        onDismiss = onDismiss
    ) {
        CustomizationSheetContent(
            nameInputFieldState = idInputFieldState,
            onNameValueChange = onIdValueChange,
            selectedColorChip = selectedColorChip,
            onColorChipClick = onColorChipClick,
            onDismiss = onDismiss,
            modifier = modifier
        )
    }
}

@Composable
private fun CustomizationSheetContent(
    nameInputFieldState: InputFieldState,
    onNameValueChange: (TextFieldValue) -> Unit,
    selectedColorChip: Int,
    onColorChipClick: (Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus(true) }
                )
            }
    ) {
        UiOutlinedInputField(
            value = nameInputFieldState.value,
            onValueChange = { onNameValueChange(it) },
            label = stringResource(Res.string.enter_card_name),
            placeholder = stringResource(Res.string.card_name),
            supportingText = nameInputFieldState.supportingText.asString(),
            isError = nameInputFieldState.supportingText.asString().isNotBlank(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus(true)
                }
            )
        )

        HorizontalDivider(Modifier.padding(vertical = MaterialTheme.spacing.medium))

        FlowRow(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        ) {
            val chips = CardColor.entries
            chips.forEachIndexed { index, chip ->
                FilterChip(
                    label = { },
                    onClick = { onColorChipClick(chip.ordinal) },
                    selected = selectedColorChip == index,
                    shape = CircleShape,
                    colors = FilterChipDefaults.filterChipColors().copy(
                        selectedContainerColor = chip.value,
                        containerColor = chip.value
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selectedColorChip == index,
                        borderWidth = MaterialTheme.size.scale(0.5f).extraSmall,
                        borderColor = MaterialTheme.colorScheme.surface,
                        selectedBorderWidth = MaterialTheme.size.scale(0.5f).extraSmall,
                        selectedBorderColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.small)
                        .background(
                            color = if (selectedColorChip == index) chip.value else Color.Transparent,
                            shape = MaterialTheme.shapes.extraLarge
                        )
                )
            }
        }

        UiButton(
            text = stringResource(Res.string.apply),
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth()
        )
    }
}