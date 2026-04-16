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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.plusapps.plusfisio.core.domain.model.BusinessType
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioButton
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioButtonKind
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioTextField
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisio
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisioTheme
import br.com.plusapps.plusfisio.core.presentation.text.asString
import br.com.plusapps.plusfisio.core.presentation.text.resolve
import br.com.plusapps.plusfisio.features.auth.domain.AuthSession
import br.com.plusapps.plusfisio.features.auth.presentation.components.AuthBackground
import br.com.plusapps.plusfisio.features.auth.presentation.components.AuthPrimaryCard
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.onboarding_business_mixed
import plusfisio.composeapp.generated.resources.onboarding_business_physio
import plusfisio.composeapp.generated.resources.onboarding_business_pilates
import plusfisio.composeapp.generated.resources.onboarding_cta
import plusfisio.composeapp.generated.resources.onboarding_description
import plusfisio.composeapp.generated.resources.onboarding_phone_label
import plusfisio.composeapp.generated.resources.onboarding_sign_out
import plusfisio.composeapp.generated.resources.onboarding_studio_name_label
import plusfisio.composeapp.generated.resources.onboarding_timezone_label
import plusfisio.composeapp.generated.resources.onboarding_title
import plusfisio.composeapp.generated.resources.onboarding_whatsapp_label

@Composable
fun OnboardingRoot(
    session: AuthSession,
    onCompleted: () -> Unit,
    onSignOutClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                OnboardingEvent.Completed -> onCompleted()
                is OnboardingEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message.resolve())
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        OnboardingScreen(
            state = state,
            onAction = viewModel::onAction,
            onSignOutClick = onSignOutClick,
            contentPadding = contentPadding
        )
    }
}

@Composable
fun OnboardingScreen(
    state: OnboardingState,
    onAction: (OnboardingAction) -> Unit,
    onSignOutClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val spacing = PlusFisio.spacing

    AuthBackground(contentPadding = contentPadding) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = spacing.screenHorizontal, vertical = spacing.screenVertical),
            verticalArrangement = Arrangement.Center
        ) {
            AuthPrimaryCard {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = spacing.grid5, vertical = spacing.grid6)
                ) {
                    Text(
                        text = stringResource(Res.string.onboarding_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(spacing.grid3))
                    Text(
                        text = stringResource(Res.string.onboarding_description),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(spacing.grid4))
                    PlusFisioTextField(
                        value = state.studioName,
                        onValueChange = { onAction(OnboardingAction.OnStudioNameChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        label = stringResource(Res.string.onboarding_studio_name_label),
                        isError = state.studioNameError != null,
                        supportingText = state.studioNameError?.asString(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(spacing.contentGap))
                    BusinessTypeField(
                        selected = state.selectedBusinessType,
                        onSelected = { onAction(OnboardingAction.OnBusinessTypeSelected(it)) }
                    )
                    Spacer(modifier = Modifier.height(spacing.contentGap))
                    PlusFisioTextField(
                        value = state.phone,
                        onValueChange = { onAction(OnboardingAction.OnPhoneChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        label = stringResource(Res.string.onboarding_phone_label),
                        singleLine = true,
                        isError = state.phoneError != null,
                        supportingText = state.phoneError?.asString()
                    )
                    Spacer(modifier = Modifier.height(spacing.contentGap))
                    PlusFisioTextField(
                        value = state.whatsappPhone,
                        onValueChange = { onAction(OnboardingAction.OnWhatsappChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        label = stringResource(Res.string.onboarding_whatsapp_label),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(spacing.contentGap))
                    PlusFisioTextField(
                        value = state.timezone,
                        onValueChange = { onAction(OnboardingAction.OnTimezoneChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        label = stringResource(Res.string.onboarding_timezone_label),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(spacing.grid5))
                    PlusFisioButton(
                        text = stringResource(Res.string.onboarding_cta),
                        onClick = { onAction(OnboardingAction.OnSubmitClicked) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isSubmitting,
                        loading = state.isSubmitting
                    )
                    Spacer(modifier = Modifier.height(spacing.grid3))
                    PlusFisioButton(
                        text = stringResource(Res.string.onboarding_sign_out),
                        onClick = onSignOutClick,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isSubmitting,
                        kind = PlusFisioButtonKind.Secondary
                    )
                }
            }
        }
    }
}

@Composable
private fun BusinessTypeField(
    selected: BusinessType,
    onSelected: (BusinessType) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        BusinessTypeOption(
            label = stringResource(Res.string.onboarding_business_pilates),
            selected = selected == BusinessType.Pilates,
            onClick = { onSelected(BusinessType.Pilates) }
        )
        BusinessTypeOption(
            label = stringResource(Res.string.onboarding_business_physio),
            selected = selected == BusinessType.Physiotherapy,
            onClick = { onSelected(BusinessType.Physiotherapy) }
        )
        BusinessTypeOption(
            label = stringResource(Res.string.onboarding_business_mixed),
            selected = selected == BusinessType.Mixed,
            onClick = { onSelected(BusinessType.Mixed) }
        )
    }
}

@Composable
private fun BusinessTypeOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = MaterialTheme.shapes.medium,
        onClick = onClick
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            RadioButton(selected = selected, onClick = onClick)
            Text(
                text = label,
                modifier = Modifier.padding(start = 8.dp, top = 12.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    PlusFisioTheme {
        OnboardingScreen(
            state = OnboardingState(),
            onAction = {},
            onSignOutClick = {}
        )
    }
}
