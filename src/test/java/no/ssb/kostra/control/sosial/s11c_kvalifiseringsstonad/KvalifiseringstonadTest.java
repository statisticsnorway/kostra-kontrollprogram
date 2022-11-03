package no.ssb.kostra.control.sosial.s11c_kvalifiseringsstonad;

import no.ssb.kostra.control.felles.ControlRecordLengde;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.FieldDefinition;
import no.ssb.kostra.felles.KostraRecord;
import no.ssb.kostra.utils.TestRecordInputAndResult;
import no.ssb.kostra.utils.TestRecordListInputAndResult;
import no.ssb.kostra.utils.TestStringInputAndResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static no.ssb.kostra.control.sosial.felles.ControlSosial.*;
import static no.ssb.kostra.control.sosial.s11c_kvalifiseringsstonad.Main.*;

class KvalifiseringstonadTest {
    private static final Arguments arguments = new Arguments(new String[]{"-s", "11CF", "-y", "2021", "-r", "420400"});
    private static final Arguments argumOslo = new Arguments(new String[]{"-s", "11CF", "-y", "2021", "-r", "030100"});
    private static final List<FieldDefinition> definitions = FieldDefinitions.getFieldDefinitions();

    static Stream<TestStringInputAndResult> control01RecordLengdeProvider() {
        return Stream.of(
                new TestStringInputAndResult(arguments, "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789", false, Constants.NO_ERROR),
                new TestStringInputAndResult(arguments, "123456789", true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control03KommunenummerProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "KOMMUNE_NR", "4204"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "KOMMUNE_NR", "3400"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control03BydelsnummerProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "BYDELSNR", "  "), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "BYDELSNR", "00"), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "BYDELSNR", "04"), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(new Arguments(new String[]{"-s", "11CF", "-y", "2021", "-r", "030101"}), new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "BYDELSNR", "01"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(new Arguments(new String[]{"-s", "11CF", "-y", "2021", "-r", "030100"}), new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "BYDELSNR", "00"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control04OppgaveAarProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "VERSION", "21"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "VERSION", "19"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control05FodselsnummerProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "FNR_OK", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096600100", "FNR_OK", "1"), definitions), true, Constants.NORMAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "FNR_OK", "1"), definitions), true, Constants.NORMAL_ERROR)
        );
    }

    static Stream<TestRecordListInputAndResult> control05AFodselsnummerDubletterProvider() {
        return Stream.of(
                new TestRecordListInputAndResult(
                        arguments
                        , List.of(
                        new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "1"), definitions)
                        , new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "789", "PERSON_FODSELSNR", "19096633133", "STATUS", "1"), definitions)
                )
                        , false
                        , Constants.NO_ERROR
                )

                , new TestRecordListInputAndResult(
                        arguments
                        , List.of(
                        new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "1"), definitions)
                        , new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "456", "PERSON_FODSELSNR", "19096632188", "STATUS", "1"), definitions)
                )
                        , true
                        , Constants.CRITICAL_ERROR
                )

                , new TestRecordListInputAndResult(
                        arguments
                        , List.of(
                        new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "1"), definitions)
                        , new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "456", "PERSON_FODSELSNR", "19096632188", "STATUS", "2"), definitions)
                )
                        , false
                        , Constants.NO_ERROR
                )

                , new TestRecordListInputAndResult(
                        arguments
                        , List.of(
                        new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "STATUS", "1"), definitions)
                        , new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "STATUS", "1"), definitions)
                )
                        , false
                        , Constants.NO_ERROR
                )
        );
    }

    static Stream<TestRecordListInputAndResult> control05BJournalnummerDubletterProvider() {
        return Stream.of(
                new TestRecordListInputAndResult(arguments
                        , List.of(
                        new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "FNR_OK", "1"), definitions)
                        , new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "456", "PERSON_FODSELSNR", "19096632188", "FNR_OK", "1"), definitions)
                        , new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096633133", "FNR_OK", "1"), definitions)
                )
                        , true
                        , Constants.CRITICAL_ERROR
                )

                , new TestRecordListInputAndResult(arguments
                        , List.of(
                        new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345"), definitions)
                )
                        , false
                        , Constants.NO_ERROR
                )
        );
    }

    static Stream<TestRecordInputAndResult> control06AlderUnder18AarProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "ALDER", "19", "FNR_OK", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "ALDER", "18", "FNR_OK", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "ALDER", "17", "FNR_OK", "1"), definitions), true, Constants.NORMAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control07AlderEr68AarEllerOverProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "ALDER", "69", "FNR_OK", "1"), definitions), true, Constants.NORMAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "ALDER", "68", "FNR_OK", "1"), definitions), true, Constants.NORMAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "ALDER", "67", "FNR_OK", "1"), definitions), false, Constants.NO_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control08KjonnProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "KJONN", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "KJONN", "2"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "KJONN", "0"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control09SivilstandProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "EKTSTAT", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "EKTSTAT", "2"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "EKTSTAT", "0"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control10Bu18Provider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BU18", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "BU18", "2"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "BU18", "0"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control11Bu18AntBu18Provider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BU18", "1", "ANTBU18", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "BU18", "2", "ANTBU18", "0"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "BU18", "1", "ANTBU18", "0"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control12AntBu18Bu18Provider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BU18", "1", "ANTBU18", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "BU18", "2", "ANTBU18", "0"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "BU18", "2", "ANTBU18", "1"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control13AntBu18Provider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "ANTBU18", " "), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "ANTBU18", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "ANTBU18", "13"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "ANTBU18", "14"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control14RegDatoProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "REG_DATO", "010120", "VERSION", "22"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "REG_DATO", "      ", "VERSION", "22"), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "REG_DATO", "321320", "VERSION", "22"), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "REG_DATO", "010117", "VERSION", "22"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control15VedtakDatoProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VEDTAK_DATO", "010120", "VERSION", "22", "KOMMUNE_NR", "3401"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "VEDTAK_DATO", "      ", "VERSION", "22", "KOMMUNE_NR", "3401"), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "VEDTAK_DATO", "321320", "VERSION", "22", "KOMMUNE_NR", "3401"), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VEDTAK_DATO", "010117", "VERSION", "22", "KOMMUNE_NR", "3401"), definitions), true, Constants.CRITICAL_ERROR),

                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VEDTAK_DATO", "010120", "VERSION", "22", "KOMMUNE_NR", "0301"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "VEDTAK_DATO", "      ", "VERSION", "22", "KOMMUNE_NR", "0301"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "VEDTAK_DATO", "321320", "VERSION", "22", "KOMMUNE_NR", "0301"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VEDTAK_DATO", "010117", "VERSION", "22", "KOMMUNE_NR", "0301"), definitions), false, Constants.NO_ERROR)

        );
    }

    static Stream<TestRecordInputAndResult> control16BegyntDatoProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BEGYNT_DATO", "010120", "VERSION", "22", "KOMMUNE_NR", "3401"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "BEGYNT_DATO", "      ", "VERSION", "22", "KOMMUNE_NR", "3401"), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "BEGYNT_DATO", "321320", "VERSION", "22", "KOMMUNE_NR", "3401"), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BEGYNT_DATO", "010117", "VERSION", "22", "KOMMUNE_NR", "3401"), definitions), true, Constants.CRITICAL_ERROR),

                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BEGYNT_DATO", "010120", "VERSION", "22", "KOMMUNE_NR", "0301"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "BEGYNT_DATO", "      ", "VERSION", "22", "KOMMUNE_NR", "0301"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "BEGYNT_DATO", "321320", "VERSION", "22", "KOMMUNE_NR", "0301"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BEGYNT_DATO", "010117", "VERSION", "22", "KOMMUNE_NR", "0301"), definitions), false, Constants.NO_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control19KvalifiseringsprogramIAnnenKommuneProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "KVP_KOMM", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "KVP_KOMM", "2"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "KVP_KOMM", "0"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control20KvalifiseringsprogramIAnnenKommuneKommunenummerProvider() {
        return Stream.of(
                new TestRecordInputAndResult(argumOslo, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "KVP_KOMM", " ", "KOMMNR_KVP_KOMM", "0301"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(argumOslo, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "KVP_KOMM", "1", "KOMMNR_KVP_KOMM", "0301"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(argumOslo, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "KVP_KOMM", "2", "KOMMNR_KVP_KOMM", "    "), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "KVP_KOMM", "1", "KOMMNR_KVP_KOMM", "0101"), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "KVP_KOMM", "1", "KOMMNR_KVP_KOMM", "    "), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "KVP_KOMM", "2", "KOMMNR_KVP_KOMM", "    "), definitions), false, Constants.NO_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control20AFraKvalifiseringsprogramIAnnenBydelIOsloProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "KOMMUNE_NR", "1103", "KVP_OSLO", " "), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(argumOslo, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "KOMMUNE_NR", "0301", "KVP_OSLO", "0"), definitions), true, Constants.NORMAL_ERROR),
                new TestRecordInputAndResult(argumOslo, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "KOMMUNE_NR", "0301", "KVP_OSLO", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(argumOslo, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "KOMMUNE_NR", "0301", "KVP_OSLO", "2"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(argumOslo, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "KOMMUNE_NR", "0301", "KVP_OSLO", " "), definitions), true, Constants.NORMAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control21YtelserProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "YTELSE_SOSHJELP", " ", "YTELSE_TYPE_SOSHJ", " "), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "YTELSE_SOSHJELP", "1", "YTELSE_TYPE_SOSHJ", "2"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "YTELSE_SOSHJELP", "1", "YTELSE_TYPE_SOSHJ", "3"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "YTELSE_SOSHJELP", "1", "YTELSE_TYPE_SOSHJ", "1"), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "YTELSE_SOSHJELP", "1", "YTELSE_TYPE_SOSHJ", " "), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control26MottattStotteProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "KVP_MED_ASTONAD", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "KVP_MED_ASTONAD", "2"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "KVP_MED_ASTONAD", " "), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "KVP_MED_ASTONAD", "0"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control27MottattOkonomiskSosialhjelpProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "KVP_MED_ASTONAD", " ", "KVP_MED_KOMMBOS", " ", "KVP_MED_HUSBANK", " ", "KVP_MED_SOSHJ_ENGANG", " ", "KVP_MED_SOSHJ_PGM", " ", "KVP_MED_SOSHJ_SUP", " "), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "KVP_MED_ASTONAD", "1", "KVP_MED_KOMMBOS", " ", "KVP_MED_HUSBANK", " ", "KVP_MED_SOSHJ_ENGANG", " ", "KVP_MED_SOSHJ_PGM", " ", "KVP_MED_SOSHJ_SUP", " "), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "KVP_MED_ASTONAD", "1", "KVP_MED_KOMMBOS", "4", "KVP_MED_HUSBANK", " ", "KVP_MED_SOSHJ_ENGANG", " ", "KVP_MED_SOSHJ_PGM", " ", "KVP_MED_SOSHJ_SUP", " "), definitions), false, Constants.NO_ERROR),

                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "KVP_MED_ASTONAD", "2", "KVP_MED_KOMMBOS", " ", "KVP_MED_HUSBANK", " ", "KVP_MED_SOSHJ_ENGANG", " ", "KVP_MED_SOSHJ_PGM", " ", "KVP_MED_SOSHJ_SUP", " "), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "KVP_MED_ASTONAD", "2", "KVP_MED_KOMMBOS", "4", "KVP_MED_HUSBANK", " ", "KVP_MED_SOSHJ_ENGANG", " ", "KVP_MED_SOSHJ_PGM", " ", "KVP_MED_SOSHJ_SUP", " "), definitions), true, Constants.CRITICAL_ERROR)

        );
    }

    static Stream<TestRecordInputAndResult> control28MaanederMedKvalifiseringsstonadProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STMND_1", "01", "KVP_STONAD", "123456", "STATUS", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STMND_1", "  ", "KVP_STONAD", "123456", "STATUS", "1"), definitions), true, Constants.NORMAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STMND_1", "  ", "KVP_STONAD", "      ", "STATUS", "1"), definitions), true, Constants.NORMAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STMND_1", "  ", "KVP_STONAD", "      ", "STATUS", "2"), definitions), false, Constants.NO_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control29KvalifiseringssumManglerProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "KVP_STONAD", "123456"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "KVP_STONAD", "      "), definitions), true, Constants.NORMAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control30HarVarighetMenManglerKvalifiseringssumProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STMND_1", "01", "KVP_STONAD", "123456"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STMND_1", "01", "KVP_STONAD", "      "), definitions), true, Constants.NORMAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STMND_1", "  ", "KVP_STONAD", "      "), definitions), false, Constants.NO_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control31HarKvalifiseringssumMenManglerVarighetProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STMND_1", "01", "KVP_STONAD", "123456"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STMND_1", "  ", "KVP_STONAD", "123456"), definitions), true, Constants.NORMAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STMND_1", "  ", "KVP_STONAD", "      "), definitions), false, Constants.NO_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control32KvalifiseringssumOverMaksimumProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "KVP_STONAD", "123456"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "KVP_STONAD", "600000"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "KVP_STONAD", "600001"), definitions), true, Constants.NORMAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "KVP_STONAD", "      "), definitions), false, Constants.NO_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control36StatusForDeltakelseIKvalifiseringsprogramProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", " "), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "2"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "3"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "4"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "5"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "6"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "X"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control37DatoForAvsluttetProgramProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "1", "AVSL_DATO", "      "), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "2", "AVSL_DATO", "      "), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "3", "AVSL_DATO", "010120"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "4", "AVSL_DATO", "010120"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "5", "AVSL_DATO", "010120"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "6", "AVSL_DATO", "      "), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "3", "AVSL_DATO", "      "), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "4", "AVSL_DATO", "      "), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "5", "AVSL_DATO", "      "), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "3", "AVSL_DATO", "321320"), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "4", "AVSL_DATO", "321320"), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "5", "AVSL_DATO", "321320"), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "5", "AVSL_DATO", "000020"), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", " ", "AVSL_DATO", "000020"), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", " ", "AVSL_DATO", "010120"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control38FullforteAvsluttedeProgramSituasjonProvider() {
        Map<String, String> mapOk1 = new HashMap<>();
        mapOk1.put("SAKSBEHANDLER"    , "Sara Sak");
        mapOk1.put("PERSON_JOURNALNR" , "123");
        mapOk1.put("PERSON_FODSELSNR" , "19096632188");
        mapOk1.put("STATUS"           , "1");

        mapOk1.put("AVSL_ORDINAERTARB", "");
        mapOk1.put("AVSL_ARBLONNSTILS", "");
        mapOk1.put("AVSL_ARBMARK", "");
        mapOk1.put("AVSL_SKOLE", "");
        mapOk1.put("AVSL_UFORE", "");
        mapOk1.put("AVSL_AAP", "");
        mapOk1.put("AVSL_OK_AVKLAR", "");
        mapOk1.put("AVSL_UTEN_OK_AVKLAR", "");
        mapOk1.put("AVSL_ANNET", "");
        mapOk1.put("AVSL_UKJENT", "");

        Map<String, String> mapOk2 = new HashMap<>();
        mapOk2.put("SAKSBEHANDLER"    , "Sara Sak");
        mapOk2.put("PERSON_JOURNALNR" , "123");
        mapOk2.put("PERSON_FODSELSNR" , "19096632188");
        mapOk2.put("STATUS"           , "3");

        mapOk2.put("AVSL_ORDINAERTARB", "01");
        mapOk2.put("AVSL_ARBLONNSTILS", "");
        mapOk2.put("AVSL_ARBMARK", "");
        mapOk2.put("AVSL_SKOLE", "");
        mapOk2.put("AVSL_UFORE", "");
        mapOk2.put("AVSL_AAP", "");
        mapOk2.put("AVSL_OK_AVKLAR", "");
        mapOk2.put("AVSL_UTEN_OK_AVKLAR", "");
        mapOk2.put("AVSL_ANNET", "");
        mapOk2.put("AVSL_UKJENT", "");

        Map<String, String> mapFail = new HashMap<>();
        mapFail.put("SAKSBEHANDLER"    , "Sara Sak");
        mapFail.put("PERSON_JOURNALNR" , "123");
        mapFail.put("PERSON_FODSELSNR" , "19096632188");
        mapFail.put("STATUS"           , "3");

        mapFail.put("AVSL_ORDINAERTARB", "");
        mapFail.put("AVSL_ARBLONNSTILS", "");
        mapFail.put("AVSL_ARBMARK", "");
        mapFail.put("AVSL_SKOLE", "");
        mapFail.put("AVSL_UFORE", "");
        mapFail.put("AVSL_AAP", "");
        mapFail.put("AVSL_OK_AVKLAR", "");
        mapFail.put("AVSL_UTEN_OK_AVKLAR", "");
        mapFail.put("AVSL_ANNET", "");
        mapFail.put("AVSL_UKJENT", "");

        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(mapOk1, definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(mapOk2, definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(mapFail, definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control39FullforteAvsluttedeProgramInntektkildeProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "1", "AVSL_VIKTIGSTE_INNTEKT", "  "), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "3", "AVSL_VIKTIGSTE_INNTEKT", "01"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "3", "AVSL_VIKTIGSTE_INNTEKT", "  "), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new KostraRecord(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STATUS", "3", "AVSL_VIKTIGSTE_INNTEKT", "XX"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    @AfterEach
    public void resetStaticRecordCounter() {
        KostraRecord.resetLineCount();
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control01RecordLengdeProvider")
    void control01RecordLengdeTest(TestStringInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), ControlRecordLengde.doControl(List.of(inputAndResult.getString()), inputAndResult.getErrorReport(), FieldDefinitions.getFieldLength()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control03KommunenummerProvider")
    void control03KommunenummerTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control03Kommunenummer(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control03BydelsnummerProvider")
    void control03BydelsnummerTest(TestRecordInputAndResult inputAndResult) {
        boolean result = control03Bydelsnummer(inputAndResult.getErrorReport(), inputAndResult.getRecord());
        System.out.println(inputAndResult.getErrorReport().generateReport());

        Assertions.assertEquals(inputAndResult.isResult(), result);
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control04OppgaveAarProvider")
    void control04OppgaveAarTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control04OppgaveAar(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control05FodselsnummerProvider")
    void control05FodselsnummerTest(TestRecordInputAndResult inputAndResult) {
        var result = control05Fodselsnummer(inputAndResult.getErrorReport(), inputAndResult.getRecord());

        System.out.println(inputAndResult.getErrorReport().generateReport());

        Assertions.assertEquals(inputAndResult.isResult(), result);
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control05AFodselsnummerDubletterProvider")
    void control05AFodselsnummerDubletterTest(TestRecordListInputAndResult inputAndResult) {
        var result = control05AFodselsnummerDubletter(inputAndResult.getErrorReport(), inputAndResult.getRecordList());

        System.out.println(inputAndResult.getErrorReport().generateReport());

        Assertions.assertEquals(inputAndResult.isResult(), result);
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control05BJournalnummerDubletterProvider")
    void control05BJournalnummerDubletterTest(TestRecordListInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control05BJournalnummerDubletter(inputAndResult.getErrorReport(), inputAndResult.getRecordList()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control06AlderUnder18AarProvider")
    void control06AlderUnder18AarTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control06AlderUnder18Aar(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control07AlderEr68AarEllerOverProvider")
    void control07AlderEr68AarEllerOverTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control07AlderEr68AarEllerOver(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control08KjonnProvider")
    void control08KjonnTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control08Kjonn(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control09SivilstandProvider")
    void control09SivilstandTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control09Sivilstand(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control10Bu18Provider")
    void control10Bu18Test(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control10Bu18(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control11Bu18AntBu18Provider")
    void control11Bu18AntBu18Test(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control11Bu18AntBu18(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control12AntBu18Bu18Provider")
    void control12AntBu18Bu18Test(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control12AntBu18Bu18(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control13AntBu18Provider")
    void control13AntBu18Test(TestRecordInputAndResult inputAndResult) {
        final var result = control13AntBu18(inputAndResult.getErrorReport(), inputAndResult.getRecord());

        System.out.println(inputAndResult.getErrorReport().generateReport());

        Assertions.assertEquals(inputAndResult.isResult(), result);
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control14RegDatoProvider")
    void control14RegDatoTest(TestRecordInputAndResult inputAndResult) {
        final var result = control14RegDato(inputAndResult.getErrorReport(), inputAndResult.getRecord());

        System.out.println(inputAndResult.getErrorReport().generateReport());

        Assertions.assertEquals(inputAndResult.isResult(), result);
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control15VedtakDatoProvider")
    void control15VedtakDatoTest(TestRecordInputAndResult inputAndResult) {
        final var result = control15VedtakDato(inputAndResult.getErrorReport(), inputAndResult.getRecord());

        System.out.println(inputAndResult.getErrorReport().generateReport());

        Assertions.assertEquals(inputAndResult.isResult(), result);
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control16BegyntDatoProvider")
    void control16BegyntDatoTest(TestRecordInputAndResult inputAndResult) {
        final var result = control16BegyntDato(inputAndResult.getErrorReport(), inputAndResult.getRecord());

        System.out.println(inputAndResult.getErrorReport().generateReport());

        Assertions.assertEquals(inputAndResult.isResult(), result);
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control19KvalifiseringsprogramIAnnenKommuneProvider")
    void control19KvalifiseringsprogramIAnnenKommuneTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control19KvalifiseringsprogramIAnnenKommune(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control20KvalifiseringsprogramIAnnenKommuneKommunenummerProvider")
    void control20KvalifiseringsprogramIAnnenKommuneKommunenummerTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control20KvalifiseringsprogramIAnnenKommuneKommunenummer(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control20AFraKvalifiseringsprogramIAnnenBydelIOsloProvider")
    void control20AFraKvalifiseringsprogramIAnnenBydelIOsloTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control20AFraKvalifiseringsprogramIAnnenBydelIOslo(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control21YtelserProvider")
    void control21YtelserTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control21Ytelser(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control26MottattStotteProvider")
    void control26MottattStotteTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control26MottattStotte(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control27MottattOkonomiskSosialhjelpProvider")
    void control27MottattOkonomiskSosialhjelpTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control27MottattOkonomiskSosialhjelp(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control28MaanederMedKvalifiseringsstonadProvider")
    void control28MaanederMedKvalifiseringsstonadTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control28MaanederMedKvalifiseringsstonad(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control29KvalifiseringssumManglerProvider")
    void control29KvalifiseringssumManglerTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control29KvalifiseringssumMangler(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control30HarVarighetMenManglerKvalifiseringssumProvider")
    void control30HarVarighetMenManglerKvalifiseringssumTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control30HarVarighetMenManglerKvalifiseringssum(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control31HarKvalifiseringssumMenManglerVarighetProvider")
    void control31HarKvalifiseringssumMenManglerVarighetTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control31HarKvalifiseringssumMenManglerVarighet(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control32KvalifiseringssumOverMaksimumProvider")
    void control32KvalifiseringssumOverMaksimumTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control32KvalifiseringssumOverMaksimum(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control36StatusForDeltakelseIKvalifiseringsprogramProvider")
    void control36StatusForDeltakelseIKvalifiseringsprogramTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control36StatusForDeltakelseIKvalifiseringsprogram(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control37DatoForAvsluttetProgramProvider")
    void control37DatoForAvsluttetProgramTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control37DatoForAvsluttetProgram(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control38FullforteAvsluttedeProgramSituasjonProvider")
    void control38FullforteAvsluttedeProgramSituasjonTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control38FullforteAvsluttedeProgramSituasjon(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control39FullforteAvsluttedeProgramInntektkildeProvider")
    void control39FullforteAvsluttedeProgramInntektkildeTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control39FullforteAvsluttedeProgramInntektkilde(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }
}
