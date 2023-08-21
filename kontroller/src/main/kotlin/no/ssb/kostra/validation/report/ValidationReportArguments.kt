package no.ssb.kostra.validation.report

import jakarta.inject.Inject
import no.ssb.kostra.felles.git.GitProperties
import no.ssb.kostra.program.KotlinArguments
import java.time.LocalDateTime

data class ValidationReportArguments(
    val kotlinArguments: KotlinArguments,
    val validationResult: ValidationResult,
    val endTime: LocalDateTime = LocalDateTime.now(),

    ) {
    @Inject
    lateinit var gitProperties: GitProperties // inneholder versjonsinformasjonﬁ
}