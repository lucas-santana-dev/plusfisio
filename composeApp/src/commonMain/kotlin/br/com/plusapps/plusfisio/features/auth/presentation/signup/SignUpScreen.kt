package br.com.plusapps.plusfisio.features.auth.presentation.signup

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioButton
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioButtonKind
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioTextField
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisio
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisioTheme
import br.com.plusapps.plusfisio.core.presentation.text.asString
import br.com.plusapps.plusfisio.core.presentation.text.resolve
import br.com.plusapps.plusfisio.features.auth.domain.AuthSession
import br.com.plusapps.plusfisio.features.auth.presentation.components.AuthBackground
import br.com.plusapps.plusfisio.features.auth.presentation.components.AuthBranding
import br.com.plusapps.plusfisio.features.auth.presentation.components.AuthHighlightCard
import br.com.plusapps.plusfisio.features.auth.presentation.components.AuthPrimaryCard
import org.koin.compose.viewmodel.koinViewModel
import org.jetbrains.compose.resources.stringResource
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.auth_brand_badge
import plusfisio.composeapp.generated.resources.auth_brand_name
import plusfisio.composeapp.generated.resources.auth_confirm_password_label
import plusfisio.composeapp.generated.resources.auth_email_label
import plusfisio.composeapp.generated.resources.auth_hide_password
import plusfisio.composeapp.generated.resources.auth_login_brand_subtitle
import plusfisio.composeapp.generated.resources.auth_name_label
import plusfisio.composeapp.generated.resources.auth_password_label
import plusfisio.composeapp.generated.resources.auth_show_password
import plusfisio.composeapp.generated.resources.auth_signup_button
import plusfisio.composeapp.generated.resources.auth_signup_description
import plusfisio.composeapp.generated.resources.auth_signup_have_account
import plusfisio.composeapp.generated.resources.auth_signup_helper_info
import plusfisio.composeapp.generated.resources.auth_signup_highlight_description
import plusfisio.composeapp.generated.resources.auth_signup_highlight_title
import plusfisio.composeapp.generated.resources.auth_signup_title

@Composable
fun SignUpRoot(
    onAuthenticated: (AuthSession) -> Unit,
    onNavigateToLogin: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: SignUpViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is SignUpEvent.Authenticated -> onAuthenticated(event.session)
                SignUpEvent.NavigateToLogin -> onNavigateToLogin()
                is SignUpEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message.resolve())
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        SignUpScreen(
            state = state,
            onAction = viewModel::onAction,
            contentPadding = contentPadding
        )
    }
}

@Composable
fun SignUpScreen(
    state: SignUpState,
    onAction: (SignUpAction) -> Unit,
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
            AuthBranding(
                badgeText = stringResource(Res.string.auth_brand_badge),
                title = stringResource(Res.string.auth_brand_name),
                subtitle = stringResource(Res.string.auth_login_brand_subtitle)
            )
            Spacer(modifier = Modifier.height(spacing.grid7))
            SignUpCard(
                state = state,
                onAction = onAction
            )
        }
    }
}

@Composable
private fun SignUpCard(
    state: SignUpState,
    onAction: (SignUpAction) -> Unit
) {
    AuthPrimaryCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PlusFisio.spacing.grid5, vertical = PlusFisio.spacing.grid6)
        ) {
            Text(
                text = stringResource(Res.string.auth_signup_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.auth_signup_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(PlusFisio.spacing.grid5))
            PlusFisioTextField(
                value = state.name,
                onValueChange = { onAction(SignUpAction.OnNameChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.auth_name_label),
                singleLine = true,
                supportingText = state.nameError?.asString(),
                isError = state.nameError != null
            )
            Spacer(modifier = Modifier.height(PlusFisio.spacing.contentGap))
            PlusFisioTextField(
                value = state.email,
                onValueChange = { onAction(SignUpAction.OnEmailChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.auth_email_label),
                singleLine = true,
                supportingText = state.emailError?.asString(),
                isError = state.emailError != null
            )
            Spacer(modifier = Modifier.height(PlusFisio.spacing.contentGap))
            PasswordField(
                value = state.password,
                onValueChange = { onAction(SignUpAction.OnPasswordChanged(it)) },
                label = stringResource(Res.string.auth_password_label),
                isVisible = state.isPasswordVisible,
                onToggleVisibility = { onAction(SignUpAction.OnTogglePasswordVisibility) },
                supportingText = state.passwordError?.asString(),
                isError = state.passwordError != null
            )
            Spacer(modifier = Modifier.height(PlusFisio.spacing.contentGap))
            PasswordField(
                value = state.confirmPassword,
                onValueChange = { onAction(SignUpAction.OnConfirmPasswordChanged(it)) },
                label = stringResource(Res.string.auth_confirm_password_label),
                isVisible = state.isConfirmPasswordVisible,
                onToggleVisibility = { onAction(SignUpAction.OnToggleConfirmPasswordVisibility) },
                supportingText = state.confirmPasswordError?.asString(),
                isError = state.confirmPasswordError != null
            )
            Spacer(modifier = Modifier.height(PlusFisio.spacing.grid4))
            PlusFisioButton(
                text = stringResource(Res.string.auth_signup_button),
                onClick = { onAction(SignUpAction.OnSignUpClicked) },
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = !state.isLoading,
                loading = state.isLoading
            )
            Spacer(modifier = Modifier.height(12.dp))
            PlusFisioButton(
                text = stringResource(Res.string.auth_signup_have_account),
                onClick = { onAction(SignUpAction.OnLoginClicked) },
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = !state.isLoading,
                kind = PlusFisioButtonKind.Secondary
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = stringResource(Res.string.auth_signup_helper_info),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(22.dp))
            AuthHighlightCard(
                title = stringResource(Res.string.auth_signup_highlight_title),
                description = stringResource(Res.string.auth_signup_highlight_description)
            )
        }
    }
}

@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isVisible: Boolean,
    onToggleVisibility: () -> Unit,
    supportingText: String?,
    isError: Boolean
) {
    PlusFisioTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = label,
        singleLine = true,
        supportingText = supportingText,
        isError = isError,
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
            onAction = {}
        )
    }
}
