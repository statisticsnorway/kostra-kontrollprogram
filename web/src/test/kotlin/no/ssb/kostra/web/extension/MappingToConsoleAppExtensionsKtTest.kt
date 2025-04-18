package no.ssb.kostra.web.extension

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.web.extensions.NAME_FALLBACK_VALUE
import no.ssb.kostra.web.extensions.QUARTER_FALLBACK_VALUE
import no.ssb.kostra.web.extensions.toKostraArguments
import no.ssb.kostra.web.viewmodel.KostraFormVm
import java.time.Year

class MappingToConsoleAppExtensionsKtTest :
    BehaviorSpec({

        Given("minimal KostraFormVm instance") {
            forAll(
                row(
                    KostraFormVm(
                        skjema = "0A",
                        aar = Year.now().value,
                        region = "123456",
                    ),
                ),
                row(
                    KostraFormVm(
                        skjema = "0A",
                        aar = Year.now().value,
                        region = "123456",
                        orgnrForetak = "",
                    ),
                ),
                row(
                    KostraFormVm(
                        skjema = "0A",
                        aar = Year.now().value,
                        region = "123456",
                        navn = "~navn~",
                    ),
                ),
                row(
                    KostraFormVm(
                        skjema = "0AK1",
                        aar = Year.now().value,
                        region = "123456",
                    ),
                ),
            ) { sut ->
                When("toKostraArguments $sut") {
                    val arguments = sut.toKostraArguments("".byteInputStream(), null)

                    Then("arguments should be as expected") {
                        assertSoftly(arguments) {
                            aargang shouldBe Year.now().value.toString()
                            region shouldBe sut.region
                            skjema shouldBe sut.skjema
                            kvartal shouldBe QUARTER_FALLBACK_VALUE

                            orgnr shouldBe generateCompanyIdInTest(' ')

                            if (sut.navn == null) {
                                navn shouldBe NAME_FALLBACK_VALUE
                            } else {
                                navn shouldBe sut.navn
                            }
                        }
                    }
                }
            }
        }

        Given("KostraFormVm instance with orgnrForetak") {
            val sut =
                KostraFormVm(
                    skjema = "0A",
                    aar = Year.now().value,
                    region = "123456",
                    orgnrForetak = generateCompanyIdInTest('9'),
                )

            When("toKostraArguments") {
                val arguments = sut.toKostraArguments(inputStream = "".byteInputStream(), kvartal = "1")

                Then("arguments should be as expected") {
                    assertSoftly(arguments) {
                        orgnr shouldBe generateCompanyIdInTest('9')
                        kvartal shouldBe "1"
                    }
                }
            }
        }
    }) {
    companion object {
        internal fun generateCompanyIdInTest(charToRepeat: Char): String = "$charToRepeat".repeat(9)
    }
}
