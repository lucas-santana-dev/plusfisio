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

    fun onSignOut() {
        viewModelScope.launch {
            signOutUseCase()
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
    data object Onboarding : AppRoute
    data object Home : AppRoute
}

private fun AuthSession?.toRoute(): AppRoute {
    return when {
        this == null -> AppRoute.Login
        studioId.isNullOrBlank() -> AppRoute.Onboarding
        else -> AppRoute.Home
    }
}
