package br.com.plusapps.plusfisio.features.onboarding.presentation

import app.cash.turbine.test
import br.com.plusapps.plusfisio.core.domain.Result
import br.com.plusapps.plusfisio.core.domain.model.BusinessType
import br.com.plusapps.plusfisio.features.onboarding.domain.BootstrapStudioInput
import br.com.plusapps.plusfisio.features.onboarding.domain.BootstrapStudioUseCase
import br.com.plusapps.plusfisio.features.onboarding.domain.OnboardingError
import br.com.plusapps.plusfisio.features.onboarding.domain.StudioBootstrapRepository
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
class OnboardingViewModelTest {

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
    fun `empty fields update onboarding errors`() = runTest(dispatcher) {
        val viewModel = OnboardingViewModel(BootstrapStudioUseCase(FakeStudioBootstrapRepository()))

        viewModel.onAction(OnboardingAction.OnSubmitClicked)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertNotNull(state.studioNameError)
        assertNotNull(state.phoneError)
        assertFalse(state.isSubmitting)
    }

    @Test
    fun `successful bootstrap emits completed event`() = runTest(dispatcher) {
        val viewModel = OnboardingViewModel(BootstrapStudioUseCase(FakeStudioBootstrapRepository()))

        viewModel.onAction(OnboardingAction.OnStudioNameChanged("Studio Move"))
        viewModel.onAction(OnboardingAction.OnPhoneChanged("11999999999"))
        viewModel.onAction(OnboardingAction.OnBusinessTypeSelected(BusinessType.Mixed))

        viewModel.events.test {
            viewModel.onAction(OnboardingAction.OnSubmitClicked)
            advanceUntilIdle()
            assertEquals(OnboardingEvent.Completed, awaitItem())
        }
    }

    @Test
    fun `bootstrap failure emits message`() = runTest(dispatcher) {
        val repo = FakeStudioBootstrapRepository(result = Result.Failure(OnboardingError.PermissionDenied))
        val viewModel = OnboardingViewModel(BootstrapStudioUseCase(repo))

        viewModel.onAction(OnboardingAction.OnStudioNameChanged("Studio Move"))
        viewModel.onAction(OnboardingAction.OnPhoneChanged("11999999999"))

        viewModel.events.test {
            viewModel.onAction(OnboardingAction.OnSubmitClicked)
            advanceUntilIdle()
            assertIs<OnboardingEvent.ShowMessage>(awaitItem())
        }
    }

    @Test
    fun `submitting keeps loading while bootstrap is in progress`() = runTest(dispatcher) {
        val gate = CompletableDeferred<Unit>()
        val repo = FakeStudioBootstrapRepository(pendingGate = gate)
        val viewModel = OnboardingViewModel(BootstrapStudioUseCase(repo))

        viewModel.onAction(OnboardingAction.OnStudioNameChanged("Studio Move"))
        viewModel.onAction(OnboardingAction.OnPhoneChanged("11999999999"))
        viewModel.onAction(OnboardingAction.OnSubmitClicked)
        runCurrent()

        assertTrue(viewModel.state.value.isSubmitting)

        gate.complete(Unit)
        advanceUntilIdle()

        assertFalse(viewModel.state.value.isSubmitting)
    }
}

private class FakeStudioBootstrapRepository(
    private val result: Result<Unit, OnboardingError> = Result.Success(Unit),
    private val pendingGate: CompletableDeferred<Unit>? = null
) : StudioBootstrapRepository {
    override suspend fun bootstrapStudio(input: BootstrapStudioInput): Result<Unit, OnboardingError> {
        pendingGate?.await()
        return result
    }
}
