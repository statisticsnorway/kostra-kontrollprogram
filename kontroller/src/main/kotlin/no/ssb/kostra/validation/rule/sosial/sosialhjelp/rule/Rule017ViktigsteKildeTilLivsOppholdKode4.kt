package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule017ViktigsteKildeTilLivsOppholdKode4 : ViktigsteKildeTilLivsOppholdRule(
    SosialhjelpRuleId.SOSIALHJELP_K017_VKLO_STIPEND.title,
    Severity.ERROR
) {
    override val vkldColumnFilter = "4"
    override val validCodes = setOf("03")
}