package no.ssb.kostra.control.famvern.s55;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.famvern.Definitions;
import no.ssb.kostra.control.felles.ControlAlleFeltIListeHarLikSum;
import no.ssb.kostra.control.felles.ControlFelt1LikSumAvListe;
import no.ssb.kostra.control.felles.ControlFilbeskrivelse;
import no.ssb.kostra.control.felles.ControlRecordLengde;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.utils.Format;

import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static String createLinenumber(Integer l, int line) {
        return "Linje " + Format.sprintf("%0" + l + "d", line);
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
        String saksbehandler = "Filuttrekk";
        Integer n = records.size();
        Integer l = String.valueOf(n).length();


        // filbeskrivelsesskontroller
        ControlFilbeskrivelse.doControl(records, er);

        if (er.getErrorType() == Constants.CRITICAL_ERROR) {
            return er;
        }

        List<String> clTSSSTF = List.of("TOT", "SEP", "SAM", "SAK", "TILB", "FLY");
        List<String> clTBE = List.of("TOT", "BEGGE", "EN");
        List<String> clT12 = List.of("TOT", "1", "2");
        List<String> clT123 = List.of("TOT", "1", "2", "3");
        List<String> clT1234 = List.of("TOT", "1", "2", "3", "4");

        records.forEach(r -> {
            // Kontroll 03: Fylkesnummer
            if (!Definitions.isFylkeValid(r.getFieldAsString("FYLKE_NR"))) {
                er.addEntry(
                        new ErrorReportEntry(
                                "FILUTTREKK"
                                , createLinenumber(l, r.getLine())
                                , String.valueOf(r.getLine())
                                , " "
                                , "Kontroll 03 fylkesnummer"
                                , "Fylkesnummeret som er oppgitt i recorden fins ikke i listen med gyldige fylkesnumre. "
                                + "Fant '" + r.getFieldAsString("FYLKE_NR") + "', forventet én av : " + Definitions.getFylkeAsList() + ". "
                                + "Feltet er obligatorisk og må fylles ut."
                                , Constants.NORMAL_ERROR
                        )

                );
            }

            // Kontroll 05 Avsluttede meklinger etter tidsbruk
            {
                String measure = "MEKLING";
                List<List<String>> fieldLists = ControlFelt1LikSumAvListe.createFieldList(measure, clTSSSTF, clT123);

                ControlFelt1LikSumAvListe.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                "FILUTTREKK"
                                , createLinenumber(l, r.getLine())
                                , String.valueOf(r.getLine())
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
                List<List<String>> fieldLists = ControlFelt1LikSumAvListe.createFieldList(clTSSSTF, clTBE);

                ControlFelt1LikSumAvListe.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                "FILUTTREKK"
                                , createLinenumber(l, r.getLine())
                                , String.valueOf(r.getLine())
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
                String measure = "VENTETID";
                List<List<String>> fieldLists = ControlFelt1LikSumAvListe.createFieldList(measure, clTSSSTF, clT1234);

                ControlFelt1LikSumAvListe.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                "FILUTTREKK"
                                , createLinenumber(l, r.getLine())
                                , String.valueOf(r.getLine())
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
                List<List<String>> fieldLists = List.of(List.of("FORHOLD_TOT", "FORHOLD_MEKLER", "FORHOLD_KLIENT"));

                ControlFelt1LikSumAvListe.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                "FILUTTREKK"
                                , createLinenumber(l, r.getLine())
                                , String.valueOf(r.getLine())
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
                String measure = "VARIGHET";
                List<List<String>> fieldLists = ControlFelt1LikSumAvListe.createFieldList(measure, clTSSSTF, clT123);

                ControlFelt1LikSumAvListe.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                "FILUTTREKK"
                                , createLinenumber(l, r.getLine())
                                , String.valueOf(r.getLine())
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
                String measure = "BARNDELT";
                List<List<String>> fieldLists = List.of(
                        clTSSSTF.stream()
                                .map(item -> measure.concat("_").concat(item).concat("_TOT"))
                                .collect(Collectors.toList()));

                ControlFelt1LikSumAvListe.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                "FILUTTREKK"
                                , createLinenumber(l, r.getLine())
                                , String.valueOf(r.getLine())
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
                String measure = "RESULT";
                List<List<String>> fieldLists = ControlFelt1LikSumAvListe.createFieldList(measure, clTSSSTF, clT123);

                ControlFelt1LikSumAvListe.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                "FILUTTREKK"
                                , createLinenumber(l, r.getLine())
                                , String.valueOf(r.getLine())
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
                String measure = "AVTALE";
                List<List<String>> fieldLists = ControlFelt1LikSumAvListe.createFieldList(measure, clTSSSTF, clT123);

                ControlFelt1LikSumAvListe.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                "FILUTTREKK"
                                , createLinenumber(l, r.getLine())
                                , String.valueOf(r.getLine())
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
                String measure = "BEKYMR";
                List<List<String>> fieldLists = ControlFelt1LikSumAvListe.createFieldList(measure, clTSSSTF, clT12);

                ControlFelt1LikSumAvListe.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                "FILUTTREKK"
                                , createLinenumber(l, r.getLine())
                                , String.valueOf(r.getLine())
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
                List<String> fieldLists = List.of("MEKLING_TOT_TOT", "TOT_TOT", "VENTETID_TOT_TOT", "VARIGHET_TOT_TOT", "RESULT_TOT_TOT", "BEKYMR_TOT_TOT");

                ControlAlleFeltIListeHarLikSum.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                "FILUTTREKK"
                                , createLinenumber(l, r.getLine())
                                , String.valueOf(r.getLine())
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

                List<String> fieldLists = List.of("RESULT_TOT_1", "AVTALE_TOT_TOT");

                ControlAlleFeltIListeHarLikSum.doControl(
                        r
                        , er
                        , new ErrorReportEntry(
                                "FILUTTREKK"
                                , createLinenumber(l, r.getLine())
                                , String.valueOf(r.getLine())
                                , " "
                                , "Kontroll 15 Kontroll av totalsummer for skriftlige avtaler"
                                , "Totalene summerer seg ikke som de skal."
                                , Constants.NORMAL_ERROR
                        )
                        , fieldLists
                );
            }
        });

        return er;
    }
}
