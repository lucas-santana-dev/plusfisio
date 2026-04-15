package br.com.plusapps.plusfisio.features.auth.presentation.login

import app.cash.turbine.test
import br.com.plusapps.plusfisio.core.domain.Result
import br.com.plusapps.plusfisio.features.auth.domain.AuthError
import br.com.plusapps.plusfisio.features.auth.domain.AuthRepository
import br.com.plusapps.plusfisio.features.auth.domain.AuthSession
import br.com.plusapps.plusfisio.features.auth.domain.SignInUseCase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

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
    fun `empty credentials update field errors`() = runTest(dispatcher) {
        val viewModel = LoginViewModel(SignInUseCase(FakeAuthRepositoryForTest()))

        viewModel.onAction(LoginAction.OnLoginClicked)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertNotNull(state.emailError)
        assertNotNull(state.passwordError)
        assertFalse(state.isLoading)
    }

    @Test
    fun `successful login emits authenticated event`() = runTest(dispatcher) {
        val repository = FakeAuthRepositoryForTest(
            signInResult = Result.Success(
                AuthSession(
                    userId = "user-1",
                    email = "owner@plusfisio.com",
                    displayName = "Camila",
                    studioId = "studio-1"
                )
            )
        )
        val viewModel = LoginViewModel(SignInUseCase(repository))

        viewModel.onAction(LoginAction.OnEmailChanged("owner@plusfisio.com"))
        viewModel.onAction(LoginAction.OnPasswordChanged("123456"))

        viewModel.events.test {
            viewModel.onAction(LoginAction.OnLoginClicked)
            advanceUntilIdle()

            assertEquals(LoginEvent.Authenticated, awaitItem())
        }

        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `invalid credentials emit ui message`() = runTest(dispatcher) {
        val repository = FakeAuthRepositoryForTest(
            signInResult = Result.Failure(AuthError.InvalidCredentials)
        )
        val viewModel = LoginViewModel(SignInUseCase(repository))

        viewModel.onAction(LoginAction.OnEmailChanged("owner@plusfisio.com"))
        viewModel.onAction(LoginAction.OnPasswordChanged("123456"))

        viewModel.events.test {
            viewModel.onAction(LoginAction.OnLoginClicked)
            advanceUntilIdle()

            assertIs<LoginEvent.ShowMessage>(awaitItem())
        }
    }

    @Test
    fun `loading stays true while sign in is in progress`() = runTest(dispatcher) {
        val gate = CompletableDeferred<Unit>()
        val repository = FakeAuthRepositoryForTest(
            signInResult = Result.Success(
                AuthSession(
                    userId = "user-1",
                    email = "owner@plusfisio.com",
                    displayName = "Camila",
                    studioId = "studio-1"
                )
            ),
            pendingGate = gate
        )
        val viewModel = LoginViewModel(SignInUseCase(repository))

        viewModel.onAction(LoginAction.OnEmailChanged("owner@plusfisio.com"))
        viewModel.onAction(LoginAction.OnPasswordChanged("123456"))
        viewModel.onAction(LoginAction.OnLoginClicked)
        runCurrent()

        assertTrue(viewModel.state.value.isLoading)

        gate.complete(Unit)
        advanceUntilIdle()

        assertFalse(viewModel.state.value.isLoading)
    }
}

private class FakeAuthRepositoryForTest(
    private val signInResult: Result<AuthSession, AuthError> = Result.Success(
        AuthSession(
            userId = "default-user",
            email = "owner@plusfisio.com",
            displayName = "Camila",
            studioId = "studio-1"
        )
    ),
    private val pendingGate: CompletableDeferred<Unit>? = null
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): Result<AuthSession, AuthError> {
        pendingGate?.await()
        return signInResult
    }

    override suspend fun getCurrentSession(): AuthSession? = null

    override suspend fun signOut() = Unit
}
