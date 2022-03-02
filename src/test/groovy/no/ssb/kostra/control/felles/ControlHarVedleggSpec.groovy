package no.ssb.kostra.control.felles


import no.ssb.kostra.controlprogram.Arguments
import no.ssb.kostra.felles.ErrorReport
import spock.lang.Specification

import static no.ssb.kostra.felles.Constants.CRITICAL_ERROR
import static no.ssb.kostra.felles.Constants.NO_ERROR

class ControlHarVedleggSpec extends Specification {
    def "should validate existence of attachment"() {
        given:
        Arguments arguments = new Arguments(
                "*",
                "2022",
                " ",
                "123400",
                "UOPPGITT",
                "         ",
                "         ",
                hasAttachment,
                false,
                content
        )
        ErrorReport errorReport = new ErrorReport(arguments)

        when:
        boolean result = ControlHarVedlegg.doControl(errorReport)

        then:
        noExceptionThrown()
        result == expectedResult
        errorReport.getErrorType() == errorLevel

        where:
        hasAttachment | content              || expectedResult | errorLevel
        true          | List.of("123456789") || false          | NO_ERROR
        false         | List.of(" ", " ")    || false          | NO_ERROR
        false         | List.of("  ", "  ")  || false          | NO_ERROR
        false         | List.of(" ")         || false          | NO_ERROR
        false         | List.of("12", "23")  || true           | CRITICAL_ERROR
    }
}
