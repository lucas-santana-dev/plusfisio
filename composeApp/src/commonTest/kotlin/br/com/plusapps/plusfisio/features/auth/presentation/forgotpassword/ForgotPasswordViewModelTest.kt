package br.com.plusapps.plusfisio.features.auth.presentation.forgotpassword

import app.cash.turbine.test
import br.com.plusapps.plusfisio.core.domain.Result
import br.com.plusapps.plusfisio.features.auth.domain.AuthError
import br.com.plusapps.plusfisio.features.auth.domain.AuthRepository
import br.com.plusapps.plusfisio.features.auth.domain.AuthSession
import br.com.plusapps.plusfisio.features.auth.domain.SendPasswordResetUseCase
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
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ForgotPasswordViewModelTest {

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
    fun `empty email updates validation error`() = runTest(dispatcher) {
        val viewModel = ForgotPasswordViewModel(SendPasswordResetUseCase(FakeForgotPasswordAuthRepository()))

        viewModel.onAction(ForgotPasswordAction.OnSubmitClicked)
        advanceUntilIdle()

        assertNotNull(viewModel.state.value.emailError)
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `successful reset emits message and marks sent`() = runTest(dispatcher) {
        val viewModel = ForgotPasswordViewModel(SendPasswordResetUseCase(FakeForgotPasswordAuthRepository()))
        viewModel.onAction(ForgotPasswordAction.OnEmailChanged("owner@plusfisio.com"))

        viewModel.events.test {
            viewModel.onAction(ForgotPasswordAction.OnSubmitClicked)
            advanceUntilIdle()

            assertIs<ForgotPasswordEvent.ShowMessage>(awaitItem())
        }

        assertTrue(viewModel.state.value.isSent)
    }

    @Test
    fun `loading stays true while reset is in progress`() = runTest(dispatcher) {
        val gate = CompletableDeferred<Unit>()
        val repo = FakeForgotPasswordAuthRepository(pendingGate = gate)
        val viewModel = ForgotPasswordViewModel(SendPasswordResetUseCase(repo))

        viewModel.onAction(ForgotPasswordAction.OnEmailChanged("owner@plusfisio.com"))
        viewModel.onAction(ForgotPasswordAction.OnSubmitClicked)
        runCurrent()

        assertTrue(viewModel.state.value.isLoading)

        gate.complete(Unit)
        advanceUntilIdle()

        assertFalse(viewModel.state.value.isLoading)
    }
}

private class FakeForgotPasswordAuthRepository(
    private val resetResult: Result<Unit, AuthError> = Result.Success(Unit),
    private val pendingGate: CompletableDeferred<Unit>? = null
) : AuthRepository {
    override suspend fun signUp(
        name: String,
        whatsapp: String,
        email: String,
        password: String
    ): Result<AuthSession, AuthError> = Result.Failure(AuthError.Unknown)

    override suspend fun signIn(email: String, password: String): Result<AuthSession, AuthError> {
        return Result.Failure(AuthError.Unknown)
    }

    override suspend fun sendPasswordReset(email: String): Result<Unit, AuthError> {
        pendingGate?.await()
        return resetResult
    }

    override suspend fun getCurrentSession(): AuthSession? = null

    override suspend fun signOut() = Unit
}
