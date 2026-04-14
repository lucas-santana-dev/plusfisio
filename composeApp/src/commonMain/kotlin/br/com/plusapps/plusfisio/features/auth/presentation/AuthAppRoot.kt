package br.com.plusapps.plusfisio.features.auth.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.plusapps.plusfisio.core.presentation.text.resolve
import br.com.plusapps.plusfisio.features.auth.presentation.login.LoginEvent
import br.com.plusapps.plusfisio.features.auth.presentation.login.LoginScreen
import br.com.plusapps.plusfisio.features.auth.presentation.login.LoginViewModel
import br.com.plusapps.plusfisio.features.auth.presentation.splash.SplashScreen
import kotlinx.coroutines.delay

private const val SPLASH_DURATION_MILLIS = 1400L

private enum class AuthRoute {
    Splash,
    Login
}

/**
 * Root do fluxo inicial de autenticacao.
 *
 * Faz o wiring entre o state holder, a splash temporaria e a tela de login.
 */
@Composable
fun AuthAppRoot(
    viewModel: LoginViewModel = remember { LoginViewModel() }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val currentRoute = rememberAuthRoute()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is LoginEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message.resolve())
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        when (currentRoute) {
            AuthRoute.Splash -> SplashScreen(contentPadding = innerPadding)
            AuthRoute.Login -> LoginScreen(
                state = state,
                onAction = viewModel::onAction,
                contentPadding = innerPadding
            )
        }
    }
}

@Composable
private fun rememberAuthRoute(): AuthRoute {
    val route = remember { mutableStateOf(AuthRoute.Splash) }

    LaunchedEffect(Unit) {
        delay(SPLASH_DURATION_MILLIS)
        route.value = AuthRoute.Login
    }

    return route.value
}
