package br.com.plusapps.plusfisio.features.auth.data

import br.com.plusapps.plusfisio.core.domain.Result
import br.com.plusapps.plusfisio.features.auth.domain.AuthError
import br.com.plusapps.plusfisio.features.auth.domain.AuthRepository
import br.com.plusapps.plusfisio.features.auth.domain.AuthSession
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuthException
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.FirebaseFirestoreException
import dev.gitlive.firebase.firestore.firestore
import kotlinx.serialization.Serializable

class FirebaseAuthRepository : AuthRepository {

    private val auth = Firebase.auth
    private val firestore = Firebase.firestore

    override suspend fun signIn(email: String, password: String): Result<AuthSession, AuthError> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password)
            val session = buildSession(requireNotNull(authResult.user))
            Result.Success(session)
        } catch (error: FirebaseAuthException) {
            Result.Failure(error.toAuthError())
        } catch (error: FirebaseFirestoreException) {
            Result.Failure(AuthError.Network)
        } catch (_: Exception) {
            Result.Failure(AuthError.Unknown)
        }
    }

    override suspend fun getCurrentSession(): AuthSession? {
        val currentUser = auth.currentUser ?: return null

        return try {
            buildSession(currentUser)
        } catch (_: FirebaseFirestoreException) {
            AuthSession(
                userId = currentUser.uid,
                email = currentUser.email.orEmpty(),
                displayName = currentUser.displayName,
                studioId = null
            )
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    private suspend fun buildSession(user: dev.gitlive.firebase.auth.FirebaseUser): AuthSession {
        val profile = upsertAndReadUserProfile(user)

        return AuthSession(
            userId = user.uid,
            email = profile.email.ifBlank { user.email.orEmpty() },
            displayName = profile.displayName ?: user.displayName,
            studioId = profile.studioId
        )
    }

    private suspend fun upsertAndReadUserProfile(
        user: dev.gitlive.firebase.auth.FirebaseUser
    ): UserProfileDocument {
        val reference = firestore.collection(USERS_COLLECTION).document(user.uid)
        val snapshot = reference.get()

        if (snapshot.exists) {
            return snapshot.data<UserProfileDocument>().mergeWith(user)
        }

        val profile = UserProfileDocument(
            email = user.email.orEmpty(),
            displayName = user.displayName,
            studioId = null
        )
        reference.set(profile)
        return profile
    }

    private fun UserProfileDocument.mergeWith(
        user: dev.gitlive.firebase.auth.FirebaseUser
    ): UserProfileDocument {
        return copy(
            email = email.ifBlank { user.email.orEmpty() },
            displayName = displayName ?: user.displayName
        )
    }

    private companion object {
        const val USERS_COLLECTION = "users"
    }
}

@Serializable
private data class UserProfileDocument(
    val email: String = "",
    val displayName: String? = null,
    val studioId: String? = null
)

private fun FirebaseAuthException.toAuthError(): AuthError {
    return mapFirebaseAuthMessageToError(message)
}

internal fun mapFirebaseAuthMessageToError(message: String?): AuthError {
    val normalizedMessage = message?.lowercase() ?: return AuthError.Unknown

    return when {
        "operation_not_allowed" in normalizedMessage -> AuthError.ProviderDisabled
        "password login is disabled" in normalizedMessage -> AuthError.ProviderDisabled
        "email/password accounts are not enabled" in normalizedMessage -> AuthError.ProviderDisabled
        "password" in normalizedMessage -> AuthError.InvalidCredentials
        "credential" in normalizedMessage -> AuthError.InvalidCredentials
        "user" in normalizedMessage && "not found" in normalizedMessage -> AuthError.InvalidCredentials
        "network" in normalizedMessage -> AuthError.Network
        else -> AuthError.Unknown
    }
}
