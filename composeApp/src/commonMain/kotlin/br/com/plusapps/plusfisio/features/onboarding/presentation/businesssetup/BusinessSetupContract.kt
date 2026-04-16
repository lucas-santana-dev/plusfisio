package br.com.plusapps.plusfisio.features.onboarding.presentation.businesssetup

import androidx.compose.runtime.Stable
import br.com.plusapps.plusfisio.core.domain.model.BusinessType
import br.com.plusapps.plusfisio.core.domain.model.TeamSizeRange
import br.com.plusapps.plusfisio.core.presentation.text.UiText

@Stable
data class BusinessSetupState(
    val businessName: String = "",
    val businessType: BusinessType? = null,
    val teamSize: TeamSizeRange? = null,
    val cityState: String = "",
    val cityStateSuggestions: List<String> = emptyList(),
    val isSubmitting: Boolean = false,
    val businessNameError: UiText? = null,
    val businessTypeError: UiText? = null,
    val teamSizeError: UiText? = null,
    val cityStateError: UiText? = null
)

sealed interface BusinessSetupAction {
    data class OnBusinessNameChanged(val value: String) : BusinessSetupAction
    data class OnBusinessTypeSelected(val value: BusinessType) : BusinessSetupAction
    data class OnTeamSizeSelected(val value: TeamSizeRange) : BusinessSetupAction
    data class OnCityStateChanged(val value: String) : BusinessSetupAction
    data class OnCityStateSuggestionSelected(val value: String) : BusinessSetupAction
    data object OnSubmitClicked : BusinessSetupAction
}

sealed interface BusinessSetupEvent {
    data object Completed : BusinessSetupEvent
    data class ShowMessage(val message: UiText) : BusinessSetupEvent
}
