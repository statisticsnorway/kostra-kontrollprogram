package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.VEDTAK_DATO_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.VERSION_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Control15VedtakDato : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.VEDTAK_DATO_15.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments): List<ValidationReportEntry>? {
        val reportingYear = context.getFieldAsIntegerDefaultEquals0(VERSION_COL_NAME)
        val vedtakDato = context.getFieldAsLocalDate(VEDTAK_DATO_COL_NAME)

        return if (vedtakDato == null || (2000 + reportingYear - vedtakDato.year) > 4) {
            createSingleReportEntryList(
                "Feltet for 'Hvilken dato det ble fattet vedtak om program? (søknad innvilget)' med " +
                        "verdien (${context.getFieldAsString(VEDTAK_DATO_COL_NAME)}) enten mangler utfylling, " +
                        "har ugyldig dato eller dato som er eldre enn 4 år fra rapporteringsåret ($reportingYear). " +
                        "Feltet er obligatorisk å fylle ut."
            )
        } else null
    }
}