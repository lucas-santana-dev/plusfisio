package br.com.plusapps.plusfisio.features.onboarding.domain

import br.com.plusapps.plusfisio.core.domain.model.BusinessType

data class BootstrapStudioInput(
    val studioName: String,
    val businessType: BusinessType,
    val phone: String,
    val whatsappPhone: String,
    val timezone: String
)
