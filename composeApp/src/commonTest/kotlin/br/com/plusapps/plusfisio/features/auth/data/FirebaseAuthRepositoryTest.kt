package br.com.plusapps.plusfisio.features.auth.data

import br.com.plusapps.plusfisio.features.auth.domain.AuthError
import kotlin.test.Test
import kotlin.test.assertEquals

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
}
