package br.com.plusapps.plusfisio.features.onboarding.data

import br.com.plusapps.plusfisio.core.data.firestore.UserProfileDocument
import br.com.plusapps.plusfisio.core.domain.model.StudioUserRole
import kotlin.test.Test
import kotlin.test.assertEquals

class FirebaseStudioBootstrapRepositoryTest {

    @Test
    fun `bootstrap profile preserves createdAt from existing user profile`() {
        val existingProfile = UserProfileDocument(
            userId = "user-123",
            email = "owner@clinic.com",
            displayName = "Owner",
            studioId = null,
            role = null,
            createdAtEpochMillis = 101L,
            updatedAtEpochMillis = 202L
        )

        val bootstrapProfile = existingProfile.toBootstrapProfile(
            userId = "user-123",
            email = "owner@clinic.com",
            displayName = "Owner",
            studioId = "studio_user-123",
            now = 999L
        )

        assertEquals(101L, bootstrapProfile.createdAtEpochMillis)
        assertEquals(999L, bootstrapProfile.updatedAtEpochMillis)
        assertEquals("studio_user-123", bootstrapProfile.studioId)
        assertEquals(StudioUserRole.OwnerAdmin, bootstrapProfile.role)
    }

    @Test
    fun `bootstrap profile falls back to auth data when profile is missing`() {
        val bootstrapProfile = (null as UserProfileDocument?).toBootstrapProfile(
            userId = "user-123",
            email = "owner@clinic.com",
            displayName = "Owner",
            studioId = "studio_user-123",
            now = 999L
        )

        assertEquals("user-123", bootstrapProfile.userId)
        assertEquals("owner@clinic.com", bootstrapProfile.email)
        assertEquals("Owner", bootstrapProfile.displayName)
        assertEquals(999L, bootstrapProfile.createdAtEpochMillis)
        assertEquals(999L, bootstrapProfile.updatedAtEpochMillis)
    }
}
