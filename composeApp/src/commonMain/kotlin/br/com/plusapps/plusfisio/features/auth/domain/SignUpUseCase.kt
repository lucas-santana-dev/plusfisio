package br.com.plusapps.plusfisio.features.auth.domain

import br.com.plusapps.plusfisio.core.domain.Result

class SignUpUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        name: String,
        whatsapp: String,
        email: String,
        password: String
    ): Result<AuthSession, AuthError> {
        return authRepository.signUp(
            name = name,
            whatsapp = whatsapp,
            email = email,
            password = password
        )
    }
}
