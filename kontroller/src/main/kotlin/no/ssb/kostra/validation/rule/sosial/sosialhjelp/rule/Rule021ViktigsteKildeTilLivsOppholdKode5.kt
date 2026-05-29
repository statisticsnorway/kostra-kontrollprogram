package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule021ViktigsteKildeTilLivsOppholdKode5 : ViktigsteKildeTilLivsOppholdRule(
    SosialhjelpRuleId.SOSIALHJELP_K021_SOSIALHJELP.title,
    Severity.WARNING
) {
    override val vkloColumnFilter = "5"
    override val validArbSitCodes = setOf("02", "03", "04", "05", "06", "07", "08")
}