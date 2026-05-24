package com.wynndie.spwallet.sharedCore.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.wynndie.spwallet.sharedCore.presentation.extensions.shimmerEffect

@Composable
fun AsyncImage(
    url: String,
    contentDescription: String?,
    error: Painter,
    contentScale: ContentScale = ContentScale.Crop,
    contentSpacing: Dp = 0.dp,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        success = {
            Image(
                painter = it.painter,
                contentDescription = null,
                contentScale = contentScale,
                modifier = Modifier.padding(contentSpacing)
            )
        },
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.small)
                    .shimmerEffect()
            )
        },
        error = {
            Image(
                painter = error,
                contentDescription = null,
                contentScale = contentScale,
                modifier = Modifier.padding(contentSpacing)
            )
        },
        modifier = modifier
    )
}