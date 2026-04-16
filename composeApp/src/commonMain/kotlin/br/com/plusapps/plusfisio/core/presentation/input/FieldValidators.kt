package br.com.plusapps.plusfisio.core.presentation.input

private val EmailRegex = Regex("^[A-Za-z0-9+_.%\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$")
private val CityStateRegex = Regex("^.+,\\s*[A-Za-z]{2}$")

fun isValidEmail(value: String): Boolean = EmailRegex.matches(sanitizeEmail(value))

fun isValidBrazilPhone(value: String): Boolean {
    val digits = extractDigits(value)
    return digits.length == 10 || digits.length == 11
}

fun isValidCityState(value: String): Boolean = CityStateRegex.matches(normalizeCityState(value))
