package br.com.plusapps.plusfisio.core.data.firestore

import br.com.plusapps.plusfisio.core.domain.model.StudioUserRole
import kotlinx.serialization.Serializable

@Serializable
internal data class UserProfileDocument(
    val userId: String = "",
    val email: String = "",
    val displayName: String? = null,
    val studioId: String? = null,
    val role: StudioUserRole? = null,
    val createdAtEpochMillis: Long = 0L,
    val updatedAtEpochMillis: Long = 0L
)

internal fun buildBaseUserProfile(
    userId: String,
    email: String,
    displayName: String?,
    now: Long
): UserProfileDocument {
    return UserProfileDocument(
        userId = userId,
        email = email,
        displayName = displayName,
        studioId = null,
        role = null,
        createdAtEpochMillis = now,
        updatedAtEpochMillis = now
    )
}

internal fun UserProfileDocument.mergeWithAuth(
    userId: String,
    email: String,
    displayName: String?
): UserProfileDocument {
    return copy(
        userId = this.userId.ifBlank { userId },
        email = this.email.ifBlank { email },
        displayName = this.displayName ?: displayName
    )
}
