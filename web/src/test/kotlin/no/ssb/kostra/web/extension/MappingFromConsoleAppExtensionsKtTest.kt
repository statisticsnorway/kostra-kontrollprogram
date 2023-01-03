package no.ssb.kostra.web.extension

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.controlprogram.ArgumentConstants.COMPANY_ORGNR_ABBR
import no.ssb.kostra.controlprogram.ArgumentConstants.REGION_ABBR
import no.ssb.kostra.controlprogram.ArgumentConstants.SCHEMA_ABBR
import no.ssb.kostra.controlprogram.ArgumentConstants.UNIT_ORGNR_ABBR
import no.ssb.kostra.controlprogram.ArgumentConstants.YEAR_ABBR
import no.ssb.kostra.controlprogram.Arguments
import no.ssb.kostra.felles.Constants.CRITICAL_ERROR
import no.ssb.kostra.felles.Constants.NORMAL_ERROR
import no.ssb.kostra.felles.Constants.NO_ERROR
import no.ssb.kostra.felles.Constants.PARAMETER_ERROR
import no.ssb.kostra.felles.Constants.SYSTEM_ERROR
import no.ssb.kostra.web.viewmodel.KostraErrorCode
import java.time.Year

class MappingFromConsoleAppExtensionsKtTest : BehaviorSpec({

    given("populated list of arguments") {

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

            `when`("toReportRequestVm $virksomhetsnummer") {
                val kostraFormVm = sut.toReportRequestVm()

                then("mapped view model should be as expected") {
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

    given("all error codes as int") {
        forAll(
            row(NORMAL_ERROR, KostraErrorCode.NORMAL_ERROR),
            row(CRITICAL_ERROR, KostraErrorCode.CRITICAL_ERROR),
            row(SYSTEM_ERROR, KostraErrorCode.SYSTEM_ERROR),
            row(PARAMETER_ERROR, KostraErrorCode.PARAMETER_ERROR),
            row(NO_ERROR, KostraErrorCode.NO_ERROR)
        ) { errorCodeAsInt, expectedEnumErrorCode ->
            `when`("toKostraErrorCode $errorCodeAsInt") {
                val mappedErrorCode = errorCodeAsInt.toKostraErrorCode()

                then("mappedErrorCode should be as expected") {
                    mappedErrorCode shouldBe expectedEnumErrorCode
                }
            }
        }
    }
})
