package no.ssb.kostra.control.famvern.s52a

import no.ssb.kostra.controlprogram.Arguments
import no.ssb.kostra.felles.Constants
import no.ssb.kostra.felles.ErrorReport
import no.ssb.kostra.felles.FieldDefinition
import no.ssb.kostra.felles.Record
import spock.lang.Specification
import spock.lang.Subject

class Famvern52aSpec extends Specification {
    @Subject
    private ErrorReport errorReport

    @Subject
    private List<FieldDefinition> fieldDefinitions

    @Subject
    private Integer fieldLength

    // runs before each test
    void setup() {
        Record.resetLineCount()
        Arguments arguments = new Arguments(new String[]{"-s", "52AF", "-y", "2021", "-r", "667600"})
        errorReport = new ErrorReport(arguments)
        fieldDefinitions = FieldDefinitions.getFieldDefinitions()
        fieldLength = FieldDefinitions.getFieldLength()
    }

    def "K03, kontrollere regionnummer REGION_NR_A, #region_nr_a -> #hasError -> #errorlevel"() {
        given:
        def record = new Record(Map.of("REGION_NR_A", region_nr_a, "KONTOR_NR_A", kontor_nr_a), fieldDefinitions)

        when:
        def testResult = Main.kontroll03Regionsnummer(errorReport, record)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == hasError
            errorReport.getErrorType() == errorlevel
        }

        where:
        region_nr_a | kontor_nr_a || hasError | errorlevel
        "667600"    | "017"       || false    | Constants.NO_ERROR
        "999999"    | "999"       || true     | Constants.NORMAL_ERROR
    }

    def "K04, kontrollere kontornummer, #kontor_nr_a -> #hasError -> #errorlevel"() {
        given:
        def record = new Record(Map.of("REGION_NR_A", region_nr_a, "KONTOR_NR_A", kontor_nr_a), fieldDefinitions)

        when:
        def testResult = Main.kontroll04Kontornummer(errorReport, record)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == hasError
            errorReport.getErrorType() == errorlevel
        }

        where:
        region_nr_a | kontor_nr_a || hasError | errorlevel
        "667600"    | "017"       || false    | Constants.NO_ERROR
        "999999"    | "999"       || true     | Constants.NORMAL_ERROR
    }

    def "K05, kontrollere samsvar mellom regionnummer REGION_NR_A (#region_nr_a) og kontornummer KONTOR_NR_A (#kontor_nr_a), #hasError -> #errorlevel"() {
        given:
        def record = new Record(Map.of("REGION_NR_A", region_nr_a, "KONTOR_NR_A", kontor_nr_a), fieldDefinitions)

        when:
        def testResult = Main.kontroll05RegionsnummerKontornummer(errorReport, record)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == hasError
            errorReport.getErrorType() == errorlevel
        }

        where:
        region_nr_a | kontor_nr_a || hasError | errorlevel
        "667600"    | "017"       || false    | Constants.NO_ERROR
        "667600"    | "205"       || true     | Constants.NORMAL_ERROR
        "999999"    | "205"       || true     | Constants.NORMAL_ERROR
        "999999"    | "999"       || true     | Constants.NORMAL_ERROR
    }

    def "K06, Kontrollere at journalnummeret er unikt for hver record for det enkelte kontor. #kontornummerJournalnummerList -> #hasError -> #errorlevel"() {
        given:
        List<Record> records = kontornummerJournalnummerList.stream()
                .map(m -> {
                    String kontor = m.get("kontor");
                    String journalnummer = m.get("journalnummer");
                    return new Record(Map.of("KONTOR_NR_A", kontor, "JOURNAL_NR_A", journalnummer), fieldDefinitions);
                })
                .collect()

        when:
        def testResult = Main.kontroll06Dubletter(errorReport, records)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == hasError
            errorReport.getErrorType() == errorlevel
        }

        where:
        kontornummerJournalnummerList                                                                                   || hasError | errorlevel
        List.of(Map.of("kontor", "017", "journalnummer", "123456"), Map.of("kontor", "017", "journalnummer", "123456")) || true     | Constants.NORMAL_ERROR
        List.of(Map.of("kontor", "017", "journalnummer", "123456"), Map.of("kontor", "017", "journalnummer", "654321")) || false    | Constants.NO_ERROR
        List.of(Map.of("kontor", "017", "journalnummer", "123456"), Map.of("kontor", "023", "journalnummer", "123456")) || false    | Constants.NO_ERROR
    }

    def "K07 Henvendelsesdato '#henv_dato_a' -> #hasError -> #errorlevel"() {
        given:
        def record = new Record(Map.of("REGION_NR_A", region_nr_a, "KONTOR_NR_A", kontor_nr_a, "HENV_DATO_A", henv_dato_a), fieldDefinitions)

        when:
        def testResult = Main.kontroll07Henvendelsesdato(errorReport, record)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == hasError
            errorReport.getErrorType() == errorlevel
        }

        where:
        region_nr_a | kontor_nr_a | henv_dato_a || hasError | errorlevel
        "667600"    | "017"       | "        "  || true     | Constants.NORMAL_ERROR
        "667600"    | "017"       | "01012020"  || false    | Constants.NO_ERROR
        "667600"    | "017"       | "32012020"  || true     | Constants.NORMAL_ERROR
    }

    def "K09 Kontakt tidligere '#kontakt_tidl_a' -> #hasError -> #errorlevel"() {
        given:
        def record = new Record(Map.of("REGION_NR_A", region_nr_a, "KONTOR_NR_A", kontor_nr_a, "KONTAKT_TIDL_A", kontakt_tidl_a), fieldDefinitions)

        when:
        def testResult = Main.kontroll09KontaktTidligere(errorReport, record)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == hasError
            errorReport.getErrorType() == errorlevel
        }

        where:
        region_nr_a | kontor_nr_a | kontakt_tidl_a || hasError | errorlevel
        "667600"    | "017"       | "1"            || false    | Constants.NO_ERROR
        "667600"    | "017"       | "2"            || false    | Constants.NO_ERROR
        "667600"    | "017"       | "3"            || false    | Constants.NO_ERROR
        "667600"    | "017"       | "4"            || false    | Constants.NO_ERROR
        "667600"    | "017"       | "X"            || true     | Constants.NORMAL_ERROR
    }

    def "K11 Henvendelses begrunnelse '#henv_grunn_a' -> #hasError -> #errorlevel"() {
        given:
        def record = new Record(Map.of("REGION_NR_A", region_nr_a, "KONTOR_NR_A", kontor_nr_a, "HENV_GRUNN_A", henv_grunn_a), fieldDefinitions)

        when:
        def testResult = Main.kontroll11HenvendelsesBegrunnelse(errorReport, record)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == hasError
            errorReport.getErrorType() == errorlevel
        }

        where:
        region_nr_a | kontor_nr_a | henv_grunn_a || hasError | errorlevel
        "667600"    | "017"       | "1"          || false    | Constants.NO_ERROR
        "667600"    | "017"       | "2"          || false    | Constants.NO_ERROR
        "667600"    | "017"       | "3"          || false    | Constants.NO_ERROR
        "667600"    | "017"       | "4"          || false    | Constants.NO_ERROR
        "667600"    | "017"       | "X"          || true     | Constants.NORMAL_ERROR
    }

    def "K13 Kjonn '#primk_kjonn_a' -> #hasError -> #errorlevel"() {
        given:
        def record = new Record(Map.of("REGION_NR_A", region_nr_a, "KONTOR_NR_A", kontor_nr_a, "PRIMK_KJONN_A", primk_kjonn_a), fieldDefinitions)

        when:
        def testResult = Main.kontroll13Kjonn(errorReport, record)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == hasError
            errorReport.getErrorType() == errorlevel
        }

        where:
        region_nr_a | kontor_nr_a | primk_kjonn_a || hasError | errorlevel
        "667600"    | "017"       | " "           || true     | Constants.NORMAL_ERROR
        "667600"    | "017"       | "1"           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "2"           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "X"           || true     | Constants.NORMAL_ERROR
    }

    def "K14 Fodselsaar '#primk_fodt_a' -> #hasError -> #errorlevel"() {
        given:
        def record = new Record(Map.of("REGION_NR_A", region_nr_a, "KONTOR_NR_A", kontor_nr_a, "PRIMK_FODT_A", primk_fodt_a), fieldDefinitions)

        when:
        def testResult = Main.kontroll14Fodselsaar(errorReport, record)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == hasError
            errorReport.getErrorType() == errorlevel
        }

        where:
        region_nr_a | kontor_nr_a | primk_fodt_a || hasError | errorlevel
        "667600"    | "017"       | "    "       || true     | Constants.NORMAL_ERROR
        "667600"    | "017"       | "2015"       || false    | Constants.NO_ERROR
        "667600"    | "017"       | "XXXX"       || true     | Constants.NORMAL_ERROR
    }

    def "K15 Samlivsstatus '#primk_sivils_a' -> #hasError -> #errorlevel"() {
        given:
        def record = new Record(Map.of("REGION_NR_A", region_nr_a, "KONTOR_NR_A", kontor_nr_a, "PRIMK_SIVILS_A", primk_sivils_a), fieldDefinitions)

        when:
        def testResult = Main.kontroll15Samlivsstatus(errorReport, record)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == hasError
            errorReport.getErrorType() == errorlevel
        }

        where:
        region_nr_a | kontor_nr_a | primk_sivils_a || hasError | errorlevel
        "667600"    | "017"       | "1"            || false    | Constants.NO_ERROR
        "667600"    | "017"       | "2"            || false    | Constants.NO_ERROR
        "667600"    | "017"       | "3"            || false    | Constants.NO_ERROR
        "667600"    | "017"       | "4"            || false    | Constants.NO_ERROR
        "667600"    | "017"       | "X"            || true     | Constants.NORMAL_ERROR
    }

    def "K16 Formell sivilstand '#primk_sivils_a' / '#formell_sivils_a' -> #hasError -> #errorlevel"() {
        given:
        def record = new Record(Map.of("REGION_NR_A", region_nr_a, "KONTOR_NR_A", kontor_nr_a, "PRIMK_SIVILS_A", primk_sivils_a, "FORMELL_SIVILS_A", formell_sivils_a), fieldDefinitions)

        when:
        def testResult = Main.kontroll16FormellSivilstand(errorReport, record)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == hasError
            errorReport.getErrorType() == errorlevel
        }

        where:
        region_nr_a | kontor_nr_a | primk_sivils_a | formell_sivils_a || hasError | errorlevel
        "667600"    | "017"       | "1"            | " "              || false    | Constants.NO_ERROR
        "667600"    | "017"       | "2"            | " "              || false    | Constants.NO_ERROR
        "667600"    | "017"       | "3"            | " "              || true     | Constants.NORMAL_ERROR
        "667600"    | "017"       | "3"            | "1"              || false    | Constants.NO_ERROR
        "667600"    | "017"       | "3"            | "X"              || true     | Constants.NORMAL_ERROR
        "667600"    | "017"       | "4"            | " "              || true     | Constants.NORMAL_ERROR
        "667600"    | "017"       | "4"            | "1"              || false    | Constants.NO_ERROR
        "667600"    | "017"       | "4"            | "X"              || true     | Constants.NORMAL_ERROR
        "667600"    | "017"       | "X"            | " "              || false    | Constants.NO_ERROR
    }

    def "K17 Bosituasjon '#primk_sambo_a' -> #hasError -> #errorlevel"() {
        given:
        def record = new Record(Map.of("REGION_NR_A", region_nr_a, "KONTOR_NR_A", kontor_nr_a, "PRIMK_SAMBO_A", primk_sambo_a), fieldDefinitions)

        when:
        def testResult = Main.kontroll17Bosituasjon(errorReport, record)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == hasError
            errorReport.getErrorType() == errorlevel
        }

        where:
        region_nr_a | kontor_nr_a | primk_sambo_a || hasError | errorlevel
        "667600"    | "017"       | " "           || true     | Constants.NORMAL_ERROR
        "667600"    | "017"       | "1"           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "2"           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "3"           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "4"           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "5"           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "X"           || true     | Constants.NORMAL_ERROR
    }

    def "K18 Arbeidssituasjon '#primk_arbsit_a' -> #hasError -> #errorlevel"() {
        given:
        def record = new Record(Map.of("REGION_NR_A", region_nr_a, "KONTOR_NR_A", kontor_nr_a, "PRIMK_ARBSIT_A", primk_arbsit_a), fieldDefinitions)

        when:
        def testResult = Main.kontroll18Arbeidsosituasjon(errorReport, record)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == hasError
            errorReport.getErrorType() == errorlevel
        }

        where:
        region_nr_a | kontor_nr_a | primk_arbsit_a || hasError | errorlevel
        "667600"    | "017"       | " "            || true     | Constants.NORMAL_ERROR
        "667600"    | "017"       | "1"            || false    | Constants.NO_ERROR
        "667600"    | "017"       | "2"            || false    | Constants.NO_ERROR
        "667600"    | "017"       | "3"            || false    | Constants.NO_ERROR
        "667600"    | "017"       | "4"            || false    | Constants.NO_ERROR
        "667600"    | "017"       | "5"            || false    | Constants.NO_ERROR
        "667600"    | "017"       | "6"            || false    | Constants.NO_ERROR
        "667600"    | "017"       | "7"            || false    | Constants.NO_ERROR
        "667600"    | "017"       | "X"            || true     | Constants.NORMAL_ERROR
    }

    def "K19A Varighet samtalepartner '#primk_vsrelasj_a' / '#part_lengde_a' -> #hasError -> #errorlevel"() {
        given:
        def record = new Record(Map.of("REGION_NR_A", region_nr_a, "KONTOR_NR_A", kontor_nr_a, "PRIMK_VSRELASJ_A", primk_vsrelasj_a, "PART_LENGDE_A", part_lengde_a), fieldDefinitions)

        when:
        def testResult = Main.kontroll19AVarighetSamtalepartner(errorReport, record)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == hasError
            errorReport.getErrorType() == errorlevel
        }

        where:
        region_nr_a | kontor_nr_a | primk_vsrelasj_a | part_lengde_a || hasError | errorlevel
        "667600"    | "017"       | " "              | " "           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "1"              | " "           || true     | Constants.NORMAL_ERROR
        "667600"    | "017"       | "1"              | "1"           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "1"              | "2"           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "1"              | "3"           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "1"              | "4"           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "1"              | "5"           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "1"              | "6"           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "1"              | "X"           || true     | Constants.NORMAL_ERROR
        "667600"    | "017"       | "X"              | " "           || false    | Constants.NO_ERROR
    }

    def "K19B1 Varighet siden brudd '#primk_vsrelasj_a' / '#ekspart_lengde_a' -> #hasError -> #errorlevel"() {
        given:
        def record = new Record(Map.of("REGION_NR_A", region_nr_a, "KONTOR_NR_A", kontor_nr_a, "PRIMK_VSRELASJ_A", primk_vsrelasj_a, "EKSPART_LENGDE_A", ekspart_lengde_a), fieldDefinitions)

        when:
        def testResult = Main.kontroll19B1VarighetSidenBrudd(errorReport, record)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == hasError
            errorReport.getErrorType() == errorlevel
        }

        where:
        region_nr_a | kontor_nr_a | primk_vsrelasj_a | ekspart_lengde_a || hasError | errorlevel
        "667600"    | "017"       | " "              | " "           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "2"              | " "           || true     | Constants.NORMAL_ERROR
        "667600"    | "017"       | "2"              | "1"           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "2"              | "2"           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "2"              | "3"           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "2"              | "4"           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "2"              | "5"           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "2"              | "6"           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "2"              | "X"           || true     | Constants.NORMAL_ERROR
        "667600"    | "017"       | "X"              | " "           || false    | Constants.NO_ERROR
    }

    def "K19B2 Varighet ekspartner '#primk_vsrelasj_a' / '#ekspart_varigh_a' -> #hasError -> #errorlevel"() {
        given:
        def record = new Record(Map.of("REGION_NR_A", region_nr_a, "KONTOR_NR_A", kontor_nr_a, "PRIMK_VSRELASJ_A", primk_vsrelasj_a, "EKSPART_VARIGH_A", ekspart_varigh_a), fieldDefinitions)

        when:
        def testResult = Main.kontroll19B2VarighetEkspartner(errorReport, record)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == hasError
            errorReport.getErrorType() == errorlevel
        }

        where:
        region_nr_a | kontor_nr_a | primk_vsrelasj_a | ekspart_varigh_a || hasError | errorlevel
        "667600"    | "017"       | " "              | " "           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "2"              | " "           || true     | Constants.NORMAL_ERROR
        "667600"    | "017"       | "2"              | "1"           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "2"              | "2"           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "2"              | "3"           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "2"              | "4"           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "2"              | "5"           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "2"              | "6"           || false    | Constants.NO_ERROR
        "667600"    | "017"       | "2"              | "X"           || true     | Constants.NORMAL_ERROR
        "667600"    | "017"       | "X"              | " "           || false    | Constants.NO_ERROR
    }

    def "K20 Dato forste behandlingssamtale '#forste_samt_a' -> #hasError -> #errorlevel"() {
        given:
        def record = new Record(Map.of("REGION_NR_A", region_nr_a, "KONTOR_NR_A", kontor_nr_a, "FORSTE_SAMT_A", forste_samt_a), fieldDefinitions)

        when:
        def testResult = Main.kontroll20DatoForsteBehandlingssamtale(errorReport, record)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == hasError
            errorReport.getErrorType() == errorlevel
        }

        where:
        region_nr_a | kontor_nr_a | forste_samt_a || hasError | errorlevel
        "667600"    | "017"       | "        "    || true     | Constants.NORMAL_ERROR
        "667600"    | "017"       | "00000000"    || true     | Constants.NORMAL_ERROR
        "667600"    | "017"       | "01012021"    || false    | Constants.NO_ERROR
        "667600"    | "017"       | "XXXXXXXX"    || true     | Constants.NORMAL_ERROR
    }

    def "K21 Dato forste behandlingssamtale er etter henvendelsesdato '#henv_dato_a' / '#forste_samt_a' -> #hasError -> #errorlevel"() {
        given:
        def record = new Record(Map.of("REGION_NR_A", region_nr_a, "KONTOR_NR_A", kontor_nr_a, "HENV_DATO_A", henv_dato_a, "FORSTE_SAMT_A", forste_samt_a), fieldDefinitions)

        when:
        def testResult = Main.kontroll21DatoForsteBehandlingssamtaleEtterHenvendelse(errorReport, record)

        then:
        if (Constants.DEBUG)
            System.out.println(errorReport.generateReport())

        verifyAll {
            testResult == hasError
            errorReport.getErrorType() == errorlevel
        }

        where:
        region_nr_a | kontor_nr_a | henv_dato_a | forste_samt_a || hasError | errorlevel
        "667600"    | "017"       | "02022021"  | "01012021"            || true     | Constants.NORMAL_ERROR
        "667600"    | "017"       | "02022021"  | "        "            || false    | Constants.NO_ERROR
        "667600"    | "017"       | "02022021"  | "02022021"            || false    | Constants.NO_ERROR
        "667600"    | "017"       | "02022021"  | "03032021"            || false    | Constants.NO_ERROR
    }
}


// kontroll19B1VarighetSidenBrudd