package br.com.plusapps.plusfisio.features.clients.presentation.form

import androidx.compose.runtime.Stable
import br.com.plusapps.plusfisio.core.presentation.text.UiText

@Stable
data class ClientFormState(
    val mode: ClientFormMode = ClientFormMode.Create,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val fullName: String = "",
    val phone: String = "",
    val email: String = "",
    val birthDate: String = "",
    val primaryModality: String = "",
    val responsibleProfessional: String = "",
    val acquisitionSource: String = "",
    val notes: String = "",
    val fullNameError: UiText? = null,
    val phoneError: UiText? = null,
    val emailError: UiText? = null,
    val birthDateError: UiText? = null,
    val errorMessage: UiText? = null
)

enum class ClientFormMode {
    Create,
    Edit
}

sealed interface ClientFormAction {
    data class OnFullNameChanged(val value: String) : ClientFormAction
    data class OnPhoneChanged(val value: String) : ClientFormAction
    data class OnEmailChanged(val value: String) : ClientFormAction
    data class OnBirthDateChanged(val value: String) : ClientFormAction
    data class OnPrimaryModalityChanged(val value: String) : ClientFormAction
    data class OnResponsibleProfessionalChanged(val value: String) : ClientFormAction
    data class OnAcquisitionSourceChanged(val value: String) : ClientFormAction
    data class OnNotesChanged(val value: String) : ClientFormAction
    data object OnSaveClicked : ClientFormAction
    data object OnBackClicked : ClientFormAction
    data object OnRetryClicked : ClientFormAction
    data object OnHomeTabClicked : ClientFormAction
    data object OnAgendaTabClicked : ClientFormAction
    data object OnFinanceTabClicked : ClientFormAction
    data object OnMoreTabClicked : ClientFormAction
}

sealed interface ClientFormEvent {
    data class ClientSaved(val clientId: String) : ClientFormEvent
    data object NavigateBack : ClientFormEvent
    data object NavigateHome : ClientFormEvent
    data class ShowMessage(val message: UiText) : ClientFormEvent
}
