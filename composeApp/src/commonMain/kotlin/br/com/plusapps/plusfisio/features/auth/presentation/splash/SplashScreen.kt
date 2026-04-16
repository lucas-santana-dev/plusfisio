package br.com.plusapps.plusfisio.features.auth.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisio
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisioTheme
import br.com.plusapps.plusfisio.features.auth.presentation.components.AuthBackground
import org.jetbrains.compose.resources.stringResource
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.auth_brand_name
import plusfisio.composeapp.generated.resources.auth_splash_status
import plusfisio.composeapp.generated.resources.auth_splash_subtitle

@Composable
fun SplashScreen(
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val visible = remember { true }
    val animatedAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
    )
    val animatedScale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.95f,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
    )

    AuthBackground(contentPadding = contentPadding) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(
                    horizontal = PlusFisio.spacing.grid4,
                    vertical = PlusFisio.spacing.grid8
                )
                .graphicsLayer {
                    alpha = animatedAlpha
                    scaleX = animatedScale
                    scaleY = animatedScale
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f),
                shape = PlusFisio.shapes.panel,
                border = BorderStroke(1.dp, PlusFisio.colors.line)
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = PlusFisio.spacing.grid6,
                        vertical = PlusFisio.spacing.grid6
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SplashOrb()
                    Spacer(modifier = Modifier.height(PlusFisio.spacing.grid4))
                    Text(
                        text = stringResource(Res.string.auth_brand_name),
                        style = PlusFisio.typeScale.display32,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(PlusFisio.spacing.grid2))
                    Text(
                        text = stringResource(Res.string.auth_splash_subtitle),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(PlusFisio.spacing.grid5))
                    SplashSummaryCard()
                }
            }
            Spacer(modifier = Modifier.height(PlusFisio.spacing.grid5))
            Row(horizontalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid2)) {
                SplashChip(label = "Agenda diaria")
                SplashChip(label = "Financeiro", isSoft = true)
            }
            Spacer(modifier = Modifier.height(PlusFisio.spacing.grid5))
            Text(
                text = stringResource(Res.string.auth_splash_status),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(PlusFisio.spacing.grid3))
            Surface(
                color = PlusFisio.colors.line,
                shape = PlusFisio.shapes.pill
            ) {
                Row(modifier = Modifier.width(108.dp)) {
                    Spacer(
                        modifier = Modifier
                            .width(42.dp)
                            .height(6.dp)
                            .clip(PlusFisio.shapes.pill)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun SplashSummaryCard() {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = PlusFisio.shapes.card,
        border = BorderStroke(1.dp, PlusFisio.colors.line)
    ) {
        Column(
            modifier = Modifier.padding(PlusFisio.spacing.grid5)
        ) {
            Text(
                text = "Seu dia mais organizado",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(PlusFisio.spacing.grid2))
            Text(
                text = "Atendimentos, presenca e cobranca com menos atrito e mais clareza.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SplashChip(
    label: String,
    isSoft: Boolean = false
) {
    Surface(
        color = if (isSoft) PlusFisio.colors.brandSoft else MaterialTheme.colorScheme.surface,
        shape = PlusFisio.shapes.pill,
        border = BorderStroke(1.dp, PlusFisio.colors.line)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .width(6.dp)
                    .height(6.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun SplashOrb() {
    Surface(
        modifier = Modifier
            .width(120.dp)
            .height(120.dp),
        color = PlusFisio.colors.brandSoft,
        shape = CircleShape
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "PF",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    PlusFisioTheme {
        SplashScreen()
    }
}
