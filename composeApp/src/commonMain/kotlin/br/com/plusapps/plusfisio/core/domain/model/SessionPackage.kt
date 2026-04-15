package br.com.plusapps.plusfisio.core.domain.model

data class SessionPackage(
    val packageId: String,
    val studioId: String,
    val clientId: String,
    val clientNameSnapshot: String,
    val title: String,
    val sessionCountTotal: Int,
    val sessionCountRemaining: Int,
    val status: String,
    val startDateEpochMillis: Long,
    val expiresAtEpochMillis: Long?,
    val createdAtEpochMillis: Long,
    val updatedAtEpochMillis: Long
)
