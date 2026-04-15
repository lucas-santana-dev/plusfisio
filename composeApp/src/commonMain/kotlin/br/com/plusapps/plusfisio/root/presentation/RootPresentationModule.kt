package br.com.plusapps.plusfisio.root.presentation

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val rootPresentationModule = module {
    viewModelOf(::AppRootViewModel)
}
