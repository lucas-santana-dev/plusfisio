package br.com.plusapps.plusfisio.features.auth.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisio
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisioTheme
import br.com.plusapps.plusfisio.features.auth.presentation.components.AuthBackground
import br.com.plusapps.plusfisio.features.auth.presentation.components.AuthBranding
import org.jetbrains.compose.resources.stringResource
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.auth_brand_badge
import plusfisio.composeapp.generated.resources.auth_brand_name
import plusfisio.composeapp.generated.resources.auth_splash_status
import plusfisio.composeapp.generated.resources.auth_splash_subtitle

/**
 * Splash screen inicial do app.
 *
 * Compartilha a mesma base visual da autenticacao, mas usa um conteudo central
 * proprio para a abertura do produto.
 */
@Composable
fun SplashScreen(
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val spacing = PlusFisio.spacing
    val visible = remember { true }
    val animatedAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
    )
    val animatedScale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.92f,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
    )

    AuthBackground(contentPadding = contentPadding) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = spacing.grid7, vertical = spacing.screenVertical)
                .graphicsLayer {
                    alpha = animatedAlpha
                    scaleX = animatedScale
                    scaleY = animatedScale
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = PlusFisio.shapes.panel,
                color = PlusFisio.colors.surface.copy(alpha = 0.88f),
                tonalElevation = spacing.grid1 + spacing.grid2
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = spacing.grid7, vertical = spacing.grid6),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AuthBranding(
                        badgeText = stringResource(Res.string.auth_brand_badge),
                        title = stringResource(Res.string.auth_brand_name),
                        subtitle = stringResource(Res.string.auth_splash_subtitle),
                        centered = true
                    )
                    Spacer(modifier = Modifier.height(spacing.grid4))
                    Text(
                        text = stringResource(Res.string.auth_splash_status),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = PlusFisio.colors.brand,
                        textAlign = TextAlign.Center
                    )
                }
            }
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
