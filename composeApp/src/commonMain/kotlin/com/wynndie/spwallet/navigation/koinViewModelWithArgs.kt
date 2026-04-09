package com.wynndie.spwallet.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
inline fun <reified VM : ViewModel> koinViewModelWithArgs(vararg parameters: Any?): VM {
    return koinViewModel<VM> { parametersOf(*parameters) }
}