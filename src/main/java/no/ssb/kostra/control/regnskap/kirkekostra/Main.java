package no.ssb.kostra.control.regnskap.kirkekostra;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.felles.ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste;
import no.ssb.kostra.control.felles.ControlFelt1InneholderKodeFraKodeliste;
import no.ssb.kostra.control.felles.ControlHeltall;
import no.ssb.kostra.control.felles.ControlRecordLengde;
import no.ssb.kostra.control.regnskap.FieldDefinitions;
import no.ssb.kostra.control.regnskap.kostra.Definitions;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.utils.Between;
import no.ssb.kostra.utils.Format;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {
    public static ErrorReport doControls(Arguments args) {
        ErrorReport er = new ErrorReport(args);
        List<String> list1 = args.getInputFileContent();
        List<FieldDefinition> fieldDefinitions = FieldDefinitions.getFieldDefinitions();
        List<String> bevilgningRegnskapList = List.of("0F");
        List<String> balanseRegnskapList = List.of("0G");
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
                .map(p -> ControlFelt1InneholderKodeFraKodeliste.doControl(
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
                .map(p -> ControlFelt1InneholderKodeFraKodeliste.doControl(
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
                .map(p -> ControlFelt1InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                , "Kontroll Kvartal"
                                , "Korreksjon: Rett opp til rett kvartal (" + args.getKvartal() + ")"
                                , Constants.CRITICAL_ERROR
                        )
                        , "kvartal"
                        , Collections.singletonList(args.getKvartal())
                ))
                .map(p -> ControlFelt1InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                , "Kontroll fylkeskommune-/kommune-/bydelsnummeret."
                                , "Korreksjon: Rett fylkeskommune-/kommune-/bydelsnummeret. (" + args.getRegion() + ")"
                                , Constants.CRITICAL_ERROR
                        )
                        , "region"
                        , Collections.singletonList(args.getRegion())
                ))
                .map(p -> ControlFelt1InneholderKodeFraKodeliste.doControl(
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
                .map(p -> ControlFelt1InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                , "Kontroll Foretaksnummer."
                                , "Korreksjon: Foretaksnummer skal være 9 blanke tegn / mellomrom. (" + args.getForetaknr() + ")"
                                , Constants.CRITICAL_ERROR
                        )
                        , "foretaksnr"
                        , Collections.singletonList("         ")
                ))
                .map(p -> ControlFelt1InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                , "Kontroll Kontoklasse."
                                , "Korreksjon: Rett kontoklassen. (" + p.getFieldAsString("kontoklasse") + ")"
                                , Constants.CRITICAL_ERROR
                        )
                        , "kontoklasse"
                        , no.ssb.kostra.control.regnskap.kostra.Definitions.getKontoklasseAsList(p.getFieldAsString("skjema"))
                ))
                .map(p -> {
                            if (bevilgningRegnskapList.contains(args.getSkjema())) {
                                return ControlFelt1InneholderKodeFraKodeliste.doControl(
                                        p
                                        , er
                                        , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                                , "Kontroll Funksjon."
                                                , "Korreksjon: Rett opp feil funksjon med riktig funksjon i henhold til liste. (" + p.getFieldAsString("funksjon_kapittel") + ")"
                                                , Constants.CRITICAL_ERROR
                                        )
                                        , "funksjon_kapittel"
                                        , no.ssb.kostra.control.regnskap.kostra.Definitions.getFunksjonKapittelAsList(p.getFieldAsString("skjema"), p.getFieldAsString("region"))
                                );
                            }

                            return p;
                        }
                )
                .map(p -> {
                            if (balanseRegnskapList.contains(args.getSkjema())) {
                                return ControlFelt1InneholderKodeFraKodeliste.doControl(
                                        p
                                        , er
                                        , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                                , "Kontroll kapittel."
                                                , "Korreksjon: Rett opp feil kapittel med riktig kapittel i henhold til liste. (" + p.getFieldAsString("funksjon_kapittel") + ")"
                                                , Constants.CRITICAL_ERROR
                                        )
                                        , "funksjon_kapittel"
                                        , no.ssb.kostra.control.regnskap.kostra.Definitions.getFunksjonKapittelAsList(p.getFieldAsString("skjema"), p.getFieldAsString("region"))
                                );
                            }

                            return p;
                        }
                )
                .map(p -> {
                            if (bevilgningRegnskapList.contains(args.getSkjema())) {
                                return ControlFelt1InneholderKodeFraKodeliste.doControl(
                                        p
                                        , er
                                        , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                                , "Kontroll Art."
                                                , "Korreksjon: Rett opp feil art med riktig art i henhold til liste. (" + p.getFieldAsString("art_sektor") + ")"
                                                , Constants.CRITICAL_ERROR
                                        )
                                        , "art_sektor"
                                        , no.ssb.kostra.control.regnskap.kostra.Definitions.getArtSektorAsList(p.getFieldAsString("skjema"), p.getFieldAsString("region"))
                                );
                            }

                            return p;
                        }
                )
                .map(p -> {
                            if (balanseRegnskapList.contains(args.getSkjema())) {
                                return ControlFelt1InneholderKodeFraKodeliste.doControl(
                                        p
                                        , er
                                        , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                                , "Kontroll sektor."
                                                , "Korreksjon: Rett opp feil sektor med riktig sektor i henhold til liste. (" + p.getFieldAsString("art_sektor") + ")"
                                                , Constants.CRITICAL_ERROR
                                        )
                                        , "art_sektor"
                                        , Definitions.getFunksjonKapittelAsList(p.getFieldAsString("skjema"), p.getFieldAsString("region"))
                                );
                            }

                            return p;
                        }
                )
                .peek(p -> ControlHeltall.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                , "Kontroll beløp."
                                , "Korreksjon: Rett opp feil beløp(" + p.getFieldAsString("belop") + ")"
                                , Constants.CRITICAL_ERROR
                        )
                        , "belop"
                ));

        // Kombinasjonskontroller, per record
        regnskap.stream()
                // Kontroll Kombinasjon kontoklasse og art i investeringsregnskapet
                .peek(p -> {
                    if (bevilgningRegnskapList.contains(args.getSkjema())) {
                        ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                                p
                                , er
                                , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                        , "Kontroll Kombinasjon kontoklasse og art i investeringsregnskapet"
                                        , "Rett opp til art som er gyldig i investeringsregnskapet, eller overfør posteringen til driftsregnskapet"
                                        , Constants.CRITICAL_ERROR
                                )
                                , "art_sektor"
                                , List.of("280", "285", "670", "910", "970")
                                , "kontoklasse"
                                , List.of(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("I")));
                    }
                })
                .peek(p -> {
                    if (bevilgningRegnskapList.contains(args.getSkjema())) {
                        ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                                p
                                , er
                                , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                        , "Kontroll Kombinasjon kontoklasse og art i driftsregnskapet"
                                        , "Rett opp til art som er gyldig i driftsregnskapet, eller overfør posteringen til investeringsregnskapet"
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
                        , "Kontroll Summeringskontroller bevilgningsregnskap"
                        , "Rett opp differansen (" + sumInvestering + ") mellom inntekter og utgifter i investeringsregnskapet"
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
                        , "Kontroll Summeringskontroller bevilgningsregnskap"
                        , "Rett opp slik at fila inneholder utgiftsposteringene i driftsregnskapet"
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
                        , "Kontroll Summeringskontroller bevilgningsregnskap"
                        , "Rett opp slik at fila inneholder inntektsposteringene i driftsregnskapet"
                        , Constants.CRITICAL_ERROR
                ));
            }

            // 4) Sum drift = Sum driftsutgifter + Sum driftsinntekter.. Differanser opptil +30' godtas, og skal ikke utlistes.
            int sumDrift = sumDriftsUtgifter + sumDriftsInntekter;

            if (!Between.betweenInclusive(sumDrift, -30, 30)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Summeringskontroller bevilgningsregnskap"
                        , "Rett opp differansen (" + sumDrift + ") mellom inntekter og utgifter i driftsregnskapet"
                        , Constants.CRITICAL_ERROR
                ));
            }

            // 5) Sum art 830 for sum funksjoner 3.089 < 0
            int sumOverforinger = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                            && p.getFieldAsString("funksjon_kapittel").equalsIgnoreCase("089")
                            && p.getFieldAsString("art_sektor").equalsIgnoreCase("830"))
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            if (!(sumOverforinger < 0)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Summeringskontroller bevilgningsregnskap"
                        , "Rett opp slik at fila inneholder tilskudd fra kommunen"
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
                        , "Kontroll Summeringskontroller balanseregnskap"
                        , "Rett opp slik at fila inneholder registrering av aktiva i balanse."
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
                        , "Kontroll Summeringskontroller balanseregnskap"
                        , "Rett opp slik at fila inneholder registrering av aktiva i balanse."
                        , Constants.CRITICAL_ERROR
                ));
            }

            // 3) Aktiva skal være lik passiva. Differanser opptil +10' godtas, og skal ikke utlistes.
            int sumBalanse = sumAktiva + sumPassiva;

            if (!Between.betweenInclusive(sumBalanse, -10, 10)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Summeringskontroller balanseregnskap"
                        , "Rett opp differansen mellom aktiva og passiva i fila (Differanser opptil +10' godtas)"
                        , Constants.CRITICAL_ERROR
                ));
            }
        }


        // Kontroll Interne overføringer
        //D = Driftsregnskap (kontoklasse 3)
        //I = Investeringsregnskap (kontoklasse 4)
        if (bevilgningRegnskapList.contains(args.getSkjema())) {
            int sumInternKjopOgSalg = regnskap.stream()
                    .filter(p -> List.of("380", "780").contains(p.getFieldAsString("art_sektor")))
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            if (!Between.betweenInclusive(sumInternKjopOgSalg, -30, 30)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Interne overføringer"
                        , "Rett opp i fila slik at internkjøp og internsalg stemmer overens (margin på +/- 30')"
                        , Constants.CRITICAL_ERROR
                ));
            }

            int sumKalkulatoriske = regnskap.stream()
                    .filter(p -> List.of("380", "780").contains(p.getFieldAsString("art_sektor")))
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            if (!Between.betweenInclusive(sumKalkulatoriske, -30, 30)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Interne overføringer"
                        , "Rett opp i fila slik at kalkulatoriske utgifter og inntekter ved kommunal tjenesteytelse stemmer overens (margin på +/- 30')"
                        , Constants.CRITICAL_ERROR
                ));
            }

            int sumOverforingerMidler = regnskap.stream()
                    .filter(p -> List.of("465", "865").contains(p.getFieldAsString("art_sektor")))
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            if (!Between.betweenInclusive(sumOverforingerMidler, -30, 30)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Interne overføringer"
                        , "Rett opp i fila slik at overføringer av midler og innsamlede midler stemmer overens (margin på +/- 30')"
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
                        , "Rett opp i fila slik at overføringer mellom drifts- og investeringsregnskapet stemmer overens"
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
                            && List.of("590", "990").contains(p.getFieldAsString("art_sektor").equalsIgnoreCase("590"))
                    )
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);


            if (!Between.betweenInclusive(avskrivninger, -30, 30)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Avskrivninger"
                        , "Rett opp i fila slik at art 590 stemmer overens med art 990 (margin på +/- 30')"
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
                    .peek(p -> ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                            p
                            , er
                            , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                    , "Kontroll Funksjon 089"
                                    , "Rett opp art i driftsregnskapet som er gyldig mot funksjon 3.089"
                                    , Constants.NORMAL_ERROR
                            )
                            , "art_sektor"
                            , finansielleArter
                            , "funksjon_kapittel"
                            , List.of("089"))
                    );

            regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("I")))
                    .peek(p -> ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                            p
                            , er
                            , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                    , "Kontroll Funksjon 089"
                                    , "Rett opp art i investeringsregnskapet som er gyldig mot funksjon 3.089"
                                    , Constants.NORMAL_ERROR
                            )
                            , "art_sektor"
                            , finansielleArter
                            , "funksjon_kapittel"
                            , List.of("089"))
                    );
        }


        return er;
    }
}
