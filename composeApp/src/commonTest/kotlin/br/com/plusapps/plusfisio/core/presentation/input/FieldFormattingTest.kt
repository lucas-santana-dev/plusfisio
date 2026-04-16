package br.com.plusapps.plusfisio.core.presentation.input

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FieldFormattingTest {

    @Test
    fun `format brazil phone applies eleven digit mask`() {
        assertEquals("(11) 98765-4321", formatBrazilPhone("11987654321"))
    }

    @Test
    fun `valid city state requires city and uf`() {
        assertTrue(isValidCityState("Sao Paulo, SP"))
        assertFalse(isValidCityState("Sao Paulo"))
    }

    @Test
    fun `extract digits removes formatting`() {
        assertEquals("11987654321", extractDigits("(11) 98765-4321"))
    }
}
