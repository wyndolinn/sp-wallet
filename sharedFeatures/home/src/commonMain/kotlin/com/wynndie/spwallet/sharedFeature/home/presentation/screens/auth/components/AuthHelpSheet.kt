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
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.OutlinedButton
import com.wynndie.spwallet.sharedCore.presentation.components.overlays.BottomSheet
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing

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
                    text = "Для активации карты",
                    style = MaterialTheme.typography.titleMedium
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
                ) {
                    Text(
                        text = "1. На сайте sp worlds зайдите во вкладку \"Кошелёк\", выберете карту и нажмите иконку \"Поделится\"",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "2. Зайдите на сервер и сгенерируйте для карты новый token",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "3. На сервере, в чат, вам придут данные карты. Введите их в приложение любым удобным способом",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

            }

            OutlinedButton(
                text = "Понятно",
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}