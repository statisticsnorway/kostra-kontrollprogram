package no.ssb.kostra.validation.report

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.ssb.kostra.felles.git.GitProperties
import no.ssb.kostra.felles.git.GitPropertiesLoader
import java.time.Duration
import java.time.LocalDateTime
import kotlin.collections.count

class StructuredValidationReport(
    val validationReportArguments: ValidationReportArguments,
    val gitProperties: GitProperties = GitPropertiesLoader.loadGitProperties(
        GitPropertiesLoader.DEFAULT_GIT_PROPERTIES_FILENAME
    )
) {

    private val mapper: ObjectMapper = jacksonObjectMapper()
        .registerModule(JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    @Override
    override fun toString(): String {
        val now = LocalDateTime.now()
        val reportGenerationTime = Duration.between(validationReportArguments.kotlinArguments.startTime, now)

        with(validationReportArguments.validationResult) {
            val validationSummary = ValidationSummary(
                region = validationReportArguments.kotlinArguments.region,
                name = validationReportArguments.kotlinArguments.navn,
                scheme = validationReportArguments.kotlinArguments.skjema,
                year = validationReportArguments.kotlinArguments.aargang,
                reportStartedAt = validationReportArguments.kotlinArguments.startTime,
                numberOfControls = numberOfControls,
                programVersion = gitProperties.tags,
                reportCompletedAt = now,
                reportGenerationTime = reportGenerationTime,
                numFatal = getNumberOfEntriesBySeverity(reportEntries, Severity.FATAL),
                numErrors = getNumberOfEntriesBySeverity(reportEntries, Severity.ERROR),
                numWarnings = getNumberOfEntriesBySeverity(reportEntries, Severity.WARNING),
                numInfo = getNumberOfEntriesBySeverity(reportEntries, Severity.INFO),
                numOk = reportEntries.size - (
                        getNumberOfEntriesBySeverity(reportEntries, Severity.FATAL) +
                                getNumberOfEntriesBySeverity(reportEntries, Severity.ERROR) +
                                getNumberOfEntriesBySeverity(reportEntries, Severity.WARNING) +
                                getNumberOfEntriesBySeverity(reportEntries, Severity.INFO)
                        ),
                severity = severity.info.returnCode
            )
            val reportEntries = reportEntries
            val result = StructuredValidationContainer(
                validationSummary = validationSummary,
                reportEntries = reportEntries
            )
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result)
        }
    }

    private fun getNumberOfEntriesBySeverity(reportEntires: List<ValidationReportEntry>, severity: Severity): Int {
        return reportEntires.count { it.severity == severity }
    }
}

data class StructuredValidationContainer(
    val validationSummary: ValidationSummary,
    val reportEntries: List<ValidationReportEntry>
)

data class ValidationSummary(
    val region: String,
    val name: String,
    val programVersion: String,
    val reportStartedAt: LocalDateTime,
    val reportCompletedAt: LocalDateTime,
    val reportGenerationTime: Duration,
    val scheme: String,
    val year: String,
    val numberOfControls: Int,
    val severity: Int,
    val numFatal: Int,
    val numErrors: Int,
    val numWarnings: Int,
    val numInfo: Int,
    val numOk: Int
)
