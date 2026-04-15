package br.com.plusapps.plusfisio.features.auth.presentation.signup

import app.cash.turbine.test
import br.com.plusapps.plusfisio.core.domain.Result
import br.com.plusapps.plusfisio.features.auth.domain.AuthError
import br.com.plusapps.plusfisio.features.auth.domain.AuthRepository
import br.com.plusapps.plusfisio.features.auth.domain.AuthSession
import br.com.plusapps.plusfisio.features.auth.domain.SignUpUseCase
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
class SignUpViewModelTest {

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
    fun `empty fields update field errors`() = runTest(dispatcher) {
        val viewModel = SignUpViewModel(SignUpUseCase(FakeSignUpAuthRepository()))

        viewModel.onAction(SignUpAction.OnSignUpClicked)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertNotNull(state.nameError)
        assertNotNull(state.emailError)
        assertNotNull(state.passwordError)
        assertNotNull(state.confirmPasswordError)
        assertFalse(state.isLoading)
    }

    @Test
    fun `mismatched passwords update confirmation error`() = runTest(dispatcher) {
        val viewModel = SignUpViewModel(SignUpUseCase(FakeSignUpAuthRepository()))

        viewModel.onAction(SignUpAction.OnNameChanged("Camila"))
        viewModel.onAction(SignUpAction.OnEmailChanged("owner@plusfisio.com"))
        viewModel.onAction(SignUpAction.OnPasswordChanged("123456"))
        viewModel.onAction(SignUpAction.OnConfirmPasswordChanged("654321"))
        viewModel.onAction(SignUpAction.OnSignUpClicked)
        advanceUntilIdle()

        assertNotNull(viewModel.state.value.confirmPasswordError)
    }

    @Test
    fun `successful sign up emits authenticated event`() = runTest(dispatcher) {
        val session = AuthSession(
            userId = "user-1",
            email = "owner@plusfisio.com",
            displayName = "Camila",
            studioId = null,
            role = null
        )
        val viewModel = SignUpViewModel(
            SignUpUseCase(
                FakeSignUpAuthRepository(signUpResult = Result.Success(session))
            )
        )

        viewModel.onAction(SignUpAction.OnNameChanged("Camila"))
        viewModel.onAction(SignUpAction.OnEmailChanged("owner@plusfisio.com"))
        viewModel.onAction(SignUpAction.OnPasswordChanged("123456"))
        viewModel.onAction(SignUpAction.OnConfirmPasswordChanged("123456"))

        viewModel.events.test {
            viewModel.onAction(SignUpAction.OnSignUpClicked)
            advanceUntilIdle()

            assertEquals(SignUpEvent.Authenticated(session), awaitItem())
        }

        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `backend failure emits ui message`() = runTest(dispatcher) {
        val viewModel = SignUpViewModel(
            SignUpUseCase(
                FakeSignUpAuthRepository(signUpResult = Result.Failure(AuthError.EmailAlreadyInUse))
            )
        )

        viewModel.onAction(SignUpAction.OnNameChanged("Camila"))
        viewModel.onAction(SignUpAction.OnEmailChanged("owner@plusfisio.com"))
        viewModel.onAction(SignUpAction.OnPasswordChanged("123456"))
        viewModel.onAction(SignUpAction.OnConfirmPasswordChanged("123456"))

        viewModel.events.test {
            viewModel.onAction(SignUpAction.OnSignUpClicked)
            advanceUntilIdle()

            assertIs<SignUpEvent.ShowMessage>(awaitItem())
        }
    }

    @Test
    fun `login click emits navigation event`() = runTest(dispatcher) {
        val viewModel = SignUpViewModel(SignUpUseCase(FakeSignUpAuthRepository()))

        viewModel.events.test {
            viewModel.onAction(SignUpAction.OnLoginClicked)

            assertEquals(SignUpEvent.NavigateToLogin, awaitItem())
        }
    }

    @Test
    fun `loading stays true while sign up is in progress`() = runTest(dispatcher) {
        val gate = CompletableDeferred<Unit>()
        val viewModel = SignUpViewModel(
            SignUpUseCase(
                FakeSignUpAuthRepository(
                    pendingGate = gate
                )
            )
        )

        viewModel.onAction(SignUpAction.OnNameChanged("Camila"))
        viewModel.onAction(SignUpAction.OnEmailChanged("owner@plusfisio.com"))
        viewModel.onAction(SignUpAction.OnPasswordChanged("123456"))
        viewModel.onAction(SignUpAction.OnConfirmPasswordChanged("123456"))
        viewModel.onAction(SignUpAction.OnSignUpClicked)
        runCurrent()

        assertTrue(viewModel.state.value.isLoading)

        gate.complete(Unit)
        advanceUntilIdle()

        assertFalse(viewModel.state.value.isLoading)
    }
}

private class FakeSignUpAuthRepository(
    private val signUpResult: Result<AuthSession, AuthError> = Result.Success(
        AuthSession(
            userId = "default-user",
            email = "owner@plusfisio.com",
            displayName = "Camila",
            studioId = null,
            role = null
        )
    ),
    private val pendingGate: CompletableDeferred<Unit>? = null
) : AuthRepository {

    override suspend fun signUp(name: String, email: String, password: String): Result<AuthSession, AuthError> {
        pendingGate?.await()
        return signUpResult
    }

    override suspend fun signIn(email: String, password: String): Result<AuthSession, AuthError> {
        return Result.Failure(AuthError.Unknown)
    }

    override suspend fun getCurrentSession(): AuthSession? = null

    override suspend fun signOut() = Unit
}
