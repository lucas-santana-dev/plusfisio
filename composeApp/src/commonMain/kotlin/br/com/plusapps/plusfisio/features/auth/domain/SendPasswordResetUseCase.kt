package br.com.plusapps.plusfisio.features.auth.domain

import br.com.plusapps.plusfisio.core.domain.Result

class SendPasswordResetUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<Unit, AuthError> {
        return authRepository.sendPasswordReset(email)
    }
}
