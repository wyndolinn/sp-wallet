package com.wynndie.spwallet.sharedCore.presentation.components.inputField

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing

@Composable
internal fun BasicInputField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    label: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    hasError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    readOnly: Boolean = false,
    enabled: Boolean = true,
) {
    val isFocused by interactionSource.collectIsFocusedAsState()
    val hasText = value.text.isNotEmpty()
    val isActive = hasText || isFocused || hasError

    val fontProgress by animateFloatAsState(
        targetValue = if (isActive) 1f else 0f,
        animationSpec = tween(durationMillis = 100),
    )
    val animatedFontSize = lerp(16.sp, 12.sp, fontProgress)

    val containerColor = when {
        hasError -> MaterialTheme.colorScheme.errorContainer
        isFocused -> MaterialTheme.colorScheme.surfaceContainerLowest
        else -> MaterialTheme.colorScheme.surfaceContainerLowest
    }

    val borderColor = when {
        hasError -> MaterialTheme.colorScheme.error
        isFocused -> MaterialTheme.colorScheme.primary
        else -> null
    }

    val borderWidth = if (isFocused) 2.dp else 1.dp

    val labelColor = when {
        hasError -> MaterialTheme.colorScheme.error
        isFocused -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    val textColor = MaterialTheme.colorScheme.onSurface

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = MaterialTheme.typography.bodyMedium.copy(color = textColor),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        modifier = modifier,
        decorationBox = { innerTextField ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium,
                color = containerColor,
                border = borderColor?.let { BorderStroke(borderWidth, it) },
            ) {
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.medium),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (leadingIcon != null) {
                            leadingIcon()
                            Spacer(Modifier.width(MaterialTheme.spacing.small))
                        }

                        Box(
                            contentAlignment = Alignment.CenterStart,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                        ) {
                            if (label != null) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    val baseStyle =
                                        if (isActive) MaterialTheme.typography.labelSmall else MaterialTheme.typography.bodyMedium
                                    Text(
                                        text = label,
                                        style = baseStyle.copy(fontSize = animatedFontSize),
                                        color = labelColor,
                                    )

                                    AnimatedVisibility(
                                        visible = isActive,
                                        enter = expandIn(animationSpec = tween(100)),
                                        exit = shrinkOut(animationSpec = tween(100)),
                                    ) {
                                        Column {
                                            Spacer(Modifier.height(2.dp))
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.fillMaxWidth(),
                                            ) {
                                                if (prefix != null) {
                                                    prefix()
                                                    Spacer(Modifier.width(4.dp))
                                                }
                                                Box(modifier = Modifier.weight(1f)) {
                                                    if (value.text.isEmpty() && placeholder != null) {
                                                        Text(
                                                            text = placeholder,
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            color = MaterialTheme.colorScheme.outline,
                                                        )
                                                    }
                                                    innerTextField()
                                                }
                                                if (suffix != null) {
                                                    Spacer(Modifier.width(4.dp))
                                                    suffix()
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    if (prefix != null) {
                                        prefix()
                                        Spacer(Modifier.width(4.dp))
                                    }
                                    Box(modifier = Modifier.weight(1f)) {
                                        if (value.text.isEmpty() && placeholder != null) {
                                            Text(
                                                text = placeholder,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.outline,
                                            )
                                        }
                                        innerTextField()
                                    }
                                    if (suffix != null) {
                                        Spacer(Modifier.width(4.dp))
                                        suffix()
                                    }
                                }
                            }
                        }

                        if (trailingIcon != null) {
                            Spacer(Modifier.width(MaterialTheme.spacing.small))
                            trailingIcon()
                        }
                    }
                }
            }
        },
    )
}
