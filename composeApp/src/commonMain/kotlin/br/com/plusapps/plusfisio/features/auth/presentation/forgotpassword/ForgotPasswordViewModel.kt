package br.com.plusapps.plusfisio.features.auth.presentation.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.plusapps.plusfisio.core.domain.onFailure
import br.com.plusapps.plusfisio.core.domain.onSuccess
import br.com.plusapps.plusfisio.core.presentation.input.isValidEmail
import br.com.plusapps.plusfisio.core.presentation.input.sanitizeEmail
import br.com.plusapps.plusfisio.core.presentation.text.UiText
import br.com.plusapps.plusfisio.features.auth.domain.SendPasswordResetUseCase
import br.com.plusapps.plusfisio.features.auth.presentation.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.auth_error_email_invalid
import plusfisio.composeapp.generated.resources.auth_error_email_required
import plusfisio.composeapp.generated.resources.auth_forgot_password_success

class ForgotPasswordViewModel(
    private val sendPasswordResetUseCase: SendPasswordResetUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ForgotPasswordState())
    val state = _state.asStateFlow()

    private val _events = Channel<ForgotPasswordEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onAction(action: ForgotPasswordAction) {
        when (action) {
            is ForgotPasswordAction.OnEmailChanged -> _state.update {
                it.copy(email = action.value, emailError = null)
            }

            ForgotPasswordAction.OnSubmitClicked -> submit()
        }
    }

    fun resetState() {
        _state.value = ForgotPasswordState()
    }

    private fun submit() {
        val current = state.value
        val emailError = validateEmail(current.email)

        if (emailError != null) {
            _state.update { it.copy(emailError = emailError) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            sendPasswordResetUseCase(sanitizeEmail(current.email))
                .onSuccess {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isSent = true
                        )
                    }
                    _events.send(
                        ForgotPasswordEvent.ShowMessage(
                            UiText.Resource(Res.string.auth_forgot_password_success)
                        )
                    )
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false) }
                    _events.send(ForgotPasswordEvent.ShowMessage(error.toUiText()))
                }
        }
    }

    private fun validateEmail(value: String): UiText? {
        if (value.isBlank()) return UiText.Resource(Res.string.auth_error_email_required)
        if (!isValidEmail(value)) return UiText.Resource(Res.string.auth_error_email_invalid)
        return null
    }
}
