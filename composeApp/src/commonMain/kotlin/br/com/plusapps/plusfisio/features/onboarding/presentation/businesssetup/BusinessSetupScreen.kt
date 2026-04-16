package br.com.plusapps.plusfisio.features.onboarding.presentation.businesssetup

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.plusapps.plusfisio.core.domain.model.BusinessType
import br.com.plusapps.plusfisio.core.domain.model.TeamSizeRange
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioButton
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioTextField
import br.com.plusapps.plusfisio.core.presentation.text.asString
import br.com.plusapps.plusfisio.core.presentation.text.resolve
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisio
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisioTheme
import br.com.plusapps.plusfisio.features.auth.domain.AuthSession
import br.com.plusapps.plusfisio.features.auth.presentation.components.AuthPrimaryCard
import br.com.plusapps.plusfisio.features.auth.presentation.components.InitialFlowScaffold
import org.koin.compose.viewmodel.koinViewModel
import org.jetbrains.compose.resources.stringResource
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.auth_signup_helper_info
import plusfisio.composeapp.generated.resources.onboarding_business_mixed
import plusfisio.composeapp.generated.resources.onboarding_business_physio
import plusfisio.composeapp.generated.resources.onboarding_business_pilates
import plusfisio.composeapp.generated.resources.onboarding_business_type_label
import plusfisio.composeapp.generated.resources.onboarding_city_state_label
import plusfisio.composeapp.generated.resources.onboarding_city_state_placeholder
import plusfisio.composeapp.generated.resources.onboarding_cta
import plusfisio.composeapp.generated.resources.onboarding_description
import plusfisio.composeapp.generated.resources.onboarding_sign_out
import plusfisio.composeapp.generated.resources.onboarding_studio_name_label
import plusfisio.composeapp.generated.resources.onboarding_studio_name_placeholder
import plusfisio.composeapp.generated.resources.onboarding_team_size_four_to_six
import plusfisio.composeapp.generated.resources.onboarding_team_size_label
import plusfisio.composeapp.generated.resources.onboarding_team_size_one_to_three
import plusfisio.composeapp.generated.resources.onboarding_team_size_seven_plus
import plusfisio.composeapp.generated.resources.onboarding_title

@Composable
fun BusinessSetupRoot(
    session: AuthSession,
    onCompleted: () -> Unit,
    onNavigateBack: () -> Unit,
    onSignOutClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: BusinessSetupViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    DisposableEffect(viewModel) {
        onDispose {
            viewModel.resetState()
        }
    }

    LaunchedEffect(viewModel, session.userId) {
        viewModel.events.collect { event ->
            when (event) {
                BusinessSetupEvent.Completed -> onCompleted()
                is BusinessSetupEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message.resolve())
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) {
        BusinessSetupScreen(
            state = state,
            onAction = viewModel::onAction,
            onBackClick = onNavigateBack,
            onSignOutClick = onSignOutClick,
            contentPadding = contentPadding
        )
    }
}

@Composable
fun BusinessSetupScreen(
    state: BusinessSetupState,
    onAction: (BusinessSetupAction) -> Unit,
    onBackClick: () -> Unit,
    onSignOutClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    InitialFlowScaffold(
        title = stringResource(Res.string.onboarding_title),
        description = stringResource(Res.string.onboarding_description),
        contentPadding = contentPadding,
        onBackClick = onBackClick
    ) {
        AuthPrimaryCard {
            androidx.compose.foundation.layout.Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = PlusFisio.spacing.grid4,
                        vertical = PlusFisio.spacing.grid4
                    )
            ) {
                PlusFisioTextField(
                    value = state.businessName,
                    onValueChange = { onAction(BusinessSetupAction.OnBusinessNameChanged(it)) },
                    label = stringResource(Res.string.onboarding_studio_name_label),
                    placeholder = stringResource(Res.string.onboarding_studio_name_placeholder),
                    supportingText = state.businessNameError?.asString(),
                    isError = state.businessNameError != null,
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(PlusFisio.spacing.contentGap))
                SelectionGroup(
                    label = stringResource(Res.string.onboarding_business_type_label),
                    supportingText = state.businessTypeError?.asString()
                ) {
                    SelectableOption(
                        label = stringResource(Res.string.onboarding_business_mixed),
                        selected = state.businessType == BusinessType.Mixed,
                        onClick = { onAction(BusinessSetupAction.OnBusinessTypeSelected(BusinessType.Mixed)) }
                    )
                    SelectableOption(
                        label = stringResource(Res.string.onboarding_business_pilates),
                        selected = state.businessType == BusinessType.Pilates,
                        onClick = { onAction(BusinessSetupAction.OnBusinessTypeSelected(BusinessType.Pilates)) }
                    )
                    SelectableOption(
                        label = stringResource(Res.string.onboarding_business_physio),
                        selected = state.businessType == BusinessType.Physiotherapy,
                        onClick = { onAction(BusinessSetupAction.OnBusinessTypeSelected(BusinessType.Physiotherapy)) }
                    )
                }
                Spacer(modifier = Modifier.height(PlusFisio.spacing.contentGap))
                SelectionGroup(
                    label = stringResource(Res.string.onboarding_team_size_label),
                    supportingText = state.teamSizeError?.asString()
                ) {
                    SelectableOption(
                        label = stringResource(Res.string.onboarding_team_size_one_to_three),
                        selected = state.teamSize == TeamSizeRange.OneToThree,
                        onClick = { onAction(BusinessSetupAction.OnTeamSizeSelected(TeamSizeRange.OneToThree)) }
                    )
                    SelectableOption(
                        label = stringResource(Res.string.onboarding_team_size_four_to_six),
                        selected = state.teamSize == TeamSizeRange.FourToSix,
                        onClick = { onAction(BusinessSetupAction.OnTeamSizeSelected(TeamSizeRange.FourToSix)) }
                    )
                    SelectableOption(
                        label = stringResource(Res.string.onboarding_team_size_seven_plus),
                        selected = state.teamSize == TeamSizeRange.SevenPlus,
                        onClick = { onAction(BusinessSetupAction.OnTeamSizeSelected(TeamSizeRange.SevenPlus)) }
                    )
                }
                Spacer(modifier = Modifier.height(PlusFisio.spacing.contentGap))
                PlusFisioTextField(
                    value = state.cityState,
                    onValueChange = { onAction(BusinessSetupAction.OnCityStateChanged(it)) },
                    label = stringResource(Res.string.onboarding_city_state_label),
                    placeholder = stringResource(Res.string.onboarding_city_state_placeholder),
                    supportingText = state.cityStateError?.asString(),
                    isError = state.cityStateError != null,
                    singleLine = true
                )
                if (state.cityStateSuggestions.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(PlusFisio.spacing.grid2))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.surface,
                        shape = PlusFisio.shapes.control,
                        border = BorderStroke(1.dp, PlusFisio.colors.line)
                    ) {
                        androidx.compose.foundation.layout.Column(
                            verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid1)
                        ) {
                            state.cityStateSuggestions.forEach { suggestion ->
                                SuggestionItem(
                                    label = suggestion,
                                    onClick = {
                                        onAction(
                                            BusinessSetupAction.OnCityStateSuggestionSelected(
                                                suggestion
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(PlusFisio.spacing.grid4))
                PlusFisioButton(
                    text = stringResource(Res.string.onboarding_cta),
                    onClick = { onAction(BusinessSetupAction.OnSubmitClicked) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isSubmitting,
                    loading = state.isSubmitting
                )
                Spacer(modifier = Modifier.height(12.dp))
                PlusFisioButton(
                    text = stringResource(Res.string.onboarding_sign_out),
                    onClick = onSignOutClick,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isSubmitting
                )
            }
        }
        Text(
            text = stringResource(Res.string.auth_signup_helper_info),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SelectionGroup(
    label: String,
    supportingText: String? = null,
    content: @Composable () -> Unit
) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(modifier = Modifier.height(PlusFisio.spacing.grid2))
    androidx.compose.foundation.layout.Column(
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(PlusFisio.spacing.grid2)
    ) {
        content()
    }
    if (supportingText != null) {
        Spacer(modifier = Modifier.height(PlusFisio.spacing.grid2))
        Text(
            text = supportingText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun SelectableOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = if (selected) {
            PlusFisio.colors.brandSoft
        } else {
            MaterialTheme.colorScheme.surface
        },
        shape = PlusFisio.shapes.control,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) MaterialTheme.colorScheme.primary else PlusFisio.colors.line
        )
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(
                horizontal = PlusFisio.spacing.grid4,
                vertical = PlusFisio.spacing.grid3
            ),
            style = MaterialTheme.typography.bodyLarge,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun SuggestionItem(
    label: String,
    onClick: () -> Unit
) {
    Text(
        text = label,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = PlusFisio.spacing.grid4,
                vertical = PlusFisio.spacing.grid3
            ),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Preview
@Composable
private fun BusinessSetupScreenPreview() {
    PlusFisioTheme {
        BusinessSetupScreen(
            state = BusinessSetupState(),
            onAction = {},
            onBackClick = {},
            onSignOutClick = {}
        )
    }
}
