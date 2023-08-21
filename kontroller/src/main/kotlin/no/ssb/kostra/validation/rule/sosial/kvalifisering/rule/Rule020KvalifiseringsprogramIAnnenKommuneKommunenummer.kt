package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMNR_KVP_KOMM_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_KOMM_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringConstants.JA
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeIsMissing
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Rule020KvalifiseringsprogramIAnnenKommuneKommunenummer : AbstractRule<List<KostraRecord>>(
    KvalifiseringRuleId.KVALIFISERINGSPROGRAM_I_ANNEN_KOMMUNE_KOMMUNENUMMER_20.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filter {
            it[KVP_KOMM_COL_NAME] == JA
                    && fieldDefinitions.byColumnName(KOMMNR_KVP_KOMM_COL_NAME)
                .codeIsMissing(it[KOMMNR_KVP_KOMM_COL_NAME])
        }.map {
            val kvpKommCode =
                fieldDefinitions.byColumnName(KVP_KOMM_COL_NAME).codeList.first { item -> item.code == JA }

            createValidationReportEntry(
                "Det er svart '$kvpKommCode' på om deltakeren kommer fra kvalifiseringsprogram i annen " +
                        "kommune, men kommunenummer ('${it[KOMMNR_KVP_KOMM_COL_NAME]}') mangler eller er " +
                        "ugyldig. Feltet er obligatorisk å fylle ut.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[SAKSBEHANDLER_COL_NAME],
                journalId = it[PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }
}