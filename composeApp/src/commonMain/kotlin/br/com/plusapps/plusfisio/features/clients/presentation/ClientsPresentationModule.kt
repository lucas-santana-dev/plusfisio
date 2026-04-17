package br.com.plusapps.plusfisio.features.clients.presentation

import br.com.plusapps.plusfisio.features.clients.presentation.detail.ClientDetailViewModel
import br.com.plusapps.plusfisio.features.clients.presentation.form.ClientFormViewModel
import br.com.plusapps.plusfisio.features.clients.presentation.history.ClientHistoryViewModel
import br.com.plusapps.plusfisio.features.clients.presentation.list.ClientsListViewModel
import br.com.plusapps.plusfisio.features.clients.presentation.packageflow.ClientPackageSessionsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val clientsPresentationModule = module {
    viewModelOf(::ClientsListViewModel)
    viewModelOf(::ClientFormViewModel)
    viewModelOf(::ClientDetailViewModel)
    viewModelOf(::ClientPackageSessionsViewModel)
    viewModelOf(::ClientHistoryViewModel)
}
