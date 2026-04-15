package br.com.plusapps.plusfisio.features.auth.domain

class SignOutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.signOut()
}
