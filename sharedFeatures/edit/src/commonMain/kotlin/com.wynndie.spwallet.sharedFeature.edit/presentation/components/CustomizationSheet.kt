package com.wynndie.spwallet.sharedFeature.edit.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.Button
import com.wynndie.spwallet.sharedCore.presentation.components.overlays.BottomSheet
import com.wynndie.spwallet.sharedCore.presentation.extensions.asColor
import com.wynndie.spwallet.sharedCore.presentation.extensions.thenIf
import com.wynndie.spwallet.sharedCore.presentation.theme.AppTheme
import com.wynndie.spwallet.sharedCore.presentation.theme.sizes
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.apply
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizationSheet(
    onDismiss: () -> Unit,
    selectedColor: CardColors,
    onColorClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    BottomSheet(
        onDismiss = onDismiss
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
            modifier = modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { focusManager.clearFocus(true) }
                    )
                }
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(
                    space = MaterialTheme.spacing.small,
                    alignment = Alignment.CenterHorizontally
                ),
                verticalArrangement = Arrangement.spacedBy(
                    space = MaterialTheme.spacing.small,
                    alignment = Alignment.CenterVertically
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium)
            ) {
                CardColors.entries.forEach { color ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(MaterialTheme.sizes.large)
                            .clip(MaterialTheme.shapes.large)
                            .thenIf(selectedColor == color) {
                                Modifier.background(color.asColor())
                            }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(MaterialTheme.sizes.small)
                                .clip(MaterialTheme.shapes.small)
                                .background(color.asColor())
                                .clickable { onColorClick(color.ordinal) }
                                .thenIf(selectedColor == color) {
                                    Modifier.border(
                                        width = 2.dp,
                                        color = MaterialTheme.colorScheme.surface,
                                        shape = MaterialTheme.shapes.small
                                    )
                                }
                        )
                    }
                }
            }

            Button(
                text = stringResource(Res.string.apply),
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomizableSheetPreview() {
    AppTheme {
        CustomizationSheet(
            selectedColor = CardColors.BLUE,
            onColorClick = {},
            onDismiss = {},
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        )
    }
}