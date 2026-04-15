package br.com.plusapps.plusfisio.features.onboarding.domain

import br.com.plusapps.plusfisio.core.domain.EmptyResult

class BootstrapStudioUseCase(
    private val studioBootstrapRepository: StudioBootstrapRepository
) {
    suspend operator fun invoke(input: BootstrapStudioInput): EmptyResult<OnboardingError> {
        return studioBootstrapRepository.bootstrapStudio(input)
    }
}
