package com.wynndie.spwallet.sharedCore.presentation.components.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.ic_people
import com.wynndie.spwallet.sharedCore.presentation.theme.AppTheme
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedCore.recipients
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun LabeledIconButton(
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    destructive: Boolean = false,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        OutlinedButton(
            onClick = onClick,
            enabled = enabled && !loading,
            border = null,
            contentPadding = PaddingValues(
                horizontal = MaterialTheme.spacing.small,
                vertical = MaterialTheme.spacing.extraExtraSmall,
            ),
            colors = ButtonDefaults.outlinedButtonColors().copy(
                contentColor = if (destructive) {
                    MaterialTheme.colorScheme.error
                } else MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            ),
            shape = CircleShape,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
        ) {
            Icon(
                painter = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        label?.let {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LabeledIconButtonPreview() {
    AppTheme {
        LabeledIconButton(
            icon = painterResource(Res.drawable.ic_people),
            label = stringResource(Res.string.recipients),
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}