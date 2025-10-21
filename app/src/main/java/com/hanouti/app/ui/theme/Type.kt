package com.hanouti.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.GoogleFont.Provider
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle

private val BaseTypography = Typography()

private val provider = Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = com.hanouti.app.R.array.com_google_android_gms_fonts_certs
)

private val cairoFamily = FontFamily(
    Font(googleFont = GoogleFont("Cairo"), fontProvider = provider, weight = FontWeight.Normal, style = FontStyle.Normal),
    Font(googleFont = GoogleFont("Cairo"), fontProvider = provider, weight = FontWeight.Bold, style = FontStyle.Normal),
)

val Typography = Typography(
    displayLarge = BaseTypography.displayLarge,
    displayMedium = BaseTypography.displayMedium,
    displaySmall = BaseTypography.displaySmall,
    headlineLarge = BaseTypography.headlineLarge,
    headlineMedium = BaseTypography.headlineMedium,
    headlineSmall = BaseTypography.headlineSmall,
    titleLarge = BaseTypography.titleLarge,
    titleMedium = BaseTypography.titleMedium,
    titleSmall = BaseTypography.titleSmall,
    bodyLarge = BaseTypography.bodyLarge,
    bodyMedium = BaseTypography.bodyMedium,
    bodySmall = BaseTypography.bodySmall,
    labelLarge = BaseTypography.labelLarge,
    labelMedium = BaseTypography.labelMedium,
    labelSmall = BaseTypography.labelSmall
)

fun Typography.withLocale(isArabic: Boolean): Typography {
    val family = if (isArabic) cairoFamily else FontFamily.Default
    return Typography(
        displayLarge = displayLarge.copy(fontFamily = family),
        displayMedium = displayMedium.copy(fontFamily = family),
        displaySmall = displaySmall.copy(fontFamily = family),
        headlineLarge = headlineLarge.copy(fontFamily = family),
        headlineMedium = headlineMedium.copy(fontFamily = family),
        headlineSmall = headlineSmall.copy(fontFamily = family),
        titleLarge = titleLarge.copy(fontFamily = family),
        titleMedium = titleMedium.copy(fontFamily = family),
        titleSmall = titleSmall.copy(fontFamily = family),
        bodyLarge = bodyLarge.copy(fontFamily = family),
        bodyMedium = bodyMedium.copy(fontFamily = family),
        bodySmall = bodySmall.copy(fontFamily = family),
        labelLarge = labelLarge.copy(fontFamily = family),
        labelMedium = labelMedium.copy(fontFamily = family),
        labelSmall = labelSmall.copy(fontFamily = family),
    )
}
