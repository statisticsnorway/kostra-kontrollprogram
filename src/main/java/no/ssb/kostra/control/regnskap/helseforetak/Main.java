package no.ssb.kostra.control.regnskap.helseforetak;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.felles.*;
import no.ssb.kostra.control.regnskap.FieldDefinitions;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.utils.Between;
import no.ssb.kostra.utils.Format;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static ErrorReport doControls(Arguments args) {
        ErrorReport er = new ErrorReport(args);
        List<String> list1 = args.getInputContentAsStringList();
        List<FieldDefinition> fieldDefinitions = FieldDefinitions.getFieldDefinitions();
        List<String> bevilgningRegnskapList = List.of("0X");
        List<String> balanseRegnskapList = List.of("0Y");
        List<Record> regnskap = list1.stream()
                .map(p -> new Record(p, fieldDefinitions))
                .collect(Collectors.toList());
        String saksbehandler = "Filuttrekk";
        String journalnummer = "Linje ";
        Integer n = regnskap.size();
        Integer l = String.valueOf(n).length();


        // alle records må være med korrekt lengde, ellers vil de andre kontrollene kunne feile
        // Kontroll Recordlengde
        boolean hasErrors = ControlRecordLengde.doControl(regnskap.stream(), er, FieldDefinitions.getFieldLength());

        if (hasErrors) {
            return er;
        }

        // integritetskontroller
        regnskap.stream()
                .peek(p -> ControlFelt1InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                , "Kontroll Regnskapstype"
                                , "Korreksjon: Rett opp til rett filuttrekk (" + args.getSkjema() + ")"
                                , Constants.CRITICAL_ERROR
                        )
                        , "skjema"
                        , Collections.singletonList(args.getSkjema())
                ))
                .peek(p -> ControlFelt1InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                , "Kontroll Årgang"
                                , "Korreksjon: Rett opp til rett årgang (" + args.getAargang() + ")"
                                , Constants.CRITICAL_ERROR
                        )
                        , "aargang"
                        , Collections.singletonList(args.getAargang())
                ))
                .peek(p -> ControlFelt1InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                , "Kontroll Kvartal"
                                , "Rett opp i fila slik at posisjon 7 er blank"
                                , Constants.CRITICAL_ERROR
                        )
                        , "kvartal"
                        , List.of(" ")
                ))
                .peek(p -> ControlFelt1InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                , "Kontroll Region"
                                , "Korreksjon: Rett regionskode"
                                , Constants.CRITICAL_ERROR
                        )
                        , "region"
                        , Collections.singletonList(args.getRegion())
                ))
                .peek(p -> ControlFelt1InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                , "Kontroll Organisasjonsnummer."
                                , "Korreksjon: Korrigér organisasjonsnummer i filutrekket. (" + args.getOrgnr() + ")"
                                , Constants.CRITICAL_ERROR
                        )
                        , "orgnr"
                        , Collections.singletonList(args.getOrgnr())
                ))
                .peek(p -> ControlFelt1InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                , "Kontroll Foretaksnummer."
                                , "Korreksjon: Foretaksnummer ('" + p.getFieldAsString("foretaksnr") + "') er forskjellig fra forventet foretaksnummer ('" + args.getForetaknr() + "')"
                                , Constants.CRITICAL_ERROR
                        )
                        , "foretaksnr"
                        , Stream.of(args.getForetaknr().concat(",").concat(args.getOrgnr()).split(",")).collect(Collectors.toList())
                ))
                .peek(p -> ControlFelt1InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                , "Kontroll Kontoklasse."
                                , "Posisjon 32 skal rapporteres som en blank posisjon"
                                , Constants.CRITICAL_ERROR
                        )
                        , "kontoklasse"
                        , List.of(" ")
                ))
                .peek(p -> {
                            if (bevilgningRegnskapList.contains(args.getSkjema())) {
                                ControlFelt1InneholderKodeFraKodeliste.doControl(
                                        p
                                        , er
                                        , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                                , "Kontroll Funksjon."
                                                , "Korreksjon: Rett opp feil funksjon ('" + p.getFieldAsTrimmedString("funksjon_kapittel") + "') med riktig funksjon i henhold til liste"
                                                , Constants.CRITICAL_ERROR
                                        )
                                        , "funksjon_kapittel"
                                        , Definitions.getFunksjonAsList(p.getFieldAsString("skjema"))
                                );
                            }
                        }
                )
                .peek(p -> {
                            if (balanseRegnskapList.contains(args.getSkjema())) {
                                ControlFelt1InneholderKodeFraKodeliste.doControl(
                                        p
                                        , er
                                        , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                                , "Kontroll Funksjon."
                                                , "Posisjoner for funksjonskode ('" + p.getFieldAsTrimmedString("funksjon_kapittel") + "') skal rapporteres blankt for balanseregnskapet"
                                                , Constants.CRITICAL_ERROR
                                        )
                                        , "funksjon_kapittel"
                                        , Definitions.getKontokodeAsList(p.getFieldAsString("skjema"))
                                );
                            }
                        }
                )
                .peek(p -> {
                            if (bevilgningRegnskapList.contains(args.getSkjema())) {
                                ControlFelt1InneholderKodeFraKodeliste.doControl(
                                        p
                                        , er
                                        , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                                , "Kontroll Kontokoder"
                                                , "Korreksjon: Rett opp feil kontokode ('" + p.getFieldAsTrimmedString("art_sektor") + "') med riktig kontokode i henhold til liste"
                                                , Constants.CRITICAL_ERROR
                                        )
                                        , "art_sektor"
                                        , Definitions.getKontokodeAsList(p.getFieldAsString("skjema"))
                                );
                            }
                        }
                )
                .peek(p -> {
                            if (balanseRegnskapList.contains(args.getSkjema())) {
                                ControlFelt1InneholderKodeFraKodeliste.doControl(
                                        p
                                        , er
                                        , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                                , "Kontroll Kontokoder."
                                                , "Korreksjon: Rett opp feil kontokode ('" + p.getFieldAsTrimmedString("art_sektor") + "') med riktig kontokode i henhold til liste"
                                                , Constants.CRITICAL_ERROR
                                        )
                                        , "art_sektor"
                                        , Definitions.getKontokodeAsList(p.getFieldAsString("skjema"))
                                );
                            }
                        }
                )
                .peek(p -> ControlHeltall.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                , "Kontroll beløp."
                                , "Korreksjon: Rett opp feil beløp('" + p.getFieldAsTrimmedString("belop") + "') til en gyldig heltall"
                                , Constants.CRITICAL_ERROR
                        )
                        , "belop"
                ))
                .close();

        // Kombinasjonskontroller
        // Dublett kontroll
        Stream<String> all = Stream.of();
        if (bevilgningRegnskapList.contains(args.getSkjema())) {
            all = regnskap.stream()
                    .map(p -> String.join(" * ", List.of(
                            p.getFieldAsTrimmedString("foretaksnr"),
                            p.getFieldAsTrimmedString("funksjon_kapittel"),
                            p.getFieldAsTrimmedString("art_sektor"))));
        }

        if (balanseRegnskapList.contains(args.getSkjema())) {
            all = regnskap.stream()
                    .map(p -> String.join(" * ", List.of(p.getFieldAsTrimmedString("art_sektor"))));
        }

        List<String> dubletter = all
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .filter(p -> p.getValue() > 1)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());

        if (!dubletter.isEmpty()) {
            er.addEntry(
                    new ErrorReportEntry(saksbehandler, "Dubletter", " ", " "
                            , "Kontroll Dubletter"
                            , "Dubletter summeres sammen. (Gjelder for:<br/>\n" + String.join(",<br/>\n", dubletter) + ")"
                            , Constants.NORMAL_ERROR
                    ));
        }

        regnskap.stream()
                .peek(p -> {
                    // Kontroll Funksjon 400
                    if (bevilgningRegnskapList.contains(args.getSkjema())) {
                        ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                                p
                                , er
                                , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                        , "Kontroll Funksjon 400"
                                        , "Ugyldig funksjon. Funksjonen '" + p.getFieldAsTrimmedString("funksjon_kapittel") + "') kan kun benyttes av RHF og Nasjonale felleseide HF. Korriger funksjon."
                                        , Constants.NORMAL_ERROR
                                )
                                , "funksjon_kapittel"
                                , List.of("400")
                                , "orgnr"
                                , Definitions.getFunksjon400Orgnr());
                    }
                })
                .peek(p -> {
                    // Kontroll Kontokode 320
                    if (bevilgningRegnskapList.contains(args.getSkjema())) {
                        ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                                p
                                , er
                                , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                        , "Kontroll Funksjon 400"
                                        , "Ugyldig funksjon. Kontokode 320  – ISF inntekter kan kun benyttes av somatisk, psykisk helsevern og rus funksjon (" + String.join(", ", Definitions.getKontokode320Funksjoner()) + ")"
                                        , Constants.NORMAL_ERROR
                                )
                                , "art_sektor"
                                , List.of("320")
                                , "funksjon_kapittel"
                                , Definitions.getKontokode320Funksjoner());
                    }
                })
                .peek(p -> {
                    // Kontroll Konti 190, 192, 194, 195 inneholder kun positive beløp
                    if (balanseRegnskapList.contains(args.getSkjema())) {
                        ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(
                                p
                                , er
                                , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                        , "Kontroll Konti 190, 192, 194, 195 inneholder kun positive beløp"
                                        , "Kun positive beløp er gyldig"
                                        , Constants.NORMAL_ERROR
                                )
                                , "art_sektor"
                                , Definitions.getKontokodePositiveTall()
                                , "belop"
                                , ">"
                                , 0);
                    }
                })
        ;

        // Summeringskontroller
        // Kontroll Sum inntekter og kostnader = 0
        if (bevilgningRegnskapList.contains(args.getSkjema())) {
            int differanse = regnskap.stream()
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            if (!Between.betweenInclusive(differanse, -100, 100)) {
                er.addEntry(new ErrorReportEntry(
                        " ", " ", " ", " "
                        , "Kontroll Sum inntekter og kostnader = 0"
                        , "Sjekk at sum kontonr 300 til og med kontonr 899 skal være 0, her (" + differanse + "). Differanse +/- 100 000 kroner godtas."
                        , Constants.CRITICAL_ERROR
                ));
            }
        }

        // Kontroll Eiendeler = egenkapital + gjeld
        if (balanseRegnskapList.contains(args.getSkjema())) {
            // 1) Balanse må ha føring på aktiva / eiendelskontiene , dvs. være høyere enn 0
            int sumAktiva = regnskap.stream()
                    .filter(p -> Between.betweenInclusive(p.getFieldAsInteger("art_sektor"), 100, 195))
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            // 2) Balanse må ha føring på passiva / egenkapitalskontoer + gjeldskontoer, dvs. være mindre enn 0
            int sumPassiva = regnskap.stream()
                    .filter(p -> Between.betweenInclusive(p.getFieldAsInteger("art_sektor"), 200, 299))
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            // 3) Aktiva skal være lik passiva. Differanser opptil +10' godtas, og skal ikke utlistes.
            int sumBalanse = sumAktiva + sumPassiva;

            if (!Between.betweenInclusive(sumBalanse, -50, 50)) {
                er.addEntry(new ErrorReportEntry(
                        " ", " ", " ", " "
                        , "Kontroll Eiendeler = egenkapital + gjeld"
                        , "Balansen (" + sumBalanse + ") skal balansere ved at sum eiendeler (" + sumAktiva + ")  = sum egenkapital + sum gjeld (" + sumPassiva + ") . Differanser +/- 50 000 kroner godtas"
                        , Constants.NORMAL_ERROR
                ));
            }

        }

        return er;
    }
}
