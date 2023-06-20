package no.ssb.kostra.validation.rule.sosial.rule

import no.ssb.kostra.area.sosial.extension.municipalityIdFromRegion
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.SosialRuleId

class Rule03Kommunenummer : AbstractRule<KostraRecord>(
    SosialRuleId.KOMMUNENUMMER_03.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments) =
        (arguments.region.municipalityIdFromRegion() to
                context.getFieldAsTrimmedString(KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME))
            .takeIf { (municipalityIdFromRegion, municipalityId) -> municipalityId != municipalityIdFromRegion }
            ?.let { (municipalityIdFromRegion, municipalityId) ->
                createSingleReportEntryList(
                    "Korrigér kommunenummeret. Fant $municipalityId, forventet $municipalityIdFromRegion"
                )
            }
}