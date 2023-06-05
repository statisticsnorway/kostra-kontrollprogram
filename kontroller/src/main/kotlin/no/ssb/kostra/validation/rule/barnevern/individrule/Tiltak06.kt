package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.AGE_TEN
import no.ssb.kostra.validation.rule.barnevern.extension.ageInYears

class Tiltak06 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.TILTAK_06.title,
    severity = Severity.WARNING
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.fodselsnummer
        ?.ageInYears(arguments.aargang.toInt())
        ?.let { ageInYears ->
            if (ageInYears > AGE_TEN) {
                context.tiltak
                    .filter { it.kategori.kode == "4.2" }
                    .map { tiltak ->
                        createValidationReportEntry(
                            contextId = tiltak.id,
                            messageText = "Tiltak (${tiltak.id}). Barnet er over 11 år og i SFO. " +
                                    "Barnets alder er $ageInYears år"
                        )
                    }.ifEmpty { null }
            } else null
        }
}