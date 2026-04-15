package br.com.plusapps.plusfisio.features.onboarding.data

import br.com.plusapps.plusfisio.currentEpochMillis
import br.com.plusapps.plusfisio.core.data.firestore.UserProfileDocument
import br.com.plusapps.plusfisio.core.data.firestore.buildBaseUserProfile
import br.com.plusapps.plusfisio.core.data.firestore.mergeWithAuth
import br.com.plusapps.plusfisio.core.domain.EmptyResult
import br.com.plusapps.plusfisio.core.domain.Result
import br.com.plusapps.plusfisio.core.domain.model.BusinessType
import br.com.plusapps.plusfisio.core.domain.model.FirestoreCollections
import br.com.plusapps.plusfisio.core.domain.model.StudioUserRole
import br.com.plusapps.plusfisio.features.onboarding.domain.BootstrapStudioInput
import br.com.plusapps.plusfisio.features.onboarding.domain.OnboardingError
import br.com.plusapps.plusfisio.features.onboarding.domain.StudioBootstrapRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.FirebaseFirestoreException
import dev.gitlive.firebase.firestore.firestore
import kotlinx.serialization.Serializable

class FirebaseStudioBootstrapRepository : StudioBootstrapRepository {

    private val auth = Firebase.auth
    private val firestore = Firebase.firestore

    override suspend fun bootstrapStudio(input: BootstrapStudioInput): EmptyResult<OnboardingError> {
        val currentUser = auth.currentUser ?: return Result.Failure(OnboardingError.UserNotAuthenticated)
        val now = currentEpochMillis()
        val studioId = stableStudioIdFor(currentUser.uid)
        val studioReference = firestore.collection(FirestoreCollections.STUDIOS).document(studioId)
        val memberReference = studioReference.collection(FirestoreCollections.MEMBERS).document(currentUser.uid)
        val userProfileReference = firestore.collection(FirestoreCollections.USERS).document(currentUser.uid)

        val studioDocument = StudioDocument(
            studioId = studioId,
            name = input.studioName.trim(),
            businessType = input.businessType,
            ownerUserId = currentUser.uid,
            timezone = input.timezone,
            phone = input.phone.trim(),
            whatsappPhone = input.whatsappPhone.trim().ifBlank { null },
            createdAtEpochMillis = now,
            updatedAtEpochMillis = now,
            isActive = true
        )
        val memberDocument = StudioMemberDocument(
            userId = currentUser.uid,
            studioId = studioId,
            role = StudioUserRole.OwnerAdmin,
            displayName = currentUser.displayName,
            email = currentUser.email.orEmpty(),
            isActive = true,
            createdAtEpochMillis = now,
            updatedAtEpochMillis = now
        )

        return try {
            val existingProfile = ensureBaseUserProfile(
                userProfileReference = userProfileReference,
                currentUser = currentUser,
                now = now
            )
            val userProfileDocument = existingProfile.toBootstrapProfile(
                userId = currentUser.uid,
                email = currentUser.email.orEmpty(),
                displayName = currentUser.displayName,
                studioId = studioId,
                now = now
            )

            if (existingProfile.studioId == studioId) {
                ensureBootstrapDocuments(
                    studioReference = studioReference,
                    memberReference = memberReference,
                    userProfileReference = userProfileReference,
                    studioDocument = studioDocument,
                    memberDocument = memberDocument,
                    userProfileDocument = userProfileDocument
                )
                return Result.Success(Unit)
            }

            ensureBootstrapDocuments(
                studioReference = studioReference,
                memberReference = memberReference,
                userProfileReference = userProfileReference,
                studioDocument = studioDocument,
                memberDocument = memberDocument,
                userProfileDocument = userProfileDocument
            )
            Result.Success(Unit)
        } catch (error: FirebaseFirestoreException) {
            Result.Failure(error.toOnboardingError())
        } catch (_: Exception) {
            Result.Failure(OnboardingError.Unknown)
        }
    }

    private suspend fun ensureBaseUserProfile(
        userProfileReference: dev.gitlive.firebase.firestore.DocumentReference,
        currentUser: dev.gitlive.firebase.auth.FirebaseUser,
        now: Long
    ): UserProfileDocument {
        val snapshot = userProfileReference.get()

        if (snapshot.exists) {
            return snapshot.data<UserProfileDocument>().mergeWithAuth(
                userId = currentUser.uid,
                email = currentUser.email.orEmpty(),
                displayName = currentUser.displayName
            )
        }

        val baseProfile = buildBaseUserProfile(
            userId = currentUser.uid,
            email = currentUser.email.orEmpty(),
            displayName = currentUser.displayName,
            now = now
        )
        userProfileReference.set(baseProfile)
        return baseProfile
    }

    private suspend fun ensureBootstrapDocuments(
        studioReference: dev.gitlive.firebase.firestore.DocumentReference,
        memberReference: dev.gitlive.firebase.firestore.DocumentReference,
        userProfileReference: dev.gitlive.firebase.firestore.DocumentReference,
        studioDocument: StudioDocument,
        memberDocument: StudioMemberDocument,
        userProfileDocument: UserProfileDocument
    ) {
        studioReference.set(studioDocument)
        memberReference.set(memberDocument)
        userProfileReference.set(userProfileDocument)
    }
}

@Serializable
private data class StudioDocument(
    val studioId: String,
    val name: String,
    val businessType: BusinessType,
    val ownerUserId: String,
    val timezone: String,
    val phone: String,
    val whatsappPhone: String? = null,
    val createdAtEpochMillis: Long,
    val updatedAtEpochMillis: Long,
    val isActive: Boolean
)

@Serializable
private data class StudioMemberDocument(
    val userId: String,
    val studioId: String,
    val role: StudioUserRole,
    val displayName: String? = null,
    val email: String,
    val isActive: Boolean,
    val createdAtEpochMillis: Long,
    val updatedAtEpochMillis: Long
)

private fun stableStudioIdFor(userId: String): String = "studio_${userId.take(12)}"

internal fun UserProfileDocument?.toBootstrapProfile(
    userId: String,
    email: String,
    displayName: String?,
    studioId: String,
    now: Long
): UserProfileDocument {
    return UserProfileDocument(
        userId = this?.userId?.ifBlank { userId } ?: userId,
        email = this?.email?.ifBlank { email } ?: email,
        displayName = this?.displayName ?: displayName,
        studioId = studioId,
        role = StudioUserRole.OwnerAdmin,
        createdAtEpochMillis = this?.createdAtEpochMillis ?: now,
        updatedAtEpochMillis = now
    )
}

private fun FirebaseFirestoreException.toOnboardingError(): OnboardingError {
    val normalizedMessage = message?.lowercase().orEmpty()
    return when {
        "permission" in normalizedMessage || "denied" in normalizedMessage -> OnboardingError.PermissionDenied
        "network" in normalizedMessage || "unavailable" in normalizedMessage -> OnboardingError.Network
        else -> OnboardingError.Unknown
    }
}
