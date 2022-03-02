package no.ssb.kostra.control.sosial.s11c_kvalifiseringsstonad

import no.ssb.kostra.controlprogram.Arguments
import no.ssb.kostra.felles.ErrorReport
import no.ssb.kostra.felles.FieldDefinition
import no.ssb.kostra.felles.Record
import spock.lang.Specification

import static no.ssb.kostra.felles.Constants.CRITICAL_ERROR
import static no.ssb.kostra.felles.Constants.NO_ERROR

class KvalifiseringstonadSpec extends Specification {
    private static final String skjema = "11CF"
    private static final String aargang = "2021"

    private static final List<FieldDefinition> definitions = FieldDefinitions.getFieldDefinitions()


    def "Kontroll 37 Dato for avsluttet program"() {
        given:
        Arguments arguments = new Arguments(
                skjema,
                aargang,
                " ",
                "000000",
                "UOPPGITT",
                "         ",
                "         ",
                true,
                false,
                List.of("000021")
        )
        ErrorReport errorReport = new ErrorReport(arguments)
        Record rec = new Record(Map.of(
                "SAKSBEHANDLER", "Sara Sak",
                "PERSON_JOURNALNR", "123",
                "PERSON_FODSELSNR", "19096632188",
                "STATUS", status,
                "AVSL_DATO", endDate
        ), definitions)

        when:
        def result = Main.control37DatoForAvsluttetProgram(errorReport, rec)

        then:
        result == expectedResult
        errorReport.getErrorType() == errorLevel

        where:
        status | endDate  || expectedResult | errorLevel
        "1"    | "      " || false          | NO_ERROR
        "2"    | "      " || false          | NO_ERROR
        "3"    | "010120" || false          | NO_ERROR
        "4"    | "010120" || false          | NO_ERROR
        "5"    | "010120" || false          | NO_ERROR
        "6"    | "      " || false          | NO_ERROR
        "3"    | "      " || true           | CRITICAL_ERROR
        "4"    | "      " || true           | CRITICAL_ERROR
        "5"    | "      " || true           | CRITICAL_ERROR
        "3"    | "321320" || true           | CRITICAL_ERROR
        "4"    | "321320" || true           | CRITICAL_ERROR
        "5"    | "321320" || true           | CRITICAL_ERROR
        "5"    | "000020" || true           | CRITICAL_ERROR
        " "    | "000020" || true           | CRITICAL_ERROR
        " "    | "010120" || true           | CRITICAL_ERROR
    }
}
