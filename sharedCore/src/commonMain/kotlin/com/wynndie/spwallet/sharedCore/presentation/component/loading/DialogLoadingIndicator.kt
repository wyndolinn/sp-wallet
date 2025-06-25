package com.wynndie.spwallet.sharedCore.presentation.component.loading

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.wynndie.spwallet.sharedCore.presentation.theme.radius
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogLoadingIndicator() {
    BasicAlertDialog(
        onDismissRequest = {}
    ) {
        Surface(
            modifier = Modifier.clip(MaterialTheme.radius.default)
        ) {
            LoadingIndicator(
                modifier = Modifier.padding(MaterialTheme.spacing.large)
            )
        }
    }
}