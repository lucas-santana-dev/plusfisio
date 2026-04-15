package br.com.plusapps.plusfisio.features.auth.domain

import br.com.plusapps.plusfisio.core.domain.Result

class SignInUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<AuthSession, AuthError> {
        return authRepository.signIn(email = email, password = password)
    }
}
