package br.com.plusapps.plusfisio.features.onboarding.presentation.welcome

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioButton
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisio
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisioTheme
import br.com.plusapps.plusfisio.features.auth.presentation.components.InitialFlowScaffold
import br.com.plusapps.plusfisio.features.auth.presentation.components.InitialFlowSectionCard
import org.jetbrains.compose.resources.stringResource
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.auth_login_brand_subtitle
import plusfisio.composeapp.generated.resources.onboarding_welcome_feature_agenda_description
import plusfisio.composeapp.generated.resources.onboarding_welcome_feature_agenda_title
import plusfisio.composeapp.generated.resources.onboarding_welcome_feature_clients_description
import plusfisio.composeapp.generated.resources.onboarding_welcome_feature_clients_title
import plusfisio.composeapp.generated.resources.onboarding_welcome_feature_finance_description
import plusfisio.composeapp.generated.resources.onboarding_welcome_feature_finance_title
import plusfisio.composeapp.generated.resources.onboarding_welcome_primary_cta
import plusfisio.composeapp.generated.resources.onboarding_welcome_title

@Composable
fun WelcomeScreen(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    InitialFlowScaffold(
        title = stringResource(Res.string.onboarding_welcome_title),
        contentPadding = contentPadding,
        onBackClick = onBackClick
    ) {
        Text(
            text = stringResource(Res.string.auth_login_brand_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        InitialFlowSectionCard(
            title = stringResource(Res.string.onboarding_welcome_feature_agenda_title),
            description = stringResource(Res.string.onboarding_welcome_feature_agenda_description)
        )
        InitialFlowSectionCard(
            title = stringResource(Res.string.onboarding_welcome_feature_clients_title),
            description = stringResource(Res.string.onboarding_welcome_feature_clients_description)
        )
        InitialFlowSectionCard(
            title = stringResource(Res.string.onboarding_welcome_feature_finance_title),
            description = stringResource(Res.string.onboarding_welcome_feature_finance_description)
        )
        Spacer(modifier = Modifier.height(PlusFisio.spacing.grid1))
        PlusFisioButton(
            text = stringResource(Res.string.onboarding_welcome_primary_cta),
            onClick = onContinueClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun WelcomeScreenPreview() {
    PlusFisioTheme {
        WelcomeScreen(
            onBackClick = {},
            onContinueClick = {}
        )
    }
}
