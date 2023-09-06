package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule018ViktigsteKildeTilLivsOppholdKode6 : ViktigsteKildeTilLivsOppholdRule(
    SosialhjelpRuleId.SOSIALHJELP_K018_VKLO_INTRODUKSJONSTONAD.title,
    Severity.ERROR
) {
    override val vkldColumnFilter = "6"
    override val validCodes = setOf("09")
}