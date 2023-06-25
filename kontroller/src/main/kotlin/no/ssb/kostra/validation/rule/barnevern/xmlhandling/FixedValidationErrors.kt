package no.ssb.kostra.validation.rule.barnevern.xmlhandling

import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.barnevern.AvgiverRuleId
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId

object FixedValidationErrors {

    fun xmlFileError(exceptionMessage: String?) = ValidationReportEntry(
        severity = Severity.ERROR,
        messageText = "Klarer ikke å lese fil. Får feilmeldingen: $exceptionMessage"
    )

    val individFileError = ValidationReportEntry(
        severity = Severity.ERROR,
        ruleName = IndividRuleId.INDIVID_01.title,
        messageText = "Definisjon av Individ er feil i forhold til filspesifikasjonen"
    )

    val individMissingError = ValidationReportEntry(
        severity = Severity.ERROR,
        ruleName = IndividRuleId.INDIVID_00.title,
        messageText = "Filen mangler individer"
    )

    val avgiverFileError = ValidationReportEntry(
        severity = Severity.ERROR,
        ruleName = AvgiverRuleId.AVGIVER_01.title,
        messageText = "Klarer ikke å validere Avgiver mot filspesifikasjon"
    )

    fun singleAvgiverError(found: Int) = ValidationReportEntry(
        severity = Severity.ERROR,
        ruleName = AvgiverRuleId.AVGIVER_00.title,
        messageText = "Antall avgivere skal være 1, fant $found"
    )
}