package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.BEGYNT_DATO_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.VERSION_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.yearWithCentury
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId
import java.time.LocalDate

class Control16BegyntDato : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.BEGYNT_DATO_16.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments) =
        (context.fieldAs<Int>(VERSION_COL_NAME).yearWithCentury() to context.fieldAs<LocalDate?>(BEGYNT_DATO_COL_NAME))
            .takeIf { (reportingYear, begyntDate) -> begyntDate == null || reportingYear.minus(begyntDate.year) > 4 }
            ?.let { (reportingYear, _) ->
                createSingleReportEntryList(
                    "Feltet for 'Hvilken dato begynte deltakeren i program? (iverksettelse)' med " +
                            "verdien (${context[BEGYNT_DATO_COL_NAME]}) enten mangler utfylling, har " +
                            "ugyldig dato eller dato som er eldre enn 4 år fra rapporteringsåret ($reportingYear). " +
                            "Feltet er obligatorisk å fylle ut."
                )
            }
}