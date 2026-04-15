package br.com.plusapps.plusfisio.features.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisioTheme
import br.com.plusapps.plusfisio.features.auth.domain.AuthSession
import br.com.plusapps.plusfisio.features.auth.presentation.components.AuthBackground
import org.jetbrains.compose.resources.stringResource
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.home_template_agenda
import plusfisio.composeapp.generated.resources.home_template_confirmations
import plusfisio.composeapp.generated.resources.home_template_cta_primary
import plusfisio.composeapp.generated.resources.home_template_cta_secondary
import plusfisio.composeapp.generated.resources.home_template_dashboard
import plusfisio.composeapp.generated.resources.home_template_empty
import plusfisio.composeapp.generated.resources.home_template_finance
import plusfisio.composeapp.generated.resources.home_template_greeting
import plusfisio.composeapp.generated.resources.home_template_sign_out
import plusfisio.composeapp.generated.resources.home_template_summary
import plusfisio.composeapp.generated.resources.home_template_title

@Composable
fun HomeTemplateScreen(
    session: AuthSession,
    onSignOutClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    AuthBackground(contentPadding = contentPadding) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = stringResource(Res.string.home_template_greeting, session.displayName ?: session.email),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.home_template_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(Res.string.home_template_dashboard),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))

            HomeTemplateCard(
                title = stringResource(Res.string.home_template_summary),
                description = stringResource(Res.string.home_template_empty)
            )
            Spacer(modifier = Modifier.height(16.dp))
            HomeTemplateCard(
                title = stringResource(Res.string.home_template_agenda),
                description = stringResource(Res.string.home_template_empty)
            )
            Spacer(modifier = Modifier.height(16.dp))
            HomeTemplateCard(
                title = stringResource(Res.string.home_template_confirmations),
                description = stringResource(Res.string.home_template_empty)
            )
            Spacer(modifier = Modifier.height(16.dp))
            HomeTemplateCard(
                title = stringResource(Res.string.home_template_finance),
                description = stringResource(Res.string.home_template_empty)
            )
            Spacer(modifier = Modifier.height(20.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.home_template_cta_primary),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(Res.string.home_template_cta_secondary),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(onClick = onSignOutClick) {
                            Text(stringResource(Res.string.home_template_sign_out))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeTemplateCard(
    title: String,
    description: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
private fun HomeTemplateScreenPreview() {
    PlusFisioTheme {
        HomeTemplateScreen(
            session = AuthSession(
                userId = "1",
                email = "owner@plusfisio.com",
                displayName = "Camila Rocha",
                studioId = "studio-demo",
                role = br.com.plusapps.plusfisio.core.domain.model.StudioUserRole.OwnerAdmin
            ),
            onSignOutClick = {}
        )
    }
}
