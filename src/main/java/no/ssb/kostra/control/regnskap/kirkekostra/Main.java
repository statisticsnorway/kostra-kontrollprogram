package no.ssb.kostra.control.regnskap.kirkekostra;

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

        List<FieldDefinition> fieldDefinitions = FieldDefinitions.getFieldDefinitions();
        List<String> bevilgningRegnskapList = List.of("0F");
        List<String> balanseRegnskapList = List.of("0G");
        List<Record> regnskap = list1.stream()
                .map(p -> new Record(p, fieldDefinitions))
                .collect(Collectors.toList());
        String saksbehandler = "Filuttrekk";
        Integer n = regnskap.size();
        Integer l = String.valueOf(n).length();

        // filbeskrivelsesskontroller
        ControlFilbeskrivelse.doControl(regnskap, er);

        if (er.getErrorType() == Constants.CRITICAL_ERROR) {
            return er;
        }

        // integritetskontroller
        regnskap.forEach(p -> {
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
            );

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
                            , "Korreksjon: Rett opp til rett kvartal (" + args.getKvartal() + ")"
                            , Constants.CRITICAL_ERROR
                    )
                    , "kvartal"
                    , Collections.singletonList(args.getKvartal())
            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    p
                    , er
                    , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                            , "Kontroll fylkeskommune-/kommune-/bydelsnummeret."
                            , "Korreksjon: Rett fylkeskommune-/kommune-/bydelsnummeret. (" + args.getRegion() + ")"
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
                    , Collections.singletonList(args.getOrgnr())
            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    p
                    , er
                    , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                            , "Kontroll Foretaksnummer."
                            , "Korreksjon: Foretaksnummer skal være 9 blanke tegn / mellomrom. (" + args.getForetaknr() + ")"
                            , Constants.CRITICAL_ERROR
                    )
                    , "foretaksnr"
                    , Collections.singletonList("         ")
            );

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    p
                    , er
                    , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                            , "Kontroll Kontoklasse."
                            , "Korreksjon: Rett kontoklassen. (" + p.getFieldAsString("kontoklasse") + ")"
                            , Constants.CRITICAL_ERROR
                    )
                    , "kontoklasse"
                    , Definitions.getKontoklasseAsList(p.getFieldAsString("skjema"))
            );

            if (bevilgningRegnskapList.contains(args.getSkjema())) {
                ControlFelt1InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                                , "Kontroll Funksjon."
                                , "Korreksjon: Rett opp feil funksjon med riktig funksjon i henhold til liste. (" + p.getFieldAsString("funksjon_kapittel") + ")"
                                , Constants.CRITICAL_ERROR
                        )
                        , "funksjon_kapittel"
                        , Definitions.getFunksjonKapittelAsList(p.getFieldAsString("skjema"), p.getFieldAsString("region"))
                );
            }

            if (balanseRegnskapList.contains(args.getSkjema())) {
                ControlFelt1InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                                , "Kontroll kapittel."
                                , "Korreksjon: Rett opp feil kapittel med riktig kapittel i henhold til liste. (" + p.getFieldAsString("funksjon_kapittel") + ")"
                                , Constants.CRITICAL_ERROR
                        )
                        , "funksjon_kapittel"
                        , Definitions.getFunksjonKapittelAsList(p.getFieldAsString("skjema"), p.getFieldAsString("region"))
                );
            }

            if (bevilgningRegnskapList.contains(args.getSkjema())) {
                ControlFelt1InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                                , "Kontroll Art."
                                , "Korreksjon: Rett opp feil art med riktig art i henhold til liste. (" + p.getFieldAsString("art_sektor") + ")"
                                , Constants.CRITICAL_ERROR
                        )
                        , "art_sektor"
                        , Definitions.getArtSektorAsList(p.getFieldAsString("skjema"), p.getFieldAsString("region"))
                );
            }

// kommentert ut denne kode til COS avklarer med fellesråd når det skal føres på sektorer
//            if (balanseRegnskapList.contains(args.getSkjema())) {
//                ControlFelt1InneholderKodeFraKodeliste.doControl(
//                        p
//                        , er
//                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
//                                , "Kontroll sektor."
//                                , "Korreksjon: Rett opp feil sektor med riktig sektor i henhold til liste. (" + p.getFieldAsString("art_sektor") + ")"
//                                , Constants.NORMAL_ERROR
//                        )
//                        , "art_sektor"
//                        , Definitions.getFunksjonKapittelAsList(p.getFieldAsString("skjema"), p.getFieldAsString("region"))
//                );
//            }

            ControlHeltall.doControl(
                    p
                    , er
                    , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                            , "Kontroll beløp."
                            , "Korreksjon: Rett opp feil beløp(" + p.getFieldAsString("belop") + ")"
                            , Constants.CRITICAL_ERROR
                    )
                    , "belop"
            );
        });


        // Kombinasjonskontroller, per record
        regnskap.forEach(p -> {
            if (bevilgningRegnskapList.contains(args.getSkjema())) {
                ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                                , "Kontroll Kombinasjon kontoklasse og art i investeringsregnskapet"
                                , "Rett opp til art som er gyldig i investeringsregnskapet, eller overfør posteringen til investeringsregnskapet"
                                , Constants.CRITICAL_ERROR
                        )
                        , "art_sektor"
                        , List.of("280", "285", "670", "910", "970")
                        , "kontoklasse"
                        , List.of(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("I")));
            }

            if (bevilgningRegnskapList.contains(args.getSkjema())) {
                ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                                , "Kontroll Kombinasjon kontoklasse og art i driftsregnskapet"
                                , "Rett opp til art som er gyldig i driftsregnskapet, eller overfør posteringen til driftsregnskapet"
                                , Constants.CRITICAL_ERROR
                        )
                        , "art_sektor"
                        , List.of("570", "590", "990")
                        , "kontoklasse"
                        , List.of(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D")));
            }
        });


        // Dublett kontroll
        List<String> dubletter = regnskap.stream()
                .map(p -> String.join(" * ", List.of(
                        p.getFieldAsTrimmedString("kontoklasse"),
                        p.getFieldAsTrimmedString("funksjon_kapittel"),
                        p.getFieldAsTrimmedString("art_sektor"))))
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


        // SUMMERINGSKONTROLLER

        //Kontroll Summeringskontroller bevilgningsregnskap
        //D = Driftsregnskap (kontoklasse 3)
        //I = Investeringsregnskap (kontoklasse 4)
        if (bevilgningRegnskapList.contains(args.getSkjema())) {
            int sumInvesteringsUtgifter = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("I"))
                            && Between.betweenInclusive(p.getFieldAsInteger("art_sektor"), 10, 590))
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);


            int sumInvesteringsInntekter = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("I"))
                            && Between.betweenInclusive(p.getFieldAsInteger("art_sektor"), 600, 990))
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);


            // 1) Sum investering = Sum investeringsutgifter + Sum investeringsinntekter.. Differanser opptil +30' godtas, og skal ikke utlistes.
            int sumInvestering = sumInvesteringsUtgifter + sumInvesteringsInntekter;

            if (!Between.betweenInclusive(sumInvestering, -30, 30)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Summeringskontroller investeringsregnskap, differanse"
                        , "Rett opp differansen (" + sumInvestering + ") mellom inntekter (" + sumInvesteringsInntekter + ") og utgifter (" + sumInvesteringsUtgifter + ") i investeringsregnskapet. Differanse +/- 30' godtas."
                        , Constants.CRITICAL_ERROR
                ));
            }

            // 2) Driftsregnskapet må ha utgiftsføringer, dvs. være høyere enn 0
            int sumDriftsUtgifter = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                            && Between.betweenInclusive(p.getFieldAsInteger("art_sektor"), 10, 590))
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            if (!(0 < sumDriftsUtgifter)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Summeringskontroller bevilgningsregnskap, utgifter"
                        , "Rett opp slik at fila inneholder utgiftsposteringene (" + sumDriftsUtgifter + ") i driftsregnskapet"
                        , Constants.CRITICAL_ERROR
                ));
            }

            // 3) Driftsregnskapet må ha inntektsføringer, dvs. være mindre enn 0
            int sumDriftsInntekter = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                            && Between.betweenInclusive(p.getFieldAsInteger("art_sektor"), 600, 990))
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            if (!(sumDriftsInntekter < 0)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Summeringskontroller bevilgningsregnskap, inntekter"
                        , "Rett opp slik at fila inneholder inntektsposteringene (" + sumDriftsInntekter + ") i driftsregnskapet"
                        , Constants.CRITICAL_ERROR
                ));
            }

            // 4) Sum drift = Sum driftsutgifter + Sum driftsinntekter.. Differanser opptil +30' godtas, og skal ikke utlistes.
            int sumDrift = sumDriftsUtgifter + sumDriftsInntekter;

            if (!Between.betweenInclusive(sumDrift, -30, 30)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Summeringskontroller bevilgningsregnskap, differanse"
                        , "Rett opp differansen (" + sumDrift + ") mellom utgifter (" + sumDriftsUtgifter + ") og inntekter (" + sumDriftsInntekter + ") i driftsregnskapet.  Differanse +/- 30' godtas."
                        , Constants.CRITICAL_ERROR
                ));
            }

            // 5) Sum art 830 for sum funksjoner 3.089 < 0
            int sumTilskudd = regnskap.stream()
                    .filter(p -> p.getFieldAsString("art_sektor").equalsIgnoreCase("830"))
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            if (!(sumTilskudd < 0)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Summeringskontroller bevilgningsregnskap, tilskudd"
                        , "Rett opp slik at fila inneholder tilskudd (" + sumTilskudd + ") fra kommunen"
                        , Constants.CRITICAL_ERROR
                ));
            }
        }

        //Kontroll Summeringskontroller balanseregnskap
        //B = Driftsregnskap (kontoklasse 5)
        if (balanseRegnskapList.contains(args.getSkjema())) {
            // 1) Balanse må ha føring på aktiva, dvs. være høyere enn 0
            int sumAktiva = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("B"))
                            && Between.betweenInclusive(p.getFieldAsInteger("funksjon_kapittel"), 10, 27))
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            if (!(0 < sumAktiva)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Summeringskontroller balanseregnskap, aktiva"
                        , "Rett opp slik at fila inneholder registrering av aktiva (" + sumAktiva + ") i balanse."
                        , Constants.CRITICAL_ERROR
                ));
            }

            // 2) Balanse må ha føring på passiva, dvs. være mindre enn 0
            int sumPassiva = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("B"))
                            && Between.betweenInclusive(p.getFieldAsInteger("funksjon_kapittel"), 31, 5990))
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            if (!(sumPassiva < 0)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Summeringskontroller balanseregnskap, passiva"
                        , "Rett opp slik at fila inneholder registrering av passiva (" + sumPassiva + ")i balanse."
                        , Constants.CRITICAL_ERROR
                ));
            }

            // 3) Aktiva skal være lik passiva. Differanser opptil +10' godtas, og skal ikke utlistes.
            int sumBalanse = sumAktiva + sumPassiva;

            if (!Between.betweenInclusive(sumBalanse, -10, 10)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Summeringskontroller balanseregnskap, differanse"
                        , "Rett opp differansen (" + sumBalanse + ") mellom aktiva (" + sumAktiva + ") og passiva (" + sumPassiva + ") i fila (Differanser opptil +10' godtas)"
                        , Constants.CRITICAL_ERROR
                ));
            }
        }


        // Kontroll Interne overføringer
        //D = Driftsregnskap (kontoklasse 3)
        //I = Investeringsregnskap (kontoklasse 4)
        if (bevilgningRegnskapList.contains(args.getSkjema())) {
            int sumInternKjop = regnskap.stream()
                    .filter(p -> List.of("380").contains(p.getFieldAsString("art_sektor")))
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            int sumInternSalg = regnskap.stream()
                    .filter(p -> List.of("780").contains(p.getFieldAsString("art_sektor")))
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            int sumInternKjopOgSalg = sumInternKjop + sumInternSalg;

            if (!Between.betweenInclusive(sumInternKjopOgSalg, -30, 30)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Interne overføringer, internkjøp og internsalg"
                        , "Rett opp i fila slik at differansen (" + sumInternKjopOgSalg + ") mellom internkjøp (" + sumInternKjop + ") og internsalg (" + sumInternSalg + ")  stemmer overens (margin på +/- 30')"
                        , Constants.CRITICAL_ERROR
                ));
            }

            int sumKalkulatoriskeUtgifter = regnskap.stream()
                    .filter(p -> List.of("390").contains(p.getFieldAsString("art_sektor")))
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            int sumKalkulatoriskeInntekter = regnskap.stream()
                    .filter(p -> List.of("790").contains(p.getFieldAsString("art_sektor")))
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            int sumKalkulatoriske = sumKalkulatoriskeUtgifter + sumKalkulatoriskeInntekter;

            if (!Between.betweenInclusive(sumKalkulatoriske, -30, 30)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Interne overføringer, kalkulatoriske utgifter og inntekter"
                        , "Rett opp i fila slik at differansen (" + sumKalkulatoriske + ") mellom kalkulatoriske utgifter (" + sumKalkulatoriskeUtgifter + ") og inntekter (" + sumKalkulatoriskeInntekter + ")ved kommunal tjenesteytelse stemmer overens (margin på +/- 30')"
                        , Constants.CRITICAL_ERROR
                ));
            }

            int overforingerMidler = regnskap.stream()
                    .filter(p -> List.of("465").contains(p.getFieldAsString("art_sektor")))
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            int innsamledeMidler = regnskap.stream()
                    .filter(p -> List.of("865").contains(p.getFieldAsString("art_sektor")))
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            int sumOverforingerMidler = overforingerMidler + innsamledeMidler;

            if (!Between.betweenInclusive(sumOverforingerMidler, -30, 30)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Interne overføringer, midler"
                        , "Rett opp i fila slik at differansen (" + sumOverforingerMidler + ") mellom overføringer av midler (" + overforingerMidler + ") og innsamlede midler (" + innsamledeMidler + ") stemmer overens (margin på +/- 30')"
                        , Constants.CRITICAL_ERROR
                ));
            }
        }

        // Kontroll Overføring mellom drifts- og investeringsregnskap
        //D = Driftsregnskap (kontoklasse 3)
        //I = Investeringsregnskap (kontoklasse 4)
        if (bevilgningRegnskapList.contains(args.getSkjema())) {
            int driftsoverforinger = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                            && p.getFieldAsString("art_sektor").equalsIgnoreCase("570")
                    )
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            int investeringsoverforinger = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("I"))
                            && p.getFieldAsString("art_sektor").equalsIgnoreCase("970")
                    )
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            int differanse = driftsoverforinger + investeringsoverforinger;

            if (!Between.betweenInclusive(differanse, -30, 30)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Overføring mellom drifts- og investeringsregnskap"
                        , "Rett opp i fila slik at differansen (" + differanse + ") overføringer mellom drifts (" + driftsoverforinger + ")- og investeringsregnskapet (" + investeringsoverforinger + ") stemmer overens"
                        , Constants.CRITICAL_ERROR
                ));
            }
        }

        // Kontroll Avskrivninger
        //D = Driftsregnskap (kontoklasse 1 / 3)
        //I = Investeringsregnskap (kontoklasse 0 / 4)
        if (bevilgningRegnskapList.contains(args.getSkjema())) {
            // 1) Art 590 for sum funksjoner D.041-D.089 + art 990 for sum funksjoner D.041-D.089 = < 30 og > - 30. Differanser opptil +30' godtas, og skal ikke utlistes.
            int avskrivninger = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                            && List.of("590").contains(p.getFieldAsString("art_sektor"))
                    )
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            int motpostAvskrivninger = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                            && List.of("990").contains(p.getFieldAsString("art_sektor"))
                    )
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            int sumAvskrivninger = avskrivninger + motpostAvskrivninger;

            if (!Between.betweenInclusive(sumAvskrivninger, -30, 30)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Avskrivninger, art 590, art 990"
                        , "Rett opp i fila slik at differansen (" + sumAvskrivninger + ") mellom art 590 (" + avskrivninger + ") stemmer overens med art 990 (" + motpostAvskrivninger + ") (margin på +/- 30')"
                        , Constants.CRITICAL_ERROR
                ));
            }
        }

        // Kontroll Funksjon 089
        //D = Driftsregnskap (kontoklasse 3)
        //I = Investeringsregnskap (kontoklasse 4)
        if (bevilgningRegnskapList.contains(args.getSkjema())) {
            List<String> finansielleArter = List.of(
                    "500", "510", "520", "530", "540", "550", "570", "580",
                    "830",
                    "900", "905", "910", "920", "930", "940", "950", "970", "980"
            );

            regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D")))
                    .forEach(p -> ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                            p
                            , er
                            , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                                    , "Kontroll Funksjon 089"
                                    , "Rett opp art i driftsregnskapet som er gyldig mot funksjon 3.089"
                                    , Constants.NORMAL_ERROR
                            )
                            , "funksjon_kapittel"
                            , List.of("089 ")
                            , "art_sektor"
                            , finansielleArter)
                    );

            regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("I")))
                    .forEach(p -> ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                            p
                            , er
                            , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine(), p.getRecord()), " ", " "
                                    , "Kontroll Funksjon 089"
                                    , "Rett opp art i investeringsregnskapet som er gyldig mot funksjon 4.089"
                                    , Constants.NORMAL_ERROR
                            )
                            , "funksjon_kapittel"
                            , List.of("089 ")
                            , "art_sektor"
                            , finansielleArter)
                    );
        }


        return er;
    }
}
