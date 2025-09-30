package com.wynndie.spwallet.sharedtheme.designSystem.inputField

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.wynndie.spwallet.sharedtheme.extensions.ContentState
import com.wynndie.spwallet.sharedtheme.extensions.asDescriptionColor
import com.wynndie.spwallet.sharedtheme.theme.spacing

@Composable
fun InputField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    supportingText: String? = null,
    additionalContent: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (BoxScope.() -> Unit)? = null,
    trailingIcon: @Composable (BoxScope.() -> Unit)? = null,
    suffix: String? = null,
    prefix: String? = null,
    hasError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    enabled: Boolean = true
) {

    val contentState = when {
        hasError -> ContentState.Alerted
        !enabled -> ContentState.Disabled
        else -> ContentState.Neutral
    }

    Column(
        modifier = modifier
    ) {
        label?.let {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = it,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(Modifier.height(MaterialTheme.spacing.extraSmall))

        BasicInputField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            hasError = hasError,
            leadingIcon = leadingIcon?.let {
                {
                    Box(
                        modifier = Modifier.size(24.dp),
                        contentAlignment = Alignment.Center,
                        content = it
                    )
                }
            },
            trailingIcon = trailingIcon?.let {
                {
                    Box(
                        modifier = Modifier.size(24.dp),
                        contentAlignment = Alignment.Center,
                        content = it
                    )
                }
            },
            suffix = suffix?.let {
                {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            prefix = prefix?.let {
                {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            enabled = enabled
        )

        AnimatedVisibility(
            visible = supportingText != null
        ) {
            supportingText?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentState.asDescriptionColor()
                )
            }
        }

        AnimatedVisibility(
            visible = additionalContent != null
        ) {
            additionalContent?.let { it() }
        }
    }
}