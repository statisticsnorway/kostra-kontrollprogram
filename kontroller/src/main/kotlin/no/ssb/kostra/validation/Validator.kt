package no.ssb.kostra.validation


import no.ssb.kostra.program.FieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.StatsReportEntry
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule


abstract class Validator(
    val arguments: KotlinArguments
) {
    abstract val fatalRules: List<AbstractRule<List<String>>>
    abstract val validationRules: List<AbstractRule<List<KostraRecord>>>
    abstract val fieldDefinitions: FieldDefinitions

    open fun validate(): ValidationResult {
        if (fieldDefinitions.fieldDefinitions.isEmpty())
            throw IndexOutOfBoundsException("validate(): fieldDefinitions are missing")

        val fatalValidationReportEntries: List<ValidationReportEntry> = fatalRules
            .mapNotNull { it.validate(arguments.getInputContentAsStringList()) }
            .flatten()

        val fatalSeverity: Severity = fatalValidationReportEntries
            .map { it.severity }
            .maxByOrNull { it.ordinal } ?: Severity.OK

        if (fatalSeverity == Severity.FATAL) {
            return ValidationResult(
                reportEntries = fatalValidationReportEntries,
                numberOfControls = arguments.getInputContentAsStringList().size * fatalRules.size
            )
        }

        val kostraRecordList = arguments
            .getInputContentAsStringList()
            .withIndex()
            .map { (index, recordString) ->
                recordString.toKostraRecord(
                    index = index + 1,
                    fieldDefinitions = fieldDefinitions.fieldDefinitions
                )
            }

        val validationReportEntries = validationRules
            .mapNotNull { it.validate(context = kostraRecordList, arguments = arguments) }
            .flatten()

        val validationSeverity: Severity = validationReportEntries
            .map { it.severity }
            .maxByOrNull { it.ordinal } ?: Severity.OK

        return ValidationResult(
            reportEntries = validationReportEntries,
            numberOfControls = arguments.getInputContentAsStringList().size * fatalRules.size
                    + kostraRecordList.size * validationRules.size,
            statsEntries = if (validationSeverity < Severity.ERROR) createStats(kostraRecordList) else emptyList()
        )
    }

    open fun createStats(kostraRecordList: List<KostraRecord>): List<StatsReportEntry> = emptyList()
}
