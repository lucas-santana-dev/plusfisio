package br.com.plusapps.plusfisio.root.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.plusapps.plusfisio.features.auth.presentation.login.LoginRoot
import br.com.plusapps.plusfisio.features.auth.presentation.forgotpassword.ForgotPasswordRoot
import br.com.plusapps.plusfisio.features.auth.presentation.signup.SignUpRoot
import br.com.plusapps.plusfisio.features.auth.presentation.splash.SplashScreen
import br.com.plusapps.plusfisio.features.clients.presentation.detail.ClientDetailRoot
import br.com.plusapps.plusfisio.features.clients.presentation.form.ClientFormMode
import br.com.plusapps.plusfisio.features.clients.presentation.form.ClientFormRoot
import br.com.plusapps.plusfisio.features.clients.presentation.history.ClientHistoryRoot
import br.com.plusapps.plusfisio.features.clients.presentation.list.ClientsListRoot
import br.com.plusapps.plusfisio.features.clients.presentation.packageflow.ClientPackageSessionsRoot
import br.com.plusapps.plusfisio.features.home.presentation.HomeRoot
import br.com.plusapps.plusfisio.features.onboarding.presentation.businesssetup.BusinessSetupRoot
import br.com.plusapps.plusfisio.features.onboarding.presentation.welcome.WelcomeScreen
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
                    onNavigateToForgotPassword = viewModel::onForgotPasswordRequested,
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

            state.route == AppRoute.ForgotPassword -> {
                ForgotPasswordRoot(
                    onNavigateBack = viewModel::onLoginRequested,
                    contentPadding = innerPadding
                )
            }

            state.route == AppRoute.OnboardingWelcome -> {
                WelcomeScreen(
                    onBackClick = viewModel::onLoginRequested,
                    onContinueClick = viewModel::onBusinessSetupRequested,
                    contentPadding = innerPadding
                )
            }

            state.route == AppRoute.OnboardingBusinessSetup -> {
                BusinessSetupRoot(
                    session = requireNotNull(state.session),
                    onCompleted = viewModel::onOnboardingCompleted,
                    onNavigateBack = viewModel::onOnboardingWelcomeRequested,
                    onSignOutClick = viewModel::onSignOut,
                    contentPadding = innerPadding
                )
            }

            state.route == AppRoute.Home -> {
                HomeRoot(
                    session = requireNotNull(state.session),
                    onNavigateToClients = viewModel::onClientsRequested,
                    onSignOutClick = viewModel::onSignOut,
                    contentPadding = innerPadding
                )
            }

            state.route is AppRoute.ClientsList -> {
                val route = state.route as AppRoute.ClientsList
                ClientsListRoot(
                    session = requireNotNull(state.session),
                    refreshVersion = route.refreshVersion,
                    onNavigateHome = viewModel::onHomeRequested,
                    onNavigateToCreate = viewModel::onClientCreateRequested,
                    onNavigateToClientDetail = viewModel::onClientDetailRequested
                )
            }

            state.route == AppRoute.ClientCreate -> {
                ClientFormRoot(
                    session = requireNotNull(state.session),
                    mode = ClientFormMode.Create,
                    onNavigateBack = viewModel::onClientsBackRequested,
                    onNavigateHome = viewModel::onHomeRequested,
                    onClientSaved = viewModel::onClientSaved
                )
            }

            state.route is AppRoute.ClientEdit -> {
                val route = state.route as AppRoute.ClientEdit
                ClientFormRoot(
                    session = requireNotNull(state.session),
                    mode = ClientFormMode.Edit,
                    clientId = route.clientId,
                    onNavigateBack = { viewModel.onClientDetailRequested(route.clientId) },
                    onNavigateHome = viewModel::onHomeRequested,
                    onClientSaved = viewModel::onClientSaved
                )
            }

            state.route is AppRoute.ClientDetail -> {
                val route = state.route as AppRoute.ClientDetail
                ClientDetailRoot(
                    session = requireNotNull(state.session),
                    clientId = route.clientId,
                    refreshVersion = route.refreshVersion,
                    onNavigateBack = viewModel::onClientsBackRequested,
                    onNavigateHome = viewModel::onHomeRequested,
                    onNavigateToEdit = viewModel::onClientEditRequested,
                    onNavigateToPackage = viewModel::onClientPackageRequested,
                    onNavigateToHistory = viewModel::onClientHistoryRequested
                )
            }

            state.route is AppRoute.ClientPackageSessions -> {
                val route = state.route as AppRoute.ClientPackageSessions
                ClientPackageSessionsRoot(
                    session = requireNotNull(state.session),
                    clientId = route.clientId,
                    onNavigateBack = { viewModel.onClientDetailRequested(route.clientId) },
                    onNavigateHome = viewModel::onHomeRequested
                )
            }

            state.route is AppRoute.ClientHistory -> {
                val route = state.route as AppRoute.ClientHistory
                ClientHistoryRoot(
                    session = requireNotNull(state.session),
                    clientId = route.clientId,
                    onNavigateBack = { viewModel.onClientDetailRequested(route.clientId) },
                    onNavigateHome = viewModel::onHomeRequested
                )
            }
        }
    }
}
