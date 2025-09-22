package com.wynndie.spwallet.sharedtheme.designSystem.inputField

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.clear
import com.wynndie.spwallet.sharedtheme.designSystem.titledContent.BaseTitledContentSmall
import org.jetbrains.compose.resources.stringResource

@Composable
fun TitledInputField(
    label: String,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit) = {
        AnimatedVisibility(
            visible = value.text.isNotBlank(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            IconButton(
                onClick = { onValueChange(TextFieldValue("")) }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Clear,
                    contentDescription = stringResource(Res.string.clear)
                )
            }
        }
    },
    prefix: String? = null,
    suffix: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    BaseTitledContentSmall(
        title = label,
        modifier = modifier
    ) {
        BaseInputField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            placeholder = placeholder,
            supportingText = supportingText,
            isError = isError,
            enabled = enabled,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            prefix = prefix,
            suffix = suffix ,
            visualTransformation = visualTransformation,
            singleLine = singleLine,
            minLines = minLines,
            modifier = Modifier.fillMaxWidth()
        )
    }
}