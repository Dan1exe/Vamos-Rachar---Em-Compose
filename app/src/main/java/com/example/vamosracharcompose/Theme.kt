// Theme.kt
package com.example.vamosracharcompose


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightThemeColors = lightColorScheme(
    primary = LightPrimaryColor,
    background = LightBackgroundColor,
    onBackground = LightTextColor
)

private val DarkThemeColors = darkColorScheme(
    primary = DarkPrimaryColor,
    background = DarkBackgroundColor,
    onBackground = DarkTextColor
)

@Composable
fun MyAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkThemeColors else LightThemeColors,
        content = content
    )
}
