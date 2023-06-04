package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Saksinnhold02 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.SAKSINNHOLD_02.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.melding
        .filter { it.saksinnhold.any() }
        .flatMap { melding ->
            melding.saksinnhold.filter {
                it.kode in setOf("18", "19") && it.presisering == null
            }.map { saksinnhold ->
                createValidationReportEntry(
                    journalId = context.journalnummer,
                    contextId = melding.id,
                    messageText = "Saksinnhold med kode (${saksinnhold.kode}) mangler presisering"
                )
            }
        }.ifEmpty { null }
}