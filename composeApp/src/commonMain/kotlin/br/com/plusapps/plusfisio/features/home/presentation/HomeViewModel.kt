package br.com.plusapps.plusfisio.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.plusapps.plusfisio.core.presentation.text.UiText
import br.com.plusapps.plusfisio.features.auth.domain.AuthSession
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.home_action_feature_pending

class HomeViewModel : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _events = Channel<HomeEvent>(capacity = Channel.BUFFERED)
    val events: Flow<HomeEvent> = _events.receiveAsFlow()

    private var boundUserId: String? = null
    private var currentSession: AuthSession? = null

    fun bindSession(session: AuthSession) {
        if (boundUserId == session.userId) return

        boundUserId = session.userId
        currentSession = session
        loadHome()
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OnAgendaSectionClick -> emitEvent(HomeEvent.NavigateToAgenda)
            HomeAction.OnConfirmAppointmentClick -> emitFeaturePendingMessage()
            HomeAction.OnDismissMoreMenu -> _state.update {
                it.copy(
                    isMoreMenuVisible = false,
                    selectedBottomTab = HomeBottomTab.Home
                )
            }
            HomeAction.OnNewAppointmentClick -> emitEvent(HomeEvent.NavigateToNewAppointment)
            HomeAction.OnPendingConfirmationsClick -> emitFeaturePendingMessage()
            HomeAction.OnRetryClick -> loadHome()
            HomeAction.OnSignOutClick -> {
                _state.update {
                    it.copy(
                        isMoreMenuVisible = false,
                        selectedBottomTab = HomeBottomTab.Home
                    )
                }
                emitEvent(HomeEvent.RequestSignOut)
            }

            HomeAction.OnWhatsappClick -> {
                val phone = _state.value.nextAppointment?.whatsappPhone
                if (phone.isNullOrBlank()) {
                    emitFeaturePendingMessage()
                } else {
                    emitEvent(HomeEvent.OpenWhatsApp(phone))
                }
            }

            is HomeAction.OnBottomTabClick -> handleBottomTabClick(action.tab)
        }
    }

    private fun loadHome() {
        val session = currentSession ?: return

        _state.update { it.copy(isLoading = true, errorMessage = null) }
        _state.update {
            buildEmptyHomeState(session)
        }
    }

    private fun handleBottomTabClick(tab: HomeBottomTab) {
        when (tab) {
            HomeBottomTab.Home -> _state.update {
                it.copy(
                    selectedBottomTab = HomeBottomTab.Home,
                    isMoreMenuVisible = false
                )
            }

            HomeBottomTab.More -> _state.update {
                it.copy(
                    selectedBottomTab = HomeBottomTab.More,
                    isMoreMenuVisible = true
                )
            }

            HomeBottomTab.Agenda -> {
                _state.update {
                    it.copy(
                        selectedBottomTab = HomeBottomTab.Home,
                        isMoreMenuVisible = false
                    )
                }
                emitEvent(HomeEvent.NavigateToAgenda)
            }

            HomeBottomTab.Clients -> {
                _state.update {
                    it.copy(
                        selectedBottomTab = HomeBottomTab.Home,
                        isMoreMenuVisible = false
                    )
                }
                emitEvent(HomeEvent.NavigateToClients)
            }

            HomeBottomTab.Finance -> {
                _state.update {
                    it.copy(
                        selectedBottomTab = HomeBottomTab.Home,
                        isMoreMenuVisible = false
                    )
                }
                emitEvent(HomeEvent.NavigateToFinance)
            }
        }
    }

    private fun buildEmptyHomeState(session: AuthSession): HomeState {
        val firstName = session.displayName.firstNameOrFallback()

        return HomeState(
            professionalFirstName = firstName,
            isLoading = false,
            errorMessage = null,
            nextAppointment = null,
            metrics = HomeMetricsUi(
                appointments = HomeMetricCardUi(
                    title = "Atendimentos",
                    value = "00",
                    subtitle = "Crie o primeiro do dia",
                    tone = HomeMetricCardTone.Default
                ),
                pendings = HomeMetricCardUi(
                    title = "Pendências",
                    value = "00",
                    subtitle = "Nenhuma confirmação",
                    tone = HomeMetricCardTone.Highlight
                )
            ),
            attentionBanner = null,
            agendaSection = HomeSectionUi(
                title = "Agenda de hoje",
                description = "Você ainda não tem atendimentos agendados. Comece criando o primeiro horário.",
                badgeText = null,
                isHighlighted = true
            ),
            pendingConfirmationsSection = HomeSectionUi(
                title = "Confirmações pendentes",
                description = "Nenhuma confirmação pendente no momento.",
                badgeText = null,
                isHighlighted = false
            ),
            selectedBottomTab = HomeBottomTab.Home,
            isMoreMenuVisible = false,
            isFirstRunEmptyState = true
        )
    }

    private fun emitFeaturePendingMessage() {
        emitEvent(
            HomeEvent.ShowMessage(
                UiText.Resource(Res.string.home_action_feature_pending)
            )
        )
    }

    private fun emitEvent(event: HomeEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }
}

private fun String?.firstNameOrFallback(): String {
    val trimmed = this?.trim().orEmpty()
    if (trimmed.isBlank()) return "profissional"
    return trimmed.substringBefore(" ")
}
