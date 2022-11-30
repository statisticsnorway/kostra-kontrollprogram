package no.ssb.kostra.web.extension

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.web.viewmodel.CompanyIdVm
import no.ssb.kostra.web.viewmodel.KostraFormVm
import java.time.Year

class MappingToConsoleAppExtensionsKtTest : BehaviorSpec({

    given("minimal KostraFormVm instance") {
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
                    orgnrVirksomhet = setOf()
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
            `when`("toKostraArguments ${sut.orgnrForetak} ${sut.orgnrVirksomhet} ${sut.navn}") {
                val arguments = sut.toKostraArguments("".byteInputStream())

                then("arguments should be as expected") {
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

    given("KostraFormVm instance with orgnrForetak") {
        val sut = KostraFormVm(
            skjema = "0A",
            aar = Year.now().value,
            region = "123456",
            orgnrForetak = generateCompanyIdInTest('9')
        )

        `when`("toKostraArguments") {
            val arguments = sut.toKostraArguments("".byteInputStream())

            then("arguments should be as expected") {
                arguments.foretaknr shouldBe generateCompanyIdInTest('9')
                arguments.orgnr shouldBe generateCompanyIdInTest(' ')
            }
        }
    }

    given("KostraFormVm instance with both orgnrForetak and orgnrVirksomhet") {
        val sut = KostraFormVm(
            skjema = "0A",
            aar = Year.now().value,
            region = "123456",
            orgnrForetak = generateCompanyIdInTest('9'),
            orgnrVirksomhet = setOf(
                CompanyIdVm(generateCompanyIdInTest('8')),
                CompanyIdVm(generateCompanyIdInTest('9'))
            )
        )

        `when`("toKostraArguments") {
            val arguments = sut.toKostraArguments("".byteInputStream())

            then("arguments should be as expected") {
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
