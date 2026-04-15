package br.com.plusapps.plusfisio.features.onboarding.domain

import br.com.plusapps.plusfisio.core.domain.Error

sealed interface OnboardingError : Error {
    data object UserNotAuthenticated : OnboardingError
    data object Network : OnboardingError
    data object PermissionDenied : OnboardingError
    data object Unknown : OnboardingError
}
