package br.com.plusapps.plusfisio.di

import br.com.plusapps.plusfisio.features.auth.data.authDataModule
import br.com.plusapps.plusfisio.features.auth.presentation.authPresentationModule
import br.com.plusapps.plusfisio.root.presentation.rootPresentationModule
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

private val appModules: List<Module> = listOf(
    authDataModule,
    authPresentationModule,
    rootPresentationModule
)

fun initKoin() {
    if (GlobalContext.getOrNull() != null) {
        return
    }

    startKoin {
        modules(appModules)
    }
}
