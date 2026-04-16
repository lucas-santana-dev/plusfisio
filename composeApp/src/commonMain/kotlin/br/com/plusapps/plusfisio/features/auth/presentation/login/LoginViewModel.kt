package br.com.plusapps.plusfisio.features.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.plusapps.plusfisio.core.domain.onFailure
import br.com.plusapps.plusfisio.core.domain.onSuccess
import br.com.plusapps.plusfisio.core.presentation.input.isValidEmail
import br.com.plusapps.plusfisio.core.presentation.input.sanitizeEmail
import br.com.plusapps.plusfisio.features.auth.domain.SignInUseCase
import br.com.plusapps.plusfisio.features.auth.presentation.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plusfisio.composeapp.generated.resources.auth_error_email_invalid
import plusfisio.composeapp.generated.resources.auth_error_email_required
import plusfisio.composeapp.generated.resources.auth_error_password_required
import plusfisio.composeapp.generated.resources.auth_error_password_short
import plusfisio.composeapp.generated.resources.Res
import br.com.plusapps.plusfisio.core.presentation.text.UiText

/**
 * ViewModel inicial do fluxo de login.
 *
 * Mantem a validacao local no proprio estado de tela e delega a autenticacao
 * ao use case para preservar a separacao entre presentation e data.
 */
class LoginViewModel(
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _events = Channel<LoginEvent>(capacity = Channel.BUFFERED)
    val events: Flow<LoginEvent> = _events.receiveAsFlow()

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

            LoginAction.OnCreateAccountClicked -> emitEvent(LoginEvent.NavigateToSignUp)
            LoginAction.OnLoginClicked -> submit()
            LoginAction.OnForgotPasswordClicked -> emitEvent(LoginEvent.NavigateToForgotPassword)
        }
    }

    fun resetState() {
        _state.value = LoginState()
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

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            signInUseCase(
                email = sanitizeEmail(current.email),
                password = current.password
            ).onSuccess { session ->
                _state.update { state ->
                    state.copy(isLoading = false)
                }
                emitEvent(LoginEvent.Authenticated(session))
            }.onFailure { error ->
                _state.update { state ->
                    state.copy(isLoading = false)
                }
                emitEvent(LoginEvent.ShowMessage(error.toUiText()))
            }
        }
    }

    private fun validateEmail(value: String): UiText? {
        if (value.isBlank()) return UiText.Resource(Res.string.auth_error_email_required)
        if (!isValidEmail(value)) return UiText.Resource(Res.string.auth_error_email_invalid)
        return null
    }

    private fun validatePassword(value: String): UiText? {
        if (value.isBlank()) return UiText.Resource(Res.string.auth_error_password_required)
        if (value.length < 6) return UiText.Resource(Res.string.auth_error_password_short)
        return null
    }

    private fun emitEvent(event: LoginEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }
}
