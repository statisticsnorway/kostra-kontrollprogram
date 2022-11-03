package no.ssb.kostra.control.famvern.s55;

import no.ssb.kostra.control.famvern.Definitions;
import no.ssb.kostra.control.felles.ControlAlleFeltIListeHarLikSum;
import no.ssb.kostra.control.felles.ControlFelt1LikSumAvListe;
import no.ssb.kostra.control.felles.ControlFilbeskrivelse;
import no.ssb.kostra.control.felles.ControlRecordLengde;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.felles.KostraRecord;
import no.ssb.kostra.utils.Format;

import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class Main {
    private static String createLinenumber(final Integer l, final int line) {
        return "Linje " + Format.sprintf("%0" + l + "d", line);
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
                .toList();

        // String saksbehandler = "Filuttrekk"; // TODO
        final var n = records.size();
        final var l = String.valueOf(n).length();

        // filbeskrivelsesskontroller
        ControlFilbeskrivelse.doControl(records, errorReport);

        if (errorReport.getErrorType() == Constants.CRITICAL_ERROR) {
            return errorReport;
        }

        final var clTSSSTF = List.of("TOT", "SEP", "SAM", "SAK", "TILB", "FLY");
        final var clTBE = List.of("TOT", "BEGGE", "EN");
        final var clO = List.of("OPPM");
        // List<String> clT = List.of("TOT"); // TODO
        final var clT12 = List.of("TOT", "1", "2");
        final var clT123 = List.of("TOT", "1", "2", "3");
        final var clT1234 = List.of("TOT", "1", "2", "3", "4");
        final var clT12345 = List.of("TOT", "1", "2", "3", "4", "5");

        records.forEach(currentRecord -> {
            // Kontroll 03: Fylkesnummer
            if (!Definitions.isFylkeValid(currentRecord.getFieldAsString("FYLKE_NR"))) {
                errorReport.addEntry(
                        new ErrorReportEntry(
                                "FILUTTREKK"
                                , createLinenumber(l, currentRecord.getLine())
                                , String.valueOf(currentRecord.getLine())
                                , " "
                                , "Kontroll 03 fylkesnummer"
                                , "Fylkesnummeret som er oppgitt i recorden fins ikke i listen med gyldige fylkesnumre. "
                                + "Fant '" + currentRecord.getFieldAsString("FYLKE_NR") + "', forventet én av : " + Definitions.getFylkeAsList() + ". "
                                + "Feltet er obligatorisk og må fylles ut."
                                , Constants.NORMAL_ERROR
                        )
                );
            }

            // Kontroll 05 Avsluttede meklinger etter tidsbruk
            {
                final var measure = "MEKLING";
                final var fieldLists = ControlFelt1LikSumAvListe.createFieldList(measure, clTSSSTF, clT123)
                        // Listen med feltnavn i filbeskrivelsen er inkonsistent.
                        // Mekker litt på de genererte feltnavnene slik at de passer med filbeskrivelsen.
                        .stream()
                        .map(sublist -> sublist
                                .stream()
                                .map(item -> (item.equalsIgnoreCase("MEKLING_TOT_TOT") ? "MEKLING_TOT_ALLE" : item))
                                .toList())
                        .toList();

                ControlFelt1LikSumAvListe.doControl(
                        currentRecord
                        , errorReport
                        , new ErrorReportEntry(
                                "FILUTTREKK"
                                , createLinenumber(l, currentRecord.getLine())
                                , String.valueOf(currentRecord.getLine())
                                , " "
                                , "Kontroll 05 Avsluttede meklinger etter tidsbruk"
                                , "Tallene summerer seg ikke som de skal."
                                , Constants.NORMAL_ERROR
                        )
                        , fieldLists
                );
            }

            // Kontroll 06 Avsluttede meklinger etter deltakere
            {
                final var fieldLists = ControlFelt1LikSumAvListe.createFieldList(clTSSSTF, clTBE)
                        // Listen med feltnavn i filbeskrivelsen er inkonsistent.
                        // Mekker litt på de genererte feltnavnene slik at de passer med filbeskrivelsen.
                        .stream()
                        .map(sublist -> sublist
                                .stream()
                                .map(item -> (item.equalsIgnoreCase("TOT_BEGGE") ? "BEGGE_TOT" : item))
                                .map(item -> (item.equalsIgnoreCase("TOT_EN") ? "EN_TOT" : item))
                                .map(item -> (item.equalsIgnoreCase("TOT_TOT") ? "ENBEGGE_TOT" : item))
                                .toList())
                        .toList();

                ControlFelt1LikSumAvListe.doControl(
                        currentRecord
                        , errorReport
                        , new ErrorReportEntry(
                                "FILUTTREKK"
                                , createLinenumber(l, currentRecord.getLine())
                                , String.valueOf(currentRecord.getLine())
                                , " "
                                , "Kontroll 06 Avsluttede meklinger etter deltakere"
                                , "Tallene summerer seg ikke som de skal."
                                , Constants.NORMAL_ERROR
                        )
                        , fieldLists
                );
            }

            // Kontroll 07 Avsluttede meklinger etter ventetid
            {
                final var measure = "VENTETID";
                final var fieldLists = ControlFelt1LikSumAvListe.createFieldList(measure, clTSSSTF, clT1234);

                ControlFelt1LikSumAvListe.doControl(
                        currentRecord
                        , errorReport
                        , new ErrorReportEntry(
                                "FILUTTREKK"
                                , createLinenumber(l, currentRecord.getLine())
                                , String.valueOf(currentRecord.getLine())
                                , " "
                                , "Kontroll 07 Avsluttede meklinger etter ventetid"
                                , "Tallene summerer seg ikke som de skal."
                                , Constants.NORMAL_ERROR
                        )
                        , fieldLists
                );
            }

            // Kontroll 08 Avsluttede meklinger, ikke overholdt tidsfrist
            {
                final var fieldLists = List.of(List.of("FORHOLD_TOT", "FORHOLD_MEKLER", "FORHOLD_KLIENT"));

                ControlFelt1LikSumAvListe.doControl(
                        currentRecord
                        , errorReport
                        , new ErrorReportEntry(
                                "FILUTTREKK"
                                , createLinenumber(l, currentRecord.getLine())
                                , String.valueOf(currentRecord.getLine())
                                , " "
                                , "Kontroll 08 Avsluttede meklinger, ikke overholdt tidsfrist"
                                , "Tallene summerer seg ikke som de skal."
                                , Constants.NORMAL_ERROR
                        )
                        , fieldLists
                );
            }

            // Kontroll 09 Avsluttede meklinger etter varighet
            {
                final var measure = "VARIGHET";
                final var fieldLists = ControlFelt1LikSumAvListe.createFieldList(measure, clTSSSTF, clT123);

                ControlFelt1LikSumAvListe.doControl(
                        currentRecord
                        , errorReport
                        , new ErrorReportEntry(
                                "FILUTTREKK"
                                , createLinenumber(l, currentRecord.getLine())
                                , String.valueOf(currentRecord.getLine())
                                , " "
                                , "Kontroll 09 Avsluttede meklinger etter varighet"
                                , "Tallene summerer seg ikke som de skal."
                                , Constants.NORMAL_ERROR
                        )
                        , fieldLists
                );
            }

            // Kontroll 10 Avsluttede meklinger hvor barn har deltatt
            {
                final var measure = "BARNDELT";
                final var fieldLists = ControlFelt1LikSumAvListe.createFieldList(measure, clTSSSTF, "TOT");

                ControlFelt1LikSumAvListe.doControl(
                        currentRecord
                        , errorReport
                        , new ErrorReportEntry(
                                "FILUTTREKK"
                                , createLinenumber(l, currentRecord.getLine())
                                , String.valueOf(currentRecord.getLine())
                                , " "
                                , "Kontroll 10 Avsluttede meklinger hvor barn har deltatt"
                                , "Tallene summerer seg ikke som de skal."
                                , Constants.NORMAL_ERROR
                        )
                        , fieldLists
                );
            }

            // Kontroll 11 Resultat av avsluttede meklinger
            {
                final var measure = "RESULT";
                final var fieldLists = ControlFelt1LikSumAvListe.createFieldList(measure, clTSSSTF, clT123);

                ControlFelt1LikSumAvListe.doControl(
                        currentRecord
                        , errorReport
                        , new ErrorReportEntry(
                                "FILUTTREKK"
                                , createLinenumber(l, currentRecord.getLine())
                                , String.valueOf(currentRecord.getLine())
                                , " "
                                , "Kontroll 11 Resultat av avsluttede meklinger"
                                , "Tallene summerer seg ikke som de skal."
                                , Constants.NORMAL_ERROR
                        )
                        , fieldLists
                );
            }

            // Kontroll 12 Antall avsluttede meklinger, skriftlig avtale etter resultat
            {
                final var measure = "AVTALE";
                final var fieldLists = ControlFelt1LikSumAvListe.createFieldList(measure, clTSSSTF, clT123);

                ControlFelt1LikSumAvListe.doControl(
                        currentRecord
                        , errorReport
                        , new ErrorReportEntry(
                                "FILUTTREKK"
                                , createLinenumber(l, currentRecord.getLine())
                                , String.valueOf(currentRecord.getLine())
                                , " "
                                , "Kontroll 12 Antall avsluttede meklinger, skriftlig avtale etter resultat"
                                , "Tallene summerer seg ikke som de skal."
                                , Constants.NORMAL_ERROR
                        )
                        , fieldLists
                );
            }

            // Kontroll 13 Avsluttede meklinger og bekymringsmeldinger
            {
                final var measure = "BEKYMR";
                final var fieldLists = ControlFelt1LikSumAvListe.createFieldList(measure, clTSSSTF, clT12);

                ControlFelt1LikSumAvListe.doControl(
                        currentRecord
                        , errorReport
                        , new ErrorReportEntry(
                                "FILUTTREKK"
                                , createLinenumber(l, currentRecord.getLine())
                                , String.valueOf(currentRecord.getLine())
                                , " "
                                , "Kontroll 13 Avsluttede meklinger og bekymringsmeldinger"
                                , "Tallene summerer seg ikke som de skal."
                                , Constants.NORMAL_ERROR
                        )
                        , fieldLists
                );
            }

            // Kontroll 14 Kontroll av totalsummer for meklinger
            {
                final var fieldLists = List.of("MEKLING_TOT_ALLE", "ENBEGGE_TOT", "VENTETID_TOT_TOT", "VARIGHET_TOT_TOT", "RESULT_TOT_TOT", "BEKYMR_TOT_TOT");

                ControlAlleFeltIListeHarLikSum.doControl(
                        currentRecord
                        , errorReport
                        , new ErrorReportEntry(
                                "FILUTTREKK"
                                , createLinenumber(l, currentRecord.getLine())
                                , String.valueOf(currentRecord.getLine())
                                , " "
                                , "Kontroll 14 Kontroll av totalsummer for meklinger"
                                , "Totalene summerer seg ikke som de skal."
                                , Constants.NORMAL_ERROR
                        )
                        , fieldLists
                );
            }

            // Kontroll 15 Kontroll av totalsummer for skriftlige avtaler
            {
                final var fieldLists = List.of("RESULT_TOT_1", "AVTALE_TOT_TOT");

                ControlAlleFeltIListeHarLikSum.doControl(
                        currentRecord
                        , errorReport
                        , new ErrorReportEntry(
                                "FILUTTREKK"
                                , createLinenumber(l, currentRecord.getLine())
                                , String.valueOf(currentRecord.getLine())
                                , " "
                                , "Kontroll 15 Kontroll av totalsummer for skriftlige avtaler"
                                , "Totalene summerer seg ikke som de skal."
                                , Constants.NORMAL_ERROR
                        )
                        , fieldLists
                );
            }

            // Kontroll 16 Avsluttede meklinger uten oppmøte
            {
                final var measure = "UTEN_OPPM";
                final var fieldLists = ControlFelt1LikSumAvListe.createFieldList(measure, clT12345);

                ControlFelt1LikSumAvListe.doControl(
                        currentRecord
                        , errorReport
                        , new ErrorReportEntry(
                                "FILUTTREKK"
                                , createLinenumber(l, currentRecord.getLine())
                                , String.valueOf(currentRecord.getLine())
                                , " "
                                , "Kontroll 16 Avsluttede meklinger uten oppmøte"
                                , "Tallene summerer seg ikke som de skal."
                                , Constants.NORMAL_ERROR
                        )
                        , fieldLists
                );
            }
        });
        return errorReport;
    }
}
