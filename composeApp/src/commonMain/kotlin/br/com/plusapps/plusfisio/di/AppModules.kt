package br.com.plusapps.plusfisio.di

import br.com.plusapps.plusfisio.features.auth.data.authDataModule
import br.com.plusapps.plusfisio.features.auth.presentation.authPresentationModule
import br.com.plusapps.plusfisio.features.clients.data.clientDataModule
import br.com.plusapps.plusfisio.features.clients.presentation.clientsPresentationModule
import br.com.plusapps.plusfisio.features.home.presentation.homePresentationModule
import br.com.plusapps.plusfisio.features.onboarding.data.onboardingDataModule
import br.com.plusapps.plusfisio.features.onboarding.presentation.onboardingPresentationModule
import br.com.plusapps.plusfisio.root.presentation.rootPresentationModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.mp.KoinPlatform

private val appModules: List<Module> = listOf(
    authDataModule,
    authPresentationModule,
    clientDataModule,
    clientsPresentationModule,
    homePresentationModule,
    onboardingDataModule,
    onboardingPresentationModule,
    rootPresentationModule
)

fun initKoin() {
    if (KoinPlatform.getKoinOrNull() != null) {
        return
    }

    startKoin {
        modules(appModules)
    }
}
