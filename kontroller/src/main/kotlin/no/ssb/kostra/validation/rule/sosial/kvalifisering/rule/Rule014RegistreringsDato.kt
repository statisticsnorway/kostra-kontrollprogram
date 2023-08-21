package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.REG_DATO_COL_NAME
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

class Rule014RegistreringsDato : AbstractRule<List<KostraRecord>>(
    KvalifiseringRuleId.REG_DATO_14.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filter {
            it.fieldAs<LocalDate?>(REG_DATO_COL_NAME) == null
                    ||
                    it.fieldAs<Int>(VERSION_COL_NAME).yearWithCentury()
                        .minus(it.fieldAs<LocalDate>(REG_DATO_COL_NAME).year) > 4

        }.map {
            createValidationReportEntry(
                "Feltet for 'Hvilken dato ble søknaden registrert ved NAV-kontoret?' med verdien " +
                        "(${it[REG_DATO_COL_NAME]}) enten mangler utfylling, har ugyldig " +
                        "dato eller dato som er eldre enn 4 år fra rapporteringsåret (${arguments.aargang}). " +
                        "Feltet er obligatorisk å fylle ut.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[SAKSBEHANDLER_COL_NAME],
                journalId = it[PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }
}