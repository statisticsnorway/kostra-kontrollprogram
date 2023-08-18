package no.ssb.kostra.extension

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.extensions.NAME_FALLBACK_VALUE
import no.ssb.kostra.extensions.toKostraArguments
import no.ssb.kostra.viewmodel.CompanyIdVm
import no.ssb.kostra.viewmodel.KostraFormVm
import java.time.Year

class MappingToConsoleAppExtensionsKtTest : BehaviorSpec({

    Given("minimal KostraFormVm instance") {
        forAll(
            row(
                KostraFormVm(
                    skjema = "0A",
                    aar = Year.now().value,
                    region = "123456"
                )
            ),
            row(
                KostraFormVm(
                    skjema = "0A",
                    aar = Year.now().value,
                    region = "123456",
                    orgnrForetak = ""
                )
            ),
            row(
                KostraFormVm(
                    skjema = "0A",
                    aar = Year.now().value,
                    region = "123456",
                    orgnrVirksomhet = emptyList()
                )
            ),
            row(
                KostraFormVm(
                    skjema = "0A",
                    aar = Year.now().value,
                    region = "123456",
                    navn = "~navn~"
                )
            )
        ) { sut ->
            When("toKostraArguments ${sut.orgnrForetak} ${sut.orgnrVirksomhet} ${sut.navn}") {
                val arguments = sut.toKostraArguments("".byteInputStream())

                Then("arguments should be as expected") {
                    arguments.aargang shouldBe Year.now().value.toString()
                    arguments.region shouldBe "123456"

                    arguments.foretaknr shouldBe generateCompanyIdInTest(' ')
                    arguments.orgnr shouldBe generateCompanyIdInTest(' ')

                    if (sut.navn == null) arguments.navn shouldBe NAME_FALLBACK_VALUE
                    else arguments.navn shouldBe sut.navn
                }
            }
        }
    }

    Given("KostraFormVm instance with orgnrForetak") {
        val sut = KostraFormVm(
            skjema = "0A",
            aar = Year.now().value,
            region = "123456",
            orgnrForetak = generateCompanyIdInTest('9')
        )

        When("toKostraArguments") {
            val arguments = sut.toKostraArguments("".byteInputStream())

            Then("arguments should be as expected") {
                arguments.foretaknr shouldBe generateCompanyIdInTest(' ')
                arguments.orgnr shouldBe generateCompanyIdInTest('9')
            }
        }
    }

    Given("KostraFormVm instance with both orgnrForetak and orgnrVirksomhet") {
        val sut = KostraFormVm(
            skjema = "0A",
            aar = Year.now().value,
            region = "123456",
            orgnrForetak = generateCompanyIdInTest('9'),
            orgnrVirksomhet = listOf(
                CompanyIdVm(generateCompanyIdInTest('8')),
                CompanyIdVm(generateCompanyIdInTest('9'))
            )
        )

        When("toKostraArguments") {
            val arguments = sut.toKostraArguments("".byteInputStream())

            Then("arguments should be as expected") {
                arguments.foretaknr shouldBe generateCompanyIdInTest('9')
                arguments.orgnr shouldBe
                        "${generateCompanyIdInTest('8')},${generateCompanyIdInTest('9')}"
            }
        }
    }
}) {
    companion object {
        fun generateCompanyIdInTest(charToRepeat: Char): String = "$charToRepeat".repeat(9)
    }
}
