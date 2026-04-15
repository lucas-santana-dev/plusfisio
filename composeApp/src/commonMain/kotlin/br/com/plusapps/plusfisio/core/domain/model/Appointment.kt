package br.com.plusapps.plusfisio.core.domain.model

data class Appointment(
    val appointmentId: String,
    val studioId: String,
    val clientId: String,
    val clientNameSnapshot: String,
    val professionalUserId: String,
    val professionalNameSnapshot: String,
    val startsAtEpochMillis: Long,
    val endsAtEpochMillis: Long,
    val dayKey: String,
    val status: String,
    val confirmationStatus: String,
    val packageId: String?,
    val notes: String?,
    val createdAtEpochMillis: Long,
    val updatedAtEpochMillis: Long
)
