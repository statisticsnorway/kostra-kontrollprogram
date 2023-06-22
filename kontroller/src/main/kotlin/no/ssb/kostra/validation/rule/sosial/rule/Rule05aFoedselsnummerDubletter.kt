package no.ssb.kostra.validation.rule.sosial.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.SosialRuleId
import no.ssb.kostra.validation.util.SsnValidationUtils.isValidSocialSecurityIdOrDnr

class Rule05aFoedselsnummerDubletter : AbstractRule<List<KostraRecord>>(
    SosialRuleId.FODSELSNUMMER_DUBLETTER_05A.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) =
        context.takeIf { it.size > 1 }?.let {
            it.mapIndexed { index, kostraRecord -> kostraRecord to index + 1 }
                .filter { (kostraRecord, _) ->
                    isValidSocialSecurityIdOrDnr(kostraRecord.getFieldAsTrimmedString(PERSON_FODSELSNR_COL_NAME))
                }
                .groupBy { (kostraRecord, _) -> kostraRecord.getFieldAsString(PERSON_FODSELSNR_COL_NAME) }
                .filter { (_, group) -> group.size > 1 }
                .flatMap { (foedselsnummer, group) ->
                    group.map { (kostraRecord, lineNumber) ->
                        val journalId = kostraRecord.getFieldAsString(PERSON_JOURNALNR_COL_NAME)
                        val otherJournalIds = group
                            .filter { (kostraRecord, _) -> kostraRecord.getFieldAsString(PERSON_FODSELSNR_COL_NAME) != foedselsnummer }
                            .joinToString(", ") { (kostraRecord, _) ->
                                kostraRecord.getFieldAsString(PERSON_JOURNALNR_COL_NAME)
                            }

                        createValidationReportEntry(
                            "Fødselsnummeret i journalnummer $journalId fins også i journalene $otherJournalIds",
                            lineNumbers = listOf(lineNumber)
                        ).copy(
                            caseworker = kostraRecord.getFieldAsString(SAKSBEHANDLER_COL_NAME),
                            journalId = journalId,
                            individId = foedselsnummer
                        )
                    }
                }.ifEmpty { null }
        }
}