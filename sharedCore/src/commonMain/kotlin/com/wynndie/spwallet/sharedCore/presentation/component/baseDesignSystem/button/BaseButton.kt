package com.wynndie.spwallet.sharedCore.presentation.component.baseDesignSystem.button

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.wynndie.spwallet.sharedCore.presentation.component.loading.LoadingIndicator
import com.wynndie.spwallet.sharedCore.presentation.theme.radius
import com.wynndie.spwallet.sharedCore.presentation.theme.size

@Composable
fun BaseButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        shape = MaterialTheme.radius.medium,
        enabled = enabled,
        modifier = modifier.height(MaterialTheme.size.large)
    ) {

        Crossfade(
            targetState = isLoading,
            animationSpec = tween(500)
        ) { isLoading ->

            if (isLoading) {
                LoadingIndicator()
            } else {
                leadingIcon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null
                    )
                }

                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge
                )

                trailingIcon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null
                    )
                }
            }
        }
    }
}