package br.com.plusapps.plusfisio.core.presentation.input

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

object BrazilPhoneVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.take(11)
        val formatted = formatBrazilPhone(digits)

        val mapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val clamped = offset.coerceIn(0, digits.length)
                var transformedIndex = 0
                var digitsSeen = 0

                while (transformedIndex < formatted.length && digitsSeen < clamped) {
                    if (formatted[transformedIndex].isDigit()) {
                        digitsSeen++
                    }
                    transformedIndex++
                }

                return transformedIndex.coerceAtMost(formatted.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                val clamped = offset.coerceIn(0, formatted.length)
                return formatted
                    .take(clamped)
                    .count(Char::isDigit)
                    .coerceAtMost(digits.length)
            }
        }

        return TransformedText(AnnotatedString(formatted), mapping)
    }
}
