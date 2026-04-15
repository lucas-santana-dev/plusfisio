package br.com.plusapps.plusfisio.features.auth.domain

import br.com.plusapps.plusfisio.core.domain.Result

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Result<AuthSession, AuthError>
    suspend fun getCurrentSession(): AuthSession?
    suspend fun signOut()
}
