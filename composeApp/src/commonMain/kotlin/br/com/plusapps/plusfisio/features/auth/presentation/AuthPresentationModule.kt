package br.com.plusapps.plusfisio.features.auth.presentation

import br.com.plusapps.plusfisio.features.auth.presentation.login.LoginViewModel
import br.com.plusapps.plusfisio.features.auth.presentation.forgotpassword.ForgotPasswordViewModel
import br.com.plusapps.plusfisio.features.auth.presentation.signup.SignUpViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authPresentationModule = module {
    viewModelOf(::ForgotPasswordViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignUpViewModel)
}
