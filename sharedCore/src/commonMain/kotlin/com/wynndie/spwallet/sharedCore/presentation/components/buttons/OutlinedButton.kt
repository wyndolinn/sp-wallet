package com.wynndie.spwallet.sharedCore.presentation.components.buttons

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.base.ButtonLayout
import com.wynndie.spwallet.sharedCore.presentation.components.loading.LoadingIndicator
import com.wynndie.spwallet.sharedCore.presentation.theme.sizes
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing

@Composable
fun OutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    destructive: Boolean = false,
    enabled: Boolean = true,
    loading: Boolean = false
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled && !loading,
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.spacing.medium,
            vertical = MaterialTheme.spacing.small
        ),
        colors = ButtonDefaults.outlinedButtonColors().copy(
            contentColor = if (destructive) {
                MaterialTheme.colorScheme.error
            } else MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        border = BorderStroke(
            width = 2.dp,
            color = if (destructive) {
                MaterialTheme.colorScheme.errorContainer
            } else MaterialTheme.colorScheme.primaryContainer
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
                    leadingIcon = icon,
                    modifier = Modifier
                )
            }
        }
    }
}
