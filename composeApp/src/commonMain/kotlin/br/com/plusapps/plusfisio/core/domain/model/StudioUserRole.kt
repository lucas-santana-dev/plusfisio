package br.com.plusapps.plusfisio.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class StudioUserRole {
    OwnerAdmin,
    Professional
}
