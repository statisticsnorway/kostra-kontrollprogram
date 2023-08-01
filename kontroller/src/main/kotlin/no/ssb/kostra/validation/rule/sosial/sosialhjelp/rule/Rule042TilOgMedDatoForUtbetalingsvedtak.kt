package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.UTBETTOMDATO_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARSOSLOV_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.fieldAs
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId
import java.time.LocalDate

class Rule042TilOgMedDatoForUtbetalingsvedtak : AbstractRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K042_UTBETALINGSVEDTAK.title,
    Severity.ERROR
) {
    override fun validate(
        context: List<KostraRecord>,
        arguments: KotlinArguments
    ): List<ValidationReportEntry>? = context
        .filter {
            it[VILKARSOSLOV_COL_NAME] == "1"
        }.filterNot {
            it.fieldAs<LocalDate?>(UTBETTOMDATO_COL_NAME) != null
        }.takeIf {
            it.any()
        }?.map {
            createValidationReportEntry(
                "Feltet for 'Hvis ja på spørsmålet Stilles det vilkår til mottakeren etter " +
                        "sosialtjenesteloven', så skal utbetalingsvedtakets til og med dato " +
                        "(${it[UTBETTOMDATO_COL_NAME]}) oppgis. " +
                        "Feltet er obligatorisk å fylle ut."
            )
        }
}