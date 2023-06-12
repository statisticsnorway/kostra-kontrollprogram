package no.ssb.kostra.validation.rule

import no.ssb.kostra.program.KotlinArguments
import java.time.Year

object RuleTestData {

    val argumentsInTest = KotlinArguments(
        skjema = "15F",
        aargang = (Year.now().value - 1).toString(),
        region = "123414",
        orgnr = "123456789"
    )
}