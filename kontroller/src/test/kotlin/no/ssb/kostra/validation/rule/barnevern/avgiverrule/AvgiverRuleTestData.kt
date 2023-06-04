package no.ssb.kostra.validation.rule.barnevern.avgiverrule

import no.ssb.kostra.barn.xsd.KostraAvgiverType
import java.time.Year

object AvgiverRuleTestData {

    val kostraAvgiverTypeInTest = KostraAvgiverType(
        organisasjonsnummer = "123456789",
        versjon = Year.now().value - 1,
        kommunenummer = "1234",
        kommunenavn = "~kommunenavn~"
    )
}