package br.com.plusapps.plusfisio.features.auth.data

import br.com.plusapps.plusfisio.features.auth.domain.AuthRepository
import br.com.plusapps.plusfisio.features.auth.domain.ObserveAuthSessionUseCase
import br.com.plusapps.plusfisio.features.auth.domain.SendPasswordResetUseCase
import br.com.plusapps.plusfisio.features.auth.domain.SignInUseCase
import br.com.plusapps.plusfisio.features.auth.domain.SignOutUseCase
import br.com.plusapps.plusfisio.features.auth.domain.SignUpUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    singleOf(::FirebaseAuthRepository) { bind<AuthRepository>() }
    singleOf(::ObserveAuthSessionUseCase)
    singleOf(::SendPasswordResetUseCase)
    singleOf(::SignInUseCase)
    singleOf(::SignUpUseCase)
    singleOf(::SignOutUseCase)
}
