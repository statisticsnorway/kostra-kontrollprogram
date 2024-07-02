package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.BEGYNT_DATO_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.VERSION_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.fieldAs
import no.ssb.kostra.program.extension.yearWithCentury
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId
import java.time.LocalDate
import java.time.Month

class Rule016BegyntDato : AbstractRule<List<KostraRecord>>(
    KvalifiseringRuleId.BEGYNT_DATO_16.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filterNot { record -> record[KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME] == "0301" }
        .filter {
            it.fieldAs<LocalDate?>(BEGYNT_DATO_COL_NAME) == null
                    ||
                    arguments.aargang.toInt().minus(it.fieldAs<LocalDate>(BEGYNT_DATO_COL_NAME).year) > 4
                    ||
                    it.fieldAs<LocalDate>(BEGYNT_DATO_COL_NAME) >
                    LocalDate.of(arguments.aargang.toInt(), Month.DECEMBER, 31)

        }
        .map {
            createValidationReportEntry(
                "Feltet for 'Hvilken dato begynte deltakeren i program? (iverksettelse)' med " +
                        "verdien (${it[BEGYNT_DATO_COL_NAME]}) enten mangler utfylling, har " +
                        "ugyldig dato eller dato som er eldre enn 4 år fra rapporteringsåret (${arguments.aargang}). " +
                        "Feltet er obligatorisk å fylle ut.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[SAKSBEHANDLER_COL_NAME],
                journalId = it[PERSON_JOURNALNR_COL_NAME],
            )
        }
        .ifEmpty { null }
}