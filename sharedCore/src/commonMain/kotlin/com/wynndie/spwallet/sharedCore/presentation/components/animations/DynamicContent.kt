package com.wynndie.spwallet.sharedCore.presentation.components.animations

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T> DynamicContent(
    targetState: T,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    AnimatedContent(
        targetState = targetState,
        transitionSpec = {
            val enter = fadeIn(spring(stiffness = Spring.StiffnessMedium))
            val exit = fadeOut(spring(stiffness = Spring.StiffnessMedium))
            val sizeTransform = SizeTransform { _, _ -> spring(stiffness = Spring.StiffnessMedium) }

            enter togetherWith exit using sizeTransform
        },
        modifier = modifier,
        content = { content(it) }
    )
}