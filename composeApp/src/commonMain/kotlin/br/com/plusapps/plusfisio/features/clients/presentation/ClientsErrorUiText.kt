package br.com.plusapps.plusfisio.features.clients.presentation

import br.com.plusapps.plusfisio.core.presentation.text.UiText
import br.com.plusapps.plusfisio.features.clients.domain.ClientError
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.clients_error_network
import plusfisio.composeapp.generated.resources.clients_error_not_found
import plusfisio.composeapp.generated.resources.clients_error_permission_denied
import plusfisio.composeapp.generated.resources.clients_error_unknown

fun ClientError.toUiText(): UiText {
    return when (this) {
        ClientError.Network -> UiText.Resource(Res.string.clients_error_network)
        ClientError.NotFound -> UiText.Resource(Res.string.clients_error_not_found)
        ClientError.PermissionDenied -> UiText.Resource(Res.string.clients_error_permission_denied)
        ClientError.Unknown -> UiText.Resource(Res.string.clients_error_unknown)
    }
}
