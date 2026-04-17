package br.com.plusapps.plusfisio.features.clients.presentation.packageflow

import androidx.compose.runtime.Stable
import br.com.plusapps.plusfisio.core.presentation.text.UiText

@Stable
data class ClientPackageSessionsState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val packageTitle: String = "",
    val packageDescription: String = "",
    val usageSummary: String = "",
    val nextRecommendation: String = "",
    val historyItems: List<String> = emptyList()
)

sealed interface ClientPackageSessionsAction {
    data object OnBackClicked : ClientPackageSessionsAction
    data object OnRegisterAttendanceClicked : ClientPackageSessionsAction
    data object OnRegisterMissedClicked : ClientPackageSessionsAction
    data object OnRenewPackageClicked : ClientPackageSessionsAction
    data object OnAdjustBalanceClicked : ClientPackageSessionsAction
    data object OnRetryClicked : ClientPackageSessionsAction
    data object OnHomeTabClicked : ClientPackageSessionsAction
    data object OnAgendaTabClicked : ClientPackageSessionsAction
    data object OnFinanceTabClicked : ClientPackageSessionsAction
    data object OnMoreTabClicked : ClientPackageSessionsAction
}

sealed interface ClientPackageSessionsEvent {
    data object NavigateBack : ClientPackageSessionsEvent
    data object NavigateHome : ClientPackageSessionsEvent
    data class ShowMessage(val message: UiText) : ClientPackageSessionsEvent
}
