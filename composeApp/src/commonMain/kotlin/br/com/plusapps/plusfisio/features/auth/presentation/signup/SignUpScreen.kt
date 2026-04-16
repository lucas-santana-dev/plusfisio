package br.com.plusapps.plusfisio.features.auth.presentation.signup

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import br.com.plusapps.plusfisio.core.presentation.input.BrazilPhoneVisualTransformation
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
import plusfisio.composeapp.generated.resources.auth_confirm_password_label
import plusfisio.composeapp.generated.resources.auth_confirm_password_placeholder
import plusfisio.composeapp.generated.resources.auth_email_label
import plusfisio.composeapp.generated.resources.auth_email_placeholder
import plusfisio.composeapp.generated.resources.auth_hide_password
import plusfisio.composeapp.generated.resources.auth_name_label
import plusfisio.composeapp.generated.resources.auth_name_placeholder
import plusfisio.composeapp.generated.resources.auth_password_label
import plusfisio.composeapp.generated.resources.auth_password_placeholder
import plusfisio.composeapp.generated.resources.auth_show_password
import plusfisio.composeapp.generated.resources.auth_signup_button
import plusfisio.composeapp.generated.resources.auth_signup_description
import plusfisio.composeapp.generated.resources.auth_signup_have_account
import plusfisio.composeapp.generated.resources.auth_signup_title
import plusfisio.composeapp.generated.resources.auth_whatsapp_label
import plusfisio.composeapp.generated.resources.auth_whatsapp_placeholder

@Composable
fun SignUpRoot(
    onAuthenticated: (AuthSession) -> Unit,
    onNavigateToLogin: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: SignUpViewModel = koinViewModel()
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
                is SignUpEvent.Authenticated -> onAuthenticated(event.session)
                SignUpEvent.NavigateToLogin -> onNavigateToLogin()
                is SignUpEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message.resolve())
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) {
        SignUpScreen(
            state = state,
            onAction = viewModel::onAction,
            onBackClick = onNavigateToLogin,
            contentPadding = contentPadding
        )
    }
}

@Composable
fun SignUpScreen(
    state: SignUpState,
    onAction: (SignUpAction) -> Unit,
    onBackClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    InitialFlowScaffold(
        title = stringResource(Res.string.auth_signup_title),
        description = stringResource(Res.string.auth_signup_description),
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
                    value = state.name,
                    onValueChange = { onAction(SignUpAction.OnNameChanged(it)) },
                    label = stringResource(Res.string.auth_name_label),
                    placeholder = stringResource(Res.string.auth_name_placeholder),
                    supportingText = state.nameError?.asString(),
                    isError = state.nameError != null,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                Spacer(modifier = Modifier.height(PlusFisio.spacing.contentGap))
                PlusFisioTextField(
                    value = state.whatsapp,
                    onValueChange = { onAction(SignUpAction.OnWhatsappChanged(it)) },
                    label = stringResource(Res.string.auth_whatsapp_label),
                    placeholder = stringResource(Res.string.auth_whatsapp_placeholder),
                    supportingText = state.whatsappError?.asString(),
                    isError = state.whatsappError != null,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    visualTransformation = BrazilPhoneVisualTransformation
                )
                Spacer(modifier = Modifier.height(PlusFisio.spacing.contentGap))
                PlusFisioTextField(
                    value = state.email,
                    onValueChange = { onAction(SignUpAction.OnEmailChanged(it)) },
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
                SignUpPasswordField(
                    value = state.password,
                    onValueChange = { onAction(SignUpAction.OnPasswordChanged(it)) },
                    label = stringResource(Res.string.auth_password_label),
                    placeholder = stringResource(Res.string.auth_password_placeholder),
                    isVisible = state.isPasswordVisible,
                    supportingText = state.passwordError?.asString(),
                    isError = state.passwordError != null,
                    onToggleVisibility = { onAction(SignUpAction.OnTogglePasswordVisibility) }
                )
                Spacer(modifier = Modifier.height(PlusFisio.spacing.contentGap))
                SignUpPasswordField(
                    value = state.confirmPassword,
                    onValueChange = { onAction(SignUpAction.OnConfirmPasswordChanged(it)) },
                    label = stringResource(Res.string.auth_confirm_password_label),
                    placeholder = stringResource(Res.string.auth_confirm_password_placeholder),
                    isVisible = state.isConfirmPasswordVisible,
                    supportingText = state.confirmPasswordError?.asString(),
                    isError = state.confirmPasswordError != null,
                    onToggleVisibility = { onAction(SignUpAction.OnToggleConfirmPasswordVisibility) }
                )
                Spacer(modifier = Modifier.height(PlusFisio.spacing.grid4))
                PlusFisioButton(
                    text = stringResource(Res.string.auth_signup_button),
                    onClick = { onAction(SignUpAction.OnSignUpClicked) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading,
                    loading = state.isLoading
                )
                Spacer(modifier = Modifier.height(12.dp))
                PlusFisioButton(
                    text = stringResource(Res.string.auth_signup_have_account),
                    onClick = { onAction(SignUpAction.OnLoginClicked) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading,
                    kind = PlusFisioButtonKind.Secondary
                )
            }
        }
    }
}

@Composable
private fun SignUpPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    isVisible: Boolean,
    supportingText: String?,
    isError: Boolean,
    onToggleVisibility: () -> Unit
) {
    PlusFisioTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        supportingText = supportingText,
        isError = isError,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),
        visualTransformation = if (isVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingLabel = if (isVisible) {
            stringResource(Res.string.auth_hide_password)
        } else {
            stringResource(Res.string.auth_show_password)
        },
        onTrailingClick = onToggleVisibility
    )
}

@Preview
@Composable
private fun SignUpScreenPreview() {
    PlusFisioTheme {
        SignUpScreen(
            state = SignUpState(),
            onAction = {},
            onBackClick = {}
        )
    }
}
