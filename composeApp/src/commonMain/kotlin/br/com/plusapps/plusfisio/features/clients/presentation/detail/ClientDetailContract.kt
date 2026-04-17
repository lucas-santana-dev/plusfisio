package br.com.plusapps.plusfisio.features.clients.presentation.detail

import androidx.compose.runtime.Stable
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioStatusTone
import br.com.plusapps.plusfisio.core.presentation.text.UiText

@Stable
data class ClientDetailState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val clientId: String = "",
    val name: String = "",
    val subtitle: String = "",
    val statusLabel: String = "",
    val statusTone: PlusFisioStatusTone = PlusFisioStatusTone.Success,
    val nextStepDescription: String = "",
    val summaryDescription: String = "",
    val packageDescription: String = "",
    val historyDescription: String = ""
)

sealed interface ClientDetailAction {
    data object OnBackClicked : ClientDetailAction
    data object OnEditClicked : ClientDetailAction
    data object OnPresenceClicked : ClientDetailAction
    data object OnChargeClicked : ClientDetailAction
    data object OnWhatsappClicked : ClientDetailAction
    data object OnPackageClicked : ClientDetailAction
    data object OnHistoryClicked : ClientDetailAction
    data object OnRetryClicked : ClientDetailAction
    data object OnHomeTabClicked : ClientDetailAction
    data object OnAgendaTabClicked : ClientDetailAction
    data object OnFinanceTabClicked : ClientDetailAction
    data object OnMoreTabClicked : ClientDetailAction
}

sealed interface ClientDetailEvent {
    data object NavigateBack : ClientDetailEvent
    data object NavigateHome : ClientDetailEvent
    data class NavigateToEdit(val clientId: String) : ClientDetailEvent
    data class NavigateToPackage(val clientId: String) : ClientDetailEvent
    data class NavigateToHistory(val clientId: String) : ClientDetailEvent
    data class ShowMessage(val message: UiText) : ClientDetailEvent
}
