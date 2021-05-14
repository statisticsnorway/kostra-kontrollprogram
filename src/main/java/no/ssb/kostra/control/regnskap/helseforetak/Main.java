package no.ssb.kostra.control.regnskap.helseforetak;

import no.ssb.kostra.control.felles.*;
import no.ssb.kostra.control.regnskap.FieldDefinitions;
import no.ssb.kostra.control.regnskap.felles.ControlIntegritet;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.*;
import no.ssb.kostra.utils.Format;

import java.util.List;

public class Main {
    private static String createLinenumber(Integer l, int line, String record) {
        return "Linje " + Format.sprintf("%0" + l + "d", line) + " : <pre>" + record + "</pre>";
    }

    public static ErrorReport doControls(Arguments args) {
        ErrorReport er = new ErrorReport(args);
        List<String> list1 = args.getInputContentAsStringList();

        // alle records må være med korrekt lengde, ellers vil de andre kontrollene kunne feile
        // Kontroll Recordlengde
        boolean hasErrors = ControlRecordLengde.doControl(list1, er, FieldDefinitions.getFieldLength());

        if (hasErrors) {
            return er;
        }

        List<FieldDefinition> fieldDefinitions = Utils.mergeFieldDefinitionsAndArguments(FieldDefinitions.getFieldDefinitions(), args);
        List<Record> regnskap = Utils.getValidRecords(list1, fieldDefinitions);
        List<String> bevilgningRegnskapList = List.of("0X");
        List<String> balanseRegnskapList = List.of("0Y");
        String saksbehandler = "Filuttrekk";
        Integer n = regnskap.size();
        Integer l = String.valueOf(n).length();

        // filbeskrivelsesskontroller
        ControlFilbeskrivelse.doControl(regnskap, er);

        if (er.getErrorType() == Constants.CRITICAL_ERROR) {
            return er;
        }

        // integritetskontroller
        ControlIntegritet.doControl(regnskap, er, l, args, bevilgningRegnskapList, balanseRegnskapList
                , List.of(" ")
                , Definitions.getFunksjonKapittelAsList(args.getSkjema())
                , Definitions.getArtSektorAsList(args.getSkjema())
        );



        // Dublett kontroll
        if (Comparator.isCodeInCodelist(args.getSkjema(), bevilgningRegnskapList)) {
            ControlDubletter.doControl(regnskap, er, List.of("foretaksnr", "funksjon_kapittel", "art_sektor"), List.of("foretaksnr", "funksjon", "kontokode"));

        } else if (Comparator.isCodeInCodelist(args.getSkjema(), balanseRegnskapList)) {
            ControlDubletter.doControl(regnskap, er, List.of("art_sektor"), List.of("sektor"));
        }

        // Kombinasjonskontroller
        regnskap.forEach(p -> {
            if (Comparator.isCodeInCodelist(args.getSkjema(), bevilgningRegnskapList)) {
                // Kontroll Funksjon 400
                ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                        er
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                                , "Kontroll Funksjon 400"
                                , "Ugyldig funksjon. Funksjonen '" + p.getFieldAsTrimmedString("funksjon_kapittel") + "') kan kun benyttes av RHF og Nasjonale felleseide HF. Korriger funksjon."
                                , Constants.NORMAL_ERROR
                        )
                        , p.getFieldAsString("funksjon_kapittel")
                        , List.of("400 ")
                        , p.getFieldAsString("orgnr")
                        , Definitions.getFunksjon400Orgnr()
                );

                // Kontroll Kontokode 320
                ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                        er
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                                , "Kontroll Kontokode 320"
                                , "Ugyldig funksjon. Kontokode 320 ISF inntekter kan kun benyttes av somatisk, psykisk helsevern og rus. Korriger funksjon."
                                , Constants.NORMAL_ERROR
                        )
                        , p.getFieldAsString("art_sektor")
                        , List.of("320")
                        , p.getFieldAsString("funksjon_kapittel")
                        , Definitions.getKontokode320Funksjoner());
            }

            if (Comparator.isCodeInCodelist(args.getSkjema(), balanseRegnskapList)) {
                // Kontroll Konti 190, 192, 194, 195 inneholder kun positive beløp
                ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(
                        er
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                                , "Kontroll Konti 190, 192, 194, 195 inneholder kun positive beløp"
                                , "Kun positive beløp er gyldig. Fant ugyldig beløp (" + p.getFieldAsTrimmedString("belop") + ")"
                                , Constants.NORMAL_ERROR
                        )
                        , p.getFieldAsString("art_sektor")
                        , Definitions.getKontokodePositiveTall()
                        , p.getFieldAsIntegerDefaultEquals0("belop")
                        , ">"
                        , 0);
            }
        });


        // Summeringskontroller
        // Kontroll Sum inntekter og kostnader = 0
        if (Comparator.isCodeInCodelist(args.getSkjema(), bevilgningRegnskapList)) {
            int differanse = regnskap.stream()
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            if (!Comparator.between(differanse, -100, 100)) {
                er.addEntry(new ErrorReportEntry(
                        " ", " ", " ", " "
                        , "Kontroll Sum inntekter og kostnader = 0"
                        , "Sjekk at sum art 300 til og med art 899 skal være 0, her (" + differanse + "). Differanse +/- 100' kroner godtas."
                        , Constants.NORMAL_ERROR
                ));
            }
        }

        // Kontroll Eiendeler = egenkapital + gjeld
        if (Comparator.isCodeInCodelist(args.getSkjema(), balanseRegnskapList)) {
            // 1) Balanse må ha føring på eiendelskontiene , dvs. være høyere enn 0
            int sumEiendeler = regnskap.stream()
                    .filter(p -> Comparator.between(p.getFieldAsIntegerDefaultEquals0("art_sektor"), 100, 195))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            // 2) Balanse må ha føring på egenkapitalskontoer og/eller gjeldskontoer, dvs. være mindre enn 0
            int sumEgenkapital = regnskap.stream()
                    .filter(p -> Comparator.between(p.getFieldAsIntegerDefaultEquals0("art_sektor"), 200, 209))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            int sumGjeld = regnskap.stream()
                    .filter(p -> Comparator.between(p.getFieldAsIntegerDefaultEquals0("art_sektor"), 210, 299))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            // 3) sumEiendeler skal være lik Egenkapital + Gjeld. Differanser opptil +50' godtas, og skal ikke utlistes.
            int sumBalanse = sumEiendeler + (sumEgenkapital + sumGjeld);

            if (!Comparator.between(sumBalanse, -50, 50)) {
                er.addEntry(new ErrorReportEntry(
                        " ", " ", " ", " "
                        , "Kontroll Eiendeler = egenkapital + gjeld"
                        , "Balansen (" + sumBalanse + ") skal balansere ved at sum eiendeler (" + sumEiendeler + ")  = sum egenkapital (" + sumEgenkapital + ") + sum gjeld (" + sumGjeld + ") . Differanser +/- 50' kroner godtas"
                        , Constants.NORMAL_ERROR
                ));
            }

        }

        return er;
    }
}
