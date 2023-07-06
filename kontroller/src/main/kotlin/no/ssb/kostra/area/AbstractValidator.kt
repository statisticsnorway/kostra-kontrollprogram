package no.ssb.kostra.area

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.ValidationResult

abstract class AbstractValidator(
    open val arguments: KotlinArguments
) {
    open val osloKommuner = listOf(
        // @formatter:off
        "030100",
        "030101", "030102", "030103", "030104", "030105",
        "030106", "030107", "030108", "030109", "030110",
        "030111", "030112", "030113", "030114", "030115"
        // @formatter:on
    )

    open val fatalRules: List<AbstractRule<List<String>>> = emptyList()
    open val validationRules: List<AbstractRule<List<KostraRecord>>> = emptyList()

    fun validate(): ValidationResult {
        val fatalValidationReportEntries: List<ValidationReportEntry> = fatalRules
            .mapNotNull { it.validate(arguments.getInputContentAsStringList()) }
            .flatten()

        val fatalSeverity: Severity = fatalValidationReportEntries
            .map { it.severity }
            .maxByOrNull { it.ordinal } ?: Severity.OK

        if (fatalSeverity == Severity.FATAL)
            return ValidationResult(
                reportEntries = fatalValidationReportEntries,
                numberOfControls = fatalValidationReportEntries.size * fatalRules.size
            )


        val derivedKostraRecords = arguments
            .getInputContentAsStringList()
            .withIndex()
            .map { (index, recordString) ->
                recordString.toKostraRecord(
                    index = index + 1,
                    fieldDefinitions = RegnskapFieldDefinitions.getFieldDefinitionsMergedWithKotlinArguments(arguments)
                )
            }

        val validationReportEntries = validationRules
            .mapNotNull { it.validate(derivedKostraRecords) }
            .flatten()

        return ValidationResult(
            reportEntries = validationReportEntries,
            numberOfControls = validationReportEntries.size * validationRules.size
        )

    }

    open fun mappingDuplicates(): Pair<List<String>, List<String>> =
        when (RegnskapConstants.getRegnskapTypeBySkjema(arguments.skjema)) {
            listOf(RegnskapConstants.ACCOUNTING_TYPE_BEVILGNING, RegnskapConstants.ACCOUNTING_TYPE_REGIONALE) ->
                listOf(
                    RegnskapConstants.FIELD_KONTOKLASSE,
                    RegnskapConstants.FIELD_FUNKSJON,
                    RegnskapConstants.FIELD_ART
                ) to listOf(
                    RegnskapConstants.TITLE_KONTOKLASSE,
                    RegnskapConstants.TITLE_FUNKSJON,
                    RegnskapConstants.TITLE_ART
                )

            listOf(RegnskapConstants.ACCOUNTING_TYPE_BALANSE) ->
                listOf(
                    RegnskapConstants.FIELD_KONTOKLASSE,
                    RegnskapConstants.FIELD_KAPITTEL,
                    RegnskapConstants.FIELD_SEKTOR
                ) to listOf(
                    RegnskapConstants.TITLE_KONTOKLASSE,
                    RegnskapConstants.TITLE_KAPITTEL,
                    RegnskapConstants.TITLE_SEKTOR
                )

            else -> emptyList<String>() to emptyList()
        }
}