package br.com.plusapps.plusfisio.core.domain.model

data class PackageLedgerEntry(
    val entryId: String,
    val studioId: String,
    val packageId: String,
    val clientId: String,
    val appointmentId: String?,
    val type: String,
    val delta: Int,
    val reason: String?,
    val createdAtEpochMillis: Long,
    val createdByUserId: String
)
