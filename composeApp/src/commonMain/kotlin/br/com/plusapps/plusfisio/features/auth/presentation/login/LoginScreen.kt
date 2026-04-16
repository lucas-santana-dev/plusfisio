package br.com.plusapps.plusfisio.features.auth.presentation.login

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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioButton
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioButtonKind
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioTextField
import br.com.plusapps.plusfisio.core.presentation.text.UiText
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
import plusfisio.composeapp.generated.resources.auth_email_label
import plusfisio.composeapp.generated.resources.auth_email_placeholder
import plusfisio.composeapp.generated.resources.auth_error_email_invalid
import plusfisio.composeapp.generated.resources.auth_error_password_short
import plusfisio.composeapp.generated.resources.auth_forgot_password
import plusfisio.composeapp.generated.resources.auth_hide_password
import plusfisio.composeapp.generated.resources.auth_login_button
import plusfisio.composeapp.generated.resources.auth_login_create_account
import plusfisio.composeapp.generated.resources.auth_login_description
import plusfisio.composeapp.generated.resources.auth_login_loading
import plusfisio.composeapp.generated.resources.auth_login_placeholder_info
import plusfisio.composeapp.generated.resources.auth_login_title
import plusfisio.composeapp.generated.resources.auth_password_label
import plusfisio.composeapp.generated.resources.auth_show_password
import plusfisio.composeapp.generated.resources.auth_password_placeholder

@Composable
fun LoginRoot(
    onAuthenticated: (AuthSession) -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: LoginViewModel = koinViewModel()
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
                is LoginEvent.Authenticated -> onAuthenticated(event.session)
                LoginEvent.NavigateToSignUp -> onNavigateToSignUp()
                LoginEvent.NavigateToForgotPassword -> onNavigateToForgotPassword()
                is LoginEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message.resolve())
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) {
        LoginScreen(
            state = state,
            onAction = viewModel::onAction,
            contentPadding = contentPadding
        )
    }
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    InitialFlowScaffold(
        title = stringResource(Res.string.auth_login_title),
        description = stringResource(Res.string.auth_login_description),
        contentPadding = contentPadding
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
                    onValueChange = { onAction(LoginAction.OnEmailChanged(it)) },
                    label = stringResource(Res.string.auth_email_label),
                    placeholder = stringResource(Res.string.auth_email_placeholder),
                    supportingText = state.emailError?.asString(),
                    isError = state.emailError != null,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )
                Spacer(modifier = Modifier.height(PlusFisio.spacing.contentGap))
                PlusFisioTextField(
                    value = state.password,
                    onValueChange = { onAction(LoginAction.OnPasswordChanged(it)) },
                    label = stringResource(Res.string.auth_password_label),
                    placeholder = stringResource(Res.string.auth_password_placeholder),
                    supportingText = state.passwordError?.asString(),
                    isError = state.passwordError != null,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = if (state.isPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingLabel = if (state.isPasswordVisible) {
                        stringResource(Res.string.auth_hide_password)
                    } else {
                        stringResource(Res.string.auth_show_password)
                    },
                    onTrailingClick = { onAction(LoginAction.OnTogglePasswordVisibility) }
                )
                Spacer(modifier = Modifier.height(PlusFisio.spacing.grid3))
                PlusFisioButton(
                    text = if (state.isLoading) {
                        stringResource(Res.string.auth_login_loading)
                    } else {
                        stringResource(Res.string.auth_login_button)
                    },
                    onClick = { onAction(LoginAction.OnLoginClicked) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading,
                    loading = state.isLoading
                )
                Spacer(modifier = Modifier.height(12.dp))
                PlusFisioButton(
                    text = stringResource(Res.string.auth_login_create_account),
                    onClick = { onAction(LoginAction.OnCreateAccountClicked) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading,
                    kind = PlusFisioButtonKind.Secondary
                )
                Spacer(modifier = Modifier.height(PlusFisio.spacing.grid3))
                PlusFisioButton(
                    text = stringResource(Res.string.auth_forgot_password),
                    onClick = { onAction(LoginAction.OnForgotPasswordClicked) },
                    kind = PlusFisioButtonKind.Tertiary
                )
            }
        }
        Text(
            text = stringResource(Res.string.auth_login_placeholder_info),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    PlusFisioTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun LoginScreenErrorPreview() {
    PlusFisioTheme {
        LoginScreen(
            state = LoginState(
                email = "contato",
                password = "123",
                emailError = UiText.Resource(Res.string.auth_error_email_invalid),
                passwordError = UiText.Resource(Res.string.auth_error_password_short)
            ),
            onAction = {}
        )
    }
}
