package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule015ViktigsteKildeTilLivsOppholdKode1 : ViktigsteKildeTilLivsOppholdRule(
    SosialhjelpRuleId.SOSIALHJELP_K015_VKLO_ARBEIDSINNTEKT.title,
    Severity.ERROR
) {
    override val vkldColumnFilter = "1"
    override val validCodes = setOf("01", "02")
}