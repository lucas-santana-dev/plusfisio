package br.com.plusapps.plusfisio.features.onboarding.presentation

import br.com.plusapps.plusfisio.core.presentation.text.UiText
import br.com.plusapps.plusfisio.features.onboarding.domain.OnboardingError
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.onboarding_error_network
import plusfisio.composeapp.generated.resources.onboarding_error_permission_denied
import plusfisio.composeapp.generated.resources.onboarding_error_unknown
import plusfisio.composeapp.generated.resources.onboarding_error_user_not_authenticated

fun OnboardingError.toUiText(): UiText {
    return when (this) {
        OnboardingError.Network -> UiText.Resource(Res.string.onboarding_error_network)
        OnboardingError.PermissionDenied -> UiText.Resource(Res.string.onboarding_error_permission_denied)
        OnboardingError.Unknown -> UiText.Resource(Res.string.onboarding_error_unknown)
        OnboardingError.UserNotAuthenticated -> UiText.Resource(Res.string.onboarding_error_user_not_authenticated)
    }
}
