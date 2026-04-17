package br.com.plusapps.plusfisio.features.clients.data

import br.com.plusapps.plusfisio.currentEpochMillis
import br.com.plusapps.plusfisio.core.domain.Result
import br.com.plusapps.plusfisio.core.domain.model.Client
import br.com.plusapps.plusfisio.core.domain.model.FirestoreCollections
import br.com.plusapps.plusfisio.features.clients.domain.ClientError
import br.com.plusapps.plusfisio.features.clients.domain.ClientRepository
import br.com.plusapps.plusfisio.features.clients.domain.CreateClientInput
import br.com.plusapps.plusfisio.features.clients.domain.UpdateClientInput
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FirebaseFirestoreException
import dev.gitlive.firebase.firestore.firestore
import kotlinx.serialization.Serializable
import kotlin.random.Random

class FirebaseClientRepository : ClientRepository {

    override suspend fun getClients(studioId: String): Result<List<Client>, ClientError> {
        val firestore = firestoreOrNull() ?: return Result.Failure(ClientError.Unknown)

        return try {
            val documents = clientsCollection(firestore, studioId)
                .get()
                .documents
                .map { snapshot -> snapshot.data<ClientDocument>().toDomain() }
                .sortedWith(
                    compareByDescending<Client> { it.updatedAtEpochMillis }
                        .thenBy { it.fullName.lowercase() }
                )

            Result.Success(documents)
        } catch (error: FirebaseFirestoreException) {
            Result.Failure(error.toClientError())
        } catch (_: Exception) {
            Result.Failure(ClientError.Unknown)
        }
    }

    override suspend fun getClient(studioId: String, clientId: String): Result<Client, ClientError> {
        val firestore = firestoreOrNull() ?: return Result.Failure(ClientError.Unknown)

        return try {
            val snapshot = clientsCollection(firestore, studioId).document(clientId).get()
            if (!snapshot.exists) {
                return Result.Failure(ClientError.NotFound)
            }

            Result.Success(snapshot.data<ClientDocument>().toDomain())
        } catch (error: FirebaseFirestoreException) {
            Result.Failure(error.toClientError())
        } catch (_: Exception) {
            Result.Failure(ClientError.Unknown)
        }
    }

    override suspend fun createClient(input: CreateClientInput): Result<Client, ClientError> {
        val firestore = firestoreOrNull() ?: return Result.Failure(ClientError.Unknown)
        val now = currentEpochMillis()
        val clientId = "client_${now}_${Random.nextInt(1000, 9999)}"
        val document = input.toDocument(
            clientId = clientId,
            createdAtEpochMillis = now,
            updatedAtEpochMillis = now
        )

        return try {
            clientsCollection(firestore, input.studioId)
                .document(clientId)
                .set(document)

            Result.Success(document.toDomain())
        } catch (error: FirebaseFirestoreException) {
            Result.Failure(error.toClientError())
        } catch (_: Exception) {
            Result.Failure(ClientError.Unknown)
        }
    }

    override suspend fun updateClient(input: UpdateClientInput): Result<Client, ClientError> {
        val firestore = firestoreOrNull() ?: return Result.Failure(ClientError.Unknown)
        val reference = clientsCollection(firestore, input.studioId).document(input.clientId)

        return try {
            val existingSnapshot = reference.get()
            if (!existingSnapshot.exists) {
                return Result.Failure(ClientError.NotFound)
            }

            val existing = existingSnapshot.data<ClientDocument>()
            val updatedDocument = input.toDocument(
                createdAtEpochMillis = existing.createdAtEpochMillis,
                updatedAtEpochMillis = currentEpochMillis()
            )

            reference.set(updatedDocument)
            Result.Success(updatedDocument.toDomain())
        } catch (error: FirebaseFirestoreException) {
            Result.Failure(error.toClientError())
        } catch (_: Exception) {
            Result.Failure(ClientError.Unknown)
        }
    }

    private fun firestoreOrNull() = runCatching { Firebase.firestore }.getOrNull()
}

@Serializable
internal data class ClientDocument(
    val clientId: String,
    val studioId: String,
    val fullName: String,
    val phone: String,
    val whatsappPhone: String? = null,
    val email: String? = null,
    val birthDate: String? = null,
    val primaryModality: String? = null,
    val responsibleProfessional: String? = null,
    val acquisitionSource: String? = null,
    val notes: String? = null,
    val status: String,
    val searchName: String,
    val createdAtEpochMillis: Long,
    val updatedAtEpochMillis: Long
)

private fun clientsCollection(
    firestore: dev.gitlive.firebase.firestore.FirebaseFirestore,
    studioId: String
) = firestore
    .collection(FirestoreCollections.STUDIOS)
    .document(studioId)
    .collection(FirestoreCollections.CLIENTS)

private fun CreateClientInput.toDocument(
    clientId: String,
    createdAtEpochMillis: Long,
    updatedAtEpochMillis: Long
): ClientDocument {
    val normalizedName = fullName.normalizeClientText()

    return ClientDocument(
        clientId = clientId,
        studioId = studioId,
        fullName = normalizedName,
        phone = phone.normalizePhone(),
        whatsappPhone = whatsappPhone.normalizeNullablePhone(),
        email = email.normalizeNullableText(),
        birthDate = birthDate.normalizeNullableText(),
        primaryModality = primaryModality.normalizeNullableText(),
        responsibleProfessional = responsibleProfessional.normalizeNullableText(),
        acquisitionSource = acquisitionSource.normalizeNullableText(),
        notes = notes.normalizeNullableText(),
        status = status.ifBlank { "active" },
        searchName = normalizedName.toSearchName(),
        createdAtEpochMillis = createdAtEpochMillis,
        updatedAtEpochMillis = updatedAtEpochMillis
    )
}

private fun UpdateClientInput.toDocument(
    createdAtEpochMillis: Long,
    updatedAtEpochMillis: Long
): ClientDocument {
    val normalizedName = fullName.normalizeClientText()

    return ClientDocument(
        clientId = clientId,
        studioId = studioId,
        fullName = normalizedName,
        phone = phone.normalizePhone(),
        whatsappPhone = whatsappPhone.normalizeNullablePhone(),
        email = email.normalizeNullableText(),
        birthDate = birthDate.normalizeNullableText(),
        primaryModality = primaryModality.normalizeNullableText(),
        responsibleProfessional = responsibleProfessional.normalizeNullableText(),
        acquisitionSource = acquisitionSource.normalizeNullableText(),
        notes = notes.normalizeNullableText(),
        status = status.ifBlank { "active" },
        searchName = normalizedName.toSearchName(),
        createdAtEpochMillis = createdAtEpochMillis,
        updatedAtEpochMillis = updatedAtEpochMillis
    )
}

internal fun ClientDocument.toDomain(): Client {
    return Client(
        clientId = clientId,
        studioId = studioId,
        fullName = fullName,
        phone = phone,
        whatsappPhone = whatsappPhone,
        email = email,
        birthDate = birthDate,
        primaryModality = primaryModality,
        responsibleProfessional = responsibleProfessional,
        acquisitionSource = acquisitionSource,
        notes = notes,
        status = status,
        searchName = searchName,
        createdAtEpochMillis = createdAtEpochMillis,
        updatedAtEpochMillis = updatedAtEpochMillis
    )
}

internal fun String.toSearchName(): String {
    if (isBlank()) return ""

    return lowercase()
        .map { character -> SearchCharMap[character] ?: character }
        .joinToString(separator = "")
        .replace(Regex("[^a-z0-9\\s]"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()
}

private fun String.normalizeClientText(): String {
    return trim()
        .replace(Regex("\\s+"), " ")
}

private fun String.normalizePhone(): String {
    return filter(Char::isDigit).take(11)
}

private fun String?.normalizeNullablePhone(): String? {
    val digits = this.normalizeNullableText()?.filter(Char::isDigit).orEmpty().take(11)
    return digits.ifBlank { null }
}

private fun String?.normalizeNullableText(): String? {
    val normalized = this?.trim()?.replace(Regex("\\s+"), " ").orEmpty()
    return normalized.ifBlank { null }
}

private fun FirebaseFirestoreException.toClientError(): ClientError {
    val normalizedMessage = message?.lowercase().orEmpty()

    return when {
        "permission" in normalizedMessage || "denied" in normalizedMessage -> ClientError.PermissionDenied
        "network" in normalizedMessage || "unavailable" in normalizedMessage -> ClientError.Network
        "not found" in normalizedMessage -> ClientError.NotFound
        else -> ClientError.Unknown
    }
}

private val SearchCharMap = mapOf(
    'á' to 'a',
    'à' to 'a',
    'ã' to 'a',
    'â' to 'a',
    'ä' to 'a',
    'é' to 'e',
    'è' to 'e',
    'ê' to 'e',
    'ë' to 'e',
    'í' to 'i',
    'ì' to 'i',
    'î' to 'i',
    'ï' to 'i',
    'ó' to 'o',
    'ò' to 'o',
    'õ' to 'o',
    'ô' to 'o',
    'ö' to 'o',
    'ú' to 'u',
    'ù' to 'u',
    'û' to 'u',
    'ü' to 'u',
    'ç' to 'c'
)
