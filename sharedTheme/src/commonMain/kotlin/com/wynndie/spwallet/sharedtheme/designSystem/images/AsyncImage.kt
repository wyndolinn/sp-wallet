package com.wynndie.spwallet.sharedtheme.designSystem.images

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.wynndie.spwallet.sharedtheme.designSystem.loading.LoadingIndicator
import com.wynndie.spwallet.sharedtheme.theme.spacing

@Composable
fun AsyncImage(
    url: String,
    onError: String,
    contentScale: ContentScale = ContentScale.Fit,
    shape: CornerBasedShape = RoundedCornerShape(0.dp),
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = null,
        loading = {
            LoadingIndicator()
        },
        error = {
            TextImage(
                text = onError,
                contentPadding = PaddingValues(MaterialTheme.spacing.extraSmall),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                shape = shape
            )
        },
        success = {
            Image(
                painter = it.painter,
                contentDescription = null,
                modifier = Modifier.clip(shape)
            )
        },
        contentScale = contentScale,
        alignment = Alignment.Center,
        modifier = modifier
    )
}

@Composable
fun AsyncImage(
    url: String,
    onError: Painter,
    contentScale: ContentScale = ContentScale.Fit,
    shape: CornerBasedShape = RoundedCornerShape(0.dp),
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = null,
        loading = {
            LoadingIndicator()
        },
        error = {
            Image(
                painter = onError,
                contentDescription = null,
                modifier = Modifier.clip(shape)
            )
        },
        success = {
            Image(
                painter = it.painter,
                contentDescription = null,
                modifier = Modifier.clip(shape)
            )
        },
        contentScale = contentScale,
        alignment = Alignment.Center,
        modifier = modifier
    )
}