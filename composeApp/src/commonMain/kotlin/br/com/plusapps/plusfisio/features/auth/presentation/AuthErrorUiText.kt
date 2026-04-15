package br.com.plusapps.plusfisio.features.auth.presentation

import br.com.plusapps.plusfisio.core.presentation.text.UiText
import br.com.plusapps.plusfisio.features.auth.domain.AuthError
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.auth_error_invalid_credentials
import plusfisio.composeapp.generated.resources.auth_error_network
import plusfisio.composeapp.generated.resources.auth_error_provider_disabled
import plusfisio.composeapp.generated.resources.auth_error_unknown

fun AuthError.toUiText(): UiText {
    return when (this) {
        AuthError.InvalidCredentials -> UiText.Resource(Res.string.auth_error_invalid_credentials)
        AuthError.ProviderDisabled -> UiText.Resource(Res.string.auth_error_provider_disabled)
        AuthError.Network -> UiText.Resource(Res.string.auth_error_network)
        AuthError.Unknown -> UiText.Resource(Res.string.auth_error_unknown)
    }
}
