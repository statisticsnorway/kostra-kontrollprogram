package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMNR_KVP_KOMM_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.area.sosial.kvalifisering.codeIsMissing
import no.ssb.kostra.area.sosial.kvalifisering.findByColumnName
import no.ssb.kostra.felles.Code
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Control20KvalifiseringsprogramIAnnenKommuneKommunenummer : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.KVALIFISERINGSPROGRAM_I_ANNEN_KOMMUNE_KOMMUNENUMMER_20.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments): List<ValidationReportEntry>? {
        val fieldDefinition = fieldDefinitions.findByColumnName(KOMMNR_KVP_KOMM_COL_NAME)
        val kvpKomm = context.getFieldAsString(KvalifiseringColumnNames.KVP_KOMM_COL_NAME)
        val kommnrKvpKomm = context.getFieldAsString(KOMMNR_KVP_KOMM_COL_NAME)

        val kvpKommCode = fieldDefinition.codeList.find { it.code == kvpKomm } ?: Code(" ", "Uoppgitt")

        return if (kvpKomm == "1" && fieldDefinition.codeIsMissing(kommnrKvpKomm))  {
            createSingleReportEntryList(
                "Det er svart '$kvpKommCode' på om deltakeren kommer fra kvalifiseringsprogram i annen " +
                        "kommune, men kommunenummer ('$kommnrKvpKomm') mangler eller er " +
                        "ugyldig. Feltet er obligatorisk å fylle ut."
            )
        } else null
    }
}