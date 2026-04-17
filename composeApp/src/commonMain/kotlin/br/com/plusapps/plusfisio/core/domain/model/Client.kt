package br.com.plusapps.plusfisio.core.domain.model

data class Client(
    val clientId: String,
    val studioId: String,
    val fullName: String,
    val phone: String,
    val whatsappPhone: String?,
    val email: String?,
    val birthDate: String?,
    val primaryModality: String?,
    val responsibleProfessional: String?,
    val acquisitionSource: String?,
    val notes: String?,
    val status: String,
    val searchName: String,
    val createdAtEpochMillis: Long,
    val updatedAtEpochMillis: Long
)
