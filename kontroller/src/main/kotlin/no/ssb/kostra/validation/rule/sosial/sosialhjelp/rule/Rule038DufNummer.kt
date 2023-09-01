package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.PERSON_DUF_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.util.SsnValidationUtils.validateDUF
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule038DufNummer : AbstractNoArgsRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K038_DUFNUMMER.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { it.fieldAsTrimmedString(PERSON_FODSELSNR_COL_NAME).isBlank() }
        .filterNot { validateDUF(it[PERSON_DUF_COL_NAME]) }
        .map {
            createValidationReportEntry(
                "Det er ikke oppgitt fødselsnummer/d-nummer på sosialhjelpsmottakeren eller " +
                        "fødselsnummeret/d-nummeret inneholder feil. Oppgi ett 12-sifret DUF-nummer.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[SAKSBEHANDLER_COL_NAME],
                journalId = it[PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }
}