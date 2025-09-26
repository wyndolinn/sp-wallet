package com.wynndie.spwallet.sharedtheme.designSystem.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.wynndie.spwallet.sharedtheme.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingDialog() {
    BasicAlertDialog(
        onDismissRequest = {}
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            LoadingIndicator(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(MaterialTheme.spacing.large)
            )
        }
    }
}