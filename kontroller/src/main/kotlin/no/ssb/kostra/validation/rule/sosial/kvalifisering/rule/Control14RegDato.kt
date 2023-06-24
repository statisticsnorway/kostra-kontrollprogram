package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.program.extension.toYearWithCentury
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.REG_DATO_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.VERSION_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Control14RegDato : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.REG_DATO_14.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments) =
        (context.getFieldAsIntegerOrDefault(VERSION_COL_NAME).toYearWithCentury() to
                context.getFieldAsLocalDate(REG_DATO_COL_NAME))
            .takeIf { (reportingYear, regDato) ->
                regDato == null || reportingYear.minus(regDato.year) > 4
            }?.let { (reportingYear, _) ->
                createSingleReportEntryList(
                    "Feltet for 'Hvilken dato ble søknaden registrert ved NAV-kontoret?' med verdien " +
                            "(${context.getFieldAsString(REG_DATO_COL_NAME)}) enten mangler utfylling, har ugyldig " +
                            "dato eller dato som er eldre enn 4 år fra rapporteringsåret ($reportingYear). " +
                            "Feltet er obligatorisk å fylle ut."
                )
            }
}