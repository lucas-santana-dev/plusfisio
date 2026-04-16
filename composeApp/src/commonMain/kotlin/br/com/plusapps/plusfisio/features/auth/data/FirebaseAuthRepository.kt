package br.com.plusapps.plusfisio.features.auth.data

import br.com.plusapps.plusfisio.currentEpochMillis
import br.com.plusapps.plusfisio.core.data.firestore.UserProfileDocument
import br.com.plusapps.plusfisio.core.data.firestore.buildBaseUserProfile
import br.com.plusapps.plusfisio.core.data.firestore.mergeWithAuth
import br.com.plusapps.plusfisio.core.domain.model.FirestoreCollections
import br.com.plusapps.plusfisio.core.domain.Result
import br.com.plusapps.plusfisio.core.presentation.input.formatBrazilPhone
import br.com.plusapps.plusfisio.features.auth.domain.AuthError
import br.com.plusapps.plusfisio.features.auth.domain.AuthRepository
import br.com.plusapps.plusfisio.features.auth.domain.AuthSession
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuthException
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.FirebaseFirestoreException
import dev.gitlive.firebase.firestore.firestore

class FirebaseAuthRepository : AuthRepository {

    override suspend fun signUp(
        name: String,
        whatsapp: String,
        email: String,
        password: String
    ): Result<AuthSession, AuthError> {
        val auth = authOrNull() ?: return Result.Failure(AuthError.ProviderDisabled)
        val normalizedName = name.trim()
        val normalizedEmail = email.trim()
        val normalizedWhatsapp = formatBrazilPhone(whatsapp)
        val userResult: Result<dev.gitlive.firebase.auth.FirebaseUser, AuthError> = try {
            Result.Success(requireNotNull(auth.createUserWithEmailAndPassword(normalizedEmail, password).user))
        } catch (error: FirebaseAuthException) {
            Result.Failure(error.toAuthError())
        } catch (_: Exception) {
            Result.Failure(AuthError.Unknown)
        }

        val user = when (userResult) {
            is Result.Failure -> return userResult
            is Result.Success -> userResult.data
        }

        if (normalizedName.isNotBlank()) {
            try {
                user.updateProfile(displayName = normalizedName)
            } catch (_: Exception) {
                // Best effort only. The profile document remains the source of truth for the app.
            }
        }

        return resolveSession(
            user = user,
            preferredDisplayName = normalizedName,
            preferredWhatsapp = normalizedWhatsapp
        )
    }

    override suspend fun signIn(email: String, password: String): Result<AuthSession, AuthError> {
        val auth = authOrNull() ?: return Result.Failure(AuthError.ProviderDisabled)
        val userResult: Result<dev.gitlive.firebase.auth.FirebaseUser, AuthError> = try {
            Result.Success(requireNotNull(auth.signInWithEmailAndPassword(email, password).user))
        } catch (error: FirebaseAuthException) {
            Result.Failure(error.toAuthError())
        } catch (_: Exception) {
            Result.Failure(AuthError.Unknown)
        }

        val user = when (userResult) {
            is Result.Failure -> return userResult
            is Result.Success -> userResult.data
        }

        return resolveSession(user)
    }

    override suspend fun getCurrentSession(): AuthSession? {
        val auth = authOrNull() ?: return null
        val currentUser = auth.currentUser ?: return null

        return when (val sessionResult = resolveSession(currentUser)) {
            is Result.Failure -> null
            is Result.Success -> sessionResult.data
        }
    }

    override suspend fun sendPasswordReset(email: String): Result<Unit, AuthError> {
        val auth = authOrNull() ?: return Result.Failure(AuthError.ProviderDisabled)

        return try {
            auth.sendPasswordResetEmail(email.trim())
            Result.Success(Unit)
        } catch (error: FirebaseAuthException) {
            Result.Failure(error.toPasswordResetError())
        } catch (_: Exception) {
            Result.Failure(AuthError.PasswordResetFailed)
        }
    }

    override suspend fun signOut() {
        authOrNull()?.signOut()
    }

    private suspend fun resolveSession(
        user: dev.gitlive.firebase.auth.FirebaseUser,
        preferredDisplayName: String? = null,
        preferredWhatsapp: String? = null
    ): Result<AuthSession, AuthError> {
        return try {
            Result.Success(
                buildSession(
                    user = user,
                    preferredDisplayName = preferredDisplayName,
                    preferredWhatsapp = preferredWhatsapp
                )
            )
        } catch (error: FirebaseFirestoreException) {
            Result.Failure(error.toProfileAuthError())
        } catch (_: Exception) {
            Result.Failure(AuthError.Unknown)
        }
    }

    private suspend fun buildSession(
        user: dev.gitlive.firebase.auth.FirebaseUser,
        preferredDisplayName: String? = null,
        preferredWhatsapp: String? = null
    ): AuthSession {
        val profile = ensureBaseUserProfile(
            user = user,
            preferredDisplayName = preferredDisplayName,
            preferredWhatsapp = preferredWhatsapp
        )

        return AuthSession(
            userId = user.uid,
            email = profile.email.ifBlank { user.email.orEmpty() },
            displayName = profile.displayName ?: preferredDisplayName ?: user.displayName,
            studioId = profile.studioId,
            role = profile.role
        )
    }

    private suspend fun ensureBaseUserProfile(
        user: dev.gitlive.firebase.auth.FirebaseUser,
        preferredDisplayName: String? = null,
        preferredWhatsapp: String? = null
    ): UserProfileDocument {
        val firestore = firestoreOrNull() ?: throw IllegalStateException("Firebase Firestore is not configured")
        val reference = firestore.collection(FirestoreCollections.USERS).document(user.uid)
        val snapshot = reference.get()

        if (snapshot.exists) {
            val existingProfile = snapshot.data<UserProfileDocument>()
            val mergedProfile = existingProfile.mergeWithAuth(
                userId = user.uid,
                email = user.email.orEmpty(),
                displayName = preferredDisplayName ?: user.displayName,
                whatsapp = preferredWhatsapp
            )
            if (mergedProfile != existingProfile) {
                reference.set(mergedProfile)
            }
            return mergedProfile
        }

        val now = currentEpochMillis()
        val profile = buildBaseUserProfile(
            userId = user.uid,
            email = user.email.orEmpty(),
            displayName = preferredDisplayName ?: user.displayName,
            whatsapp = preferredWhatsapp,
            now = now
        )
        reference.set(profile)
        return profile
    }

    private fun authOrNull() = runCatching { Firebase.auth }.getOrNull()

    private fun firestoreOrNull() = runCatching { Firebase.firestore }.getOrNull()
}

private fun FirebaseAuthException.toAuthError(): AuthError {
    return mapFirebaseAuthMessageToError(message)
}

internal fun mapFirestoreMessageToAuthError(message: String?): AuthError {
    val normalizedMessage = message?.lowercase().orEmpty()

    return when {
        "network" in normalizedMessage || "unavailable" in normalizedMessage -> AuthError.Network
        else -> AuthError.ProfileSetupFailed
    }
}

private fun FirebaseFirestoreException.toProfileAuthError(): AuthError {
    return mapFirestoreMessageToAuthError(message)
}

internal fun mapFirebaseAuthMessageToError(message: String?): AuthError {
    val normalizedMessage = message?.lowercase() ?: return AuthError.Unknown

    return when {
        "operation_not_allowed" in normalizedMessage -> AuthError.ProviderDisabled
        "password login is disabled" in normalizedMessage -> AuthError.ProviderDisabled
        "email/password accounts are not enabled" in normalizedMessage -> AuthError.ProviderDisabled
        "email-already-in-use" in normalizedMessage -> AuthError.EmailAlreadyInUse
        "already in use" in normalizedMessage -> AuthError.EmailAlreadyInUse
        "weak-password" in normalizedMessage -> AuthError.WeakPassword
        "at least 6 characters" in normalizedMessage -> AuthError.WeakPassword
        "password should be at least" in normalizedMessage -> AuthError.WeakPassword
        "password" in normalizedMessage -> AuthError.InvalidCredentials
        "credential" in normalizedMessage -> AuthError.InvalidCredentials
        "user" in normalizedMessage && "not found" in normalizedMessage -> AuthError.InvalidCredentials
        "network" in normalizedMessage -> AuthError.Network
        else -> AuthError.Unknown
    }
}

private fun FirebaseAuthException.toPasswordResetError(): AuthError {
    val authError = mapFirebaseAuthMessageToError(message)
    return when (authError) {
        AuthError.Network -> AuthError.Network
        AuthError.ProviderDisabled -> AuthError.ProviderDisabled
        else -> AuthError.PasswordResetFailed
    }
}
