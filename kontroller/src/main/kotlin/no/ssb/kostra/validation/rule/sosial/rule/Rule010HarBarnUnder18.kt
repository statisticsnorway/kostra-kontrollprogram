package no.ssb.kostra.validation.rule.sosial.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.HAR_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.program.extension.codeListToString
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.SosialRuleId

class Rule010HarBarnUnder18 : AbstractRule<List<KostraRecord>>(
    SosialRuleId.BU_18_10.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filterNot {
            fieldDefinitions.byColumnName(HAR_BARN_UNDER_18_COL_NAME).codeExists(it[HAR_BARN_UNDER_18_COL_NAME])
        }.map {
            createValidationReportEntry(
                "Korrigér forsørgerplikt. Fant '${it[HAR_BARN_UNDER_18_COL_NAME]}', forventet én av " +
                        "${
                            fieldDefinitions.byColumnName(HAR_BARN_UNDER_18_COL_NAME).codeListToString()
                        }'. Det er ikke krysset av for om deltakeren " +
                        "har barn under 18 år, som deltakeren (eventuelt ektefelle/samboer) har forsørgerplikt " +
                        "for, og som bor i husholdningen ved siste kontakt. Feltet er obligatorisk å fylle ut.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[SAKSBEHANDLER_COL_NAME],
                journalId = it[PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }
}