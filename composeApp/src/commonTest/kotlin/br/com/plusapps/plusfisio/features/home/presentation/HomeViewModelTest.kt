package br.com.plusapps.plusfisio.features.home.presentation

import app.cash.turbine.test
import br.com.plusapps.plusfisio.core.domain.model.StudioUserRole
import br.com.plusapps.plusfisio.features.auth.domain.AuthSession
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
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

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
    fun `binding session builds empty first-run home`() = runTest(dispatcher) {
        val viewModel = HomeViewModel()

        viewModel.bindSession(testSession())
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertTrue(state.isFirstRunEmptyState)
        assertEquals("Camila", state.professionalFirstName)
        assertEquals("00", state.metrics.appointments.value)
        assertEquals("00", state.metrics.pendings.value)
        assertEquals(null, state.nextAppointment)
    }

    @Test
    fun `agenda tab click emits navigate event`() = runTest(dispatcher) {
        val viewModel = HomeViewModel()
        viewModel.bindSession(testSession())

        viewModel.events.test {
            viewModel.onAction(HomeAction.OnBottomTabClick(HomeBottomTab.Agenda))
            assertEquals(HomeEvent.NavigateToAgenda, awaitItem())
        }
    }

    @Test
    fun `sign out action emits request sign out event`() = runTest(dispatcher) {
        val viewModel = HomeViewModel()
        viewModel.bindSession(testSession())

        viewModel.events.test {
            viewModel.onAction(HomeAction.OnBottomTabClick(HomeBottomTab.More))
            viewModel.onAction(HomeAction.OnSignOutClick)
            assertEquals(HomeEvent.RequestSignOut, awaitItem())
        }
    }
}

private fun testSession() = AuthSession(
    userId = "user-1",
    email = "owner@plusfisio.com",
    displayName = "Camila Rocha",
    studioId = "studio-1",
    role = StudioUserRole.OwnerAdmin
)
