package com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.img_logo
import com.wynndie.spwallet.sharedCore.presentation.components.AsyncImage
import org.jetbrains.compose.resources.painterResource

@Composable
fun UserImage(
    username: String,
    modifier: Modifier = Modifier
) {
    if (username.isNotBlank()) {
        AsyncImage(
            url = "https://avatars.spworlds.ru/face/$username?w=32",
            contentDescription = null,
            error = painterResource(Res.drawable.img_logo),
            modifier = modifier
        )
    } else {
        Image(
            painter = painterResource(Res.drawable.img_logo),
            contentDescription = null,
            modifier = modifier
        )
    }
}