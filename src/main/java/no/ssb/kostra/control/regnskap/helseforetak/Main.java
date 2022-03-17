package no.ssb.kostra.control.regnskap.helseforetak;

import no.ssb.kostra.control.felles.*;
import no.ssb.kostra.control.regnskap.FieldDefinitions;
import no.ssb.kostra.control.felles.ControlIntegritet;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.*;
import no.ssb.kostra.utils.Format;

import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class Main {
    private static String createLinenumber(final Integer l, final int line, final String record) {
        return "Linje " + Format.sprintf("%0" + l + "d", line) + " : <pre>" + record + "</pre>";
    }

    public static ErrorReport doControls(final Arguments args) {
        final var errorReport = new ErrorReport(args);
        final var list1 = args.getInputContentAsStringList();

        // alle records må være med korrekt lengde, ellers vil de andre kontrollene kunne feile
        // Kontroll Recordlengde
        final var hasErrors = ControlRecordLengde.doControl(list1, errorReport, FieldDefinitions.getFieldLength());

        if (hasErrors) {
            return errorReport;
        }

        final var fieldDefinitions = Utils.mergeFieldDefinitionsAndArguments(FieldDefinitions.getFieldDefinitions(), args);
        final var regnskap = Utils.addLineNumbering(Utils.getValidRecords(list1, fieldDefinitions));
        final var bevilgningRegnskapList = List.of("0X");
        final var balanseRegnskapList = List.of("0Y");
        final var saksbehandler = "Filuttrekk";
        final var n = regnskap.size();
        final var l = String.valueOf(n).length();

        // filbeskrivelsesskontroller
        ControlFilbeskrivelse.doControl(regnskap, errorReport);

        if (errorReport.getErrorType() == Constants.CRITICAL_ERROR) {
            return errorReport;
        }

        // integritetskontroller
        ControlIntegritet.doControl(regnskap, errorReport, args, bevilgningRegnskapList, balanseRegnskapList
                , List.of(" ")
                , Definitions.getFunksjonKapittelAsList(args.getSkjema())
                , Definitions.getArtSektorAsList(args.getSkjema())
        );

        // Dublett kontroll
        if (Comparator.isCodeInCodeList(args.getSkjema(), bevilgningRegnskapList)) {
            ControlDubletter.doControl(regnskap, errorReport, List.of("foretaksnr", "funksjon_kapittel", "art_sektor"), List.of("foretaksnr", "funksjon", "kontokode"));
        } else if (Comparator.isCodeInCodeList(args.getSkjema(), balanseRegnskapList)) {
            ControlDubletter.doControl(regnskap, errorReport, List.of("art_sektor"), List.of("sektor"));
        }

        // Kombinasjonskontroller
        regnskap.forEach(record -> {
            if (Comparator.isCodeInCodeList(args.getSkjema(), bevilgningRegnskapList)) {
                // Kontroll Funksjon 400
                ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                        errorReport
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, record.getLine(), record.getRecord()), " ", " "
                                , "Kontroll Funksjon 400"
                                , "Ugyldig funksjon. Funksjonen '" + record.getFieldAsTrimmedString("funksjon_kapittel") + "') kan kun benyttes av RHF og Nasjonale felleseide HF. Korriger funksjon."
                                , Constants.NORMAL_ERROR
                        )
                        , record.getFieldAsString("funksjon_kapittel")
                        , List.of("400 ")
                        , record.getFieldAsString("orgnr")
                        , Definitions.getFunksjon400Orgnr()
                );

                // Kontroll Kontokode 320
                ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                        errorReport
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, record.getLine(), record.getRecord()), " ", " "
                                , "Kontroll Kontokode 320"
                                , "Ugyldig funksjon. Kontokode 320 ISF inntekter kan kun benyttes av somatisk, psykisk helsevern og rus. Korriger funksjon."
                                , Constants.NORMAL_ERROR
                        )
                        , record.getFieldAsString("art_sektor")
                        , List.of("320")
                        , record.getFieldAsString("funksjon_kapittel")
                        , Definitions.getKontokode320Funksjoner());
            }

            if (Comparator.isCodeInCodeList(args.getSkjema(), balanseRegnskapList)) {
                // Kontroll Konti 190, 192, 194, 195 inneholder kun positive beløp
                ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(
                        errorReport
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, record.getLine(), record.getRecord()), " ", " "
                                , "Kontroll Konti 190, 192, 194, 195 inneholder kun positive beløp"
                                , "Kun positive beløp er gyldig. Fant ugyldig beløp (" + record.getFieldAsTrimmedString("belop") + ")"
                                , Constants.NORMAL_ERROR
                        )
                        , record.getFieldAsString("art_sektor")
                        , Definitions.getKontokodePositiveTall()
                        , record.getFieldAsIntegerDefaultEquals0("belop")
                        , ">"
                        , 0);
            }
        });

        // Summeringskontroller
        // Kontroll Sum inntekter og kostnader = 0
        if (Comparator.isCodeInCodeList(args.getSkjema(), bevilgningRegnskapList)) {
            final var differanse = regnskap.stream()
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            if (!Comparator.between(differanse, -100, 100)) {
                errorReport.addEntry(new ErrorReportEntry(
                        " ", " ", " ", " "
                        , "Kontroll Sum inntekter og kostnader = 0"
                        , "Sjekk at sum art 300 til og med art 899 skal være 0, her (" + differanse + "). Differanse +/- 100' kroner godtas."
                        , Constants.NORMAL_ERROR
                ));
            }
        }

        // Kontroll Eiendeler = egenkapital + gjeld
        if (Comparator.isCodeInCodeList(args.getSkjema(), balanseRegnskapList)) {
            // 1) Balanse må ha føring på eiendelskontiene , dvs. være høyere enn 0
            final var sumEiendeler = regnskap.stream()
                    .filter(p -> Comparator.between(p.getFieldAsIntegerDefaultEquals0("art_sektor"), 100, 195))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            // 2) Balanse må ha føring på egenkapitalskontoer og/eller gjeldskontoer, dvs. være mindre enn 0
            final var sumEgenkapital = regnskap.stream()
                    .filter(p -> Comparator.between(p.getFieldAsIntegerDefaultEquals0("art_sektor"), 200, 209))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            final var sumGjeld = regnskap.stream()
                    .filter(p -> Comparator.between(p.getFieldAsIntegerDefaultEquals0("art_sektor"), 210, 299))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            // 3) sumEiendeler skal være lik Egenkapital + Gjeld. Differanser opptil +50' godtas, og skal ikke utlistes.
            final var sumBalanse = sumEiendeler + (sumEgenkapital + sumGjeld);

            if (!Comparator.between(sumBalanse, -50, 50)) {
                errorReport.addEntry(new ErrorReportEntry(
                        " ", " ", " ", " "
                        , "Kontroll Eiendeler = egenkapital + gjeld"
                        , "Balansen (" + sumBalanse + ") skal balansere ved at sum eiendeler (" + sumEiendeler + ")  = sum egenkapital (" + sumEgenkapital + ") + sum gjeld (" + sumGjeld + ") . Differanser +/- 50' kroner godtas"
                        , Constants.NORMAL_ERROR
                ));
            }
        }
        return errorReport;
    }
}
