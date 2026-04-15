package br.com.plusapps.plusfisio.core.domain.model

data class Payment(
    val paymentId: String,
    val studioId: String,
    val clientId: String,
    val clientNameSnapshot: String,
    val packageId: String?,
    val amountCents: Long,
    val currency: String,
    val dueDateEpochMillis: Long,
    val status: String,
    val description: String,
    val paidAtEpochMillis: Long?,
    val createdAtEpochMillis: Long,
    val updatedAtEpochMillis: Long
)
