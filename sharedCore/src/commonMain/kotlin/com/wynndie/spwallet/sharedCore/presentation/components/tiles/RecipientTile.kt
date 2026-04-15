package com.wynndie.spwallet.sharedCore.presentation.components.tiles

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wynndie.spwallet.sharedCore.presentation.extensions.thenIfNotNull
import com.wynndie.spwallet.sharedCore.presentation.theme.AppTheme
import com.wynndie.spwallet.sharedCore.presentation.theme.sizes
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.ic_arrow_right
import com.wynndie.spwallet.sharedResources.ic_person
import com.wynndie.spwallet.sharedResources.recipient
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun RecipientTile(
    label: String,
    title: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .heightIn(min = MaterialTheme.sizes.extraLarge)
            .thenIfNotNull(onClick) { Modifier.clickable(onClick = it) }
            .padding(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.extraSmall
            )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(MaterialTheme.sizes.medium)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .border(2.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_person),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = MaterialTheme.spacing.medium)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Icon(
            painter = painterResource(Res.drawable.ic_arrow_right),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipientTilePreview() {
    AppTheme {
        RecipientTile(
            label = stringResource(Res.string.recipient),
            title = "55555",
        )
    }
}