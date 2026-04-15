package br.com.plusapps.plusfisio.root.presentation

import br.com.plusapps.plusfisio.core.domain.Result
import br.com.plusapps.plusfisio.core.domain.model.StudioUserRole
import br.com.plusapps.plusfisio.features.auth.domain.AuthError
import br.com.plusapps.plusfisio.features.auth.domain.AuthRepository
import br.com.plusapps.plusfisio.features.auth.domain.AuthSession
import br.com.plusapps.plusfisio.features.auth.domain.ObserveAuthSessionUseCase
import br.com.plusapps.plusfisio.features.auth.domain.SignOutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class AppRootViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `without session starts at login`() = runTest(dispatcher) {
        val repository = RootAuthRepositoryForTest(currentSession = null)

        val viewModel = AppRootViewModel(
            observeAuthSessionUseCase = ObserveAuthSessionUseCase(repository),
            signOutUseCase = SignOutUseCase(repository)
        )
        advanceUntilIdle()

        assertEquals(AppRoute.Login, viewModel.state.value.route)
        assertNull(viewModel.state.value.session)
    }

    @Test
    fun `session without studio goes to onboarding`() = runTest(dispatcher) {
        val session = AuthSession(
            userId = "user-setup",
            email = "setup@plusfisio.com",
            displayName = "Bruno",
            studioId = null,
            role = null
        )
        val repository = RootAuthRepositoryForTest(currentSession = session)

        val viewModel = AppRootViewModel(
            observeAuthSessionUseCase = ObserveAuthSessionUseCase(repository),
            signOutUseCase = SignOutUseCase(repository)
        )
        advanceUntilIdle()

        assertEquals(AppRoute.Onboarding, viewModel.state.value.route)
        assertEquals(session, viewModel.state.value.session)
    }

    @Test
    fun `session with studio goes to home`() = runTest(dispatcher) {
        val session = AuthSession(
            userId = "user-owner",
            email = "owner@plusfisio.com",
            displayName = "Camila",
            studioId = "studio-1",
            role = StudioUserRole.OwnerAdmin
        )
        val repository = RootAuthRepositoryForTest(currentSession = session)

        val viewModel = AppRootViewModel(
            observeAuthSessionUseCase = ObserveAuthSessionUseCase(repository),
            signOutUseCase = SignOutUseCase(repository)
        )
        advanceUntilIdle()

        assertEquals(AppRoute.Home, viewModel.state.value.route)
        assertEquals(session, viewModel.state.value.session)
    }

    @Test
    fun `login completion uses returned session immediately`() = runTest(dispatcher) {
        val session = AuthSession(
            userId = "user-owner",
            email = "owner@plusfisio.com",
            displayName = "Camila",
            studioId = null,
            role = null
        )
        val repository = RootAuthRepositoryForTest(currentSession = null)

        val viewModel = AppRootViewModel(
            observeAuthSessionUseCase = ObserveAuthSessionUseCase(repository),
            signOutUseCase = SignOutUseCase(repository)
        )
        advanceUntilIdle()

        assertEquals(AppRoute.Login, viewModel.state.value.route)

        viewModel.onLoginCompleted(session)

        assertEquals(AppRoute.Onboarding, viewModel.state.value.route)
        assertEquals(session, viewModel.state.value.session)
    }

    @Test
    fun `sign up request navigates to sign up route`() = runTest(dispatcher) {
        val repository = RootAuthRepositoryForTest(currentSession = null)
        val viewModel = AppRootViewModel(
            observeAuthSessionUseCase = ObserveAuthSessionUseCase(repository),
            signOutUseCase = SignOutUseCase(repository)
        )
        advanceUntilIdle()

        viewModel.onSignUpRequested()

        assertEquals(AppRoute.SignUp, viewModel.state.value.route)
    }

    @Test
    fun `login request navigates back to login route`() = runTest(dispatcher) {
        val repository = RootAuthRepositoryForTest(currentSession = null)
        val viewModel = AppRootViewModel(
            observeAuthSessionUseCase = ObserveAuthSessionUseCase(repository),
            signOutUseCase = SignOutUseCase(repository)
        )
        advanceUntilIdle()

        viewModel.onSignUpRequested()
        viewModel.onLoginRequested()

        assertEquals(AppRoute.Login, viewModel.state.value.route)
    }
}

private class RootAuthRepositoryForTest(
    private var currentSession: AuthSession?
) : AuthRepository {

    override suspend fun signUp(name: String, email: String, password: String): Result<AuthSession, AuthError> {
        return Result.Failure(AuthError.Unknown)
    }

    override suspend fun signIn(email: String, password: String): Result<AuthSession, AuthError> {
        return Result.Failure(AuthError.Unknown)
    }

    override suspend fun getCurrentSession(): AuthSession? = currentSession

    override suspend fun signOut() {
        currentSession = null
    }
}
