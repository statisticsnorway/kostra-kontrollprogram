package no.ssb.kostra.control.famvern.s53;

import no.ssb.kostra.control.famvern.Definitions;
import no.ssb.kostra.control.felles.ControlFelt1Boolsk;
import no.ssb.kostra.control.felles.ControlFelt1BoolskSaaFelt2Boolsk;
import no.ssb.kostra.control.felles.ControlFilbeskrivelse;
import no.ssb.kostra.control.felles.ControlRecordLengde;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.*;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("SpellCheckingInspection")
public class Main {
    private static String createFylkeNr(final String fylkenr) {
        return "Fylkenummer ".concat(fylkenr);
    }

    private static String createKontorNr(final String kontornr, final String linjenr) {
        return "Kontornummer ".concat(kontornr).concat(" / Linjenummer ").concat(linjenr);
    }

    public static ErrorReport doControls(final Arguments args) {
        final var errorReport = new ErrorReport(args);
        final var inputFileContent = args.getInputContentAsStringList();

        // alle records må være med korrekt lengde, ellers vil de andre kontrollene kunne feile
        // Kontroll Recordlengde
        final var hasErrors = ControlRecordLengde.doControl(inputFileContent, errorReport, FieldDefinitions.getFieldLength());

        if (hasErrors) {
            return errorReport;
        }

        final var fieldDefinitions = FieldDefinitions.getFieldDefinitions();
        final var records = inputFileContent.stream()
                .map(p -> new KostraRecord(p, fieldDefinitions))
                .collect(Collectors.toList());

        //final var n = records.size(); // TOOD: Not in use

        // filbeskrivelsesskontroller
        ControlFilbeskrivelse.doControl(records, errorReport);

//        if (er.getErrorType() == Constants.CRITICAL_ERROR) {
//            return er;
//        }

        records.forEach(currentRecord -> {
            // Kontroll 3: Fylkesnummer
            if (!Definitions.isFylkeValid(currentRecord.getFieldAsString("FYLKE_NR"))) {
                errorReport.addEntry(
                        new ErrorReportEntry(
                                createFylkeNr(currentRecord.getFieldAsString("FYLKE_NR"))
                                , createKontorNr(currentRecord.getFieldAsString("KONTORNR"), String.valueOf(currentRecord.getLine()))
                                , String.valueOf(currentRecord.getLine())
                                , " "
                                , "Kontroll 03 Fylkesnummer"
                                , "Fylkesnummeret som er oppgitt i recorden fins ikke i listen med gyldige fylkesnumre. "
                                + "Fant '" + currentRecord.getFieldAsString("FYLKE_NR") + "', forventet én av : " + Definitions.getFylkeAsList() + ". "
                                , Constants.NORMAL_ERROR
                        )
                );
            }

            // Kontroll 4: Kontornummer
            if (!Definitions.isKontorValid(currentRecord.getFieldAsString("KONTORNR"))) {
                errorReport.addEntry(
                        new ErrorReportEntry(
                                createFylkeNr(currentRecord.getFieldAsString("FYLKE_NR"))
                                , createKontorNr(currentRecord.getFieldAsString("KONTORNR"), String.valueOf(currentRecord.getLine()))
                                , String.valueOf(currentRecord.getLine())
                                , " "
                                , "Kontroll 04 Kontornummer"
                                , "Kontornummeret som er oppgitt i recorden fins ikke i listen med gyldige kontornumre. "
                                + "Fant '" + currentRecord.getFieldAsString("KONTORNR") + "', forventet én av : " + Definitions.getKontorAsList() + ". "
                                + "Feltet er obligatorisk og må fylles ut."
                                , Constants.NORMAL_ERROR
                        )
                );
            }

            // Kontroll 5: Manglende samsvar mellom fylkes- og kontornummer
            if (!Definitions.isFylkeAndKontorValid(currentRecord.getFieldAsString("FYLKE_NR"), currentRecord.getFieldAsString("KONTORNR"))) {
                errorReport.addEntry(
                        new ErrorReportEntry(
                                createFylkeNr(currentRecord.getFieldAsString("FYLKE_NR"))
                                , createKontorNr(currentRecord.getFieldAsString("KONTORNR"), String.valueOf(currentRecord.getLine()))
                                , String.valueOf(currentRecord.getLine())
                                , " "
                                , "Kontroll 05 Manglende samsvar mellom fylkes- og kontornummer"
                                , "Fylkesnummer (" + currentRecord.getFieldAsString("FYLKE_NR") + ") og "
                                + "kontornummer (" + currentRecord.getFieldAsString("KONTOR_NR_A") + ") stemmer ikke overens."
                                , Constants.NORMAL_ERROR
                        )
                );
            }

            // Kontroll 10
            List.of(
                    List.of("TILTAK_PUBLIKUM", "Andre tiltak mot publikum"),
                    List.of("VEILEDNING_STUDENTER", "Undervisning/veiledning studenter"),
                    List.of("VEILEDNING_HJELPEAP", "Veiledning/konsultasjon for hjelpeapparatet"),
                    List.of("INFO_MEDIA", "Informasjon i media"),
                    List.of("TILSYN", "Tilsyn"),
                    List.of("FORELDREVEIL", "Foreldreveiledning"),
                    List.of("ANNET", "Annet")
            ).forEach(item -> {
                        final var baseField = item.get(0);
                        final var title = item.get(1);

                        // Kontrollere tiltak
                        final var tiltakField = String.join("_", baseField, "TILTAK");
                        final var tiltakKontrollNr = String.join(", ", String.join(" ", "Kontroll 10", title), "tiltak");
                        final var tiltakErrorText = "Det er ikke fylt hvor mange tiltak ("
                                .concat(String.valueOf(currentRecord.getFieldAsIntegerDefaultEquals0(tiltakField)))
                                .concat(") kontoret har gjennomført når det gjelder '")
                                .concat(title)
                                .concat(", tiltak'. Sjekk om det er glemt å rapportere ")
                                .concat(title)
                                .concat(", tiltak.");

                        ControlFelt1Boolsk.doControl(
                                errorReport
                                , new ErrorReportEntry(
                                        createFylkeNr(currentRecord.getFieldAsString("FYLKE_NR"))
                                        , createKontorNr(currentRecord.getFieldAsString("KONTORNR"), String.valueOf(currentRecord.getLine()))
                                        , String.valueOf(currentRecord.getLine())
                                        , " "
                                        , tiltakKontrollNr
                                        , tiltakErrorText
                                        , Constants.NORMAL_ERROR
                                )
                                , currentRecord.getFieldAsIntegerDefaultEquals0(tiltakField)
                                , ">"
                                , 0
                        );

                        // Kontrollere timer
                        final var timerField = String.join("_", baseField, "TIMER");
                        final var timerKontrollNr = String.join(", ", String.join(" ", "Kontroll 10", title), "timer");
                        final var timerErrorText = "For '"
                                .concat(title)
                                .concat("' er det rapportert (")
                                .concat(String.valueOf(currentRecord.getFieldAsIntegerDefaultEquals0(tiltakField)))
                                .concat(") tiltak, men det mangler hvor mange timer (")
                                .concat(String.valueOf(currentRecord.getFieldAsIntegerDefaultEquals0(timerField)))
                                .concat(") kontoret har brukt for å gjennomføre tiltakene. Sjekk om det er glemt å rapportere. ");

                        ControlFelt1BoolskSaaFelt2Boolsk.doControl(
                                errorReport
                                , new ErrorReportEntry(
                                        createFylkeNr(currentRecord.getFieldAsString("FYLKE_NR"))
                                        , createKontorNr(currentRecord.getFieldAsString("KONTORNR"), String.valueOf(currentRecord.getLine()))
                                        , String.valueOf(currentRecord.getLine())
                                        , " "
                                        , timerKontrollNr
                                        , timerErrorText
                                        , Constants.NORMAL_ERROR
                                )
                                , currentRecord.getFieldAsInteger(tiltakField)
                                , ">"
                                , 0
                                , currentRecord.getFieldAsInteger(timerField)
                                , ">"
                                , 0
                        );
                    }
            );
        });
        return errorReport;
    }
}
