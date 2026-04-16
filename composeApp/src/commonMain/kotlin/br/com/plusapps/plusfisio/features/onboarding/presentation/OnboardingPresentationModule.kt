package br.com.plusapps.plusfisio.features.onboarding.presentation

import br.com.plusapps.plusfisio.features.onboarding.presentation.businesssetup.BusinessSetupViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val onboardingPresentationModule = module {
    viewModelOf(::BusinessSetupViewModel)
}
