package no.ssb.kostra.validation


import no.ssb.kostra.program.FieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.StatsReportEntry
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.report.ValidationResult
import no.ssb.kostra.validation.rule.AbstractRule


abstract class PositionedFileValidator(
    override val arguments: KotlinArguments
) : Validator {
    abstract val preValidationRules: List<AbstractRule<List<String>>>
    abstract val validationRules: List<AbstractRule<List<KostraRecord>>>
    abstract val fieldDefinitions: FieldDefinitions

    override fun validate(): ValidationResult {
        if (fieldDefinitions.fieldDefinitions.isEmpty())
            throw IndexOutOfBoundsException("validate(): fieldDefinitions are missing")

        val preCheckValidationReportEntries: List<ValidationReportEntry>? =
            preValidationRules.firstNotNullOfOrNull { it.validate(arguments.getInputContentAsStringList(), arguments) }

        if (preCheckValidationReportEntries != null)

                return ValidationResult(
                    reportEntries = preCheckValidationReportEntries,
                    numberOfControls = preValidationRules.size
                )


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
            numberOfControls = arguments.getInputContentAsStringList().size * preValidationRules.size
                    + kostraRecordList.size * validationRules.size,
            statsReportEntries = if (validationSeverity < Severity.ERROR) createStats(kostraRecordList) else emptyList()
        )
    }

    open fun createStats(kostraRecordList: List<KostraRecord>): List<StatsReportEntry> = emptyList()
}
