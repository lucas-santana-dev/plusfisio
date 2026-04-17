package br.com.plusapps.plusfisio.features.clients.data

import br.com.plusapps.plusfisio.features.clients.domain.ClientRepository
import org.koin.dsl.module

val clientDataModule = module {
    single<ClientRepository> { FirebaseClientRepository() }
}
