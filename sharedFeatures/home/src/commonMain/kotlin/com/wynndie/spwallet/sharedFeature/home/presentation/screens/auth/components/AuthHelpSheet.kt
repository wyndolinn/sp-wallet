package com.wynndie.spwallet.sharedFeature.home.presentation.screens.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.auth_step_1
import com.wynndie.spwallet.sharedCore.auth_step_2
import com.wynndie.spwallet.sharedCore.auth_step_3
import com.wynndie.spwallet.sharedCore.auth_steps_title
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.OutlinedButton
import com.wynndie.spwallet.sharedCore.presentation.components.overlays.BottomSheet
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedCore.understood
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthHelpSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomSheet(
        onDismiss = onDismiss
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                Text(
                    text = stringResource(Res.string.auth_steps_title),
                    style = MaterialTheme.typography.titleMedium
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
                ) {
                    Text(
                        text = stringResource(Res.string.auth_step_1),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = stringResource(Res.string.auth_step_2),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = stringResource(Res.string.auth_step_3),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

            }

            OutlinedButton(
                text = stringResource(Res.string.understood),
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}