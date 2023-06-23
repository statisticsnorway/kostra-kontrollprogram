package no.ssb.kostra.validation.rule.barnevern

import no.ssb.kostra.validation.rule.barnevern.avgiverrule.Avgiver02
import no.ssb.kostra.validation.rule.barnevern.avgiverrule.Avgiver03
import no.ssb.kostra.validation.rule.barnevern.avgiverrule.Avgiver04
import no.ssb.kostra.validation.rule.barnevern.avgiverrule.Avgiver06

object AvgiverRules {

    val avgiverRules = setOf(
        Avgiver02(),
        Avgiver03(),
        Avgiver04(),
        Avgiver06()
    )
}