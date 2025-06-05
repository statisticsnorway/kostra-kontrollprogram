package no.ssb.kostra.validation.rule.sosial.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.fieldAs
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.sosial.SosialRuleId
import java.time.LocalDate

class Rule004AFodselsDato :
    AbstractNoArgsRule<List<KostraRecord>>(
        SosialRuleId.FODSELSDATO_04A.title,
        Severity.ERROR,
    ) {
    override fun validate(context: List<KostraRecord>) =
        context
            .filter { it.fieldAs<LocalDate?>(KvalifiseringColumnNames.FODSELSDATO_COL_NAME) == null }
            .map {
                createValidationReportEntry(
                    messageText = "Det er ikke oppgitt fødselsdato eller fødselsdato er ugyldig. " +
                        "Korriger fødselsdato til en gyldig dato. " +
                        "Fant (${it[KvalifiseringColumnNames.FODSELSDATO_COL_NAME]}), forventet (DDMMÅÅÅÅ)",
                    lineNumbers = listOf(it.lineNumber),
                ).copy(
                    caseworker = it[KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME],
                    journalId = it[KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME],
                )
            }.ifEmpty { null }
}
