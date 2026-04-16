package br.com.plusapps.plusfisio.core.presentation.input

fun formatBrazilPhone(value: String): String {
    val digits = extractDigits(value).take(11)

    return when {
        digits.isEmpty() -> ""
        digits.length <= 2 -> "(${digits}"
        digits.length <= 6 -> "(${digits.take(2)}) ${digits.drop(2)}"
        digits.length <= 10 -> "(${digits.take(2)}) ${digits.drop(2).take(4)}-${digits.drop(6)}"
        else -> "(${digits.take(2)}) ${digits.drop(2).take(5)}-${digits.drop(7)}"
    }
}
