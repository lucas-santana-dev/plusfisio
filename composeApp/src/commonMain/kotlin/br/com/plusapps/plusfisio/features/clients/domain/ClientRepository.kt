package br.com.plusapps.plusfisio.features.clients.domain

import br.com.plusapps.plusfisio.core.domain.Error
import br.com.plusapps.plusfisio.core.domain.Result
import br.com.plusapps.plusfisio.core.domain.model.Client

interface ClientRepository {
    suspend fun getClients(studioId: String): Result<List<Client>, ClientError>
    suspend fun getClient(studioId: String, clientId: String): Result<Client, ClientError>
    suspend fun createClient(input: CreateClientInput): Result<Client, ClientError>
    suspend fun updateClient(input: UpdateClientInput): Result<Client, ClientError>
}

data class CreateClientInput(
    val studioId: String,
    val fullName: String,
    val phone: String,
    val whatsappPhone: String?,
    val email: String?,
    val birthDate: String?,
    val primaryModality: String?,
    val responsibleProfessional: String?,
    val acquisitionSource: String?,
    val notes: String?,
    val status: String = "active"
)

data class UpdateClientInput(
    val studioId: String,
    val clientId: String,
    val fullName: String,
    val phone: String,
    val whatsappPhone: String?,
    val email: String?,
    val birthDate: String?,
    val primaryModality: String?,
    val responsibleProfessional: String?,
    val acquisitionSource: String?,
    val notes: String?,
    val status: String = "active"
)

sealed interface ClientError : Error {
    data object NotFound : ClientError
    data object PermissionDenied : ClientError
    data object Network : ClientError
    data object Unknown : ClientError
}
