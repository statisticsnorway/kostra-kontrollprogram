package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule016ViktigsteKildeTilLivsOppholdKode2 : ViktigsteKildeTilLivsOppholdRule(
    SosialhjelpRuleId.SOSIALHJELP_K016_VKLO_KURS.title,
    Severity.ERROR
) {
    override val vkloColumnFilter = "2"
    override val validArbSitCodes = setOf("03", "05", "06")
}