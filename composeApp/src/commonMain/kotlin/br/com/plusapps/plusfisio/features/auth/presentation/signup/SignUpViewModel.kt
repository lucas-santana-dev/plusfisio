package br.com.plusapps.plusfisio.features.auth.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.plusapps.plusfisio.core.domain.onFailure
import br.com.plusapps.plusfisio.core.domain.onSuccess
import br.com.plusapps.plusfisio.core.presentation.text.UiText
import br.com.plusapps.plusfisio.features.auth.domain.SignUpUseCase
import br.com.plusapps.plusfisio.features.auth.presentation.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.auth_error_confirm_password_required
import plusfisio.composeapp.generated.resources.auth_error_email_invalid
import plusfisio.composeapp.generated.resources.auth_error_email_required
import plusfisio.composeapp.generated.resources.auth_error_name_required
import plusfisio.composeapp.generated.resources.auth_error_password_mismatch
import plusfisio.composeapp.generated.resources.auth_error_password_required
import plusfisio.composeapp.generated.resources.auth_error_password_short

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpState())
    val state: StateFlow<SignUpState> = _state.asStateFlow()

    private val _events = Channel<SignUpEvent>(capacity = Channel.BUFFERED)
    val events: Flow<SignUpEvent> = _events.receiveAsFlow()

    fun onAction(action: SignUpAction) {
        when (action) {
            is SignUpAction.OnNameChanged -> {
                _state.update { current ->
                    current.copy(name = action.value, nameError = null)
                }
            }

            is SignUpAction.OnEmailChanged -> {
                _state.update { current ->
                    current.copy(email = action.value, emailError = null)
                }
            }

            is SignUpAction.OnPasswordChanged -> {
                _state.update { current ->
                    current.copy(password = action.value, passwordError = null)
                }
            }

            is SignUpAction.OnConfirmPasswordChanged -> {
                _state.update { current ->
                    current.copy(confirmPassword = action.value, confirmPasswordError = null)
                }
            }

            SignUpAction.OnTogglePasswordVisibility -> {
                _state.update { current ->
                    current.copy(isPasswordVisible = !current.isPasswordVisible)
                }
            }

            SignUpAction.OnToggleConfirmPasswordVisibility -> {
                _state.update { current ->
                    current.copy(isConfirmPasswordVisible = !current.isConfirmPasswordVisible)
                }
            }

            SignUpAction.OnLoginClicked -> emitEvent(SignUpEvent.NavigateToLogin)
            SignUpAction.OnSignUpClicked -> submit()
        }
    }

    private fun submit() {
        val current = _state.value
        val nameError = validateName(current.name)
        val emailError = validateEmail(current.email)
        val passwordError = validatePassword(current.password)
        val confirmPasswordError = validateConfirmPassword(
            password = current.password,
            confirmPassword = current.confirmPassword
        )

        if (
            nameError != null ||
            emailError != null ||
            passwordError != null ||
            confirmPasswordError != null
        ) {
            _state.update {
                it.copy(
                    nameError = nameError,
                    emailError = emailError,
                    passwordError = passwordError,
                    confirmPasswordError = confirmPasswordError
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            signUpUseCase(
                name = current.name.trim(),
                email = current.email.trim(),
                password = current.password
            ).onSuccess { session ->
                _state.update { state -> state.copy(isLoading = false) }
                emitEvent(SignUpEvent.Authenticated(session))
            }.onFailure { error ->
                _state.update { state -> state.copy(isLoading = false) }
                emitEvent(SignUpEvent.ShowMessage(error.toUiText()))
            }
        }
    }

    private fun validateName(value: String): UiText? {
        if (value.isBlank()) return UiText.Resource(Res.string.auth_error_name_required)
        return null
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

    private fun validateConfirmPassword(
        password: String,
        confirmPassword: String
    ): UiText? {
        if (confirmPassword.isBlank()) return UiText.Resource(Res.string.auth_error_confirm_password_required)
        if (password != confirmPassword) return UiText.Resource(Res.string.auth_error_password_mismatch)
        return null
    }

    private fun emitEvent(event: SignUpEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }

    private companion object {
        val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    }
}
