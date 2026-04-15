package br.com.plusapps.plusfisio.features.auth.domain

import br.com.plusapps.plusfisio.core.domain.model.StudioUserRole

data class AuthSession(
    val userId: String,
    val email: String,
    val displayName: String?,
    val studioId: String?,
    val role: StudioUserRole?
)
