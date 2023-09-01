package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_VIKTIGSTE_INNTEKT_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringConstants.FULLFORT_PROGRAM
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeIsMissing
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Rule039FullforteAvsluttedeProgramInntektkilde : AbstractRule<List<KostraRecord>>(
    KvalifiseringRuleId.FULLFORTE_AVSLUTTEDE_PROGRAM_INNTEKTKILDE_39.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filter { it[STATUS_COL_NAME] == FULLFORT_PROGRAM }
        .filter {
            fieldDefinitions.byColumnName(AVSL_VIKTIGSTE_INNTEKT_COL_NAME)
                .codeIsMissing(it[AVSL_VIKTIGSTE_INNTEKT_COL_NAME])
        }.map {
            createValidationReportEntry(
                "Feltet 'Hva var deltakerens <b>viktigste</b> inntektskilde umiddelbart etter avslutningen? " +
                        "Må fylles ut dersom det er krysset av for svaralternativ " +
                        "3 = Deltakeren har fullført program eller avsluttet program etter avtale (gjelder ikke flytting) eller " +
                        "7 = Deltakeren program er avsluttet etter avbrudd under feltet for " +
                        "'Hva er status for deltakelsen i kvalifiseringsprogrammet per per 31.12.${arguments.aargang}'?",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME],
                journalId = it[KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }
}