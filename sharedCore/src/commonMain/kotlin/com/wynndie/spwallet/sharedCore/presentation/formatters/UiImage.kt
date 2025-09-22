package com.wynndie.spwallet.sharedCore.presentation.formatters

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import coil3.compose.rememberAsyncImagePainter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

sealed interface UiImage {

    data class Drawable(val id: DrawableResource) : UiImage
    data class Vector(val vector: ImageVector) : UiImage
    data class Url(val value: String) : UiImage

    @Composable
    fun asPainter(): Painter {
        return when (this) {
            is Drawable -> painterResource(id)
            is Vector -> rememberVectorPainter(vector)
            is Url -> rememberAsyncImagePainter(value)
        }
    }
}