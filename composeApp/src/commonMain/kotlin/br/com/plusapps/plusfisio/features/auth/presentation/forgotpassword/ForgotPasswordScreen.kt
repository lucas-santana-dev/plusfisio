package br.com.plusapps.plusfisio.features.auth.presentation.forgotpassword

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioButton
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioTextField
import br.com.plusapps.plusfisio.core.presentation.text.asString
import br.com.plusapps.plusfisio.core.presentation.text.resolve
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisio
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisioTheme
import br.com.plusapps.plusfisio.features.auth.presentation.components.AuthPrimaryCard
import br.com.plusapps.plusfisio.features.auth.presentation.components.InitialFlowScaffold
import org.koin.compose.viewmodel.koinViewModel
import org.jetbrains.compose.resources.stringResource
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.auth_email_label
import plusfisio.composeapp.generated.resources.auth_email_placeholder
import plusfisio.composeapp.generated.resources.auth_forgot_password_button
import plusfisio.composeapp.generated.resources.auth_forgot_password_description
import plusfisio.composeapp.generated.resources.auth_forgot_password_success
import plusfisio.composeapp.generated.resources.auth_forgot_password_title

@Composable
fun ForgotPasswordRoot(
    onNavigateBack: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: ForgotPasswordViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    DisposableEffect(viewModel) {
        onDispose {
            viewModel.resetState()
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is ForgotPasswordEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message.resolve())
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) {
        ForgotPasswordScreen(
            state = state,
            onAction = viewModel::onAction,
            onBackClick = onNavigateBack,
            contentPadding = contentPadding
        )
    }
}

@Composable
fun ForgotPasswordScreen(
    state: ForgotPasswordState,
    onAction: (ForgotPasswordAction) -> Unit,
    onBackClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    InitialFlowScaffold(
        title = stringResource(Res.string.auth_forgot_password_title),
        description = stringResource(Res.string.auth_forgot_password_description),
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
                    value = state.email,
                    onValueChange = { onAction(ForgotPasswordAction.OnEmailChanged(it)) },
                    label = stringResource(Res.string.auth_email_label),
                    placeholder = stringResource(Res.string.auth_email_placeholder),
                    supportingText = state.emailError?.asString(),
                    isError = state.emailError != null,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    )
                )
                Spacer(modifier = Modifier.height(PlusFisio.spacing.grid4))
                PlusFisioButton(
                    text = stringResource(Res.string.auth_forgot_password_button),
                    onClick = { onAction(ForgotPasswordAction.OnSubmitClicked) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading,
                    loading = state.isLoading
                )
            }
        }
        if (state.isSent) {
            Text(
                text = stringResource(Res.string.auth_forgot_password_success),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview
@Composable
private fun ForgotPasswordScreenPreview() {
    PlusFisioTheme {
        ForgotPasswordScreen(
            state = ForgotPasswordState(),
            onAction = {},
            onBackClick = {}
        )
    }
}
