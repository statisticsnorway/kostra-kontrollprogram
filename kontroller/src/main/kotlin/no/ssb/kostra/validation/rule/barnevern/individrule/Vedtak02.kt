package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId

class Vedtak02 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.VEDTAK_02.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments): List<ValidationReportEntry>? = context.melding
        .mapNotNull { it.undersokelse }
        .flatMap { undersokelse ->
            undersokelse.vedtaksgrunnlag.filter {
                it.kode in codesThatRequiresClarification
                        && it.presisering == null
            }.map { vedtaksgrunnlag ->
                createValidationReportEntry(
                    contextId = undersokelse.id,
                    messageText = "Vedtaksgrunnlag med kode ${vedtaksgrunnlag.kode} mangler presisering"
                )
            }
        }.ifEmpty { null }

    companion object {
        val codesThatRequiresClarification = setOf("18", "19")
    }
}