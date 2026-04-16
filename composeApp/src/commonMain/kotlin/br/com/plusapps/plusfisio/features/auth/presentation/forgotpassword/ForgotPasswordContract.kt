package br.com.plusapps.plusfisio.features.auth.presentation.forgotpassword

import br.com.plusapps.plusfisio.core.presentation.text.UiText

data class ForgotPasswordState(
    val email: String = "",
    val isLoading: Boolean = false,
    val isSent: Boolean = false,
    val emailError: UiText? = null
)

sealed interface ForgotPasswordAction {
    data class OnEmailChanged(val value: String) : ForgotPasswordAction
    data object OnSubmitClicked : ForgotPasswordAction
}

sealed interface ForgotPasswordEvent {
    data class ShowMessage(val message: UiText) : ForgotPasswordEvent
}
