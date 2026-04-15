package com.wynndie.spwallet.sharedCore.presentation.controllers.overlay

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.Alignment
import com.wynndie.spwallet.sharedCore.presentation.formatters.UiText

data class Snackbar(
    val message: UiText,
    val actionLabel: UiText? = null,
    val withDismissAction: Boolean = true,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val alignment: Alignment.Vertical = Alignment.Bottom
)