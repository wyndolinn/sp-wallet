package com.wynndie.spwallet.sharedCore.presentation.components.buttons

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.base.ButtonLayout
import com.wynndie.spwallet.sharedCore.presentation.components.loading.LoadingIndicator
import com.wynndie.spwallet.sharedCore.presentation.theme.sizes
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing

@Composable
fun Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    color: Color = MaterialTheme.colorScheme.primary,
    destructive: Boolean = false,
    enabled: Boolean = true,
    loading: Boolean = false
) {
    Button(
        onClick = onClick,
        enabled = enabled && !loading,
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.spacing.medium,
            vertical = MaterialTheme.spacing.small
        ),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = if (destructive) {
                MaterialTheme.colorScheme.error
            } else color
        ),
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.height(MaterialTheme.sizes.extraLarge)
    ) {
        Crossfade(targetState = loading) { isLoading ->
            if (isLoading) {
                LoadingIndicator()
            } else {
                ButtonLayout(
                    text = text,
                    leadingIcon = icon
                )
            }
        }
    }
}