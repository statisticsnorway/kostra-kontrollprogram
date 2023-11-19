package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barnevern.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.BVL1992
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.BVL2021
import java.time.LocalDate

class Tiltak24 : AbstractRule<KostraIndividType>(
    ruleName = IndividRuleId.TILTAK_24.title,
    severity = Severity.ERROR
) {
    override fun validate(context: KostraIndividType, arguments: KotlinArguments) = context.tiltak
        .mapNotNull { tiltak ->
            val bvlThresholdDate: LocalDate = LocalDate.of(2022, 12, 31)

            when {
                tiltak.startDato.isBefore(bvlThresholdDate.plusDays(1))
                        && (tiltak.lovhjemmel.lov != BVL1992 || tiltak.jmfrLovhjemmel.any { it.lov != BVL1992 }) ->
                    createValidationReportEntry(
                        contextId = tiltak.id,
                        messageText = "Tiltak (${tiltak.id}). " +
                                "Tiltak opprettet før 01.01.2023 krever lov = '$BVL1992'"
                    )

                tiltak.startDato.isAfter(bvlThresholdDate)
                        && (
                        tiltak.lovhjemmel.lov !in listOf(BVL1992, BVL2021)
                                || tiltak.jmfrLovhjemmel.all { it.lov !in listOf(BVL1992, BVL2021) }
                        ) ->
                    createValidationReportEntry(
                        contextId = tiltak.id,
                        messageText = "Tiltak (${tiltak.id}). " +
                                "Tiltak opprettet 01.01.2023 eller etter, krever lov = '$BVL1992' eller '$BVL2021'"

                    )

                else -> null
            }
        }
        .ifEmpty { null }
}
