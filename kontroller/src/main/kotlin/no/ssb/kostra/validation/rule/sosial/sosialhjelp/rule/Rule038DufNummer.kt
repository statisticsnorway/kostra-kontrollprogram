package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.PERSON_DUF_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.util.SsnValidationUtils.validateDUF
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule038DufNummer : AbstractRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K038_DUFNUMMER.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filter {
            it.fieldAsTrimmedString(PERSON_FODSELSNR_COL_NAME).isBlank()
        }.filterNot {
            validateDUF(it[PERSON_DUF_COL_NAME])
        }.map {
            createValidationReportEntry(
                "Det er ikke oppgitt fødselsnummer/d-nummer på sosialhjelpsmottakeren eller " +
                        "fødselsnummeret/d-nummeret inneholder feil. Oppgi ett 12-sifret DUF-nummer.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[SAKSBEHANDLER_COL_NAME],
                journalId = it[PERSON_JOURNALNR_COL_NAME],
                individId = it[PERSON_FODSELSNR_COL_NAME],
            )
        }.ifEmpty { null }
}