package com.wynndie.spwallet

import androidx.compose.ui.window.ComposeUIViewController
import com.wynndie.spwallet.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}