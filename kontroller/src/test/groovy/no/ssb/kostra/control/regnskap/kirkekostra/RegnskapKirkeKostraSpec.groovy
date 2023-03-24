package no.ssb.kostra.control.regnskap.kirkekostra

import no.ssb.kostra.control.felles.Utils
import no.ssb.kostra.control.regnskap.FieldDefinitions
import no.ssb.kostra.controlprogram.Arguments
import no.ssb.kostra.felles.Constants
import no.ssb.kostra.felles.ErrorReport
import no.ssb.kostra.felles.FieldDefinition
import no.ssb.kostra.felles.KostraRecord
import spock.lang.Specification

class RegnskapKirkeKostraSpec extends Specification {
    private static final String yyyy = "2022"
    private static final List<FieldDefinition> definitions = FieldDefinitions.getFieldDefinitions()

    def "Skal validere Kontroll 200, #skjema / #funksjonKapittel / #artSektor -> #expectedResult / #errorlevel"() {
        given:
        Arguments args = new Arguments(new String[]{"-s", skjema, "-y", yyyy, "-r", "123456", "-u", "987654321"})
        ErrorReport errorReport = new ErrorReport(args)
        List<KostraRecord> records = Utils.addLineNumbering(List.of(new KostraRecord(
                Map.of("skjema", skjema
                        , "aargang", yyyy
                        , "kvartal", " "
                        , "region", "    "
                        , "orgnr", "         "
                        , "foretaksnr", "         "
                        , "kontoklasse", " "
                        , "funksjon_kapittel", funksjonKapittel
                        , "art_sektor", artSektor
                        , "belop", "1"
                )
                , definitions))
        )

        when:
        boolean testResult = Main.control200Funksjon089(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == expectedResult
            errorReport.getErrorType() == errorlevel
        }

        where:
        skjema | funksjonKapittel | artSektor || expectedResult | errorlevel
        "0F"   | "041 "           | "010"     || false          | Constants.NO_ERROR
        "0F"   | "089 "           | "500"     || false          | Constants.NO_ERROR
        "0F"   | "089 "           | "010"     || true           | Constants.CRITICAL_ERROR
        "0G"   | "089 "           | "010"     || false          | Constants.NO_ERROR
    }

}
