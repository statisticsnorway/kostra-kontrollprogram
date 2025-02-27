package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_DATO_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.fieldAs
import no.ssb.kostra.program.extension.isValidDate
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId
import java.time.LocalDate
import java.time.Month

class Rule037DatoForAvsluttetProgram :
    AbstractRule<List<KostraRecord>>(
        KvalifiseringRuleId.DATO_FOR_AVSLUTTET_PROGRAM_37.title,
        Severity.ERROR,
    ) {
    override fun validate(
        context: List<KostraRecord>,
        arguments: KotlinArguments,
    ) = context
        .filter {
            it[STATUS_COL_NAME] in codesThatRequiresDate
        }.filter {
            it.fieldAs<LocalDate?>(AVSL_DATO_COL_NAME) == null ||
                !it
                    .fieldAsString(AVSL_DATO_COL_NAME)
                    .isValidDate(
                        fieldDefinitions
                            .byColumnName(AVSL_DATO_COL_NAME)
                            .datePattern,
                    ) ||
                it.fieldAs<LocalDate>(AVSL_DATO_COL_NAME) <
                LocalDate.of(arguments.aargang.toInt(), Month.JANUARY, 1) ||
                it.fieldAs<LocalDate>(AVSL_DATO_COL_NAME) >
                LocalDate.of(arguments.aargang.toInt(), Month.DECEMBER, 31)
        }.map {
            createValidationReportEntry(
                "Feltet for 'Hvilken dato avsluttet deltakeren programmet?', må fylles " +
                    "ut dersom det er krysset av for svaralternativ $codeListThatRequiredDate under feltet for " +
                    "'Hva er status for deltakelsen i kvalifiseringsprogrammet per 31.12.${arguments.aargang}'?",
                lineNumbers = listOf(it.lineNumber),
            ).copy(
                caseworker = it[KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME],
                journalId = it[KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }

    companion object {
        internal val codesThatRequiresDate = setOf("3", "4", "5", "7")

        internal val codeListThatRequiredDate =
            fieldDefinitions
                .byColumnName(STATUS_COL_NAME)
                .codeList
                .filter { it.code in codesThatRequiresDate }
                .map { it.toString() }
    }
}
