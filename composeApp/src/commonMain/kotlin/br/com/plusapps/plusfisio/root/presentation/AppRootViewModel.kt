package br.com.plusapps.plusfisio.root.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.plusapps.plusfisio.features.auth.domain.AuthSession
import br.com.plusapps.plusfisio.features.auth.domain.ObserveAuthSessionUseCase
import br.com.plusapps.plusfisio.features.auth.domain.SignOutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppRootViewModel(
    private val observeAuthSessionUseCase: ObserveAuthSessionUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AppRootState())
    val state: StateFlow<AppRootState> = _state.asStateFlow()
    private var clientsRefreshVersion: Int = 0

    init {
        refreshDestination()
    }

    fun onSignUpRequested() {
        _state.update {
            it.copy(
                isResolving = false,
                route = AppRoute.SignUp,
                session = null
            )
        }
    }

    fun onForgotPasswordRequested() {
        _state.update {
            it.copy(
                isResolving = false,
                route = AppRoute.ForgotPassword
            )
        }
    }

    fun onLoginRequested() {
        _state.update {
            it.copy(
                isResolving = false,
                route = AppRoute.Login,
                session = null
            )
        }
    }

    fun onLoginCompleted(session: AuthSession) {
        _state.update {
            it.copy(
                isResolving = false,
                session = session,
                route = session.toRoute()
            )
        }
    }

    fun onSignUpCompleted(session: AuthSession) {
        onLoginCompleted(session)
    }

    fun onOnboardingCompleted() {
        refreshDestination()
    }

    fun onHomeRequested() {
        _state.update {
            it.copy(
                isResolving = false,
                route = AppRoute.Home
            )
        }
    }

    fun onClientsRequested() {
        _state.update {
            it.copy(
                isResolving = false,
                route = AppRoute.ClientsList(refreshVersion = clientsRefreshVersion)
            )
        }
    }

    fun onClientCreateRequested() {
        _state.update {
            it.copy(
                isResolving = false,
                route = AppRoute.ClientCreate
            )
        }
    }

    fun onClientDetailRequested(clientId: String) {
        _state.update {
            it.copy(
                isResolving = false,
                route = AppRoute.ClientDetail(
                    clientId = clientId,
                    refreshVersion = clientsRefreshVersion
                )
            )
        }
    }

    fun onClientEditRequested(clientId: String) {
        _state.update {
            it.copy(
                isResolving = false,
                route = AppRoute.ClientEdit(clientId = clientId)
            )
        }
    }

    fun onClientPackageRequested(clientId: String) {
        _state.update {
            it.copy(
                isResolving = false,
                route = AppRoute.ClientPackageSessions(clientId = clientId)
            )
        }
    }

    fun onClientHistoryRequested(clientId: String) {
        _state.update {
            it.copy(
                isResolving = false,
                route = AppRoute.ClientHistory(clientId = clientId)
            )
        }
    }

    fun onClientSaved(clientId: String) {
        clientsRefreshVersion += 1
        _state.update {
            it.copy(
                isResolving = false,
                route = AppRoute.ClientDetail(
                    clientId = clientId,
                    refreshVersion = clientsRefreshVersion
                )
            )
        }
    }

    fun onClientsBackRequested() {
        _state.update {
            it.copy(
                isResolving = false,
                route = AppRoute.ClientsList(refreshVersion = clientsRefreshVersion)
            )
        }
    }

    fun onOnboardingWelcomeRequested() {
        _state.update {
            it.copy(
                isResolving = false,
                route = AppRoute.OnboardingWelcome
            )
        }
    }

    fun onBusinessSetupRequested() {
        _state.update {
            it.copy(
                isResolving = false,
                route = AppRoute.OnboardingBusinessSetup
            )
        }
    }

    fun onSignOut() {
        viewModelScope.launch {
            signOutUseCase()
            clientsRefreshVersion = 0
            _state.update {
                it.copy(
                    isResolving = false,
                    route = AppRoute.Login,
                    session = null
                )
            }
        }
    }

    fun refreshDestination() {
        viewModelScope.launch {
            _state.update { it.copy(isResolving = true, route = AppRoute.Splash) }
            val session = observeAuthSessionUseCase()

            _state.update {
                it.copy(
                    isResolving = false,
                    session = session,
                    route = session.toRoute()
                )
            }
        }
    }
}

data class AppRootState(
    val isResolving: Boolean = true,
    val route: AppRoute = AppRoute.Splash,
    val session: AuthSession? = null
)

sealed interface AppRoute {
    data object Splash : AppRoute
    data object Login : AppRoute
    data object SignUp : AppRoute
    data object ForgotPassword : AppRoute
    data object OnboardingWelcome : AppRoute
    data object OnboardingBusinessSetup : AppRoute
    data object Home : AppRoute
    data class ClientsList(val refreshVersion: Int) : AppRoute
    data object ClientCreate : AppRoute
    data class ClientEdit(val clientId: String) : AppRoute
    data class ClientDetail(
        val clientId: String,
        val refreshVersion: Int
    ) : AppRoute
    data class ClientPackageSessions(val clientId: String) : AppRoute
    data class ClientHistory(val clientId: String) : AppRoute
}

private fun AuthSession?.toRoute(): AppRoute {
    return when {
        this == null -> AppRoute.Login
        studioId.isNullOrBlank() -> AppRoute.OnboardingWelcome
        else -> AppRoute.Home
    }
}
