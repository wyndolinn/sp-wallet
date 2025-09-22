package com.wynndie.spwallet.sharedtheme.designSystem.button

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun BaseIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false
) {
    IconButton(
        onClick = onClick,
        enabled = enabled && !loading,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
    }
}