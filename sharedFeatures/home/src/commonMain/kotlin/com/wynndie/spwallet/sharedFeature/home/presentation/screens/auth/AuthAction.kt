package com.wynndie.spwallet.sharedFeature.home.presentation.screens.auth

import androidx.compose.ui.text.input.TextFieldValue

sealed interface AuthAction {
    data object NavigateBack : AuthAction

    data class AuthCard(val id: String, val token: String) : AuthAction
    data class ToggleHelpSheet(val isOpen: Boolean) : AuthAction

    data class ChangeIdValue(val value: TextFieldValue) : AuthAction
    data class ChangeTokenValue(val value: TextFieldValue) : AuthAction

    data object ClearIdFocus : AuthAction
    data object ClearTokenFocus : AuthAction
}