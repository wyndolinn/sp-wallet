package com.wynndie.spwallet.sharedCore.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter.State
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import com.wynndie.spwallet.sharedCore.presentation.components.animations.DynamicContent
import com.wynndie.spwallet.sharedCore.presentation.extensions.shimmerEffect

@Composable
fun AsyncImage(
    url: String,
    contentDescription: String?,
    error: Painter,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    contentSpacing: Dp = 0.dp,
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalPlatformContext.current).data(url).build(),
        contentDescription = contentDescription,
        modifier = modifier,
    ) {
        val painterState by painter.state.collectAsState()

        DynamicContent(painterState) { state ->
            when (state) {
                is State.Loading, State.Empty -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.shapes.small)
                            .shimmerEffect(),
                    )
                }

                is State.Error -> {
                    Image(
                        painter = error,
                        contentDescription = null,
                        contentScale = contentScale,
                        modifier = Modifier.padding(contentSpacing),
                    )
                }

                is State.Success -> {
                    Image(
                        painter = state.painter,
                        contentDescription = null,
                        contentScale = contentScale,
                        modifier = Modifier.padding(contentSpacing),
                    )
                }
            }
        }
    }
}