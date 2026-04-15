package br.com.plusapps.plusfisio.features.auth.data

import br.com.plusapps.plusfisio.core.data.firestore.buildBaseUserProfile
import br.com.plusapps.plusfisio.features.auth.domain.AuthError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class FirebaseAuthRepositoryTest {

    @Test
    fun `provider disabled message maps to provider disabled error`() {
        val error = mapFirebaseAuthMessageToError(
            "FirebaseAuthException: OPERATION_NOT_ALLOWED - Password sign-in is disabled for this project."
        )

        assertEquals(AuthError.ProviderDisabled, error)
    }

    @Test
    fun `invalid credential message maps to invalid credentials`() {
        val error = mapFirebaseAuthMessageToError(
            "The password is invalid or the user does not have a password."
        )

        assertEquals(AuthError.InvalidCredentials, error)
    }

    @Test
    fun `email already in use message maps to email already in use error`() {
        val error = mapFirebaseAuthMessageToError(
            "FirebaseAuthUserCollisionException: The email address is already in use by another account."
        )

        assertEquals(AuthError.EmailAlreadyInUse, error)
    }

    @Test
    fun `weak password message maps to weak password error`() {
        val error = mapFirebaseAuthMessageToError(
            "FirebaseAuthWeakPasswordException: The given password is invalid. Password should be at least 6 characters"
        )

        assertEquals(AuthError.WeakPassword, error)
    }

    @Test
    fun `firestore permission message maps to profile setup failed`() {
        val error = mapFirestoreMessageToAuthError(
            "PERMISSION_DENIED: Missing or insufficient permissions."
        )

        assertEquals(AuthError.ProfileSetupFailed, error)
    }

    @Test
    fun `firestore network message maps to network error`() {
        val error = mapFirestoreMessageToAuthError(
            "The service is currently unavailable because of network issues."
        )

        assertEquals(AuthError.Network, error)
    }

    @Test
    fun `base profile keeps user data and starts without studio context`() {
        val profile = buildBaseUserProfile(
            userId = "user-123",
            email = "owner@plusfisio.com",
            displayName = "Camila",
            now = 101L
        )

        assertEquals("user-123", profile.userId)
        assertEquals("owner@plusfisio.com", profile.email)
        assertEquals("Camila", profile.displayName)
        assertNull(profile.studioId)
        assertNull(profile.role)
        assertEquals(101L, profile.createdAtEpochMillis)
        assertEquals(101L, profile.updatedAtEpochMillis)
    }
}
