package com.wynndie.spwallet.sharedCore.presentation.component.keyboard.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.wynndie.spwallet.sharedtheme.theme.radius

@Composable
fun KeyboardButton(
    symbol: @Composable BoxScope.() -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: CornerBasedShape = MaterialTheme.radius.default
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .minimumInteractiveComponentSize()
            .clip(shape)
            .clickable { onClick() }
            .then(modifier)
    ) {
        symbol()
    }
}