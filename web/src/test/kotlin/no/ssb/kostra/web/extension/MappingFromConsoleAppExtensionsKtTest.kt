package no.ssb.kostra.web.extension

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.controlprogram.ArgumentConstants.*
import no.ssb.kostra.controlprogram.Arguments
import no.ssb.kostra.felles.Constants.*
import no.ssb.kostra.web.extensions.toKostraErrorCode
import no.ssb.kostra.web.extensions.toReportRequestVm
import no.ssb.kostra.web.viewmodel.KostraErrorCode
import java.time.Year

class MappingFromConsoleAppExtensionsKtTest : BehaviorSpec({

    Given("populated list of arguments") {

        forAll(
            row("987654321,876543219", 2),
            row("987654321", 1),
            row(",987654321", 1),
            row(",", 0),
            row("", 0),
            row(null, 0),
        ) { virksomhetsnummer, expectedNumberOfCompanyIds ->
            val arguments = listOf(
                SCHEMA_ABBR to "~skjema~",
                YEAR_ABBR to Year.now().value.toString(),
                REGION_ABBR to "123456",
                UNIT_ORGNR_ABBR to virksomhetsnummer,
                COMPANY_ORGNR_ABBR to "888888888"
            ).flatMap { listOf("-${it.first}", it.second) }.toTypedArray()

            val sut = Arguments(arguments)

            When("toReportRequestVm $virksomhetsnummer") {
                val kostraFormVm = sut.toReportRequestVm()

                Then("mapped view model should be as expected") {
                    assertSoftly(kostraFormVm) {
                        skjema shouldBe "~skjema~"
                        aar shouldBe Year.now().value
                        region shouldBe "123456"
                        orgnrForetak shouldBe "888888888"

                        orgnrVirksomhet.size shouldBe expectedNumberOfCompanyIds
                        if (expectedNumberOfCompanyIds > 0) {
                            orgnrVirksomhet.first().orgnr shouldBe "987654321"
                        }
                    }
                }
            }
        }
    }

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
