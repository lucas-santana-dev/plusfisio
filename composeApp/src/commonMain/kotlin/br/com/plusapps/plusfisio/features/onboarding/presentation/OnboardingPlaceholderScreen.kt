package br.com.plusapps.plusfisio.features.onboarding.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisioTheme
import br.com.plusapps.plusfisio.features.auth.domain.AuthSession
import br.com.plusapps.plusfisio.features.auth.presentation.components.AuthBackground
import org.jetbrains.compose.resources.stringResource
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.onboarding_placeholder_body
import plusfisio.composeapp.generated.resources.onboarding_placeholder_cta
import plusfisio.composeapp.generated.resources.onboarding_placeholder_email
import plusfisio.composeapp.generated.resources.onboarding_placeholder_sign_out
import plusfisio.composeapp.generated.resources.onboarding_placeholder_title

@Composable
fun OnboardingPlaceholderScreen(
    session: AuthSession,
    onSignOutClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    AuthBackground(contentPadding = contentPadding) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.onboarding_placeholder_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(Res.string.onboarding_placeholder_body),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = stringResource(Res.string.onboarding_placeholder_email, session.email),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(Res.string.onboarding_placeholder_cta))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = onSignOutClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(Res.string.onboarding_placeholder_sign_out))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun OnboardingPlaceholderScreenPreview() {
    PlusFisioTheme {
        OnboardingPlaceholderScreen(
            session = AuthSession(
                userId = "1",
                email = "setup@plusfisio.com",
                displayName = "Bruno Lima",
                studioId = null
            ),
            onSignOutClick = {}
        )
    }
}
