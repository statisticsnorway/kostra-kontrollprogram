package no.ssb.kostra.validation.rule.sosial.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.VERSION_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class OppgaveAar04 : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.OPPGAVE_AAR_04.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments): List<ValidationReportEntry>? {
        val version = context.getFieldAsTrimmedString(VERSION_COL_NAME)

        return if (version != arguments.aargang) {
            createSingleReportEntryList(
                "Korrigér årgang. Fant $version, forventet ${arguments.aargang}"
            )
        } else null
    }
}