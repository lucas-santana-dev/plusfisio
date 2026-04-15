package br.com.plusapps.plusfisio.features.auth.domain

class ObserveAuthSessionUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): AuthSession? = authRepository.getCurrentSession()
}
