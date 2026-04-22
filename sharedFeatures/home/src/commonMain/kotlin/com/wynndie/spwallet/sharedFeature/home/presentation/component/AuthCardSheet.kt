package com.wynndie.spwallet.sharedFeature.home.presentation.component

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import com.wynndie.spwallet.sharedCore.domain.models.cards.UnauthedCard
import com.wynndie.spwallet.sharedCore.presentation.components.BaseCarousel
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.Button
import com.wynndie.spwallet.sharedCore.presentation.components.inputField.InputField
import com.wynndie.spwallet.sharedCore.presentation.components.loading.LoadingDialog
import com.wynndie.spwallet.sharedCore.presentation.components.overlays.BottomSheet
import com.wynndie.spwallet.sharedCore.presentation.components.tiles.TransferCardTile
import com.wynndie.spwallet.sharedCore.presentation.extensions.asColor
import com.wynndie.spwallet.sharedCore.presentation.extensions.asPainter
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.formatters.InputFieldState
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.activate
import com.wynndie.spwallet.sharedResources.auth_instruction
import com.wynndie.spwallet.sharedResources.ic_add_card
import com.wynndie.spwallet.sharedResources.id
import com.wynndie.spwallet.sharedResources.token
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthCardSheet(
    onDismiss: () -> Unit,
    loadingState: LoadingState,
    isAuthButtonEnabled: Boolean,
    cards: List<UnauthedCard>,
    initialPage: Int,
    tokenInputState: InputFieldState,
    idInputState: InputFieldState,
    onChangeIdValue: (TextFieldValue) -> Unit,
    onToggleCardIdFocus: () -> Unit,
    onChangeTokenValue: (TextFieldValue) -> Unit,
    onToggleCardTokenFocus: () -> Unit,
    onClickAuthButton: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomSheet(
        onDismiss = onDismiss
    ) {

        if (loadingState is LoadingState.Loading) {
            LoadingDialog()
        }

        val focusManager = LocalFocusManager.current
        var currentPage by remember { mutableStateOf(initialPage) }

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
                BaseCarousel(
                    items = cards,
                    page = initialPage,
                    onSwipePage = {
                        currentPage = it
                        onChangeIdValue(TextFieldValue(cards[it].id))
                    },
                    contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium),
                    pageSpacing = MaterialTheme.spacing.medium
                ) { card ->
                    TransferCardTile(
                        title = card.name,
                        text = card.number,
                        icon = card.icon.asPainter(),
                        color = card.color.asColor(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
            ) {
                if (cards.isEmpty()) {
                    InputField(
                        value = idInputState.value,
                        onValueChange = { onChangeIdValue(it) },
                        label = stringResource(Res.string.id),
                        supportingText = idInputState.supportingText?.asString(),
                        hasError = idInputState.hasError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        modifier = Modifier.onFocusChanged {
                            if (!it.isFocused) onToggleCardIdFocus()
                        }
                    )
                }

                InputField(
                    value = tokenInputState.value,
                    onValueChange = { onChangeTokenValue(it) },
                    label = stringResource(Res.string.token),
                    supportingText = tokenInputState.supportingText?.asString(),
                    hasError = tokenInputState.hasError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus(true)
                            onClickAuthButton(cards[currentPage].id, tokenInputState.value.text)
                        }
                    ),
                    modifier = Modifier.onFocusChanged {
                        if (!it.isFocused) onToggleCardTokenFocus()
                    }
                )

                Text(
                    text = stringResource(Res.string.auth_instruction),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            Button(
                text = stringResource(Res.string.activate),
                icon = painterResource(Res.drawable.ic_add_card),
                onClick = {
                    val cardId = if (cards.isEmpty()) {
                        idInputState.value.text
                    } else cards[currentPage].id

                    onClickAuthButton(cardId, tokenInputState.value.text)
                },
                enabled = isAuthButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium)
            )
        }
    }
}