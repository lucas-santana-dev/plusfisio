package br.com.plusapps.plusfisio.features.auth.domain

import br.com.plusapps.plusfisio.core.domain.Error

sealed interface AuthError : Error {
    data object InvalidCredentials : AuthError
    data object ProviderDisabled : AuthError
    data object Network : AuthError
    data object Unknown : AuthError
}
