package no.ssb.kostra.web.extension

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.felles.Constants.*
import no.ssb.kostra.web.extensions.toKostraErrorCode
import no.ssb.kostra.web.viewmodel.KostraErrorCode

class MappingFromConsoleAppExtensionsKtTest : BehaviorSpec({

    Given("all error codes as int") {
        forAll(
            row(NORMAL_ERROR, KostraErrorCode.NORMAL_ERROR),
            row(CRITICAL_ERROR, KostraErrorCode.CRITICAL_ERROR),
            row(SYSTEM_ERROR, KostraErrorCode.SYSTEM_ERROR),
            row(PARAMETER_ERROR, KostraErrorCode.PARAMETER_ERROR),
            row(NO_ERROR, KostraErrorCode.NO_ERROR)
        ) { errorCodeAsInt, expectedEnumErrorCode ->
            When("toKostraErrorCode $errorCodeAsInt") {
                val mappedErrorCode = errorCodeAsInt.toKostraErrorCode()

                Then("mappedErrorCode should be as expected") {
                    mappedErrorCode shouldBe expectedEnumErrorCode
                }
            }
        }
    }
})
