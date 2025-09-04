package com.wynndie.spwallet.sharedCore.presentation.controller.overlay

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.Alignment
import com.wynndie.spwallet.sharedCore.presentation.model.UiText

sealed interface OverlayType {

    data class AlertDialog(
        val message: UiText
    ) : OverlayType

    data class BottomSheet(
        val message: UiText
    ) : OverlayType

    data class Snackbar(
        val message: UiText,
        val actionLabel: UiText? = null,
        val withDismissAction: Boolean = true,
        val duration: SnackbarDuration = SnackbarDuration.Short,
        val alignment: Alignment.Vertical = Alignment.Bottom
    ) : OverlayType
}