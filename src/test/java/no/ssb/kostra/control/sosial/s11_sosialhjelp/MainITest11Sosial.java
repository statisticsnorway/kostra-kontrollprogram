package no.ssb.kostra.control.sosial.s11_sosialhjelp;

import no.ssb.kostra.control.felles.ControlRecordLengde;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.FieldDefinition;
import no.ssb.kostra.felles.Record;
import no.ssb.kostra.utils.TestArgumentsInputAndResult;
import no.ssb.kostra.utils.TestRecordInputAndResult;
import no.ssb.kostra.utils.TestRecordListInputAndResult;
import no.ssb.kostra.utils.TestStringInputAndResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static no.ssb.kostra.control.sosial.felles.ControlSosial.*;
import static no.ssb.kostra.control.sosial.s11_sosialhjelp.Main.*;


public class MainITest11Sosial {
    private static final Arguments arguments = new Arguments(new String[]{"-s", "11F", "-y", "2021", "-r", "420400"});
    private static final List<FieldDefinition> definitions = FieldDefinitions.getFieldDefinitions();

    static Stream<TestStringInputAndResult> control01RecordLengdeProvider() {
        return Stream.of(
                new TestStringInputAndResult(arguments, "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234", false, Constants.NO_ERROR),
                new TestStringInputAndResult(arguments, "123456789", true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control03KommunenummerProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "KOMMUNE_NR", "4204"), FieldDefinitions.getFieldDefinitions()), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "KOMMUNE_NR", "3400"), FieldDefinitions.getFieldDefinitions()), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control03BydelsnummerProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "BYDELSNR", "  "), FieldDefinitions.getFieldDefinitions()), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "BYDELSNR", "00"), FieldDefinitions.getFieldDefinitions()), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "BYDELSNR", "04"), FieldDefinitions.getFieldDefinitions()), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(new Arguments(new String[]{"-s", "11F", "-y", "2021", "-r", "030101"}), new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "BYDELSNR", "01"), FieldDefinitions.getFieldDefinitions()), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(new Arguments(new String[]{"-s", "11F", "-y", "2021", "-r", "030100"}), new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "BYDELSNR", "00"), FieldDefinitions.getFieldDefinitions()), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control04OppgaveAarProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "VERSION", "21"), FieldDefinitions.getFieldDefinitions()), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "VERSION", "19"), FieldDefinitions.getFieldDefinitions()), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control05FodselsnummerProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "FNR_OK", "1"), FieldDefinitions.getFieldDefinitions()), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "FNR_OK", "1"), FieldDefinitions.getFieldDefinitions()), true, Constants.NORMAL_ERROR)
        );
    }

    static Stream<TestRecordListInputAndResult> control05AFodselsnummerDubletterProvider() {
        return Stream.of(
                new TestRecordListInputAndResult(
                        arguments
                        , List.of(
                        new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "FNR_OK", "0"), FieldDefinitions.getFieldDefinitions())
                        , new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "456", "PERSON_FODSELSNR", "19096632188", "FNR_OK", "0"), FieldDefinitions.getFieldDefinitions())
                        , new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "789", "PERSON_FODSELSNR", "19096633133", "FNR_OK", "1"), FieldDefinitions.getFieldDefinitions())
                )
                        , false
                        , Constants.NO_ERROR
                )


                , new TestRecordListInputAndResult(
                        arguments
                        , List.of(
                        new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "FNR_OK", "1"), FieldDefinitions.getFieldDefinitions())
                        , new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "456", "PERSON_FODSELSNR", "19096632188", "FNR_OK", "1"), FieldDefinitions.getFieldDefinitions())
                        , new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "789", "PERSON_FODSELSNR", "19096633133", "FNR_OK", "1"), FieldDefinitions.getFieldDefinitions())
                )
                        , true
                        , Constants.CRITICAL_ERROR
                )

                , new TestRecordListInputAndResult(
                        arguments
                        , List.of(
                        new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345"), FieldDefinitions.getFieldDefinitions())
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
                        new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "FNR_OK", "1"), FieldDefinitions.getFieldDefinitions())
                        , new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "456", "PERSON_FODSELSNR", "19096632188", "FNR_OK", "1"), FieldDefinitions.getFieldDefinitions())
                        , new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096633133", "FNR_OK", "1"), FieldDefinitions.getFieldDefinitions())
                )
                        , true
                        , Constants.CRITICAL_ERROR
                )

                , new TestRecordListInputAndResult(arguments
                        , List.of(
                        new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345"), FieldDefinitions.getFieldDefinitions())
                )
                        , false
                        , Constants.NO_ERROR
                )
        );
    }

    static Stream<TestRecordInputAndResult> control06AlderUnder18AarProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "ALDER", "19"), FieldDefinitions.getFieldDefinitions()), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "ALDER", "18"), FieldDefinitions.getFieldDefinitions()), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "ALDER", "17"), FieldDefinitions.getFieldDefinitions()), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control07AlderEr68AarEllerOverProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "ALDER", "69"), definitions), true, Constants.NORMAL_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "ALDER", "68"), definitions), true, Constants.NORMAL_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "ALDER", "67"), definitions), false, Constants.NO_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control08KjonnProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "KJONN", "1"), FieldDefinitions.getFieldDefinitions()), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "KJONN", "2"), FieldDefinitions.getFieldDefinitions()), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "KJONN", "0"), FieldDefinitions.getFieldDefinitions()), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control09SivilstandProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "EKTSTAT", "1"), FieldDefinitions.getFieldDefinitions()), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "EKTSTAT", "2"), FieldDefinitions.getFieldDefinitions()), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "EKTSTAT", "0"), FieldDefinitions.getFieldDefinitions()), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control10ForsorgerpliktForBarnUnder18AarProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BU18", "1"), FieldDefinitions.getFieldDefinitions()), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "BU18", "2"), FieldDefinitions.getFieldDefinitions()), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "BU18", "0"), FieldDefinitions.getFieldDefinitions()), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control11AntallBarnIHusholdningenManglerProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BU18", "1", "ANTBU18", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "BU18", "2", "ANTBU18", "0"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "BU18", "1", "ANTBU18", "0"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control12AntallBarnIHusholdningenProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BU18", "1", "ANTBU18", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "BU18", "2", "ANTBU18", "0"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "BU18", "2", "ANTBU18", "1"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control13MangeBarnIHusholdningenProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "ANTBU18", "9"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "ANTBU18", "10"), definitions), true, Constants.NORMAL_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "ANTBU18", "11"), definitions), true, Constants.NORMAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control14ViktigsteKildeTilLivsoppholdGyldigeVerdierProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VKLO", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "VKLO", "X"), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "VKLO", " "), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control15ViktigsteKildeTilLivsoppholdProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VKLO", "X", "ARBSIT", "XX"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "VKLO", "1", "ARBSIT", "01"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "VKLO", "1", "ARBSIT", "XX"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control16ViktigsteKildeTilLivsoppholdProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VKLO", "X", "ARBSIT", "XX"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "VKLO", "2", "ARBSIT", "03"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "VKLO", "2", "ARBSIT", "XX"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control17ViktigsteKildeTilLivsoppholdProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VKLO", "X", "ARBSIT", "XX"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "VKLO", "4", "ARBSIT", "03"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "VKLO", "4", "ARBSIT", "XX"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control18ViktigsteKildeTilLivsoppholdProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VKLO", "X", "ARBSIT", "XX"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "VKLO", "6", "ARBSIT", "09"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "VKLO", "6", "ARBSIT", "XX"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control19ViktigsteKildeTilLivsoppholdProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VKLO", "X", "ARBSIT", "XX"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "VKLO", "8", "ARBSIT", "10"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "VKLO", "8", "ARBSIT", "XX"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control20ViktigsteKildeTilLivsoppholdProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VKLO", "X", "TRYGDESIT", "XX"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "VKLO", "3", "TRYGDESIT", "01"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "VKLO", "3", "TRYGDESIT", "XX"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control22TilknytningTilTrygdesystemetOgAlderProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "TRYGDESIT", "XX", "ALDER", "XX"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "TRYGDESIT", "07", "ALDER", "63"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "TRYGDESIT", "07", "ALDER", "62"), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "TRYGDESIT", "07", "ALDER", "61"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control23TilknytningTilTrygdesystemetOgBarnProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "TRYGDESIT", "XX", "BU18", "X", "ANTBU18", "XX"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "TRYGDESIT", "05", "BU18", "1", "ANTBU18", "XX"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "TRYGDESIT", "05", "BU18", "0", "ANTBU18", " 1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "TRYGDESIT", "05", "BU18", "0", "ANTBU18", " 0"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control24TilknytningTilTrygdesystemetOgArbeidssituasjonProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "TRYGDESIT", "XX", "ARBSIT", "XX"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "TRYGDESIT", "01", "ARBSIT", "01"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "TRYGDESIT", "04", "ARBSIT", "02"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "TRYGDESIT", "04", "ARBSIT", "04"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "TRYGDESIT", "04", "ARBSIT", "07"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "TRYGDESIT", "07", "ARBSIT", "02"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "TRYGDESIT", "07", "ARBSIT", "04"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "TRYGDESIT", "07", "ARBSIT", "07"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "TRYGDESIT", "04", "ARBSIT", "12"), definitions), true, Constants.NORMAL_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "TRYGDESIT", "07", "ARBSIT", "12"), definitions), true, Constants.NORMAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control24BTilknytningTilTrygdesystemetOgArbeidssituasjonArbeidsavklaringspengerProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VKLO", "X", "TRYGDESIT", "XX", "ARBSIT", "XX"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "VKLO", "3", "TRYGDESIT", "01", "ARBSIT", "XX"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "VKLO", "3", "TRYGDESIT", "11", "ARBSIT", "XX"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "VKLO", "3", "TRYGDESIT", "11", "ARBSIT", "08"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control25ArbeidssituasjonGyldigeKoderProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "ARBSIT", "01"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "12028012345", "ARBSIT", "XX"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control26StonadsmaanederGyldigeKoderProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STMND_1", "01", "BIDRAG", "123456", "LAAN", "123456"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STMND_1", "  ", "BIDRAG", "123456", "LAAN", "123456"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control27StonadssumManglerEllerHarUgyldigeTegnProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BIDRAG", "123456", "LAAN", "123456"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BIDRAG", "000000", "LAAN", "123456"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BIDRAG", "123456", "LAAN", "000000"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BIDRAG", "000000", "LAAN", "000000"), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BIDRAG", "      ", "LAAN", "      "), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control28HarVarighetMenManglerStonadssumProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STMND_1", "01", "BIDRAG", "123456", "LAAN", "123456"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STMND_1", "01", "BIDRAG", "123456", "LAAN", "      "), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STMND_1", "01", "BIDRAG", "      ", "LAAN", "123456"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STMND_1", "01", "BIDRAG", "      ", "LAAN", "      "), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STMND_1", "01", "BIDRAG", "000000", "LAAN", "000000"), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STMND_1", "01", "BIDRAG", "      ", "LAAN", "000000"), definitions), true, Constants.CRITICAL_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STMND_1", "01", "BIDRAG", "000000", "LAAN", "      "), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control29HarStonadssumMenManglerVarighetProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STMND_1", "01", "BIDRAG", "123456", "LAAN", "123456"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STMND_1", "  ", "BIDRAG", "000000", "LAAN", "000000"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "STMND_1", "  ", "BIDRAG", "123456", "LAAN", "123456"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control30StonadssumPaaMaxEllerMerProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BIDRAG", "123456", "LAAN", "000000"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BIDRAG", "600000", "LAAN", "000000"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BIDRAG", "600001", "LAAN", "000000"), definitions), true, Constants.NORMAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control31StonadssumPaaMinEllerMindreProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BIDRAG", "123456", "LAAN", "000000"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BIDRAG", "   201", "LAAN", "000000"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BIDRAG", "   200", "LAAN", "000000"), definitions), true, Constants.NORMAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control32OkonomiskraadgivningGyldigeKoderProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "GITT_OKONOMIRAD", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "GITT_OKONOMIRAD", "2"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "GITT_OKONOMIRAD", "X"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control33UtarbeidelseAvIndividuellPlanProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "FAAT_INDIVIDUELL_PLAN", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "FAAT_INDIVIDUELL_PLAN", "2"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "FAAT_INDIVIDUELL_PLAN", "X"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control35BoligsituasjonProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BOSIT", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BOSIT", "2"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BOSIT", "X"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control36BidragFordeltPaaMmaanederProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BIDRAG", "000000", "BIDRAG_JAN", "000000", "BIDRAG_FEB", "000000"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BIDRAG", "   200", "BIDRAG_JAN", "   100", "BIDRAG_FEB", "   100"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "BIDRAG", "   100", "BIDRAG_JAN", "  2000", "BIDRAG_FEB", "  2000"), definitions), true, Constants.NORMAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control37LaanFordeltPaaMmaanederProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "LAAN", "000000", "LAAN_JAN", "000000", "LAAN_FEB", "000000"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "LAAN", "   200", "LAAN_JAN", "   100", "LAAN_FEB", "   100"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "LAAN", "   100", "LAAN_JAN", "  2000", "LAAN_FEB", "  2000"), definitions), true, Constants.NORMAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control38DUFNummerProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "PERSON_DUF", "            "), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "           ", "PERSON_DUF", "201212345603"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "           ", "PERSON_DUF", "            "), definitions), true, Constants.NORMAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control39ForsteVilkarIAaretProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VILKARSOSLOV", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VILKARSOSLOV", "2"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VILKARSOSLOV", "X"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control40ForsteVilkarIAaretSamboProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VILKARSAMEKT", "1"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VILKARSAMEKT", "2"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VILKARSAMEKT", "X"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control41DatoForUtbetalingsvedtakProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VILKARSOSLOV", "X", "UTBETDATO", "     "), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VILKARSOSLOV", "1", "UTBETDATO", "010120"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VILKARSOSLOV", "2", "UTBETDATO", "010119"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VILKARSOSLOV", "1", "UTBETDATO", "321320"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control42TilOgMedDatoForUtbetalingsvedtakProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VILKARSOSLOV", "X", "UTBETTOMDATO", "     "), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VILKARSOSLOV", "1", "UTBETTOMDATO", "010120"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VILKARSOSLOV", "2", "UTBETTOMDATO", "010119"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VILKARSOSLOV", "1", "UTBETTOMDATO", "321320"), definitions), true, Constants.CRITICAL_ERROR)
        );
    }

    static Stream<TestRecordInputAndResult> control43VilkaarProvider() {
        return Stream.of(
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VILKARSOSLOV", "X", "VILKARARBEID", "  "), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VILKARSOSLOV", "1", "VILKARARBEID", "16"), definitions), false, Constants.NO_ERROR),
                new TestRecordInputAndResult(arguments, new Record(Map.of("SAKSBEHANDLER", "Sara Sak", "PERSON_JOURNALNR", "123", "PERSON_FODSELSNR", "19096632188", "VILKARSOSLOV", "1", "VILKARARBEID", "  "), definitions), true, Constants.CRITICAL_ERROR)
        );
    }




// TODO     
    
    @AfterEach
    public void resetStaticRecordCounter() {
        Record.resetLineCount();
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control01RecordLengdeProvider")
    public void control01RecordLengdeTest(TestStringInputAndResult inputAndResult) {
        // public static boolean doControl(List<String> s, ErrorReport er, int length)
        boolean result = ControlRecordLengde.doControl(List.of(inputAndResult.getString()), inputAndResult.getErrorReport(), FieldDefinitions.getFieldLength());
        System.out.println(inputAndResult.getErrorReport().generateReport());

        Assertions.assertEquals(inputAndResult.isResult(), result);
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control03KommunenummerProvider")
    public void control03KommunenummerTest(TestRecordInputAndResult inputAndResult) {
        boolean result = control03Kommunenummer(inputAndResult.getErrorReport(), inputAndResult.getRecord());
        System.out.println(inputAndResult.getErrorReport().generateReport());

        Assertions.assertEquals(inputAndResult.isResult(), result);
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control03BydelsnummerProvider")
    public void control03BydelsnummerTest(TestRecordInputAndResult inputAndResult) {
        boolean result = control03Bydelsnummer(inputAndResult.getErrorReport(), inputAndResult.getRecord());
        System.out.println(inputAndResult.getErrorReport().generateReport());

        Assertions.assertEquals(inputAndResult.isResult(), result);
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control04OppgaveAarProvider")
    public void control04OppgaveAarTest(TestRecordInputAndResult inputAndResult) {
        boolean result = control04OppgaveAar(inputAndResult.getErrorReport(), inputAndResult.getRecord());
        System.out.println(inputAndResult.getErrorReport().generateReport());

        Assertions.assertEquals(inputAndResult.isResult(), result);
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control05FodselsnummerProvider")
    public void control05FodselsnummerTest(TestRecordInputAndResult inputAndResult) {
        String fnr = inputAndResult.getRecord().getFieldAsString("PERSON_FODSELSNR");
        System.out.println(fnr);
        boolean result = control05Fodselsnummer(inputAndResult.getErrorReport(), inputAndResult.getRecord());
        System.out.println(inputAndResult.getErrorReport().generateReport());

        Assertions.assertEquals(inputAndResult.isResult(), result);
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control05AFodselsnummerDubletterProvider")
    public void control05AFodselsnummerDubletterTest(TestRecordListInputAndResult inputAndResult) {
        boolean result = control05AFodselsnummerDubletter(inputAndResult.getErrorReport(), inputAndResult.getRecordList());
        System.out.println(inputAndResult.getErrorReport().generateReport());

        Assertions.assertEquals(inputAndResult.isResult(), result);
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control05BJournalnummerDubletterProvider")
    public void control05BJournalnummerDubletterTest(TestRecordListInputAndResult inputAndResult) {
        boolean result = control05BJournalnummerDubletter(inputAndResult.getErrorReport(), inputAndResult.getRecordList());
        System.out.println(inputAndResult.getErrorReport().generateReport());

        Assertions.assertEquals(inputAndResult.isResult(), result);
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control06AlderUnder18AarProvider")
    public void control06AlderUnder18AarTest(TestRecordInputAndResult inputAndResult) {
        boolean result = control06AlderUnder18Aar(inputAndResult.getErrorReport(), inputAndResult.getRecord());
        System.out.println(inputAndResult.getErrorReport().generateReport());

        Assertions.assertEquals(inputAndResult.isResult(), result);
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control07AlderEr68AarEllerOverProvider")
    public void control07AlderEr68AarEllerOverTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control07AlderEr68AarEllerOver(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control08KjonnProvider")
    public void control08KjonnTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control08Kjonn(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control09SivilstandProvider")
    public void control09SivilstandTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control09Sivilstand(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control10ForsorgerpliktForBarnUnder18AarProvider")
    public void control10ForsorgerpliktForBarnUnder18AarTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control10ForsorgerpliktForBarnUnder18Aar(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control11AntallBarnIHusholdningenManglerProvider")
    public void control11AntallBarnIHusholdningenManglerTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control11AntallBarnIHusholdningenMangler(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control12AntallBarnIHusholdningenProvider")
    public void control12AntallBarnIHusholdningenTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control12AntallBarnIHusholdningen(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control13MangeBarnIHusholdningenProvider")
    public void control13MangeBarnIHusholdningenTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control13MangeBarnIHusholdningen(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control14ViktigsteKildeTilLivsoppholdGyldigeVerdierProvider")
    public void control14ViktigsteKildeTilLivsoppholdGyldigeVerdierTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control14ViktigsteKildeTilLivsoppholdGyldigeVerdier(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control15ViktigsteKildeTilLivsoppholdProvider")
    public void control15ViktigsteKildeTilLivsoppholdTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control15ViktigsteKildeTilLivsopphold(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control16ViktigsteKildeTilLivsoppholdProvider")
    public void control16ViktigsteKildeTilLivsoppholdTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control16ViktigsteKildeTilLivsopphold(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control17ViktigsteKildeTilLivsoppholdProvider")
    public void control17ViktigsteKildeTilLivsoppholdTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control17ViktigsteKildeTilLivsopphold(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control18ViktigsteKildeTilLivsoppholdProvider")
    public void control18ViktigsteKildeTilLivsoppholdTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control18ViktigsteKildeTilLivsopphold(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control19ViktigsteKildeTilLivsoppholdProvider")
    public void control19ViktigsteKildeTilLivsoppholdTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control19ViktigsteKildeTilLivsopphold(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control20ViktigsteKildeTilLivsoppholdProvider")
    public void control20ViktigsteKildeTilLivsoppholdTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control20ViktigsteKildeTilLivsopphold(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control22TilknytningTilTrygdesystemetOgAlderProvider")
    public void control22TilknytningTilTrygdesystemetOgAlderTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control22TilknytningTilTrygdesystemetOgAlder(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control23TilknytningTilTrygdesystemetOgBarnProvider")
    public void control23TilknytningTilTrygdesystemetOgBarnTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control23TilknytningTilTrygdesystemetOgBarn(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control24TilknytningTilTrygdesystemetOgArbeidssituasjonProvider")
    public void control24TilknytningTilTrygdesystemetOgArbeidssituasjonTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control24TilknytningTilTrygdesystemetOgArbeidssituasjon(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control24BTilknytningTilTrygdesystemetOgArbeidssituasjonArbeidsavklaringspengerProvider")
    public void control24BTilknytningTilTrygdesystemetOgArbeidssituasjonArbeidsavklaringspengerTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control24BTilknytningTilTrygdesystemetOgArbeidssituasjonArbeidsavklaringspenger(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control25ArbeidssituasjonGyldigeKoderProvider")
    public void control25ArbeidssituasjonGyldigeKoderTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control25ArbeidssituasjonGyldigeKoder(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control26StonadsmaanederGyldigeKoderProvider")
    public void control26StonadsmaanederGyldigeKoderTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control26StonadsmaanederGyldigeKoder(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control27StonadssumManglerEllerHarUgyldigeTegnProvider")
    public void control27StonadssumManglerEllerHarUgyldigeTegnTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control27StonadssumManglerEllerHarUgyldigeTegn(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control28HarVarighetMenManglerStonadssumProvider")
    public void control28HarVarighetMenManglerStonadssumTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control28HarVarighetMenManglerStonadssum(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control29HarStonadssumMenManglerVarighetProvider")
    public void control29HarStonadssumMenManglerVarighetTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control29HarStonadssumMenManglerVarighet(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control30StonadssumPaaMaxEllerMerProvider")
    public void control30StonadssumPaaMaxEllerMerTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control30StonadssumPaaMaxEllerMer(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control31StonadssumPaaMinEllerMindreProvider")
    public void control31StonadssumPaaMinEllerMindreTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control31StonadssumPaaMinEllerMindre(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control32OkonomiskraadgivningGyldigeKoderProvider")
    public void control32OkonomiskraadgivningGyldigeKoderTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control32OkonomiskraadgivningGyldigeKoder(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control33UtarbeidelseAvIndividuellPlanProvider")
    public void control33UtarbeidelseAvIndividuellPlanTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control33UtarbeidelseAvIndividuellPlan(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control35BoligsituasjonProvider")
    public void control35BoligsituasjonTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control35Boligsituasjon(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control36BidragFordeltPaaMmaanederProvider")
    public void control36BidragFordeltPaaMmaanederTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control36BidragFordeltPaaMmaaneder(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control37LaanFordeltPaaMmaanederProvider")
    public void control37LaanFordeltPaaMmaanederTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control37LaanFordeltPaaMmaaneder(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control38DUFNummerProvider")
    public void control38DUFNummerTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control38DUFNummer(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control39ForsteVilkarIAaretProvider")
    public void control39ForsteVilkarIAaretTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control39ForsteVilkarIAaret(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control40ForsteVilkarIAaretSamboProvider")
    public void control40ForsteVilkarIAaretSamboTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control40ForsteVilkarIAaretSambo(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control41DatoForUtbetalingsvedtakProvider")
    public void control41DatoForUtbetalingsvedtakTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control41DatoForUtbetalingsvedtak(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control42TilOgMedDatoForUtbetalingsvedtakProvider")
    public void control42TilOgMedDatoForUtbetalingsvedtakTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control42TilOgMedDatoForUtbetalingsvedtak(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }

    @ParameterizedTest(name = "#{index} - Run test with {0}")
    @MethodSource("control43VilkaarProvider")
    public void control43VilkaarTest(TestRecordInputAndResult inputAndResult) {
        Assertions.assertEquals(inputAndResult.isResult(), control43Vilkaar(inputAndResult.getErrorReport(), inputAndResult.getRecord()));
        Assertions.assertEquals(inputAndResult.getExpectedErrorType(), inputAndResult.getErrorReport().getErrorType());

        System.out.println(inputAndResult.getErrorReport().generateReport());
    }
}
