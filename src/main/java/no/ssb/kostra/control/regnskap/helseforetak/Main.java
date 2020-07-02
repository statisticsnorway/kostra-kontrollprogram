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
    private static String createLinenumber(Integer l, int line, String record) {
        return "Linje " + Format.sprintf("%0" + l + "d", line) + " : <pre>" + record + "</pre>";
    }

    public static ErrorReport doControls(Arguments args) {
        ErrorReport er = new ErrorReport(args);
        List<String> list1 = args.getInputContentAsStringList();
        List<String> list2 = list1.stream()
                // filtrerer ut blanke linjer
                .filter(l -> l.trim().length() != 0)
                .collect(Collectors.toList());


        // alle records må være med korrekt lengde, ellers vil de andre kontrollene kunne feile
        // Kontroll Recordlengde
        boolean hasErrors = ControlRecordLengde.doControl(list2, er, FieldDefinitions.getFieldLength());

        if (hasErrors) {
            return er;
        }

        List<FieldDefinition> fieldDefinitions = FieldDefinitions.getFieldDefinitions();
        List<FieldDefinition> fieldDefinitionsWithCLIParametres = fieldDefinitions.stream()
                .map(d -> {
                    if (d.getName().equalsIgnoreCase("skjema")){
                        d.setCodeList(List.of(new Code(args.getSkjema(), "Skjematype")));
                    }

                    return d;
                })
                .map(d -> {
                    if (d.getName().equalsIgnoreCase("aargang")){
                        d.setCodeList(List.of(new Code(args.getAargang(), "Årgang")));
                    }

                    return d;
                })
                .map(d -> {
                    if (d.getName().equalsIgnoreCase("region")){
                        d.setCodeList(List.of(new Code(args.getRegion(), "Region")));
                    }

                    return d;
                })
                .map(d -> {
                    if (d.getName().equalsIgnoreCase("orgnr")){
                        List<Code> list = Stream.of(args.getOrgnr().split(","))
                                .collect(Collectors.toList())
                                .stream()
                                .map(n -> new Code(n, "Organisasjonsnummer"))
                                .collect(Collectors.toList());

                        d.setCodeList(list);
                    }

                    return d;
                })
                .map(d -> {
                    if (d.getName().equalsIgnoreCase("foretaksnr")){
                        List<Code> list = Stream.of(args.getForetaknr().concat(",").concat(args.getOrgnr()).split(","))
                                .collect(Collectors.toList())
                                .stream()
                                .map(n -> new Code(n, "Foretaksnummer"))
                                .collect(Collectors.toList());

                        d.setCodeList(list);
                    }

                    return d;
                })
                .collect(Collectors.toList());



        List<String> bevilgningRegnskapList = List.of("0X");
        List<String> balanseRegnskapList = List.of("0Y");
        List<Record> regnskap = list2.stream()
                .map(p -> new Record(p, fieldDefinitionsWithCLIParametres))
                .collect(Collectors.toList());
        String saksbehandler = "Filuttrekk";
        Integer n = regnskap.size();
        Integer l = String.valueOf(n).length();

        // filbeskrivelsesskontroller
        ControlFilbeskrivelse.doControl(regnskap, er);

        if (er.getErrorType() == Constants.CRITICAL_ERROR) {
            return er;
        }

        // Sjekk skjematype, hvis den er feil så er det ikke noe vits å fortsette
        regnskap.forEach(p ->
                ControlFelt1InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                                , "Kontroll Regnskapstype"
                                , "Korreksjon: Rett opp til rett filuttrekk (" + args.getSkjema() + ")"
                                , Constants.CRITICAL_ERROR
                        )
                        , "skjema"
                        , Collections.singletonList(args.getSkjema())
                ));

        if (er.getErrorType() == Constants.CRITICAL_ERROR) {
            return er;
        }


        // integritetskontroller
        regnskap.forEach(p -> {
            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    p
                    , er
                    , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                            , "Kontroll Årgang"
                            , "Korreksjon: Rett opp til rett årgang (" + args.getAargang() + ")"
                            , Constants.CRITICAL_ERROR
                    )
                    , "aargang"
                    , Collections.singletonList(args.getAargang())
            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    p
                    , er
                    , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                            , "Kontroll Kvartal"
                            , "Rett opp i fila slik at posisjon 7 er blank"
                            , Constants.CRITICAL_ERROR
                    )
                    , "kvartal"
                    , List.of(" ")
            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    p
                    , er
                    , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                            , "Kontroll Region"
                            , "Korreksjon: Rett regionskode"
                            , Constants.CRITICAL_ERROR
                    )
                    , "region"
                    , Collections.singletonList(args.getRegion())
            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    p
                    , er
                    , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                            , "Kontroll Organisasjonsnummer."
                            , "Korreksjon: Korrigér organisasjonsnummer i filutrekket. (" + args.getOrgnr() + ")"
                            , Constants.CRITICAL_ERROR
                    )
                    , "orgnr"
                    , Stream.of(args.getOrgnr().split(",")).collect(Collectors.toList())
            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    p
                    , er
                    , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                            , "Kontroll Foretaksnummer."
                            , "Korreksjon: Foretaksnummer ('" + p.getFieldAsString("foretaksnr") + "') er forskjellig fra forventet foretaksnummer ('" + args.getForetaknr() + "')"
                            , Constants.CRITICAL_ERROR
                    )
                    , "foretaksnr"
                    , Stream.of(args.getForetaknr().concat(",").concat(args.getOrgnr()).split(",")).collect(Collectors.toList())
            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    p
                    , er
                    , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                            , "Kontroll Kontoklasse."
                            , "Posisjon 32 skal rapporteres som en blank posisjon"
                            , Constants.CRITICAL_ERROR
                    )
                    , "kontoklasse"
                    , List.of(" ")
            );

            if (bevilgningRegnskapList.contains(args.getSkjema())) {
                ControlFelt1InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                                , "Kontroll Funksjon, resultatregnskap"
                                , "Korreksjon: Rett opp feil funksjon ('" + p.getFieldAsTrimmedString("funksjon_kapittel") + "') med riktig funksjon i henhold til liste"
                                , Constants.CRITICAL_ERROR
                        )
                        , "funksjon_kapittel"
                        , Definitions.getFunksjonAsList(p.getFieldAsString("skjema"))
                );

                ControlFelt1InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                                , "Kontroll Kontokoder, resultatregnskap"
                                , "Korreksjon: Rett opp feil kontokode ('" + p.getFieldAsTrimmedString("art_sektor") + "') med riktig kontokode i henhold til liste"
                                , Constants.CRITICAL_ERROR
                        )
                        , "art_sektor"
                        , Definitions.getKontokodeAsList(p.getFieldAsString("skjema"))
                );
            }

            if (balanseRegnskapList.contains(args.getSkjema())) {
                ControlFelt1InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                                , "Kontroll Funksjon, balanseregnskap"
                                , "Posisjoner for funksjonskode ('" + p.getFieldAsTrimmedString("funksjon_kapittel") + "') skal rapporteres blankt for balanseregnskapet"
                                , Constants.CRITICAL_ERROR
                        )
                        , "funksjon_kapittel"
                        , Definitions.getFunksjonAsList(p.getFieldAsString("skjema"))
                );

                ControlFelt1InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                                , "Kontroll Kontokoder, balanseregnskap"
                                , "Korreksjon: Rett opp feil kontokode ('" + p.getFieldAsTrimmedString("art_sektor") + "') med riktig kontokode i henhold til liste"
                                , Constants.CRITICAL_ERROR
                        )
                        , "art_sektor"
                        , Definitions.getKontokodeAsList(p.getFieldAsString("skjema"))
                );
            }

            ControlHeltall.doControl(
                    p
                    , er
                    , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                            , "Kontroll beløp."
                            , "Korreksjon: Rett opp feil beløp('" + p.getFieldAsTrimmedString("belop") + "') til en gyldig heltall"
                            , Constants.CRITICAL_ERROR
                    )
                    , "belop"
            );

        });

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

        regnskap.forEach(p -> {
            if (bevilgningRegnskapList.contains(args.getSkjema())) {
                // Kontroll Funksjon 400
                ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                                , "Kontroll Funksjon 400"
                                , "Ugyldig funksjon. Funksjonen '" + p.getFieldAsTrimmedString("funksjon_kapittel") + "') kan kun benyttes av RHF og Nasjonale felleseide HF. Korriger funksjon."
                                , Constants.NORMAL_ERROR
                        )
                        , "funksjon_kapittel"
                        , List.of("400")
                        , "orgnr"
                        , Definitions.getFunksjon400Orgnr()
                );

                // Kontroll Kontokode 320
                ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                                , "Kontroll Kontokode 320"
                                , "Ugyldig funksjon. Kontokode 320  – ISF inntekter kan kun benyttes av somatisk, psykisk helsevern og rus funksjon (" + String.join(", ", Definitions.getKontokode320Funksjoner()) + ")"
                                , Constants.NORMAL_ERROR
                        )
                        , "art_sektor"
                        , List.of("320")
                        , "funksjon_kapittel"
                        , Definitions.getKontokode320Funksjoner());
            }

            if (balanseRegnskapList.contains(args.getSkjema())) {
                // Kontroll Konti 190, 192, 194, 195 inneholder kun positive beløp
                ControlFelt1InneholderKodeFraKodelisteSaaFelt2Boolsk.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
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
        });


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
                        , "Sjekk at sum kontonr 300 til og med kontonr 899 skal være 0, her (" + differanse + "). Differanse +/- 100' kroner godtas."
                        , Constants.CRITICAL_ERROR
                ));
            }
        }

        // Kontroll Eiendeler = egenkapital + gjeld
        if (balanseRegnskapList.contains(args.getSkjema())) {
            // 1) Balanse må ha føring på aktiva / eiendelskontiene , dvs. være høyere enn 0
            int sumEiendeler = regnskap.stream()
                    .filter(p -> Between.betweenInclusive(p.getFieldAsInteger("art_sektor"), 100, 195))
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            // 2) Balanse må ha føring på passiva / egenkapitalskontoer + gjeldskontoer, dvs. være mindre enn 0
            int sumEgenkapital = regnskap.stream()
                    .filter(p -> Between.betweenInclusive(p.getFieldAsInteger("art_sektor"), 200, 209))
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            int sumGjeld = regnskap.stream()
                    .filter(p -> Between.betweenInclusive(p.getFieldAsInteger("art_sektor"), 210, 299))
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);


            // 3) sumEiendeler skal være lik Egenkapital + Gjeld. Differanser opptil +10' godtas, og skal ikke utlistes.
            int sumBalanse = sumEiendeler + (sumEgenkapital + sumGjeld);

            if (!Between.betweenInclusive(sumBalanse, -50, 50)) {
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
