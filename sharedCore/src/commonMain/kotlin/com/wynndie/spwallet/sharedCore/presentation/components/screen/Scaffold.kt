package com.wynndie.spwallet.sharedCore.presentation.components.screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import com.wynndie.spwallet.sharedCore.presentation.components.loading.LoadingScreen
import com.wynndie.spwallet.sharedCore.presentation.extensions.add
import com.wynndie.spwallet.sharedCore.presentation.extensions.thenIfNotNull
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState

@Composable
fun Scaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable BoxScope.() -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    loadingState: LoadingState = LoadingState.Finished,
    focusManager: FocusManager = LocalFocusManager.current,
    nestedScrollConnection: NestedScrollConnection? = null,
    content: @Composable (PaddingValues) -> Unit,
) {

    var fabHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    val screenHeightDp = with(density) { LocalWindowInfo.current.containerSize.height.toDp() }

    Scaffold(
        topBar = topBar,
        bottomBar = bottomBar,
        floatingActionButton = {
            Box(
                content = floatingActionButton,
                modifier = Modifier.onGloballyPositioned {
                    val fabRootHeight = with(density) {
                        it.positionInRoot().y.toDp() + it.size.height.toDp()
                    }
                    fabHeight = screenHeightDp - fabRootHeight
                }
            )
        },
        floatingActionButtonPosition = floatingActionButtonPosition,
        snackbarHost = snackbarHost,
        contentWindowInsets = contentWindowInsets,
        containerColor = containerColor,
        contentColor = contentColor,
        modifier = Modifier
            .imePadding()
            .thenIfNotNull(nestedScrollConnection) { Modifier.nestedScroll(it) }
            .pointerInput(Unit) { detectTapGestures { focusManager.clearFocus(true) } }
            .then(modifier)
    ) { innerPadding ->
        Crossfade(loadingState) { state ->
            when (state) {
                LoadingState.Loading -> {
                    LoadingScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }

                is LoadingState.Failed -> {

                }

                LoadingState.Finished -> {
                    content(innerPadding.add(bottom = fabHeight))
                }
            }
        }
    }
}