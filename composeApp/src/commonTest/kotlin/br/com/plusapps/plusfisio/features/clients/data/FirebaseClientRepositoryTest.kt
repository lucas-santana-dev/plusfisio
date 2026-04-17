package br.com.plusapps.plusfisio.features.clients.data

import kotlin.test.Test
import kotlin.test.assertEquals

class FirebaseClientRepositoryTest {

    @Test
    fun `search name normalizes common portuguese accents`() {
        assertEquals(
            "juliana cecilia",
            "Juliana Cecília".toSearchName()
        )
        assertEquals(
            "clinica sao jose",
            "Clínica São José".toSearchName()
        )
    }

    @Test
    fun `client document maps optional fields to domain`() {
        val document = ClientDocument(
            clientId = "client-1",
            studioId = "studio-1",
            fullName = "Juliana Martins",
            phone = "11999990000",
            whatsappPhone = "11999990000",
            email = "juliana@plusfisio.com",
            birthDate = "10/05/1992",
            primaryModality = "Pilates solo",
            responsibleProfessional = "Ana Souza",
            acquisitionSource = "Indicacao",
            notes = "Prefere atendimento no fim da tarde.",
            status = "active",
            searchName = "juliana martins",
            createdAtEpochMillis = 10L,
            updatedAtEpochMillis = 20L
        )

        val client = document.toDomain()

        assertEquals("Juliana Martins", client.fullName)
        assertEquals("Pilates solo", client.primaryModality)
        assertEquals("Ana Souza", client.responsibleProfessional)
        assertEquals("Indicacao", client.acquisitionSource)
        assertEquals("10/05/1992", client.birthDate)
    }
}
