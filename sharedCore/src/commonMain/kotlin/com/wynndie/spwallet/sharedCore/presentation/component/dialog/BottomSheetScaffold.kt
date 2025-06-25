package com.wynndie.spwallet.sharedCore.presentation.component.dialog

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.presentation.component.loading.DialogLoadingIndicator
import com.wynndie.spwallet.sharedCore.presentation.model.LoadingState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetScaffold(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    loadingState: LoadingState = LoadingState.Finished,
    content: @Composable () -> Unit
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        when (loadingState) {
            LoadingState.Loading -> {
                DialogLoadingIndicator()
            }

            is LoadingState.Failed -> {

            }

            LoadingState.Finished -> {
                content()
            }
        }
    }
}