package com.wynndie.spwallet.sharedCore.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.roboto_bold
import com.wynndie.spwallet.sharedCore.roboto_medium
import com.wynndie.spwallet.sharedCore.roboto_regular
import com.wynndie.spwallet.sharedCore.roboto_semi_bold
import com.wynndie.spwallet.sharedCore.ubuntu_bold
import com.wynndie.spwallet.sharedCore.ubuntu_medium
import com.wynndie.spwallet.sharedCore.ubuntu_regular
import org.jetbrains.compose.resources.Font

val RobotoFontFamily
    @Composable get() = FontFamily(
        Font(
            resource = Res.font.roboto_regular,
            weight = FontWeight.Normal
        ),
        Font(
            resource = Res.font.roboto_medium,
            weight = FontWeight.Medium
        ),
        Font(
            resource = Res.font.roboto_semi_bold,
            weight = FontWeight.SemiBold
        ),
        Font(
            resource = Res.font.roboto_bold,
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
            fontWeight = FontWeight.Normal,
            fontSize = 22.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = UbuntuFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp
        ),


        titleLarge = TextStyle(
            fontFamily = RobotoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        ),
        titleMedium = TextStyle(
            fontFamily = RobotoFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
        ),
        titleSmall = TextStyle(
            fontFamily = RobotoFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        ),


        bodyMedium = TextStyle(
            fontFamily = RobotoFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        bodySmall = TextStyle(
            fontFamily = RobotoFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        ),


        labelLarge = TextStyle(
            fontFamily = RobotoFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        ),
        labelMedium = TextStyle(
            fontFamily = RobotoFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        ),
        labelSmall = TextStyle(
            fontFamily = RobotoFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        )
    )