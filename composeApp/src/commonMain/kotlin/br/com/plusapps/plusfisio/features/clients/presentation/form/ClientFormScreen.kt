package br.com.plusapps.plusfisio.features.clients.presentation.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.plusapps.plusfisio.core.presentation.components.EmptyState
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioButton
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioTextField
import br.com.plusapps.plusfisio.core.presentation.input.BrazilPhoneVisualTransformation
import br.com.plusapps.plusfisio.core.presentation.text.asString
import br.com.plusapps.plusfisio.core.presentation.text.resolve
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisio
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisioTheme
import br.com.plusapps.plusfisio.features.auth.domain.AuthSession
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientsBottomBar
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientsShellTab
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientsTopBar
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.clients_birth_date_label
import plusfisio.composeapp.generated.resources.clients_email_label
import plusfisio.composeapp.generated.resources.clients_error_action
import plusfisio.composeapp.generated.resources.clients_error_title
import plusfisio.composeapp.generated.resources.clients_form_create_title
import plusfisio.composeapp.generated.resources.clients_form_edit_title
import plusfisio.composeapp.generated.resources.clients_form_save_create
import plusfisio.composeapp.generated.resources.clients_form_save_edit
import plusfisio.composeapp.generated.resources.clients_modality_label
import plusfisio.composeapp.generated.resources.clients_name_hint
import plusfisio.composeapp.generated.resources.clients_name_label
import plusfisio.composeapp.generated.resources.clients_notes_hint
import plusfisio.composeapp.generated.resources.clients_notes_label
import plusfisio.composeapp.generated.resources.clients_phone_hint
import plusfisio.composeapp.generated.resources.clients_phone_label
import plusfisio.composeapp.generated.resources.clients_responsible_label
import plusfisio.composeapp.generated.resources.clients_source_label

@Composable
fun ClientFormRoot(
    session: AuthSession,
    mode: ClientFormMode,
    clientId: String? = null,
    onNavigateBack: () -> Unit,
    onNavigateHome: () -> Unit,
    onClientSaved: (String) -> Unit,
    viewModel: ClientFormViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    DisposableEffect(viewModel) {
        onDispose {
            viewModel.resetState()
        }
    }

    LaunchedEffect(session.userId, mode, clientId) {
        viewModel.bind(session, mode, clientId)
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                ClientFormEvent.NavigateBack -> onNavigateBack()
                ClientFormEvent.NavigateHome -> onNavigateHome()
                is ClientFormEvent.ClientSaved -> onClientSaved(event.clientId)
                is ClientFormEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message.resolve())
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Box(
                modifier = Modifier.padding(
                    start = PlusFisio.spacing.screenHorizontal,
                    end = PlusFisio.spacing.screenHorizontal,
                    bottom = PlusFisio.spacing.grid3
                )
            ) {
                ClientsBottomBar(
                    selectedTab = ClientsShellTab.Clients,
                    onTabClick = { tab ->
                        when (tab) {
                            ClientsShellTab.Home -> viewModel.onAction(ClientFormAction.OnHomeTabClicked)
                            ClientsShellTab.Agenda -> viewModel.onAction(ClientFormAction.OnAgendaTabClicked)
                            ClientsShellTab.Clients -> Unit
                            ClientsShellTab.Finance -> viewModel.onAction(ClientFormAction.OnFinanceTabClicked)
                            ClientsShellTab.More -> viewModel.onAction(ClientFormAction.OnMoreTabClicked)
                        }
                    }
                )
            }
        }
    ) { scaffoldPadding ->
        ClientFormScreen(
            state = state,
            onAction = viewModel::onAction,
            onNavigateBack = onNavigateBack,
            contentPadding = scaffoldPadding
        )
    }
}

@Composable
fun ClientFormScreen(
    state: ClientFormState,
    onAction: (ClientFormAction) -> Unit,
    onNavigateBack: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (state.errorMessage != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(PlusFisio.spacing.grid4)
        ) {
            EmptyState(
                title = stringResource(Res.string.clients_error_title),
                description = state.errorMessage.asString(),
                actionLabel = stringResource(Res.string.clients_error_action),
                onActionClick = { onAction(ClientFormAction.OnRetryClicked) }
            )
        }
        return
    }

    androidx.compose.foundation.layout.Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = PlusFisio.spacing.screenHorizontal)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid4)
    ) {
        ClientsTopBar(
            title = if (state.mode == ClientFormMode.Create) {
                stringResource(Res.string.clients_form_create_title)
            } else {
                stringResource(Res.string.clients_form_edit_title)
            },
            onBackClick = onNavigateBack
        )

        androidx.compose.foundation.layout.Column(
            verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid3)
        ) {
            PlusFisioTextField(
                value = state.fullName,
                onValueChange = { onAction(ClientFormAction.OnFullNameChanged(it)) },
                label = stringResource(Res.string.clients_name_label),
                placeholder = stringResource(Res.string.clients_name_hint),
                supportingText = state.fullNameError?.asString(),
                isError = state.fullNameError != null,
                singleLine = true
            )
            PlusFisioTextField(
                value = state.phone,
                onValueChange = { onAction(ClientFormAction.OnPhoneChanged(it)) },
                label = stringResource(Res.string.clients_phone_label),
                placeholder = stringResource(Res.string.clients_phone_hint),
                supportingText = state.phoneError?.asString(),
                isError = state.phoneError != null,
                singleLine = true,
                visualTransformation = BrazilPhoneVisualTransformation
            )
            PlusFisioTextField(
                value = state.email,
                onValueChange = { onAction(ClientFormAction.OnEmailChanged(it)) },
                label = stringResource(Res.string.clients_email_label),
                supportingText = state.emailError?.asString(),
                isError = state.emailError != null,
                singleLine = true
            )
            PlusFisioTextField(
                value = state.birthDate,
                onValueChange = { onAction(ClientFormAction.OnBirthDateChanged(it)) },
                label = stringResource(Res.string.clients_birth_date_label),
                supportingText = state.birthDateError?.asString(),
                isError = state.birthDateError != null,
                singleLine = true
            )
            PlusFisioTextField(
                value = state.primaryModality,
                onValueChange = { onAction(ClientFormAction.OnPrimaryModalityChanged(it)) },
                label = stringResource(Res.string.clients_modality_label),
                singleLine = true
            )
            PlusFisioTextField(
                value = state.responsibleProfessional,
                onValueChange = { onAction(ClientFormAction.OnResponsibleProfessionalChanged(it)) },
                label = stringResource(Res.string.clients_responsible_label),
                singleLine = true
            )
            PlusFisioTextField(
                value = state.acquisitionSource,
                onValueChange = { onAction(ClientFormAction.OnAcquisitionSourceChanged(it)) },
                label = stringResource(Res.string.clients_source_label),
                singleLine = true
            )
            PlusFisioTextField(
                value = state.notes,
                onValueChange = { onAction(ClientFormAction.OnNotesChanged(it)) },
                label = stringResource(Res.string.clients_notes_label),
                placeholder = stringResource(Res.string.clients_notes_hint)
            )
            PlusFisioButton(
                text = if (state.mode == ClientFormMode.Create) {
                    stringResource(Res.string.clients_form_save_create)
                } else {
                    stringResource(Res.string.clients_form_save_edit)
                },
                onClick = { onAction(ClientFormAction.OnSaveClicked) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSaving,
                loading = state.isSaving
            )
        }
    }
}

@Preview
@Composable
private fun ClientFormScreenPreview() {
    PlusFisioTheme {
        ClientFormScreen(
            state = ClientFormState(
                fullName = "Juliana Martins",
                phone = "11900001111",
                email = "juliana@clinic.com",
                primaryModality = "Pilates solo",
                responsibleProfessional = "Ana Souza",
                acquisitionSource = "Indicacao"
            ),
            onAction = {},
            onNavigateBack = {}
        )
    }
}
