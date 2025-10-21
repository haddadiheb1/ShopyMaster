package com.hanouti.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme()
private val DarkColors = darkColorScheme()

@Composable
fun HanoutiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    val context = LocalContext.current
    val cfg = context.resources.configuration
    val currentLocale = if (android.os.Build.VERSION.SDK_INT >= 24) cfg.locales.get(0) else cfg.locale
    val isArabic = currentLocale?.language == "ar"

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography.withLocale(isArabic),
        content = content
    )
}
