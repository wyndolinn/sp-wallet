package com.wynndie.spwallet.sharedCore.presentation.components.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ScreenLayout(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        Spacer(Modifier.height(contentPadding.calculateTopPadding()))
        content()
        Spacer(Modifier.height(contentPadding.calculateBottomPadding()))
    }
}