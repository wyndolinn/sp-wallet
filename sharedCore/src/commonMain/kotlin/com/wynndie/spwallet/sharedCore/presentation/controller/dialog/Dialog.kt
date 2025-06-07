package com.wynndie.spwallet.sharedCore.presentation.controller.dialog

import com.wynndie.spwallet.sharedCore.presentation.model.UiText

sealed interface Dialog {
    data class AlertDialog(
        val message: UiText
    ) : Dialog

    data class BottomSheet(
        val message: UiText
    ) : Dialog

    data class Snackbar(
        val message: UiText
    ) : Dialog
}