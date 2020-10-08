package no.ssb.kostra.control.famvern.s53;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.famvern.Definitions;
import no.ssb.kostra.control.felles.ControlFelt1Boolsk;
import no.ssb.kostra.control.felles.ControlFelt1BoolskSaaFelt2Boolsk;
import no.ssb.kostra.control.felles.ControlFilbeskrivelse;
import no.ssb.kostra.control.felles.ControlRecordLengde;
import no.ssb.kostra.controlprogram.Arguments;

import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static String createFylkeNr(String fylkenr) {
        return "Fylkenummer ".concat(fylkenr);
    }

    private static String createKontorNr(String kontornr, String linjenr) {
        return "Kontornummer ".concat(kontornr).concat(" / Linjenummer ").concat(linjenr);
    }


    public static ErrorReport doControls(Arguments args) {
        ErrorReport er = new ErrorReport(args);
        List<String> inputFileContent = args.getInputContentAsStringList();

        // alle records må være med korrekt lengde, ellers vil de andre kontrollene kunne feile
        // Kontroll Recordlengde
        boolean hasErrors = ControlRecordLengde.doControl(inputFileContent, er, FieldDefinitions.getFieldLength());

        if (hasErrors) {
            return er;
        }

        List<FieldDefinition> fieldDefinitions = FieldDefinitions.getFieldDefinitions();
        List<Record> records = inputFileContent.stream()
                .map(p -> new Record(p, fieldDefinitions))
                .collect(Collectors.toList());
        Integer n = records.size();
        Integer l = String.valueOf(n).length();
        final String lf = Constants.lineSeparator;

        // filbeskrivelsesskontroller
        ControlFilbeskrivelse.doControl(records, er);

//        if (er.getErrorType() == Constants.CRITICAL_ERROR) {
//            return er;
//        }

        records.forEach(r -> {
            // Kontroll 3: Fylkesnummer
            if (!Definitions.isFylkeValid(r.getFieldAsString("FYLKE_NR"))) {
                er.addEntry(
                        new ErrorReportEntry(
                                createFylkeNr(r.getFieldAsString("FYLKE_NR"))
                                , createKontorNr(r.getFieldAsString("KONTORNR"), String.valueOf(r.getLine()))
                                , String.valueOf(r.getLine())
                                , " "
                                , "Kontroll 03 Fylkesnummer"
                                , "Fylkesnummeret som er oppgitt i recorden fins ikke i listen med gyldige fylkesnumre. "
                                + "Fant '" + r.getFieldAsString("FYLKE_NR") + "', forventet én av : " + Definitions.getFylkeAsList() + ". "
                                , Constants.NORMAL_ERROR
                        )

                );
            }

            // Kontroll 4: Kontornummer
            if (!Definitions.isKontorValid(r.getFieldAsString("KONTORNR"))) {
                er.addEntry(
                        new ErrorReportEntry(
                                createFylkeNr(r.getFieldAsString("FYLKE_NR"))
                                , createKontorNr(r.getFieldAsString("KONTORNR"), String.valueOf(r.getLine()))
                                , String.valueOf(r.getLine())
                                , " "
                                , "Kontroll 04 Kontornummer"
                                , "Kontornummeret som er oppgitt i recorden fins ikke i listen med gyldige kontornumre. "
                                + "Fant '" + r.getFieldAsString("KONTORNR") + "', forventet én av : " + Definitions.getKontorAsList() + ". "
                                + "Feltet er obligatorisk og må fylles ut."
                                , Constants.NORMAL_ERROR
                        )

                );
            }

            // Kontroll 5: Manglende samsvar mellom fylkes- og kontornummer
            if (!Definitions.isFylkeAndKontorValid(r.getFieldAsString("FYLKE_NR"), r.getFieldAsString("KONTORNR"))) {
                er.addEntry(
                        new ErrorReportEntry(
                                createFylkeNr(r.getFieldAsString("FYLKE_NR"))
                                , createKontorNr(r.getFieldAsString("KONTORNR"), String.valueOf(r.getLine()))
                                , String.valueOf(r.getLine())
                                , " "
                                , "Kontroll 05 Manglende samsvar mellom fylkes- og kontornummer"
                                , "Fylkesnummer (" + r.getFieldAsString("FYLKE_NR") + ") og "
                                + "kontornummer (" + r.getFieldAsString("KONTOR_NR_A") + ") stemmer ikke overens."
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
                        String baseField = item.get(0);
                        String title = item.get(1);

                        // Kontrollere tiltak
                        String tiltakField = String.join("_", baseField, "TILTAK");
                        String tiltakKontrollNr = String.join(", ", String.join(" ", "Kontroll 10", title), "tiltak");
                        String tiltakErrorText = "Det er ikke fylt hvor mange tiltak ("
                                .concat(String.valueOf(r.getFieldAsIntegerDefaultEquals0(tiltakField)))
                                .concat(") kontoret har gjennomført når det gjelder '")
                                .concat(title)
                                .concat(", tiltak'. Sjekk om det er glemt å rapportere ")
                                .concat(title)
                                .concat(", tiltak.");

                        ControlFelt1Boolsk.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        createFylkeNr(r.getFieldAsString("FYLKE_NR"))
                                        , createKontorNr(r.getFieldAsString("KONTORNR"), String.valueOf(r.getLine()))
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , tiltakKontrollNr
                                        , tiltakErrorText
                                        , Constants.NORMAL_ERROR
                                )
                                , tiltakField
                                , ">"
                                , 0
                        );

                        // Kontrollere timer
                        String timerField = String.join("_", baseField, "TIMER");
                        String timerKontrollNr = String.join(", ", String.join(" ", "Kontroll 10", title), "timer");
                        String timerErrorText = "For '"
                                .concat(title)
                                .concat("' er det rapportert (")
                                .concat(String.valueOf(r.getFieldAsIntegerDefaultEquals0(tiltakField)))
                                .concat(") tiltak, men det mangler hvor mange timer (")
                                .concat(String.valueOf(r.getFieldAsIntegerDefaultEquals0(timerField)))
                                .concat(") kontoret har brukt for å gjennomføre tiltakene. Sjekk om det er glemt å rapportere. ");

                        ControlFelt1BoolskSaaFelt2Boolsk.doControl(
                                r
                                , er
                                , new ErrorReportEntry(
                                        createFylkeNr(r.getFieldAsString("FYLKE_NR"))
                                        , createKontorNr(r.getFieldAsString("KONTORNR"), String.valueOf(r.getLine()))
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , timerKontrollNr
                                        , timerErrorText
                                        , Constants.NORMAL_ERROR
                                )
                                , tiltakField
                                , ">"
                                , 0
                                , timerField
                                , ">"
                                , 0
                        );
                    }
            );
        });

        return er;
    }
}
