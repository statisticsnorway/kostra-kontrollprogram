package no.ssb.kostra.control.famvern.s55;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.famvern.Definitions;
import no.ssb.kostra.control.felles.ControlAlleFeltIListeHarLikSum;
import no.ssb.kostra.control.felles.ControlFelt1LikSumAvListe;
import no.ssb.kostra.control.felles.ControlRecordLengde;
import no.ssb.kostra.controlprogram.Arguments;

import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static ErrorReport doControls(Arguments args) {
        ErrorReport er = new ErrorReport(args);
        List<String> inputFileContent = args.getInputFileContent();
        List<FieldDefinition> fieldDefinitions = FieldDefinitions.getFieldDefinitions();
        List<Record> records = inputFileContent.stream()
                .map(p -> new Record(p, fieldDefinitions))
                .collect(Collectors.toList());

        // alle records må være med korrekt lengde, ellers vil de andre kontrollene kunne feile
        // Kontroll Recordlengde
        boolean hasErrors = ControlRecordLengde.doControl(records.stream(), er, FieldDefinitions.getFieldLength());

        if (hasErrors) {
            return er;
        }

        List<String> c1 = List.of("SEP", "SAM", "SAK", "TILB", "FLY", "TOT");
        List<String> c2 = List.of("TOT", "1", "2", "3");
        List<String> c3 = List.of("TOT", "BEGGE", "EN");
        List<String> c4 = List.of("TOT", "1", "2", "3", "4");
        List<String> c5 = List.of("TOT", "1", "2");

        records.stream()
                // Kontroll 3: Fylkesnummer
                .peek(r -> {
                    if (!Definitions.isFylkeValid(r.getFieldAsString("FYLKE_NR"))) {
                        er.addEntry(
                                new ErrorReportEntry(
                                        " "
                                        , " "
                                        , String.valueOf(r.getLine())
                                        , " "
                                        , "Kontroll 3 fylkesnummer"
                                        , "Det er ikke oppgitt fylkesnummer, eller feil kode er benyttet. Feltet er obligatorisk og må fylles ut."
                                        , Constants.NORMAL_ERROR
                                )

                        );
                    }
                })
                // Kontroll 5 Avsluttede meklinger etter tidsbruk
                .peek(r -> {
                    String measure = "MEKLING";
                    List<List<String>> fieldLists = createFieldList(measure, c1, c2);

                    ControlFelt1LikSumAvListe.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    " "
                                    , " "
                                    , String.valueOf(r.getLine())
                                    , " "
                                    , "Kontroll 5 Avsluttede meklinger etter tidsbruk"
                                    , "Tallene summerer seg ikke som de skal."
                                    , Constants.NORMAL_ERROR
                            )
                            , fieldLists
                    );
                })
                // Kontroll 6 Avsluttede meklinger etter deltakere
                .peek(r -> {
                    List<List<String>> fieldLists = createFieldList(c1, c3);

                    ControlFelt1LikSumAvListe.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    " "
                                    , " "
                                    , String.valueOf(r.getLine())
                                    , " "
                                    , "Kontroll 6 Avsluttede meklinger etter deltakere"
                                    , "Tallene summerer seg ikke som de skal."
                                    , Constants.NORMAL_ERROR
                            )
                            , fieldLists
                    );
                })
                // Kontroll 7 Avsluttede meklinger etter ventetid
                .peek(r -> {
                    String measure = "VENTETID";
                    List<List<String>> fieldLists = createFieldList(measure, c1, c4);

                    ControlFelt1LikSumAvListe.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    " "
                                    , " "
                                    , String.valueOf(r.getLine())
                                    , " "
                                    , "Kontroll 7 Avsluttede meklinger etter ventetid"
                                    , "Tallene summerer seg ikke som de skal."
                                    , Constants.NORMAL_ERROR
                            )
                            , fieldLists
                    );
                })
                // Kontroll 8 Avsluttede meklinger, ikke overholdt tidsfrist
                .peek(r -> {
                    List<List<String>> fieldLists = List.of(List.of("FORHOLD_TOT", "FORHOLD_MEKLER", "FORHOLD_KLIENT"));

                    ControlFelt1LikSumAvListe.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    " "
                                    , " "
                                    , String.valueOf(r.getLine())
                                    , " "
                                    , "Kontroll 8 Avsluttede meklinger, ikke overholdt tidsfrist"
                                    , "Tallene summerer seg ikke som de skal."
                                    , Constants.NORMAL_ERROR
                            )
                            , fieldLists
                    );
                })
                // Kontroll 9 Avsluttede meklinger etter varighet
                .peek(r -> {
                    String measure = "VARIGHET";
                    List<List<String>> fieldLists = createFieldList(measure, c1, c2);

                    ControlFelt1LikSumAvListe.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    " "
                                    , " "
                                    , String.valueOf(r.getLine())
                                    , " "
                                    , "Kontroll 9 Avsluttede meklinger etter varighet"
                                    , "Tallene summerer seg ikke som de skal."
                                    , Constants.NORMAL_ERROR
                            )
                            , fieldLists
                    );
                })
                // Kontroll 10 Avsluttede meklinger hvor barn har deltatt
                .peek(r -> {
                    String measure = "BARNDELT";
                    List<List<String>> fieldLists = createFieldList(measure, c1, List.of("TOT"));

                    ControlFelt1LikSumAvListe.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    " "
                                    , " "
                                    , String.valueOf(r.getLine())
                                    , " "
                                    , "Kontroll 10 Avsluttede meklinger hvor barn har deltatt"
                                    , "Tallene summerer seg ikke som de skal."
                                    , Constants.NORMAL_ERROR
                            )
                            , fieldLists
                    );
                })
                // Kontroll 11 Resultat av avsluttede meklinger
                .peek(r -> {
                    String measure = "RESULT";
                    List<List<String>> fieldLists = createFieldList(measure, c1, c2);

                    ControlFelt1LikSumAvListe.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    " "
                                    , " "
                                    , String.valueOf(r.getLine())
                                    , " "
                                    , "Kontroll 11 Resultat av avsluttede meklinger"
                                    , "Tallene summerer seg ikke som de skal."
                                    , Constants.NORMAL_ERROR
                            )
                            , fieldLists
                    );
                })
                // Kontroll 12 Antall avsluttede meklinger, skriftlig avtale etter resultat
                .peek(r -> {
                    String measure = "AVTALE";
                    List<List<String>> fieldLists = createFieldList(measure, c1, c2);

                    ControlFelt1LikSumAvListe.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    " "
                                    , " "
                                    , String.valueOf(r.getLine())
                                    , " "
                                    , "Kontroll 12 Antall avsluttede meklinger, skriftlig avtale etter resultat"
                                    , "Tallene summerer seg ikke som de skal."
                                    , Constants.NORMAL_ERROR
                            )
                            , fieldLists
                    );
                })
                // Kontroll 13 Avsluttede meklinger og bekymringsmeldinger
                .peek(r -> {
                    String measure = "BEKYMR";
                    List<List<String>> fieldLists = createFieldList(measure, c1, c5);

                    ControlFelt1LikSumAvListe.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    " "
                                    , " "
                                    , String.valueOf(r.getLine())
                                    , " "
                                    , "Kontroll 13 Avsluttede meklinger og bekymringsmeldinger"
                                    , "Tallene summerer seg ikke som de skal."
                                    , Constants.NORMAL_ERROR
                            )
                            , fieldLists
                    );
                })
                // Kontroll 14 Kontroll av totalsummer for meklinger
                .peek(r -> {

                    List<String> fieldLists = List.of("MEKLING_TOT_TOT", "TOT_TOT", "VENTETID_TOT_TOT", "VARIGHET_TOT_TOT", "RESULT_TOT_TOT", "BEKYMR_TOT_TOT");

                    ControlAlleFeltIListeHarLikSum.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    " "
                                    , " "
                                    , String.valueOf(r.getLine())
                                    , " "
                                    , "Kontroll 14 Kontroll av totalsummer for meklinger"
                                    , "Totalene summerer seg ikke som de skal."
                                    , Constants.NORMAL_ERROR
                            )
                            , fieldLists
                    );
                })
                // Kontroll 15 Kontroll av totalsummer for skriftlige avtaler
                .peek(r -> {

                    List<String> fieldLists = List.of("RESULT_TOT_1", "AVTALE_TOT_TOT");

                    ControlAlleFeltIListeHarLikSum.doControl(
                            r
                            , er
                            , new ErrorReportEntry(
                                    " "
                                    , " "
                                    , String.valueOf(r.getLine())
                                    , " "
                                    , "Kontroll 15 Kontroll av totalsummer for skriftlige avtaler"
                                    , "Totalene summerer seg ikke som de skal."
                                    , Constants.NORMAL_ERROR
                            )
                            , fieldLists
                    );
                })
                .close();

        return er;
    }

    private static List<List<String>> createFieldList(String measure, List<String> c1, List<String> c2) {
        List<List<String>> fieldLists = c1.stream()
                .map(c1Item -> c2.stream()
                        .map(c2Item -> String.join("_", measure, c1Item, c2Item))
                        .collect(Collectors.toList())

                )
                .collect(Collectors.toList());

        fieldLists.addAll(c2.stream()
                .map(c2Item -> c1.stream()
                        .map(c1Item -> String.join("_", measure, c1Item, c2Item))
                        .collect(Collectors.toList())

                )
                .collect(Collectors.toList())
        );
        return fieldLists;
    }

    private static List<List<String>> createFieldList(List<String> c1, List<String> c2) {
        List<List<String>> fieldLists = c1.stream()
                .map(c1Item -> c2.stream()
                        .map(c2Item -> String.join("_", c1Item, c2Item))
                        .collect(Collectors.toList())

                )
                .collect(Collectors.toList());

        fieldLists.addAll(c2.stream()
                .map(c2Item -> c1.stream()
                        .map(c1Item -> String.join("_", c1Item, c2Item))
                        .collect(Collectors.toList())

                )
                .collect(Collectors.toList())
        );
        return fieldLists;
    }
}
