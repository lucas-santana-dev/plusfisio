package br.com.plusapps.plusfisio.features.onboarding.data

import br.com.plusapps.plusfisio.features.onboarding.domain.BootstrapStudioUseCase
import br.com.plusapps.plusfisio.features.onboarding.domain.StudioBootstrapRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val onboardingDataModule = module {
    singleOf(::FirebaseStudioBootstrapRepository) { bind<StudioBootstrapRepository>() }
    singleOf(::BootstrapStudioUseCase)
}
