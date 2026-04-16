package br.com.plusapps.plusfisio.features.onboarding.domain

import br.com.plusapps.plusfisio.core.domain.model.BusinessType
import br.com.plusapps.plusfisio.core.domain.model.TeamSizeRange

data class BootstrapStudioInput(
    val businessName: String,
    val businessType: BusinessType,
    val teamSize: TeamSizeRange,
    val cityState: String
)
