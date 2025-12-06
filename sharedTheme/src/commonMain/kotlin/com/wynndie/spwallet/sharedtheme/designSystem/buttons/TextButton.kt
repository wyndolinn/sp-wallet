package com.wynndie.spwallet.sharedtheme.designSystem.buttons

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import com.wynndie.spwallet.sharedtheme.designSystem.buttons.base.ButtonLayout
import com.wynndie.spwallet.sharedtheme.designSystem.loading.LoadingIndicator
import com.wynndie.spwallet.sharedtheme.theme.sizing

@Composable
fun TextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: Painter? = null,
    trailingIcon: Painter? = null,
    hasError: Boolean = false,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    TextButton(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.height(MaterialTheme.sizing.medium)
    ) {
        Crossfade(
            targetState = isLoading
        ) { isLoading ->

            if (isLoading) {
                LoadingIndicator()
            } else {
                ButtonLayout(
                    text = text,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    modifier = Modifier
                )
            }
        }
    }
}