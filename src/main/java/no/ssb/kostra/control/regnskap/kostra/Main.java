package no.ssb.kostra.control.regnskap.kostra;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.felles.ControlFelt1InneholderKodeFraKodeliste;
import no.ssb.kostra.control.felles.ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste;
import no.ssb.kostra.control.felles.ControlHeltall;
import no.ssb.kostra.control.felles.ControlRecordLengde;
import no.ssb.kostra.control.regnskap.FieldDefinitions;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.utils.Between;
import no.ssb.kostra.utils.Format;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {
    private static String createLinenumber(Integer l, int line) {
        return "Linje " + Format.sprintf("%0" + l + "d", line);
    }

    public static ErrorReport doControls(Arguments args) {
        ErrorReport er = new ErrorReport(args);
        List<String> list1 = args.getInputContentAsStringList();
        List<FieldDefinition> fieldDefinitions = FieldDefinitions.getFieldDefinitions();
        List<String> bevilgningRegnskapList = List.of("0A", "0C", "0I", "0K", "0M", "0P");
        List<String> regionaleBevilgningRegnskapList = List.of("0A", "0C", "0M", "0P");
        List<String> balanseRegnskapList = List.of("0B", "0D", "0J", "0L", "0N", "0Q");
        List<String> osloBydeler = List.of(
                "030101", "030102", "030103", "030104", "030105",
                "030106", "030107", "030108", "030109", "030110",
                "030111", "030112", "030113", "030114", "030115"
        );
        List<String> svalbard = List.of("211100");
        List<Record> regnskap = list1.stream()
                .map(p -> new Record(p, fieldDefinitions))
                .filter(p -> p.getFieldAsInteger("belop") != 0)
                .collect(Collectors.toList());
        String saksbehandler = "Filuttrekk";
        Integer n = regnskap.size();
        Integer l = String.valueOf(n).length();

        // alle records må være med korrekt lengde, ellers vil de andre kontrollene kunne feile
        // Kontroll Recordlengde
        ControlRecordLengde.doControl(regnskap.stream(), er, FieldDefinitions.getFieldLength());

        if (er.getErrorType() == Constants.CRITICAL_ERROR) {
            return er;
        }

        // Sjekk skjematype, hvis den er feil så er det ikke noe vits å fortsette
        regnskap.forEach(p -> ControlFelt1InneholderKodeFraKodeliste.doControl(
                p
                , er
                , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine()), " ", " "
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
                    , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine()), " ", " "
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
                    , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine()), " ", " "
                            , "Kontroll fylkeskommune-/kommune-/bydelsnummeret."
                            , "Korreksjon: Rett fylkeskommune-/kommune-/bydelsnummeret. (" + args.getRegion() + ")"
                            , Constants.CRITICAL_ERROR
                    )
                    , "region"
                    , Collections.singletonList(args.getRegion())
            );

            if (Arrays.asList("0I", "0J", "0K", "0L").contains(args.getSkjema())) {
                ControlFelt1InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine()), " ", " "
                                , "Kontroll Organisasjonsnummer."
                                , "Korreksjon: Korrigér organisasjonsnummer i filutrekket. (" + args.getOrgnr() + ")"
                                , Constants.CRITICAL_ERROR
                        )
                        , "orgnr"
                        , Collections.singletonList(args.getOrgnr())
                );
            } else {
                ControlFelt1InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine()), " ", " "
                                , "Kontroll Organisasjonsnummer."
                                , "Korreksjon: Organisasjonsnummer skal være 9 blanke tegn / mellomrom. (" + args.getOrgnr() + ")"
                                , Constants.CRITICAL_ERROR
                        )
                        , "orgnr"
                        , Collections.singletonList("         ")
                );
            }

            ControlFelt1InneholderKodeFraKodeliste.doControl(
                    p
                    , er
                    , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine()), " ", " "
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
                    , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine()), " ", " "
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
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine()), " ", " "
                                , "Kontroll Funksjon."
                                , "Korreksjon: Rett opp feil funksjon med riktig funksjon i henhold til liste. (" + p.getFieldAsTrimmedString("funksjon_kapittel") + ")"
                                , Constants.CRITICAL_ERROR
                        )
                        , "funksjon_kapittel"
                        , Definitions.getFunksjonKapittelAsList(p.getFieldAsString("skjema"), p.getFieldAsString("region"))
                );

                ControlFelt1InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine()), " ", " "
                                , "Kontroll Art."
                                , "Korreksjon: Rett opp feil art med riktig art i henhold til liste. (" + p.getFieldAsString("art_sektor") + ")"
                                , Constants.CRITICAL_ERROR
                        )
                        , "art_sektor"
                        , Definitions.getArtSektorAsList(p.getFieldAsString("skjema"), p.getFieldAsString("region"))
                );
            }

            if (balanseRegnskapList.contains(args.getSkjema())) {
                ControlFelt1InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine()), " ", " "
                                , "Kontroll kapittel."
                                , "Korreksjon: Rett opp feil kapittel med riktig kapittel i henhold til liste. (" + p.getFieldAsTrimmedString("funksjon_kapittel") + ")"
                                , Constants.CRITICAL_ERROR
                        )
                        , "funksjon_kapittel"
                        , Definitions.getFunksjonKapittelAsList(p.getFieldAsString("skjema"), p.getFieldAsString("region"))
                );

                ControlFelt1InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine()), " ", " "
                                , "Kontroll sektor."
                                , "Korreksjon: Rett opp feil sektor med riktig sektor i henhold til liste. (" + p.getFieldAsString("art_sektor") + ")"
                                , Constants.CRITICAL_ERROR
                        )
                        , "art_sektor"
                        , Definitions.getArtSektorAsList(p.getFieldAsString("skjema"), p.getFieldAsString("region"))
                );
            }

            ControlHeltall.doControl(
                    p
                    , er
                    , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine()), " ", " "
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
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine()), " ", " "
                                , "Kontroll Kombinasjon kontoklasse og art i investeringsregnskapet"
                                , "Rett opp til art som er gyldig i investeringsregnskapet, eller overfør posteringen til driftsregnskapet"
                                , Constants.CRITICAL_ERROR
                        )
                        , "art_sektor"
                        , List.of("529", "670", "910", "911", "929", "970")
                        , "kontoklasse"
                        , List.of(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("I")));

                ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine()), " ", " "
                                , "Kontroll Kombinasjon kontoklasse og art i driftsregnskapet"
                                , "Rett opp til art som er gyldig i driftsregnskapet, eller overfør posteringen til investeringsregnskapet"
                                , Constants.CRITICAL_ERROR
                        )
                        , "art_sektor"
                        , List.of("240", "509", "540", "570", "590", "800", "870", "874", "875", "877", "909", "990")
                        , "kontoklasse"
                        , List.of(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D")));

                ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine()), " ", " "
                                , "Kontroll Kombinasjon kontoklasse og funksjon i investeringsregnskapet"
                                , "Rett opp til funksjon som er gyldig i driftsregnskapet, eller overfør posteringen til investeringsregnskapet. "
                                + "I driftsregnskapet skal mva-kompensasjon (art 729) føres på funksjonen hvor utgiften oppstod."
                                , Constants.CRITICAL_ERROR
                        )
                        , "funksjon_kapittel"
                        , List.of("841")
                        , "kontoklasse"
                        , List.of(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("I")));

                ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine()), " ", " "
                                , "Kontroll Kombinasjon kontoklasse og funksjon i driftsregnskapet"
                                , "Rett opp til funksjon som er gyldig i investeringsregnskapet, eller overfør posteringen til driftsregnskapet."
                                , Constants.CRITICAL_ERROR
                        )
                        , "funksjon_kapittel"
                        , List.of("800", "840", "860")
                        , "kontoklasse"
                        , List.of(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D")));

                if (p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("I"))) {
                    ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                            p
                            , er
                            , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine()), " ", " "
                                    , "Kontroll Kombinasjon kontoklasse, funksjon og art i investeringsregnskapet"
                                    , "Rett opp til riktig kombinasjon av kontoklasse, funksjon og art. "
                                    + "Mva-kompensasjonen for anskaffelser i investeringsregnskapet er frie midler som skal "
                                    + "benyttes til felles finansiering av investeringer i bygninger, anlegg og andre varige driftsmidler."
                                    , Constants.CRITICAL_ERROR
                            )
                            , "art_sektor"
                            , List.of("729")
                            , "funksjon_kapittel"
                            , List.of("841"));
                }

                // Artene 589, 980 og 989 er kun tillat brukt i kombinasjon med funksjon 899.
                // Kontrollen må kjøres 2 ganger ettersom den ikke støtter gjensidighet
                ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine()), " ", " "
                                , "Kontroll Kombinasjon funksjon og art"
                                , "Artene 589, 980 og 989 er kun tillat brukt i kombinasjon med funksjon 899. "
                                + "Og motsatt, funksjon 899 er kun tillat brukt i kombinasjon med artene 589, 980 og 989"
                                , Constants.CRITICAL_ERROR
                        )
                        , "art_sektor"
                        , List.of("589", "980", "989")
                        , "funksjon_kapittel"
                        , List.of("899"));

                ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine()), " ", " "
                                , "Kontroll Kombinasjon funksjon og art"
                                , "Artene 589, 980 og 989 er kun tillat brukt i kombinasjon med funksjon 899. "
                                + "Og motsatt, funksjon 899 er kun tillat brukt i kombinasjon med artene 589, 980 og 989"
                                , Constants.CRITICAL_ERROR
                        )
                        , "funksjon_kapittel"
                        , List.of("899")
                        , "art_sektor"
                        , List.of("589", "980", "989"));

            }

            if (Arrays.asList("0A", "0M").contains(args.getSkjema())) {
                if (p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))) {
                    ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                            p
                            , er
                            , new ErrorReportEntry(saksbehandler, createLinenumber(l, p.getLine()), " ", " "
                                    , "Kontroll Kombinasjon kontoklasse, funksjon og art i driftsregnskapet"
                                    , "Rett opp til riktig kombinasjon av kontoklasse, funksjon og art."
                                    , Constants.CRITICAL_ERROR
                            )
                            , "art_sektor"
                            , List.of("874", "875")
                            , "funksjon_kapittel"
                            , List.of("800"));
                }
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
        //Denne kontrollen gjelder IKKE for bydelene i Oslo
        //D = Driftsregnskap (kontoklasse 1 / 3)
        //I = Investeringsregnskap (kontoklasse 0 / 4)
        if (bevilgningRegnskapList.contains(args.getSkjema())) {
            if (!osloBydeler.contains(args.getRegion())) {
                // 1) Investeringsregnskapet må ha utgiftsføringer, dvs. være høyere enn 0
                int sumInvesteringsUtgifter = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("I"))
                                && Between.betweenInclusive(p.getFieldAsInteger("art_sektor"), 10, 590))
                        .map(p -> p.getFieldAsInteger("belop"))
                        .reduce(0, Integer::sum);

                if (regionaleBevilgningRegnskapList.contains(args.getSkjema())) {
                    if (!(0 < sumInvesteringsUtgifter)) {
                        er.addEntry(new ErrorReportEntry(
                                saksbehandler, "Summeringskontroller", " ", " "
                                , "Kontroll Summeringskontroller bevilgningsregnskap"
                                , "Rett opp slik at fila inneholder utgiftsposteringene (" + sumInvesteringsUtgifter + ") i investeringsregnskapet"
                                , Constants.CRITICAL_ERROR
                        ));
                    }
                }

                // 2) Investeringsregnskapet må ha inntekter, dvs. være mindre enn 0
                int sumInvesteringsInntekter = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("I"))
                                && Between.betweenInclusive(p.getFieldAsInteger("art_sektor"), 600, 990))
                        .map(p -> p.getFieldAsInteger("belop"))
                        .reduce(0, Integer::sum);

                if (regionaleBevilgningRegnskapList.contains(args.getSkjema())) {
                    if (!(sumInvesteringsInntekter < 0)) {
                        er.addEntry(new ErrorReportEntry(
                                saksbehandler, "Summeringskontroller", " ", " "
                                , "Kontroll Summeringskontroller bevilgningsregnskap"
                                , "Rett opp slik at fila inneholder inntektsposteringene (" + sumInvesteringsInntekter + ") i investeringsregnskapet"
                                , Constants.CRITICAL_ERROR
                        ));
                    }
                }

                // 3) Sum investering = Sum investeringsutgifter + Sum investeringsinntekter.. Differanser opptil +30' godtas, og skal ikke utlistes.
                int sumInvestering = sumInvesteringsUtgifter + sumInvesteringsInntekter;

                if (!Between.betweenInclusive(sumInvestering, -30, 30)) {
                    er.addEntry(new ErrorReportEntry(
                            saksbehandler, "Summeringskontroller", " ", " "
                            , "Kontroll Summeringskontroller bevilgningsregnskap"
                            , "Rett opp differansen (" + sumInvestering + ") mellom "
                            + "inntekter (" + sumInvesteringsInntekter + ") og "
                            + "utgifter (" + sumInvesteringsUtgifter + ") i investeringsregnskapet"
                            , Constants.CRITICAL_ERROR
                    ));
                }

                // 4) Driftsregnskapet må ha utgiftsføringer, dvs. være høyere enn 0
                int sumDriftsUtgifter = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                                && Between.betweenInclusive(p.getFieldAsInteger("art_sektor"), 10, 590))
                        .map(p -> p.getFieldAsInteger("belop"))
                        .reduce(0, Integer::sum);

                if (!(0 < sumDriftsUtgifter)) {
                    er.addEntry(new ErrorReportEntry(
                            saksbehandler, "Summeringskontroller", " ", " "
                            , "Kontroll Summeringskontroller bevilgningsregnskap"
                            , "Rett opp slik at fila inneholder utgiftsposteringene (" + sumDriftsUtgifter + ") i driftsregnskapet"
                            , Constants.CRITICAL_ERROR
                    ));
                }

                // 5) Driftsregnskapet må ha inntektsføringer, dvs. være mindre enn 0
                int sumDriftsInntekter = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                                && Between.betweenInclusive(p.getFieldAsInteger("art_sektor"), 600, 990))
                        .map(p -> p.getFieldAsInteger("belop"))
                        .reduce(0, Integer::sum);

                if (!(sumDriftsInntekter < 0)) {
                    er.addEntry(new ErrorReportEntry(
                            saksbehandler, "Summeringskontroller", " ", " "
                            , "Kontroll Summeringskontroller bevilgningsregnskap"
                            , "Rett opp slik at fila inneholder inntektsposteringene (" + sumDriftsInntekter + ") i driftsregnskapet"
                            , Constants.CRITICAL_ERROR
                    ));
                }

                // 6) Sum drift = Sum driftsutgifter + Sum driftsinntekter.. Differanser opptil +30' godtas, og skal ikke utlistes.
                int sumDrift = sumDriftsUtgifter + sumDriftsInntekter;

                if (!Between.betweenInclusive(sumDrift, -30, 30)) {
                    er.addEntry(new ErrorReportEntry(
                            saksbehandler, "Summeringskontroller", " ", " "
                            , "Kontroll Summeringskontroller bevilgningsregnskap"
                            , "Rett opp differansen (" + sumDrift + ") mellom "
                            + "inntekter (" + sumDriftsInntekter + ") og "
                            + "utgifter (" + sumDriftsUtgifter + ") i driftsregnskapet"
                            , Constants.CRITICAL_ERROR
                    ));
                }
            }
        }

        //Kontroll Summeringskontroller balanseregnskap
        //B = Driftsregnskap (kontoklasse 2 / 5)
        if (balanseRegnskapList.contains(args.getSkjema())) {
            // 1) Balanse må ha føring på aktiva, dvs. være høyere enn 0
            int sumAktiva = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("B"))
                            && Between.betweenInclusive(p.getFieldAsInteger("funksjon_kapittel"), 10, 27))
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            if (!(0 < sumAktiva)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, "Summeringskontroller", " ", " "
                        , "Kontroll Summeringskontroller balanseregnskap"
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
                        saksbehandler, "Summeringskontroller", " ", " "
                        , "Kontroll Summeringskontroller balanseregnskap"
                        , "Rett opp slik at fila inneholder registrering av passiva (" + sumPassiva + ") i balanse."
                        , Constants.CRITICAL_ERROR
                ));
            }

            // 3) Aktiva skal være lik passiva. Differanser opptil ±10' godtas, og skal ikke utlistes.
            int sumBalanse = sumAktiva + sumPassiva;

            if (!Between.betweenInclusive(sumBalanse, -10, 10)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, "Summeringskontroller", " ", " "
                        , "Kontroll Summeringskontroller balanseregnskap"
                        , "Rett opp differansen (" + sumBalanse + ") mellom "
                        + "aktiva (" + sumAktiva + ") og "
                        + "passiva (" + sumPassiva + ") i fila (Differanser opptil ±10' godtas)"
                        , Constants.CRITICAL_ERROR
                ));
            }
        }

        // Kontroll Skatteinntekter og rammetilskudd
        //Denne kontrollen gjelder IKKE for bydelene i Oslo
        //Denne kontrollen gjelder IKKE for Longyearbyen Lokalstyre.
        //D = Driftsregnskap (kontoklasse 1 / 3)
        //I = Investeringsregnskap (kontoklasse 0 / 4)
        if (regionaleBevilgningRegnskapList.contains(args.getSkjema())) {
            if (!osloBydeler.contains(args.getRegion()) && !svalbard.contains(args.getRegion())) {
                // 1) Driftsregnskapet skal ha skatteinntekter; Kontoklasse D, Funksjon 800, art 870 < 0)
                int sumSkatteInntekter = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                                && p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("800")
                                && p.getFieldAsString("art_sektor").equalsIgnoreCase("870"))
                        .map(p -> p.getFieldAsInteger("belop"))
                        .reduce(0, Integer::sum);

                if (!(sumSkatteInntekter < 0)) {
                    er.addEntry(new ErrorReportEntry(
                            saksbehandler, "Summeringskontroller", " ", " "
                            , "Kontroll Skatteinntekter og rammetilskudd"
                            , "Rett opp slik at fila inneholder skatteinntekter (" + sumSkatteInntekter + ")."
                            , Constants.CRITICAL_ERROR
                    ));
                }

                // 2) Skatteinntekter skal føres på funksjon 800. Ingen andre funksjoner skal være gyldig i kombinasjon med art 870.
                int sumSkatteInntekterAndreFunksjoner = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                                && p.getFieldAsString("art_sektor").equalsIgnoreCase("870")
                                && !p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("800"))
                        .map(p -> p.getFieldAsInteger("belop"))
                        .reduce(0, Integer::sum);

                if (!(sumSkatteInntekterAndreFunksjoner == 0)) {
                    er.addEntry(new ErrorReportEntry(
                            saksbehandler, "Summeringskontroller", " ", " "
                            , "Kontroll Skatteinntekter og rammetilskudd"
                            , "Rett opp slik at skatteinntekter er ført på funksjon 800, art 870. "
                            + "Sum skatteinntekter fra andre funskjoner/art870 er (" + sumSkatteInntekterAndreFunksjoner + ")"
                            , Constants.CRITICAL_ERROR
                    ));
                }

                // 3) Rammetilskudd skal føres på funksjon 840. Ingen andre funksjoner skal være gyldig i kombinasjon med art 800.
                int funksjon800art840Andre = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                                && p.getFieldAsString("art_sektor").equalsIgnoreCase("800")
                                && !p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("840"))
                        .map(p -> p.getFieldAsInteger("belop"))
                        .reduce(0, Integer::sum);

                if (!(funksjon800art840Andre == 0)) {
                    er.addEntry(new ErrorReportEntry(
                            saksbehandler, "Summeringskontroller", " ", " "
                            , "Kontroll Skatteinntekter og rammetilskudd"
                            , "Rett opp slik at art 800 ikke benyttes på andre funksjoner enn 840."
                            , Constants.CRITICAL_ERROR
                    ));
                }
            }
        }

        // Kontroll Overføring mellom drifts- og investeringsregnskap
        //Denne kontrollen gjelder IKKE for bydelene i Oslo
        //D = Driftsregnskap (kontoklasse 1 / 3)
        //I = Investeringsregnskap (kontoklasse 0 / 4)
        if (bevilgningRegnskapList.contains(args.getSkjema())) {
            if (!osloBydeler.contains(args.getRegion())) {
                int driftsoverforinger = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                                && p.getFieldAsString("art_sektor").equalsIgnoreCase("570"))
                        .map(p -> p.getFieldAsInteger("belop"))
                        .reduce(0, Integer::sum);

                int investeringsoverforinger = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("I"))
                                && p.getFieldAsString("art_sektor").equalsIgnoreCase("970"))
                        .map(p -> p.getFieldAsInteger("belop"))
                        .reduce(0, Integer::sum);

                int differanse = driftsoverforinger + investeringsoverforinger;

                if (!Between.betweenInclusive(differanse, -30, 30)) {
                    er.addEntry(new ErrorReportEntry(
                            saksbehandler, "Summeringskontroller", " ", " "
                            , "Kontroll Overføring mellom drifts- og investeringsregnskap"
                            , "Rett opp i fila slik at overføringer mellom drifts- og investeringsregnskapet stemmer overens"
                            , Constants.CRITICAL_ERROR
                    ));
                }
            }
        }

        // Kontroll Avskrivninger
        //Denne kontrollen gjelder IKKE for bydelene i Oslo
        //D = Driftsregnskap (kontoklasse 1 / 3)
        //I = Investeringsregnskap (kontoklasse 0 / 4)
        if (bevilgningRegnskapList.contains(args.getSkjema())) {
            if (!osloBydeler.contains(args.getRegion())) {
                // 1) Funksjon D.860, art 990 ≠ 0
                int funksjon860art990 = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                                && p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("860")
                                && p.getFieldAsString("art_sektor").equalsIgnoreCase("990")
                        )
                        .map(p -> p.getFieldAsInteger("belop"))
                        .reduce(0, Integer::sum);

                if (funksjon860art990 == 0) {
                    er.addEntry(new ErrorReportEntry(
                            saksbehandler, "Summeringskontroller", " ", " "
                            , "Kontroll Avskrivninger"
                            , "Rett opp i fila slik at motpost avskrivninger er ført på funksjon 860, art 990."
                            , Constants.CRITICAL_ERROR
                    ));
                }

                // 2) Art 590 for sum funksjoner D.100-D.899 + art 990 for funksjon D.860 = < 30 og > - 30. Differanser opptil +-30' godtas, og skal ikke utlistes.
                int sumArt590 = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                                && p.getFieldAsString("art_sektor").equalsIgnoreCase("590"))
                        .map(p -> p.getFieldAsInteger("belop"))
                        .reduce(0, Integer::sum);

                int differanse = funksjon860art990 + sumArt590;

                if (!Between.betweenInclusive(differanse, -30, 30)) {
                    er.addEntry(new ErrorReportEntry(
                            saksbehandler, "Summeringskontroller", " ", " "
                            , "Kontroll Avskrivninger"
                            , "Rett opp i fila slik at motpost avskrivninger er ført på funksjon 860, art 990."
                            , Constants.CRITICAL_ERROR
                    ));
                }

                // 3) Funksjon D.800..D.899, art 590 = 0
                int sumFunksjon8XXArt590 = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                                && p.getFieldAsString("art_sektor").equalsIgnoreCase("590")
                                && Between.betweenInclusive(p.getFieldAsInteger("funksjon_kapittel"), 800, 899)
                        )
                        .map(p -> p.getFieldAsInteger("belop"))
                        .reduce(0, Integer::sum);

                if (!(sumFunksjon8XXArt590 == 0)) {
                    er.addEntry(new ErrorReportEntry(
                            saksbehandler, "Summeringskontroller", " ", " "
                            , "Kontroll Avskrivninger"
                            , "Rett opp i fila slik at avskrivningskostnadene er ført på tjenestefunksjon"
                            , Constants.CRITICAL_ERROR
                    ));
                }

                // 4) Sum funksjoner (D.100..D.850)+(D.870..D.899), art 990 = 0
                int motpostAvskrivninger = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                                && p.getFieldAsString("art_sektor").equalsIgnoreCase("990")
                                && !p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("860")
                        )
                        .map(p -> p.getFieldAsInteger("belop"))
                        .reduce(0, Integer::sum);

                if (!(motpostAvskrivninger == 0)) {
                    er.addEntry(new ErrorReportEntry(
                            saksbehandler, "Summeringskontroller", " ", " "
                            , "Kontroll Avskrivninger"
                            , "Rett opp i fila slik at motpost avskrivninger er ført på funksjon 860, art 990."
                            , Constants.CRITICAL_ERROR
                    ));
                }
            }
        }

        // Kontroll Funksjon 290
        //Denne kontrollen gjelder IKKE for bydelene i Oslo
        //D = Driftsregnskap (kontoklasse 1)
        //I = Investeringsregnskap (kontoklasse 0)
        if (List.of("0A", "0M").contains(args.getSkjema())) {
            if (!osloBydeler.contains(args.getRegion())) {
                // 1) Funksjon I.290 for sum artene 010-590 + funksjon I.290 for sum artene 600-990  = < 30 og > - 30. Differanser opptil +30' godtas, og skal ikke utlistes.
                int funksjon290Investering = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("I"))
                                && p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("290"))
                        .map(p -> p.getFieldAsInteger("belop"))
                        .reduce(0, Integer::sum);

                if (!(Between.betweenInclusive(funksjon290Investering, -30, 30))) {
                    er.addEntry(new ErrorReportEntry(
                            saksbehandler, "Summeringskontroller", " ", " "
                            , "Kontroll Funksjon 290"
                            , "Rett opp i fila slik at funksjon 290 går i 0 i investeringsregnskapet."
                            , Constants.CRITICAL_ERROR
                    ));
                }

                // 2) Funksjon D.290 for sum artene 010-590 + funksjon D.290 for sum artene 600-990 = < 30 og > - 30. Differanser opptil +30' godtas, og skal ikke utlistes.
                int funksjon290Drift = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                                && p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("290"))
                        .map(p -> p.getFieldAsInteger("belop"))
                        .reduce(0, Integer::sum);

                if (!(Between.betweenInclusive(funksjon290Drift, -30, 30))) {
                    er.addEntry(new ErrorReportEntry(
                            saksbehandler, "Summeringskontroller", " ", " "
                            , "Kontroll Funksjon 290"
                            , "Rett opp i fila slik at funksjon 290 går i 0 i driftsregnskapet."
                            , Constants.CRITICAL_ERROR
                    ));
                }
            }
        }

        // Kontroll Funksjon 465
        //D = Driftsregnskap (kontoklasse 1)
        //I = Investeringsregnskap (kontoklasse 0)
        if (List.of("0C", "0P").contains(args.getSkjema())) {
            // 1) Funksjon I.465 for sum artene 010-590 + funksjon I.465 for sum artene 600-990  = < 30 og > - 30. Differanser opptil +30' godtas, og skal ikke utlistes.
            int funksjon465Investering = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("I"))
                            && p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("465"))
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            if (!(Between.betweenInclusive(funksjon465Investering, -30, 30))) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, "Summeringskontroller", " ", " "
                        , "Kontroll Funksjon 465"
                        , "Rett opp i fila slik at funksjon 465 går i 0 i investeringsregnskapet."
                        , Constants.CRITICAL_ERROR
                ));
            }

            // 2) Funksjon D.465 for sum artene 010-590 + funksjon D.465 for sum artene 600-990 = < 30 og > - 30. Differanser opptil +-30' godtas, og skal ikke utlistes.
            int funksjon465Drift = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                            && p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("465"))
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            if (!(Between.betweenInclusive(funksjon465Drift, -30, 30))) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, "Summeringskontroller", " ", " "
                        , "Kontroll Funksjon 465"
                        , "Rett opp i fila slik at funksjon 465 går i 0 i driftsregnskapet."
                        , Constants.CRITICAL_ERROR
                ));
            }
        }

        // Kontroll Memoriakonti
        //B = Balanseregnskap (kontoklasse 2 / 5)
        if (balanseRegnskapList.contains(args.getSkjema())) {
            int sumMemoriaKonti = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("B"))
                            && List.of("9100", "9110", "9200").contains(p.getFieldAsTrimmedString("funksjon_kapittel"))
                    )
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            int sumMotkontoMemoriaKonti = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("B"))
                            && List.of("9999").contains(p.getFieldAsTrimmedString("funksjon_kapittel"))
                    )
                    .map(p -> p.getFieldAsInteger("belop"))
                    .reduce(0, Integer::sum);

            int differanse = sumMemoriaKonti + sumMotkontoMemoriaKonti;

            if (!(Between.betweenInclusive(differanse, -10, 10))) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, "Summeringskontroller", " ", " "
                        , "Kontroll Memoriakonti"
                        , "Rett opp i fila slik at differansen (" + differanse + ") mellom "
                        + "memoriakontiene (" + sumMemoriaKonti + ") og "
                        + "motkonto for memoriakontiene (" + sumMotkontoMemoriaKonti + ") går i 0"
                        , Constants.CRITICAL_ERROR
                ));
            }
        }

        return er;
    }
}
