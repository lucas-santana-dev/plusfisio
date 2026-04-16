package br.com.plusapps.plusfisio.core.presentation.input

fun cityStateSuggestions(query: String, limit: Int = 8): List<String> {
    val normalizedQuery = normalizeSearchKey(query)
    if (normalizedQuery.isBlank()) return emptyList()

    return BrazilMunicipalitiesData.suggestions
        .asSequence()
        .distinct()
        .sortedWith(
            compareBy<String> { suggestion ->
                val normalizedSuggestion = normalizeSearchKey(suggestion)
                when {
                    normalizedSuggestion.startsWith(normalizedQuery) -> 0
                    normalizedSuggestion.contains(normalizedQuery) -> 1
                    else -> 2
                }
            }.thenBy { it }
        )
        .filter { normalizeSearchKey(it).contains(normalizedQuery) }
        .take(limit)
        .toList()
}

private fun normalizeSearchKey(value: String): String {
    return value
        .trim()
        .lowercase()
        .replace("á", "a")
        .replace("à", "a")
        .replace("â", "a")
        .replace("ã", "a")
        .replace("ä", "a")
        .replace("é", "e")
        .replace("è", "e")
        .replace("ê", "e")
        .replace("ë", "e")
        .replace("í", "i")
        .replace("ì", "i")
        .replace("î", "i")
        .replace("ï", "i")
        .replace("ó", "o")
        .replace("ò", "o")
        .replace("ô", "o")
        .replace("õ", "o")
        .replace("ö", "o")
        .replace("ú", "u")
        .replace("ù", "u")
        .replace("û", "u")
        .replace("ü", "u")
        .replace("ç", "c")
}
