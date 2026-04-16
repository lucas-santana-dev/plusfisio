package br.com.plusapps.plusfisio.features.auth.domain

import br.com.plusapps.plusfisio.core.domain.Result

interface AuthRepository {
    suspend fun signUp(
        name: String,
        whatsapp: String,
        email: String,
        password: String
    ): Result<AuthSession, AuthError>
    suspend fun signIn(email: String, password: String): Result<AuthSession, AuthError>
    suspend fun sendPasswordReset(email: String): Result<Unit, AuthError>
    suspend fun getCurrentSession(): AuthSession?
    suspend fun signOut()
}
