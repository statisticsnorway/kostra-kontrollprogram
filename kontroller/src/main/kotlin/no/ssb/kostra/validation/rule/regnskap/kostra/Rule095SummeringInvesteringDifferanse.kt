package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningInvesteringRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isOsloBydel
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isUtgift

class Rule095SummeringInvesteringDifferanse : AbstractRecordRule(
    "Kontroll 095 : Summeringskontroller investeringsregnskapet, differanse i investeringsregnskapet",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { !it.isOsloBydel() && it.isBevilgningInvesteringRegnskap() }
        .takeIf { it.any() }
        ?.partition { it.isUtgift() }
        ?.let { (investeringUtgifterPosteringer,
                    investeringInntekterPosteringer) ->
            (
                    investeringUtgifterPosteringer
                        .sumOf { it.getFieldAsIntegerDefaultEquals0(RegnskapConstants.FIELD_BELOP) }
                            to
                            investeringInntekterPosteringer
                                .sumOf { it.getFieldAsIntegerDefaultEquals0(RegnskapConstants.FIELD_BELOP) }
                    )
        }
        ?.takeUnless { (investeringUtgifter, investeringInntekter) ->
            0 < investeringUtgifter
                    && investeringInntekter < 0
                    && investeringUtgifter + investeringInntekter in -30..30
        }
        ?.let { (investeringUtgifter, investeringInntekter) ->
            val investeringDifferanse = investeringUtgifter + investeringInntekter
            createSingleReportEntryList(
                messageText = "Korrigér differansen ($investeringDifferanse) mellom inntekter " +
                        "($investeringInntekter) og utgifter ($investeringUtgifter) i investeringsregnskapet"
            )
        }
}