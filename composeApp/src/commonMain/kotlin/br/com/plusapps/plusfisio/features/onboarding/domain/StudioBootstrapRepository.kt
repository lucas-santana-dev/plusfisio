package br.com.plusapps.plusfisio.features.onboarding.domain

import br.com.plusapps.plusfisio.core.domain.EmptyResult

interface StudioBootstrapRepository {
    suspend fun bootstrapStudio(input: BootstrapStudioInput): EmptyResult<OnboardingError>
}
