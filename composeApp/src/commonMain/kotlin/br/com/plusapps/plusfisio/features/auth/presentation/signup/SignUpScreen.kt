package br.com.plusapps.plusfisio.features.auth.presentation.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    AuthBackground(contentPadding = contentPadding) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            AuthBranding(
                badgeText = stringResource(Res.string.auth_brand_badge),
                title = stringResource(Res.string.auth_brand_name),
                subtitle = stringResource(Res.string.auth_login_brand_subtitle)
            )
            Spacer(modifier = Modifier.height(28.dp))
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
                .padding(horizontal = 20.dp, vertical = 24.dp)
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
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = state.name,
                onValueChange = { onAction(SignUpAction.OnNameChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(Res.string.auth_name_label)) },
                singleLine = true,
                supportingText = state.nameError?.let { { Text(it.asString()) } },
                isError = state.nameError != null
            )
            Spacer(modifier = Modifier.height(14.dp))
            OutlinedTextField(
                value = state.email,
                onValueChange = { onAction(SignUpAction.OnEmailChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(Res.string.auth_email_label)) },
                singleLine = true,
                supportingText = state.emailError?.let { { Text(it.asString()) } },
                isError = state.emailError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(14.dp))
            PasswordField(
                value = state.password,
                onValueChange = { onAction(SignUpAction.OnPasswordChanged(it)) },
                label = stringResource(Res.string.auth_password_label),
                isVisible = state.isPasswordVisible,
                onToggleVisibility = { onAction(SignUpAction.OnTogglePasswordVisibility) },
                supportingText = state.passwordError?.asString(),
                isError = state.passwordError != null
            )
            Spacer(modifier = Modifier.height(14.dp))
            PasswordField(
                value = state.confirmPassword,
                onValueChange = { onAction(SignUpAction.OnConfirmPasswordChanged(it)) },
                label = stringResource(Res.string.auth_confirm_password_label),
                isVisible = state.isConfirmPasswordVisible,
                onToggleVisibility = { onAction(SignUpAction.OnToggleConfirmPasswordVisibility) },
                supportingText = state.confirmPasswordError?.asString(),
                isError = state.confirmPasswordError != null
            )
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = { onAction(SignUpAction.OnSignUpClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                enabled = !state.isLoading,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp,
                        modifier = Modifier.height(18.dp)
                    )
                } else {
                    Text(
                        text = stringResource(Res.string.auth_signup_button),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = { onAction(SignUpAction.OnLoginClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !state.isLoading,
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(stringResource(Res.string.auth_signup_have_account))
            }
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
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = true,
        supportingText = supportingText?.let { { Text(it) } },
        isError = isError,
        visualTransformation = if (isVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            Text(
                text = if (isVisible) {
                    stringResource(Res.string.auth_hide_password)
                } else {
                    stringResource(Res.string.auth_show_password)
                },
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onToggleVisibility() }
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
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
