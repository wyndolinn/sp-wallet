package com.wynndie.spwallet.sharedCore.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wynndie.spwallet.sharedCore.presentation.theme.AppTheme

@Composable
fun ColorTriangle(
    color: Color,
    size: Dp,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
    ) {
        val trianglePx = size.toPx()
        val radiusPx = 4.dp.toPx()
        val width = this.size.width

        val path = Path().apply {
            moveTo(width - trianglePx, 0f)
            lineTo(width - radiusPx, 0f)
            arcTo(
                rect = Rect(
                    left = width - 2 * radiusPx,
                    top = 0f,
                    right = width,
                    bottom = 2 * radiusPx
                ),
                startAngleDegrees = 270f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            lineTo(width, trianglePx)
            lineTo(width - trianglePx, 0f)
            close()
        }

        drawPath(
            path = path,
            color = color
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ColorTrianglePreview() {
    AppTheme {
        ColorTriangle(
            color = MaterialTheme.colorScheme.secondary,
            size = 16.dp
        )
    }
}
