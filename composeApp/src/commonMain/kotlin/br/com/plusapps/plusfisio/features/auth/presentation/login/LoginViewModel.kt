package br.com.plusapps.plusfisio.features.auth.presentation.login

import androidx.lifecycle.ViewModel
import br.com.plusapps.plusfisio.core.presentation.text.UiText
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.auth_error_email_invalid
import plusfisio.composeapp.generated.resources.auth_error_email_required
import plusfisio.composeapp.generated.resources.auth_error_password_required
import plusfisio.composeapp.generated.resources.auth_error_password_short
import plusfisio.composeapp.generated.resources.auth_event_forgot_password_pending
import plusfisio.composeapp.generated.resources.auth_event_login_pending

/**
 * ViewModel inicial do fluxo de login.
 *
 * Nesta etapa ele faz apenas validacao local e emite feedbacks temporarios,
 * mantendo a tela pronta para a futura integracao com Firebase Auth.
 */
class LoginViewModel : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<LoginEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<LoginEvent> = _events.asSharedFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnEmailChanged -> {
                _state.update { current ->
                    current.copy(email = action.value, emailError = null)
                }
            }

            is LoginAction.OnPasswordChanged -> {
                _state.update { current ->
                    current.copy(password = action.value, passwordError = null)
                }
            }

            LoginAction.OnTogglePasswordVisibility -> {
                _state.update { current ->
                    current.copy(isPasswordVisible = !current.isPasswordVisible)
                }
            }

            LoginAction.OnLoginClicked -> submit()
            LoginAction.OnForgotPasswordClicked -> emitForgotPasswordMessage()
        }
    }

    private fun submit() {
        val current = _state.value
        val emailError = validateEmail(current.email)
        val passwordError = validatePassword(current.password)

        if (emailError != null || passwordError != null) {
            _state.update {
                it.copy(emailError = emailError, passwordError = passwordError)
            }
            return
        }

        emitEvent(LoginEvent.ShowMessage(UiText.Resource(Res.string.auth_event_login_pending)))
    }

    private fun emitForgotPasswordMessage() {
        emitEvent(LoginEvent.ShowMessage(UiText.Resource(Res.string.auth_event_forgot_password_pending)))
    }

    private fun validateEmail(value: String): UiText? {
        if (value.isBlank()) return UiText.Resource(Res.string.auth_error_email_required)
        if (!EMAIL_REGEX.matches(value.trim())) return UiText.Resource(Res.string.auth_error_email_invalid)
        return null
    }

    private fun validatePassword(value: String): UiText? {
        if (value.isBlank()) return UiText.Resource(Res.string.auth_error_password_required)
        if (value.length < 6) return UiText.Resource(Res.string.auth_error_password_short)
        return null
    }

    private fun emitEvent(event: LoginEvent) {
        _events.tryEmit(event)
    }

    private companion object {
        val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    }
}
