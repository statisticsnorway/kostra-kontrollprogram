package no.ssb.kostra.control.regnskap.helseforetak;

import no.ssb.kostra.control.felles.Comparator;
import no.ssb.kostra.control.felles.ControlDubletter;
import no.ssb.kostra.control.felles.ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk;
import no.ssb.kostra.control.felles.ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste;
import no.ssb.kostra.control.felles.ControlFilbeskrivelse;
import no.ssb.kostra.control.felles.ControlRecordLengde;
import no.ssb.kostra.control.felles.Utils;
import no.ssb.kostra.control.regnskap.FieldDefinitions;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.utils.Format;

import java.util.List;

import static no.ssb.kostra.control.felles.Comparator.isCodeInCodeList;
import static no.ssb.kostra.control.felles.ControlIntegritet.*;

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
        controlSkjema(errorReport, regnskap);
        controlAargang(errorReport, regnskap);
        controlKvartal(errorReport, regnskap);
        controlRegion(errorReport, regnskap);
        controlOrganisasjonsnummer(errorReport, regnskap);
        controlForetaksnummer(errorReport, regnskap);
        controlKontoklasse(errorReport, regnskap, List.of(" "));

        if (isCodeInCodeList(args.getSkjema(), bevilgningRegnskapList)) {
            controlFunksjon(errorReport, regnskap, Definitions.getFunksjonKapittelAsList(args.getSkjema()));
            controlArt(errorReport, regnskap, Definitions.getArtSektorAsList(args.getSkjema()));
        }

        if (isCodeInCodeList(args.getSkjema(), balanseRegnskapList)) {
            controlKapittel(errorReport, regnskap, Definitions.getFunksjonKapittelAsList(args.getSkjema()));
            controlSektor(errorReport, regnskap, balanseRegnskapList);
        }

        controlBelop(errorReport, regnskap);
        controlUgyldigeBelop(errorReport, regnskap);


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
