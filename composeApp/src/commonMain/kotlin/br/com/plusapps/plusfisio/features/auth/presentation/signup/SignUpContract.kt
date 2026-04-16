package br.com.plusapps.plusfisio.features.auth.presentation.signup

import androidx.compose.runtime.Stable
import br.com.plusapps.plusfisio.core.presentation.text.UiText
import br.com.plusapps.plusfisio.features.auth.domain.AuthSession

@Stable
data class SignUpState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val nameError: UiText? = null,
    val emailError: UiText? = null,
    val passwordError: UiText? = null,
    val confirmPasswordError: UiText? = null
)

sealed interface SignUpAction {
    data class OnNameChanged(val value: String) : SignUpAction
    data class OnEmailChanged(val value: String) : SignUpAction
    data class OnPasswordChanged(val value: String) : SignUpAction
    data class OnConfirmPasswordChanged(val value: String) : SignUpAction
    data object OnTogglePasswordVisibility : SignUpAction
    data object OnToggleConfirmPasswordVisibility : SignUpAction
    data object OnSignUpClicked : SignUpAction
    data object OnLoginClicked : SignUpAction
}

sealed interface SignUpEvent {
    data class ShowMessage(val message: UiText) : SignUpEvent
    data class Authenticated(val session: AuthSession) : SignUpEvent
    data object NavigateToLogin : SignUpEvent
}
