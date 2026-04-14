package br.com.plusapps.plusfisio.core.presentation.text

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

/**
 * Representa textos de UI de forma independente da plataforma.
 *
 * Permite que ViewModels e estados carreguem referencias de texto sem depender
 * de APIs Android ou de resolucao local em cada plataforma.
 */
sealed interface UiText {
    data class Resource(val value: StringResource) : UiText
    data class Dynamic(val value: String) : UiText
}

@Composable
fun UiText.asString(): String {
    return when (this) {
        is UiText.Resource -> stringResource(value)
        is UiText.Dynamic -> value
    }
}

suspend fun UiText.resolve(): String {
    return when (this) {
        is UiText.Resource -> getString(value)
        is UiText.Dynamic -> value
    }
}
