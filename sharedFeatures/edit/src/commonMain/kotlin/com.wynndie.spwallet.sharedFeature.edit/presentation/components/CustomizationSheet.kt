package com.wynndie.spwallet.sharedFeature.edit.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.presentation.extensions.asColor
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.apply
import com.wynndie.spwallet.sharedtheme.designSystem.overlays.BottomSheet
import com.wynndie.spwallet.sharedtheme.designSystem.buttons.Button
import com.wynndie.spwallet.sharedtheme.extensions.factor
import com.wynndie.spwallet.sharedtheme.theme.sizing
import com.wynndie.spwallet.sharedtheme.theme.spacing
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizationSheet(
    onDismiss: () -> Unit,
    selectedColorChip: CardColors,
    onColorChipClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomSheet(
        onDismiss = onDismiss
    ) {
        CustomizationSheetContent(
            selectedColorChip = selectedColorChip,
            onColorChipClick = onColorChipClick,
            onDismiss = onDismiss,
            modifier = modifier
        )
    }
}

@Composable
private fun CustomizationSheetContent(
    selectedColorChip: CardColors,
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
        FlowRow(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        ) {
            CardColors.entries.forEach { chip ->
                FilterChip(
                    label = { },
                    onClick = { onColorChipClick(chip.ordinal) },
                    selected = selectedColorChip == chip,
                    shape = CircleShape,
                    colors = FilterChipDefaults.filterChipColors().copy(
                        selectedContainerColor = chip.asColor(),
                        containerColor = chip.asColor()
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selectedColorChip == chip,
                        borderWidth = MaterialTheme.sizing.extraSmall.factor(1/2f),
                        borderColor = MaterialTheme.colorScheme.surface,
                        selectedBorderWidth = MaterialTheme.sizing.extraSmall.factor(1/2f),
                        selectedBorderColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.small)
                        .background(
                            color = if (selectedColorChip == chip) chip.asColor() else Color.Transparent,
                            shape = MaterialTheme.shapes.extraLarge
                        )
                )
            }
        }

        Button(
            text = stringResource(Res.string.apply),
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth()
        )
    }
}