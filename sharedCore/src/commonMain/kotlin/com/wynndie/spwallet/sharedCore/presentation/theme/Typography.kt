package com.wynndie.spwallet.sharedCore.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.noto_sans_bold
import com.wynndie.spwallet.sharedCore.noto_sans_medium
import com.wynndie.spwallet.sharedCore.noto_sans_regular
import com.wynndie.spwallet.sharedCore.ubuntu_bold
import com.wynndie.spwallet.sharedCore.ubuntu_medium
import com.wynndie.spwallet.sharedCore.ubuntu_regular
import org.jetbrains.compose.resources.Font

val NotoSansFontFamily
    @Composable get() = FontFamily(
        Font(
            resource = Res.font.noto_sans_regular,
            weight = FontWeight.Normal
        ),
        Font(
            resource = Res.font.noto_sans_medium,
            weight = FontWeight.Medium
        ),
        Font(
            resource = Res.font.noto_sans_bold,
            weight = FontWeight.Bold
        )
    )

val UbuntuFontFamily
    @Composable get() = FontFamily(
        Font(
            resource = Res.font.ubuntu_regular,
            weight = FontWeight.Normal
        ),
        Font(
            resource = Res.font.ubuntu_medium,
            weight = FontWeight.Medium
        ),
        Font(
            resource = Res.font.ubuntu_bold,
            weight = FontWeight.Bold
        )
    )

val Typography
    @Composable get() = Typography(
        headlineLarge = TextStyle(
            fontFamily = UbuntuFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = UbuntuFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = UbuntuFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp
        ),


        titleLarge = TextStyle(
            fontFamily = NotoSansFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        ),
        titleMedium = TextStyle(
            fontFamily = NotoSansFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
        ),
        titleSmall = TextStyle(
            fontFamily = NotoSansFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        ),


        bodyMedium = TextStyle(
            fontFamily = NotoSansFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        bodySmall = TextStyle(
            fontFamily = NotoSansFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        ),


        labelLarge = TextStyle(
            fontFamily = NotoSansFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        ),
        labelMedium = TextStyle(
            fontFamily = NotoSansFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        ),
        labelSmall = TextStyle(
            fontFamily = NotoSansFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        )
    )