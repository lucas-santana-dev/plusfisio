package br.com.plusapps.plusfisio.features.onboarding.presentation

import androidx.compose.runtime.Stable
import br.com.plusapps.plusfisio.core.domain.model.BusinessType
import br.com.plusapps.plusfisio.core.presentation.text.UiText

@Stable
data class OnboardingState(
    val studioName: String = "",
    val selectedBusinessType: BusinessType = BusinessType.Pilates,
    val phone: String = "",
    val whatsappPhone: String = "",
    val timezone: String = "America/Sao_Paulo",
    val isSubmitting: Boolean = false,
    val studioNameError: UiText? = null,
    val phoneError: UiText? = null
)

sealed interface OnboardingAction {
    data class OnStudioNameChanged(val value: String) : OnboardingAction
    data class OnBusinessTypeSelected(val value: BusinessType) : OnboardingAction
    data class OnPhoneChanged(val value: String) : OnboardingAction
    data class OnWhatsappChanged(val value: String) : OnboardingAction
    data class OnTimezoneChanged(val value: String) : OnboardingAction
    data object OnSubmitClicked : OnboardingAction
}

sealed interface OnboardingEvent {
    data object Completed : OnboardingEvent
    data class ShowMessage(val message: UiText) : OnboardingEvent
}
