package br.com.plusapps.plusfisio.features.home.presentation

import androidx.compose.runtime.Immutable
import br.com.plusapps.plusfisio.core.presentation.text.UiText

@Immutable
data class HomeState(
    val professionalFirstName: String = "",
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val nextAppointment: HomeNextAppointmentUi? = null,
    val metrics: HomeMetricsUi = HomeMetricsUi(),
    val attentionBanner: HomeAttentionBannerUi? = null,
    val agendaSection: HomeSectionUi = HomeSectionUi(
        title = "",
        description = "",
        badgeText = null,
        isHighlighted = false
    ),
    val pendingConfirmationsSection: HomeSectionUi = HomeSectionUi(
        title = "",
        description = "",
        badgeText = null,
        isHighlighted = false
    ),
    val selectedBottomTab: HomeBottomTab = HomeBottomTab.Home,
    val isMoreMenuVisible: Boolean = false,
    val isFirstRunEmptyState: Boolean = true
)

sealed interface HomeAction {
    data object OnRetryClick : HomeAction
    data object OnConfirmAppointmentClick : HomeAction
    data object OnWhatsappClick : HomeAction
    data object OnNewAppointmentClick : HomeAction
    data object OnAgendaSectionClick : HomeAction
    data object OnPendingConfirmationsClick : HomeAction
    data class OnBottomTabClick(val tab: HomeBottomTab) : HomeAction
    data object OnDismissMoreMenu : HomeAction
    data object OnSignOutClick : HomeAction
}

sealed interface HomeEvent {
    data object NavigateToAgenda : HomeEvent
    data object NavigateToClients : HomeEvent
    data object NavigateToFinance : HomeEvent
    data object NavigateToNewAppointment : HomeEvent
    data object RequestSignOut : HomeEvent
    data class OpenWhatsApp(val phoneNumber: String) : HomeEvent
    data class ShowMessage(val message: UiText) : HomeEvent
}

enum class HomeBottomTab {
    Home,
    Agenda,
    Clients,
    Finance,
    More
}

@Immutable
data class HomeNextAppointmentUi(
    val chipLabel: String,
    val title: String,
    val details: String,
    val primaryActionLabel: String,
    val secondaryActionLabel: String,
    val whatsappPhone: String? = null
)

@Immutable
data class HomeMetricsUi(
    val appointments: HomeMetricCardUi = HomeMetricCardUi(
        title = "",
        value = "",
        subtitle = "",
        tone = HomeMetricCardTone.Default
    ),
    val pendings: HomeMetricCardUi = HomeMetricCardUi(
        title = "",
        value = "",
        subtitle = "",
        tone = HomeMetricCardTone.Highlight
    )
)

@Immutable
data class HomeMetricCardUi(
    val title: String,
    val value: String,
    val subtitle: String,
    val tone: HomeMetricCardTone
)

enum class HomeMetricCardTone {
    Default,
    Highlight
}

@Immutable
data class HomeAttentionBannerUi(
    val message: String
)

@Immutable
data class HomeSectionUi(
    val title: String,
    val description: String,
    val badgeText: String?,
    val isHighlighted: Boolean
)
