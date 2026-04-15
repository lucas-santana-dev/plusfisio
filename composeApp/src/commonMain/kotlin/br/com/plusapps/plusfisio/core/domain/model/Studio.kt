package br.com.plusapps.plusfisio.core.domain.model

data class Studio(
    val studioId: String,
    val name: String,
    val businessType: BusinessType,
    val ownerUserId: String,
    val timezone: String,
    val phone: String,
    val whatsappPhone: String?,
    val isActive: Boolean,
    val createdAtEpochMillis: Long,
    val updatedAtEpochMillis: Long
)
