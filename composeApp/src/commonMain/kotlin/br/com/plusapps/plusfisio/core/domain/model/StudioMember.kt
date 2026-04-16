package br.com.plusapps.plusfisio.core.domain.model

data class StudioMember(
    val userId: String,
    val studioId: String,
    val role: StudioUserRole,
    val displayName: String?,
    val email: String,
    val isActive: Boolean,
    val createdAtEpochMillis: Long,
    val updatedAtEpochMillis: Long
)
