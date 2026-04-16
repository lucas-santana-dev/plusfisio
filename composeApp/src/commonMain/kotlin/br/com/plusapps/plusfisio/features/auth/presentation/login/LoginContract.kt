package br.com.plusapps.plusfisio.features.auth.presentation.login

import androidx.compose.runtime.Stable
import br.com.plusapps.plusfisio.core.presentation.text.UiText
import br.com.plusapps.plusfisio.features.auth.domain.AuthSession

/**
 * Estado imutavel da tela de login.
 *
 * Concentra todos os dados necessarios para a renderizacao da tela e evita
 * estado de negocio espalhado em composables.
 */
@Stable
data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val emailError: UiText? = null,
    val passwordError: UiText? = null
)

/**
 * Acoes que a tela de login pode disparar.
 */
sealed interface LoginAction {
    data class OnEmailChanged(val value: String) : LoginAction
    data class OnPasswordChanged(val value: String) : LoginAction
    data object OnTogglePasswordVisibility : LoginAction
    data object OnLoginClicked : LoginAction
    data object OnCreateAccountClicked : LoginAction
    data object OnForgotPasswordClicked : LoginAction
}

/**
 * Eventos pontuais da tela, usados para feedbacks de UI.
 */
sealed interface LoginEvent {
    data class ShowMessage(val message: UiText) : LoginEvent
    data class Authenticated(val session: AuthSession) : LoginEvent
    data object NavigateToSignUp : LoginEvent
    data object NavigateToForgotPassword : LoginEvent
}
