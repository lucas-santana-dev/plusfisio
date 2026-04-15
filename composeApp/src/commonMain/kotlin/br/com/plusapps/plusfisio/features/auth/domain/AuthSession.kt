package br.com.plusapps.plusfisio.features.auth.domain

data class AuthSession(
    val userId: String,
    val email: String,
    val displayName: String?,
    val studioId: String?
)
