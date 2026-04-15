package br.com.plusapps.plusfisio.features.auth.presentation

import br.com.plusapps.plusfisio.core.presentation.text.UiText
import br.com.plusapps.plusfisio.features.auth.domain.AuthError
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.auth_error_email_in_use
import plusfisio.composeapp.generated.resources.auth_error_invalid_credentials
import plusfisio.composeapp.generated.resources.auth_error_network
import plusfisio.composeapp.generated.resources.auth_error_profile_setup_failed
import plusfisio.composeapp.generated.resources.auth_error_provider_disabled
import plusfisio.composeapp.generated.resources.auth_error_unknown
import plusfisio.composeapp.generated.resources.auth_error_weak_password

fun AuthError.toUiText(): UiText {
    return when (this) {
        AuthError.EmailAlreadyInUse -> UiText.Resource(Res.string.auth_error_email_in_use)
        AuthError.InvalidCredentials -> UiText.Resource(Res.string.auth_error_invalid_credentials)
        AuthError.WeakPassword -> UiText.Resource(Res.string.auth_error_weak_password)
        AuthError.ProfileSetupFailed -> UiText.Resource(Res.string.auth_error_profile_setup_failed)
        AuthError.ProviderDisabled -> UiText.Resource(Res.string.auth_error_provider_disabled)
        AuthError.Network -> UiText.Resource(Res.string.auth_error_network)
        AuthError.Unknown -> UiText.Resource(Res.string.auth_error_unknown)
    }
}
