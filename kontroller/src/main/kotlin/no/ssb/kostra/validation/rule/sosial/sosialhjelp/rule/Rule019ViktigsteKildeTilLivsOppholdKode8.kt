package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule019ViktigsteKildeTilLivsOppholdKode8 : ViktigsteKildeTilLivsOppholdRule(
    SosialhjelpRuleId.SOSIALHJELP_K019_KVALIFISERINGSTONAD.title,
    Severity.ERROR
) {
    override val vkldColumnFilter = "8"
    override val validCodes = setOf("10")
}