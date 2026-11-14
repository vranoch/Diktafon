package app.diktafon.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Zakladni text min. 16 sp pro bezne pouziti, min. 14 sp pro vedlejsi/sekundarni text
// a rozsirene/administracni obrazovky - viz NFR-UX-002 a ADR-015. Vychazi ze skalovatelnych
// `sp` jednotek vychozi Typography, jen upravuje velikosti/vahy.
private val diktafonTypography = Typography()
    .run {
        copy(
            titleLarge = titleLarge.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            titleMedium = titleMedium.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold),
            bodyLarge = bodyLarge.copy(fontSize = 16.sp, lineHeight = 24.sp),
            bodyMedium = bodyMedium.copy(fontSize = 14.sp, lineHeight = 20.sp),
            bodySmall = bodySmall.copy(fontSize = 14.sp, lineHeight = 18.sp),
            labelLarge = labelLarge.copy(fontSize = 16.sp, fontWeight = FontWeight.Bold),
            labelMedium = labelMedium.copy(fontSize = 14.sp),
        )
    }

private val diktafonColorScheme = lightColorScheme(
    primary = DiktafonAccent,
    onPrimary = DiktafonAccentOn,
    primaryContainer = DiktafonAccentContainer,
    onPrimaryContainer = DiktafonAccentContainerOn,
    secondary = DiktafonInk,
    onSecondary = DiktafonBackground,
    error = DiktafonDanger,
    onError = DiktafonDangerOn,
    errorContainer = DiktafonDangerContainer,
    onErrorContainer = DiktafonDangerContainerOn,
    background = DiktafonBackground,
    onBackground = DiktafonInk,
    surface = DiktafonBackground,
    onSurface = DiktafonInk,
    surfaceVariant = DiktafonSurfaceMuted,
    onSurfaceVariant = DiktafonInkMuted,
    outline = DiktafonOutline,
)

/**
 * Spolecny vzhled aplikace podle ADR-015 (vysoky kontrast, skalovatelny text, jeden
 * akcentni odstin pro primarni a jeden pro destruktivni akci). Pouzit misto holeho
 * `MaterialTheme` v [app.diktafon.MainActivity], aby vsechny obrazovky dedily stejna
 * pravidla bez nutnosti je rucne opakovat na kazde obrazovce.
 */
@Composable
fun DiktafonTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = diktafonColorScheme,
        typography = diktafonTypography,
        content = content,
    )
}
