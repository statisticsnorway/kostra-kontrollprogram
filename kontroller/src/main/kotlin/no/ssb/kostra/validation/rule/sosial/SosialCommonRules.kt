package no.ssb.kostra.validation.rule.sosial

import no.ssb.kostra.validation.rule.sosial.rule.*

object SosialCommonRules {

    val sosialRules = listOf(
        Rule03Kommunenummer(),
        Rule03Bydelsnummer(),
        Rule04OppgaveAar(),
        Rule05Fodselsnummer(),
        Rule06AlderUnder18Aar(),
        Rule07AlderEr68AarEllerOver(),
        Rule08Kjonn(),
        Rule09Sivilstand()
    )
}