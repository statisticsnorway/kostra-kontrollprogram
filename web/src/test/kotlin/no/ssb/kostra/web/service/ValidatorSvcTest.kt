package no.ssb.kostra.web.service

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import no.ssb.kostra.web.viewmodel.KostraErrorCode
import no.ssb.kostra.web.viewmodel.ReportRequestVm
import java.util.*

class ValidatorSvcTest : BehaviorSpec({

    val sut = ValidatorSvc()

    given("request with non-fatal error") {

        val request = ReportRequestVm(
            aar = 2020,
            skjema = "52AF",
            region = "667600",
            organisasjon = "UOPPGITT",
            base64EncodedContent = Base64.getEncoder().encodeToString(PLAIN_TEXT.toByteArray())
        )

        `when`("validate with content") {

            val errorReport = sut.validateInput(request)

            then("errorReportVm should be as expected") {
                errorReport.feilkode shouldBe KostraErrorCode.NORMAL_ERROR
                errorReport.antallKontroller shouldBe 37
                errorReport.feil.size shouldBe 1

                assertSoftly(errorReport.feil.first()){
                    journalnummer shouldBe "Journalnummer 020102400 / Linjenummer 2"
                    saksbehandler shouldBe "Kontornummer 017"
                    kontrolltekst shouldStartWith  "Det er ikke krysset av for om andre deltakere (Partner)"
                    feilkode shouldBe KostraErrorCode.NORMAL_ERROR
                    kontrollnummer shouldBe
                            "Kontroll 24 Deltagelse i behandlingssamtaler med primærklienten i løpet av året, Partner"
                }
            }
        }

        `when`("validate without content") {

            shouldThrow<NullPointerException> {
                sut.validateInput(
                    request.copy(base64EncodedContent = null)
                )
            }
        }
    }
}) {
    companion object {

        val PLAIN_TEXT = """
            6676000170201001000101202011119761 1113  1       0302202021                 12122222220302              003000003005      00300521          10120052020
            6676000170201024000101202011119761 1113  1       0302202021                 12 22222220302              003000003005      00300521          10120052020
            """.trimIndent()
    }
}
