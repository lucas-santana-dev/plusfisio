package br.com.plusapps.plusfisio.root.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.plusapps.plusfisio.features.auth.presentation.login.LoginRoot
import br.com.plusapps.plusfisio.features.auth.presentation.signup.SignUpRoot
import br.com.plusapps.plusfisio.features.auth.presentation.splash.SplashScreen
import br.com.plusapps.plusfisio.features.home.presentation.HomeTemplateScreen
import br.com.plusapps.plusfisio.features.onboarding.presentation.OnboardingRoot
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppRoot(
    viewModel: AppRootViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when {
            state.isResolving || state.route == AppRoute.Splash -> {
                SplashScreen(contentPadding = innerPadding)
            }

            state.route == AppRoute.Login -> {
                LoginRoot(
                    onAuthenticated = viewModel::onLoginCompleted,
                    onNavigateToSignUp = viewModel::onSignUpRequested,
                    contentPadding = innerPadding
                )
            }

            state.route == AppRoute.SignUp -> {
                SignUpRoot(
                    onAuthenticated = viewModel::onSignUpCompleted,
                    onNavigateToLogin = viewModel::onLoginRequested,
                    contentPadding = innerPadding
                )
            }

            state.route == AppRoute.Onboarding -> {
                OnboardingRoot(
                    session = requireNotNull(state.session),
                    onCompleted = viewModel::onOnboardingCompleted,
                    onSignOutClick = viewModel::onSignOut,
                    contentPadding = innerPadding
                )
            }

            state.route == AppRoute.Home -> {
                HomeTemplateScreen(
                    session = requireNotNull(state.session),
                    onSignOutClick = viewModel::onSignOut,
                    contentPadding = innerPadding
                )
            }
        }
    }
}
