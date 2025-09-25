package com.wynndie.spwallet.sharedCore.presentation.formatters

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

sealed interface UiImage {

    @Composable
    fun asPainter(): Painter

    data class Drawable(val id: DrawableResource) : UiImage {

        @Composable
        override fun asPainter(): Painter {
            return painterResource(id)
        }
    }

    data class Vector(val vector: ImageVector) : UiImage {

        @Composable
        override fun asPainter(): VectorPainter {
            return rememberVectorPainter(vector)
        }
    }

    data class Url(val value: String) : UiImage {

        @Composable
        override fun asPainter(): AsyncImagePainter {
            return rememberAsyncImagePainter(value)
        }
    }
}