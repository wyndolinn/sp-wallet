package com.wynndie.spwallet.sharedCore.presentation.components.animations

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HidableContent(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimatedContent(
        targetState = visible,
        transitionSpec = {
            val enter = fadeIn(tween(durationMillis = ENTER_DURATION_MS, delayMillis = EXIT_DURATION_MS)) +
                    scaleIn(tween(durationMillis = ENTER_DURATION_MS, delayMillis = EXIT_DURATION_MS), SCALE_FACTOR)

            val exit = fadeOut(tween(durationMillis = EXIT_DURATION_MS)) +
                    scaleOut(tween(durationMillis = EXIT_DURATION_MS), SCALE_FACTOR)

            val sizeTransform = SizeTransform { _, _ -> tween(durationMillis = ENTER_DURATION_MS) }

            enter togetherWith exit using sizeTransform
        },
        modifier = modifier,
        content = { if (visible) content() }
    )
}

private const val ENTER_DURATION_MS = 50
private const val EXIT_DURATION_MS = 100
private const val SCALE_FACTOR = 0.95f