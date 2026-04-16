package br.com.plusapps.plusfisio.features.auth.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.plusapps.plusfisio.core.presentation.text.UiText
import br.com.plusapps.plusfisio.core.presentation.text.asString
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisio
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisioTheme
import br.com.plusapps.plusfisio.features.auth.presentation.components.AuthBackground
import br.com.plusapps.plusfisio.features.auth.presentation.components.AuthBranding
import br.com.plusapps.plusfisio.features.auth.presentation.components.AuthHighlightCard
import br.com.plusapps.plusfisio.features.auth.presentation.components.AuthPrimaryCard
import org.jetbrains.compose.resources.stringResource
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.auth_brand_badge
import plusfisio.composeapp.generated.resources.auth_brand_name
import plusfisio.composeapp.generated.resources.auth_email_label
import plusfisio.composeapp.generated.resources.auth_error_email_invalid
import plusfisio.composeapp.generated.resources.auth_error_password_short
import plusfisio.composeapp.generated.resources.auth_forgot_password
import plusfisio.composeapp.generated.resources.auth_hide_password
import plusfisio.composeapp.generated.resources.auth_highlight_description
import plusfisio.composeapp.generated.resources.auth_highlight_title
import plusfisio.composeapp.generated.resources.auth_login_brand_subtitle
import plusfisio.composeapp.generated.resources.auth_login_button
import plusfisio.composeapp.generated.resources.auth_login_description
import plusfisio.composeapp.generated.resources.auth_login_loading
import plusfisio.composeapp.generated.resources.auth_login_placeholder_info
import plusfisio.composeapp.generated.resources.auth_login_title
import plusfisio.composeapp.generated.resources.auth_password_label
import plusfisio.composeapp.generated.resources.auth_show_password

/**
 * Tela inicial de login.
 *
 * A UI recebe apenas estado e callbacks para continuar facil de testar e de
 * conectar com a autenticacao real depois.
 */
@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
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
            LoginCard(
                state = state,
                onAction = onAction
            )
        }
    }
}

@Composable
private fun LoginCard(
    state: LoginState,
    onAction: (LoginAction) -> Unit
) {
    val spacing = PlusFisio.spacing

    AuthPrimaryCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.grid5, vertical = spacing.grid6)
        ) {
            Text(
                text = stringResource(Res.string.auth_login_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(spacing.grid2))
            Text(
                text = stringResource(Res.string.auth_login_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(spacing.sectionGap))
            OutlinedTextField(
                value = state.email,
                onValueChange = { onAction(LoginAction.OnEmailChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(Res.string.auth_email_label)) },
                singleLine = true,
                supportingText = state.emailError?.let { { Text(it.asString()) } },
                isError = state.emailError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(spacing.contentGap))
            OutlinedTextField(
                value = state.password,
                onValueChange = { onAction(LoginAction.OnPasswordChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(Res.string.auth_password_label)) },
                singleLine = true,
                supportingText = state.passwordError?.let { { Text(it.asString()) } },
                isError = state.passwordError != null,
                visualTransformation = if (state.isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    Text(
                        text = if (state.isPasswordVisible) {
                            stringResource(Res.string.auth_hide_password)
                        } else {
                            stringResource(Res.string.auth_show_password)
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onAction(LoginAction.OnTogglePasswordVisibility) }
                            .padding(horizontal = spacing.grid2, vertical = spacing.grid1 + spacing.grid1 / 2),
                        style = MaterialTheme.typography.labelLarge,
                        color = PlusFisio.colors.brand
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.height(spacing.grid3))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { onAction(LoginAction.OnForgotPasswordClicked) }) {
                    Text(stringResource(Res.string.auth_forgot_password))
                }
            }
            Spacer(modifier = Modifier.height(spacing.grid3))
            Button(
                onClick = { onAction(LoginAction.OnLoginClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(spacing.controlHeight),
                shape = PlusFisio.shapes.control,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = if (state.isLoading) {
                        stringResource(Res.string.auth_login_loading)
                    } else {
                        stringResource(Res.string.auth_login_button)
                    },
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Spacer(modifier = Modifier.height(spacing.blockGap))
            Text(
                text = stringResource(Res.string.auth_login_placeholder_info),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(spacing.grid6 - spacing.grid1 / 2))
            AuthHighlightCard(
                title = stringResource(Res.string.auth_highlight_title),
                description = stringResource(Res.string.auth_highlight_description)
            )
        }
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
private fun LoginScreenWithContentPreview() {
    PlusFisioTheme {
        LoginScreen(
            state = LoginState(
                email = "contato@plusfisio.com",
                password = "123456",
                isPasswordVisible = true
            ),
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
