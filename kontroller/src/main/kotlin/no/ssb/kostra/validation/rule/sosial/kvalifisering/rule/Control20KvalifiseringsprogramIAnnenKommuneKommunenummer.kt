package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMNR_KVP_KOMM_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_KOMM_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.area.sosial.kvalifisering.codeIsMissing
import no.ssb.kostra.area.sosial.kvalifisering.findByColumnName
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Control20KvalifiseringsprogramIAnnenKommuneKommunenummer : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.KVALIFISERINGSPROGRAM_I_ANNEN_KOMMUNE_KOMMUNENUMMER_20.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments) =
        context.getFieldAsString(KOMMNR_KVP_KOMM_COL_NAME)
            .takeIf {
                context.getFieldAsString(KVP_KOMM_COL_NAME) == "1"
                        && fieldDefinitions.findByColumnName(KOMMNR_KVP_KOMM_COL_NAME).codeIsMissing(it)
            }?.let { kommnrKvpKomm ->
                val kvpKommCode = fieldDefinitions.findByColumnName(KVP_KOMM_COL_NAME).codeList
                    .first { it.code == "1" }

                createSingleReportEntryList(
                    "Det er svart '$kvpKommCode' på om deltakeren kommer fra kvalifiseringsprogram i annen " +
                            "kommune, men kommunenummer ('$kommnrKvpKomm') mangler eller er " +
                            "ugyldig. Feltet er obligatorisk å fylle ut."
                )
            }
}