package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule021ViktigsteKildeTilLivsOppholdKode5 : ViktigsteKildeTilLivsOppholdRule(
    SosialhjelpRuleId.SOSIALHJELP_K021_SOSIALHJELP.title,
    Severity.WARNING
) {
    override val vkldColumnFilter = "5"
    override val validCodes = setOf("02", "04", "05", "06", "07", "08")
}