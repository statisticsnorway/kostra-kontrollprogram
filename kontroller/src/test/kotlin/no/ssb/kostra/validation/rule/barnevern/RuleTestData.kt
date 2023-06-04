package no.ssb.kostra.validation.rule.barnevern

import no.ssb.kostra.program.Arguments
import java.time.Year

object RuleTestData {

    val argumentsInTest = Arguments(
        skjema = "15F",
        aargang = (Year.now().value - 1).toString(),
        region = "123414",
        orgnr = "123456789"
    )
}