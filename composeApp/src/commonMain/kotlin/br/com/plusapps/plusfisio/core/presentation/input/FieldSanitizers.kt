package br.com.plusapps.plusfisio.core.presentation.input

fun sanitizeEmail(value: String): String = value.trim()

fun extractDigits(value: String): String = value.filter(Char::isDigit)

fun normalizeCityState(value: String): String {
    return value
        .trim()
        .replace(Regex("\\s+"), " ")
        .replace(Regex("\\s*,\\s*"), ", ")
}
