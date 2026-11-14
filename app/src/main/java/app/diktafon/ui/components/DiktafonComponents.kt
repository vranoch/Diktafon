package app.diktafon.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.diktafon.ui.theme.DiktafonOutline

/**
 * Konzistentni hlavicka (zpet + domu) pro kazdou obrazovku bezneho/rozsireneho pouziti
 * (ADR-015). `onBack`/`onHome` null = mezera misto tlacitka, aby titulek zustal zarovnany.
 */
@Composable
fun AppHeader(
    title: String,
    onBack: (() -> Unit)? = null,
    onHome: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (onBack != null) {
            GlyphIconButton(glyph = "←", contentDescription = "Zpět", onClick = onBack)
        } else {
            Spacer(Modifier.size(48.dp))
        }
        Text(title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
        if (onHome != null) {
            GlyphIconButton(glyph = "⌂", contentDescription = "Domů", onClick = onHome)
        } else {
            Spacer(Modifier.size(48.dp))
        }
    }
}

/**
 * Kontextove kolate tlacitko min. 48x48 dp (ADR-015). Pouziva se pro akce bez vlastniho
 * popisku (zpet/domu/smazat), proto vyzaduje `contentDescription`.
 */
@Composable
fun GlyphIconButton(
    glyph: String,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.size(48.dp).semantics { this.contentDescription = contentDescription },
        contentPadding = PaddingValues(0.dp),
        shape = CircleShape,
        border = BorderStroke(1.5.dp, tint),
    ) {
        Text(glyph, fontSize = 18.sp, color = tint)
    }
}

/** Hlavni akce obrazovky - nejvyse jedna na bezne pouziti (ADR-015, hustota dle ADR-010). */
@Composable
fun PrimaryActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth().heightIn(min = 56.dp),
        shape = RoundedCornerShape(28.dp),
    ) {
        Text(text, style = MaterialTheme.typography.titleMedium)
    }
}

/** Vedlejsi akce obrazovky - obrysove tlacitko min. 48 dp vysoky. */
@Composable
fun SecondaryActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth().heightIn(min = 52.dp),
        shape = RoundedCornerShape(26.dp),
        border = BorderStroke(2.dp, DiktafonOutline),
    ) {
        Text(text, style = MaterialTheme.typography.bodyLarge, color = DiktafonOutline)
    }
}

/** Destruktivni/zrusujici akce - textove tlacitko s ikonou i textem, nikdy jen barvou. */
@Composable
fun DangerTextButton(
    text: String,
    glyph: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.error,
) {
    TextButton(onClick = onClick, modifier = modifier.heightIn(min = 48.dp)) {
        Text("$glyph $text", color = color, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
    }
}

/**
 * Stavovy odznak - vzdy ikona (glyf) + text, barva je jen doplnek (ADR-015: "barva nikdy
 * neni jediny nositel vyznamu").
 */
@Composable
fun StatusBadge(
    text: String,
    glyph: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = containerColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.5.dp, contentColor),
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
        ) {
            Text(glyph, fontWeight = FontWeight.Bold)
            Text(text, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }
    }
}

/** Nadpis sekce obrazovky (velka pismena, akcentni barva). */
@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text.uppercase(),
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier,
    )
}

/** Vedlejsi/informacni text pod ovladacimi prvky (min. 14 sp, nikdy jediny nosic vyznamu). */
@Composable
fun Caption(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        style = MaterialTheme.typography.bodyMedium,
        color = androidx.compose.ui.graphics.Color(0xFF49454F),
        modifier = modifier,
    )
}

/** Orámovaná karta se sekcí obrazovky (nadpis + obsah) - sjednocuje vzhled napříč obrazovkami. */
@Composable
fun SectionCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit,
) {
    Surface(
        border = BorderStroke(1.5.dp, DiktafonOutline),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp)) {
            SectionLabel(title)
            Spacer(Modifier.height(10.dp))
            content()
        }
    }
}
