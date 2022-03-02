package no.ssb.kostra.control.controlprogram

import no.ssb.kostra.control.sosial.s11c_kvalifiseringsstonad.Main
import no.ssb.kostra.controlprogram.Arguments
import no.ssb.kostra.felles.ErrorReport
import spock.lang.Specification

import java.nio.charset.StandardCharsets

import static no.ssb.kostra.felles.Constants.DEBUG
import static no.ssb.kostra.felles.Constants.NO_ERROR

class ArgumentsSpec  extends Specification {
    def "testDoControlWithByteOrderMark"() {
        given:
        // Mocking a blank file
        String inputFileContent = " "
        ByteArrayInputStream is = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1))
        System.setIn(is)

        Arguments args = new Arguments(new String[]{"-s", "11CF", "-y", "2020", "-r", "420400", "-a", "0"})

        when:
        ErrorReport er = Main.doControls(args)

        if (DEBUG) {
            System.out.print(er.generateReport())
        }

        then:
        noExceptionThrown()
        er.generateReport().contains("ikke finnes deltakere")
        er.getErrorType() == NO_ERROR
    }
}
