package br.com.plusapps.plusfisio.features.clients.presentation.history

import androidx.compose.runtime.Stable
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioStatusTone
import br.com.plusapps.plusfisio.core.presentation.text.UiText
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientFilterChipUi
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientHistoryItemCardUi

@Stable
data class ClientHistoryState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val subtitle: String = "",
    val filters: List<ClientFilterChipUi> = emptyList(),
    val items: List<ClientHistoryItemCardUi> = emptyList()
)

sealed interface ClientHistoryAction {
    data class OnFilterSelected(val filter: ClientHistoryFilter) : ClientHistoryAction
    data object OnBackClicked : ClientHistoryAction
    data object OnRetryClicked : ClientHistoryAction
    data object OnHomeTabClicked : ClientHistoryAction
    data object OnAgendaTabClicked : ClientHistoryAction
    data object OnFinanceTabClicked : ClientHistoryAction
    data object OnMoreTabClicked : ClientHistoryAction
}

sealed interface ClientHistoryEvent {
    data object NavigateBack : ClientHistoryEvent
    data object NavigateHome : ClientHistoryEvent
    data class ShowMessage(val message: UiText) : ClientHistoryEvent
}

enum class ClientHistoryFilter(
    val tone: PlusFisioStatusTone
) {
    All(PlusFisioStatusTone.Info),
    Registration(PlusFisioStatusTone.Success),
    Contact(PlusFisioStatusTone.Info),
    Profile(PlusFisioStatusTone.Warning),
    Notes(PlusFisioStatusTone.Neutral)
}
