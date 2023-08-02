package no.ssb.kostra.area

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapConstants.ACCOUNTING_TYPE_BALANSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KAPITTEL
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SEKTOR
import no.ssb.kostra.area.regnskap.RegnskapConstants.TITLE_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.TITLE_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.TITLE_KAPITTEL
import no.ssb.kostra.area.regnskap.RegnskapConstants.TITLE_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.TITLE_SEKTOR
import no.ssb.kostra.program.FieldDefinition
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
    open val fieldDefinitions: List<FieldDefinition> = emptyList()

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


        val kostraRecordList = arguments
            .getInputContentAsStringList()
            .withIndex()
            .map { (index, recordString) ->
                recordString.toKostraRecord(
                    index = index + 1,
                    fieldDefinitions = fieldDefinitions
                )
            }

        val validationReportEntries = validationRules
            .mapNotNull { it.validate(kostraRecordList) }
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
                    FIELD_KONTOKLASSE,
                    FIELD_FUNKSJON,
                    FIELD_ART
                ) to listOf(
                    TITLE_KONTOKLASSE,
                    TITLE_FUNKSJON,
                    TITLE_ART
                )

            listOf(ACCOUNTING_TYPE_BALANSE) ->
                listOf(
                    FIELD_KONTOKLASSE,
                    FIELD_KAPITTEL,
                    FIELD_SEKTOR
                ) to listOf(
                    TITLE_KONTOKLASSE,
                    TITLE_KAPITTEL,
                    TITLE_SEKTOR
                )

            else -> emptyList<String>() to emptyList()
        }
}