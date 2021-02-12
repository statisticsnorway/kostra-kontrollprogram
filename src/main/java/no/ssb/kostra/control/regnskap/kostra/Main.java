package no.ssb.kostra.control.regnskap.kostra;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.felles.*;
import no.ssb.kostra.control.regnskap.FieldDefinitions;
import no.ssb.kostra.control.regnskap.felles.ControlIntegritet;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.utils.Between;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Main {
    public static ErrorReport doControls(Arguments args) {
        ErrorReport er = new ErrorReport(args);
        List<String> list1 = args.getInputContentAsStringList();

        // alle records må være med korrekt lengde, ellers vil de andre kontrollene kunne feile
        // Kontroll Recordlengde
        ControlRecordLengde.doControl(list1, er, FieldDefinitions.getFieldLength());

        if (er.getErrorType() == Constants.CRITICAL_ERROR) {
            return er;
        }

        List<FieldDefinition> fieldDefinitions = Utils.mergeFieldDefinitionsAndArguments(FieldDefinitions.getFieldDefinitions(), args);
        List<Record> regnskap = Utils.getValidRecords(list1, fieldDefinitions);
        List<String> bevilgningRegnskapList = List.of("0A", "0C", "0I", "0K", "0M", "0P");
        List<String> regionaleBevilgningRegnskapList = List.of("0A", "0C", "0M", "0P");
        List<String> balanseRegnskapList = List.of("0B", "0D", "0J", "0L", "0N", "0Q");
        List<String> osloBydeler = List.of(
                "030101", "030102", "030103", "030104", "030105",
                "030106", "030107", "030108", "030109", "030110",
                "030111", "030112", "030113", "030114", "030115"
        );
        List<String> svalbard = List.of("211100");
        String kombinasjonskontroller = "4. Kombinasjonskontroller";
        String summeringskontroller = "5. Summeringskontroller";
        Integer n = regnskap.size();
        int l = String.valueOf(n).length();

        // filbeskrivelsesskontroller
        ControlFilbeskrivelse.doControl(regnskap, er);

        // integritetskontroller
        ControlIntegritet.doControl(regnskap, er, l, args, bevilgningRegnskapList, balanseRegnskapList
                , Definitions.getKontoklasseAsList(args.getSkjema())
                , Definitions.getFunksjonKapittelAsList(args.getSkjema(), args.getRegion())
                , Definitions.getArtSektorAsList(args.getSkjema(), args.getRegion())
        );


        // Kombinasjonskontroller, per record
        regnskap.forEach(p -> {
            // Kontroller for records i bevilgningsregnskapet
            if (Comparator.isCodeInCodelist(args.getSkjema(), bevilgningRegnskapList)) {
                if (p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(args.getSkjema()).get("D"))) {
                    // driftsregnskapet
                    List<String> gyldigeDriftFunksjoner = Definitions.getSpesifikkeFunksjoner(args.getSkjema(), args.getRegion(), p.getFieldAsString("kontoklasse"));
                    ControlFelt1InneholderKodeFraKodeliste.doControl(
                            p
                            , er
                            , new ErrorReportEntry(kombinasjonskontroller, Utils.createLinenumber(l, p), " ", " "
                                    , "Kontroll Kombinasjon i driftsregnskapet, kontoklasse og funksjon"
                                    , "Korrigér ugyldig funksjon (" + p.getFieldAsTrimmedString("funksjon_kapittel") + ") til en gyldig funksjon i driftsregnskapet, én av ("
                                    + gyldigeDriftFunksjoner
                                    + "), eller overfør posteringen til investeringsregnskapet."
                                    , Constants.CRITICAL_ERROR
                            )
                            , "funksjon_kapittel"
                            , gyldigeDriftFunksjoner);

                    List<String> gyldigeDriftArter = Definitions.getSpesifikkeArter(args.getSkjema(), args.getRegion(), p.getFieldAsString("kontoklasse"));
                    ControlFelt1InneholderKodeFraKodeliste.doControl(
                            p
                            , er
                            , new ErrorReportEntry(kombinasjonskontroller, Utils.createLinenumber(l, p), " ", " "
                                    , "Kontroll Kombinasjon i driftsregnskapet, kontoklasse og art"
                                    , "Korrigér ugyldig art (" + p.getFieldAsTrimmedString("art_sektor") + ") til en gyldig art i driftsregnskapet, én av ("
                                    + gyldigeDriftArter
                                    + "), eller overfør posteringen til investeringsregnskapet."
                                    , Constants.CRITICAL_ERROR
                            )
                            , "art_sektor"
                            , gyldigeDriftArter);

                    if (Arrays.asList("0A", "0M").contains(args.getSkjema())) {
                        ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                                p
                                , er
                                , new ErrorReportEntry(kombinasjonskontroller, Utils.createLinenumber(l, p), " ", " "
                                        , "Kontroll Kombinasjon i driftsregnskapet kontoklasse, funksjon og art"
                                        , "Korrigér til riktig kombinasjon av kontoklasse, funksjon og art. "
                                        + "I driftsregnskapet, kontoklasse " + p.getFieldAsString("kontoklasse") + ", er artene 874 og 875 kun tillat brukt i kombinasjon med funksjon 800"
                                        , Constants.CRITICAL_ERROR
                                )
                                , "art_sektor"
                                , List.of("874", "875")
                                , "funksjon_kapittel"
                                , List.of("800 "));
                    }

                } else if (p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(args.getSkjema()).get("I"))) {
                    // investeringsregnskapet
                    List<String> gyldigeInvesteringFunksjoner = Definitions.getSpesifikkeFunksjoner(args.getSkjema(), args.getRegion(), p.getFieldAsString("kontoklasse"));
                    ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                            p
                            , er
                            , new ErrorReportEntry(kombinasjonskontroller, Utils.createLinenumber(l, p), " ", " "
                                    , "Kontroll Kombinasjon i investeringsregnskapet, kontoklasse og funksjon"
                                    , "Korrigér ugyldig funksjon (" + p.getFieldAsTrimmedString("funksjon_kapittel") + ") til en gyldig funksjon i investeringsregnskapet, én av ("
                                    + gyldigeInvesteringFunksjoner
                                    + "), eller overfør posteringen til driftsregnskapet. "
                                    + "I driftsregnskapet skal mva-kompensasjon (art 729) føres på funksjonen hvor utgiften oppstod."
                                    , Constants.CRITICAL_ERROR
                            )
                            , "funksjon_kapittel"
                            , gyldigeInvesteringFunksjoner
                            , "kontoklasse"
                            , List.of(Definitions.getKontoklasseAsMap(args.getSkjema()).get("I")));

                    List<String> gyldigeInvesteringArter = Definitions.getSpesifikkeArter(args.getSkjema(), args.getRegion(), p.getFieldAsString("kontoklasse"));
                    ControlFelt1InneholderKodeFraKodeliste.doControl(
                            p
                            , er
                            , new ErrorReportEntry(kombinasjonskontroller, Utils.createLinenumber(l, p), " ", " "
                                    , "Kontroll Kombinasjon i investeringsregnskapet, kontoklasse og art"
                                    , "Korrigér ugyldig art (" + p.getFieldAsTrimmedString("art_sektor") + ") til en gyldig art i investeringsregnskapet, én av ("
                                    + gyldigeInvesteringArter
                                    + "), eller overfør posteringen til driftsregnskapet."
                                    , Constants.CRITICAL_ERROR
                            )
                            , "art_sektor"
                            , gyldigeInvesteringArter);


                    if (p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(args.getSkjema()).get("I"))) {
                        ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                                p
                                , er
                                , new ErrorReportEntry(kombinasjonskontroller, Utils.createLinenumber(l, p), " ", " "
                                        , "Kontroll Kombinasjon i investeringsregnskapet, kontoklasse, funksjon og art"
                                        , "Korrigér til riktig kombinasjon av kontoklasse, funksjon og art. "
                                        + "Art 729 er kun gyldig i kombinasjon funksjon 841 i investeringsregnskapet. "
                                        + "Mva-kompensasjonen for anskaffelser i investeringsregnskapet er frie midler som skal "
                                        + "benyttes til felles finansiering av investeringer i bygninger, anlegg og andre varige driftsmidler."
                                        , Constants.CRITICAL_ERROR
                                )
                                , "art_sektor"
                                , List.of("729")
                                , "funksjon_kapittel"
                                , List.of("841 "));
                    }
                }

                // Artene 589, 980 og 989 er kun tillat brukt i kombinasjon med funksjon 899.
                // Kontrollen må kjøres 2 ganger ettersom den ikke støtter gjensidighet
                ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(kombinasjonskontroller, Utils.createLinenumber(l, p), " ", " "
                                , "Kontroll Kombinasjon funksjon og art"
                                , "Artene 589, 980 og 989 er kun tillat brukt i kombinasjon med funksjon 899. "
                                + "Og motsatt, funksjon 899 er kun tillat brukt i kombinasjon med artene 589, 980 og 989"
                                , Constants.CRITICAL_ERROR
                        )
                        , "art_sektor"
                        , List.of("589", "980", "989")
                        , "funksjon_kapittel"
                        , List.of("899 "));

                ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(kombinasjonskontroller, Utils.createLinenumber(l, p), " ", " "
                                , "Kontroll Kombinasjon funksjon og art"
                                , "Artene 589, 980 og 989 er kun tillat brukt i kombinasjon med funksjon 899. "
                                + "Og motsatt, funksjon 899 er kun tillat brukt i kombinasjon med artene 589, 980 og 989"
                                , Constants.CRITICAL_ERROR
                        )
                        , "funksjon_kapittel"
                        , List.of("899 ")
                        , "art_sektor"
                        , List.of("589", "980", "989"));

            }

        });

        // Dublett kontroll
        ControlDubletter.doControl(regnskap, er, List.of("kontoklasse", "funksjon_kapittel", "art_sektor"));


        // SUMMERINGSKONTROLLER

        //Kontroll Summeringskontroller bevilgningsregnskap
        //Denne kontrollen gjelder IKKE for bydelene i Oslo
        //D = Driftsregnskap (kontoklasse 1 / 3)
        //I = Investeringsregnskap (kontoklasse 0 / 4)
        if (Comparator.isCodeInCodelist(args.getSkjema(), bevilgningRegnskapList)) {
            if (!osloBydeler.contains(args.getRegion())) {
                // 1) Investeringsregnskapet må ha utgiftsføringer, dvs. være høyere enn 0
                int sumInvesteringsUtgifter = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(args.getSkjema()).get("I"))
                                && Between.betweenInclusive(p.getFieldAsIntegerDefaultEquals0("art_sektor"), 10, 590))
                        .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                        .reduce(0, Integer::sum);

                if (regionaleBevilgningRegnskapList.contains(args.getSkjema())) {
                    if (!(0 < sumInvesteringsUtgifter)) {
                        er.addEntry(new ErrorReportEntry(
                                summeringskontroller, "Investeringsregnskapet", " ", " "
                                , "Kontroll Summeringskontroller bevilgningsregnskap, utgiftsposteringer investeringsregnskapet"
                                , "Korrigér slik at fila inneholder utgiftsposteringene (" + sumInvesteringsUtgifter + ") i investeringsregnskapet"
                                , Constants.CRITICAL_ERROR
                        ));
                    }
                }

                // 2) Investeringsregnskapet må ha inntekter, dvs. være mindre enn 0
                int sumInvesteringsInntekter = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(args.getSkjema()).get("I"))
                                && Between.betweenInclusive(p.getFieldAsIntegerDefaultEquals0("art_sektor"), 600, 990))
                        .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                        .reduce(0, Integer::sum);

                if (regionaleBevilgningRegnskapList.contains(args.getSkjema())) {
                    if (!(sumInvesteringsInntekter < 0)) {
                        er.addEntry(new ErrorReportEntry(
                                summeringskontroller, "Investeringsregnskapet", " ", " "
                                , "Kontroll Summeringskontroller investeringsregnskap, inntektsposteringene investeringsregnskapet"
                                , "Korrigér slik at fila inneholder inntektsposteringene (" + sumInvesteringsInntekter + ") i investeringsregnskapet"
                                , Constants.CRITICAL_ERROR
                        ));
                    }
                }

                // 3) Sum investering = Sum investeringsutgifter + Sum investeringsinntekter.. Differanser opptil +30' godtas, og skal ikke utlistes.
                int sumInvestering = sumInvesteringsUtgifter + sumInvesteringsInntekter;

                if (!Between.betweenInclusive(sumInvestering, -30, 30)) {
                    er.addEntry(new ErrorReportEntry(
                            summeringskontroller, "Investeringsregnskapet", " ", " "
                            , "Kontroll Summeringskontroller investeringsregnskapet, differanse i investeringsregnskapet"
                            , "Korrigér differansen (" + sumInvestering + ") mellom "
                            + "inntekter (" + sumInvesteringsInntekter + ") og "
                            + "utgifter (" + sumInvesteringsUtgifter + ") i investeringsregnskapet"
                            , Constants.CRITICAL_ERROR
                    ));
                }

                // 4) Driftsregnskapet må ha utgiftsføringer, dvs. være høyere enn 0
                int sumDriftsUtgifter = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(args.getSkjema()).get("D"))
                                && Between.betweenInclusive(p.getFieldAsIntegerDefaultEquals0("art_sektor"), 10, 590))
                        .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                        .reduce(0, Integer::sum);

                if (Comparator.isCodeInCodelist(args.getSkjema(), List.of("0A", "0C", "0I", "0M", "0P"))) {
                    if (!(0 < sumDriftsUtgifter)) {
                        er.addEntry(new ErrorReportEntry(
                                summeringskontroller, "Driftsregnskapet", " ", " "
                                , "Kontroll Summeringskontroller bevilgningsregnskap, utgiftsposteringer driftsregnskapet"
                                , "Korrigér slik at fila inneholder utgiftsposteringene (" + sumDriftsUtgifter + ") i driftsregnskapet"
                                , Constants.CRITICAL_ERROR
                        ));
                    }
                }

                // 5) Driftsregnskapet må ha inntektsføringer, dvs. være mindre enn 0
                int sumDriftsInntekter = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(args.getSkjema()).get("D"))
                                && Between.betweenInclusive(p.getFieldAsIntegerDefaultEquals0("art_sektor"), 600, 990))
                        .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                        .reduce(0, Integer::sum);

                if (Comparator.isCodeInCodelist(args.getSkjema(), List.of("0A", "0C", "0I", "0M", "0P"))) {
                    if (!(sumDriftsInntekter < 0)) {
                        er.addEntry(new ErrorReportEntry(
                                summeringskontroller, "Driftsregnskapet", " ", " "
                                , "Kontroll Summeringskontroller bevilgningsregnskap, inntektsposteringer driftsregnskapet"
                                , "Korrigér slik at fila inneholder inntektsposteringene (" + sumDriftsInntekter + ") i driftsregnskapet"
                                , Constants.CRITICAL_ERROR
                        ));
                    }
                }

                // 6) Sum drift = Sum driftsutgifter + Sum driftsinntekter.. Differanser opptil +30' godtas, og skal ikke utlistes.
                int sumDrift = sumDriftsUtgifter + sumDriftsInntekter;

                if (!Between.betweenInclusive(sumDrift, -30, 30)) {
                    er.addEntry(new ErrorReportEntry(
                            summeringskontroller, "Driftsregnskapet", " ", " "
                            , "Kontroll Summeringskontroller bevilgningsregnskap, differanse i driftsregnskapet"
                            , "Korrigér differansen (" + sumDrift + ") mellom "
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
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(args.getSkjema()).get("B"))
                            && Between.betweenInclusive(p.getFieldAsIntegerDefaultEquals0("funksjon_kapittel"), 10, 28))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            if (!(0 < sumAktiva)) {
                er.addEntry(new ErrorReportEntry(
                        summeringskontroller, "Balanseregnskap", " ", " "
                        , "Kontroll Summeringskontroller balanseregnskap, registrering av aktiva"
                        , "Korrigér slik at fila inneholder registrering av aktiva (" + sumAktiva + ") i balanse."
                        , Constants.CRITICAL_ERROR
                ));
            }

            // 2) Balanse må ha føring på passiva, dvs. være mindre enn 0
            int sumPassiva = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(args.getSkjema()).get("B"))
                            && Between.betweenInclusive(p.getFieldAsIntegerDefaultEquals0("funksjon_kapittel"), 31, 5990))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            if (!(sumPassiva < 0)) {
                er.addEntry(new ErrorReportEntry(
                        summeringskontroller, "Balanseregnskap", " ", " "
                        , "Kontroll Summeringskontroller balanseregnskap, registrering av passiva"
                        , "Korrigér slik at fila inneholder registrering av passiva (" + sumPassiva + ") i balanse."
                        , Constants.CRITICAL_ERROR
                ));
            }

            // 3) Aktiva skal være lik passiva. Differanser opptil ±10' godtas, og skal ikke utlistes.
            int sumBalanse = sumAktiva + sumPassiva;

            if (!Between.betweenInclusive(sumBalanse, -10, 10)) {
                er.addEntry(new ErrorReportEntry(
                        summeringskontroller, "Balanseregnskap", " ", " "
                        , "Kontroll Summeringskontroller balanseregnskap, differanse"
                        , "Korrigér differansen (" + sumBalanse + ") mellom "
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
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(args.getSkjema()).get("D"))
                                && p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("800")
                                && p.getFieldAsString("art_sektor").equalsIgnoreCase("870"))
                        .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                        .reduce(0, Integer::sum);

                if (!(sumSkatteInntekter < 0)) {
                    er.addEntry(new ErrorReportEntry(
                            summeringskontroller, "Skatteinntekter og rammetilskudd", " ", " "
                            , "Kontroll Skatteinntekter og rammetilskudd, skatteinntekter"
                            , "Korrigér slik at fila inneholder skatteinntekter (" + sumSkatteInntekter + ")."
                            , Constants.CRITICAL_ERROR
                    ));
                }

                // 2) Skatteinntekter skal føres på funksjon 800. Ingen andre funksjoner skal være gyldig i kombinasjon med art 870.
                int sumSkatteInntekterAndreFunksjoner = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(args.getSkjema()).get("D"))
                                && !p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("800")
                                && p.getFieldAsString("art_sektor").equalsIgnoreCase("870"))
                        .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                        .reduce(0, Integer::sum);

                if (!(sumSkatteInntekterAndreFunksjoner == 0)) {
                    er.addEntry(new ErrorReportEntry(
                            summeringskontroller, "Skatteinntekter og rammetilskudd", " ", " "
                            , "Kontroll Skatteinntekter og rammetilskudd, funksjon 800, art 870"
                            , "Korrigér slik at skatteinntekter kun er ført på funksjon 800, art 870. "
                            + "Sum skatteinntekter fra andre funksjoner/art870 er (" + sumSkatteInntekterAndreFunksjoner + ")"
                            , Constants.CRITICAL_ERROR
                    ));
                }

                // 3) Rammetilskudd skal føres på funksjon 840. Ingen andre funksjoner skal være gyldig i kombinasjon med art 800.
                int sumRammetilskuddAndreFunksjoner = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(args.getSkjema()).get("D"))
                                && !p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("840")
                                && p.getFieldAsString("art_sektor").equalsIgnoreCase("800"))
                        .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                        .reduce(0, Integer::sum);

                if (!(sumRammetilskuddAndreFunksjoner == 0)) {
                    er.addEntry(new ErrorReportEntry(
                            summeringskontroller, "Skatteinntekter og rammetilskudd", " ", " "
                            , "Kontroll Skatteinntekter og rammetilskudd, art 800"
                            , "Korrigér slik at rammetilskudd kun er ført på funksjon 840, art 800. "
                            + "Sum rammetilskudd fra andre funksjoner/art800 er (" + sumRammetilskuddAndreFunksjoner + ")"
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
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(args.getSkjema()).get("D"))
                                && p.getFieldAsString("art_sektor").equalsIgnoreCase("570"))
                        .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                        .reduce(0, Integer::sum);

                int investeringsoverforinger = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(args.getSkjema()).get("I"))
                                && p.getFieldAsString("art_sektor").equalsIgnoreCase("970"))
                        .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                        .reduce(0, Integer::sum);

                int differanse = driftsoverforinger + investeringsoverforinger;

                if (!Between.betweenInclusive(differanse, -30, 30)) {
                    er.addEntry(new ErrorReportEntry(
                            summeringskontroller, "Overføringer", " ", " "
                            , "Kontroll Overføring mellom drifts- og investeringsregnskap"
                            , "Korrigér i fila slik at differansen (" + differanse
                            + ") i overføringer mellom drifts- (" + driftsoverforinger
                            + ") og investeringsregnskapet (" + investeringsoverforinger + ") stemmer overens."
                            , Constants.CRITICAL_ERROR
                    ));
                }
            }
        }

        // Kontroll Avskrivninger
        //Denne kontrollen gjelder IKKE for bydelene i Oslo
        //D = Driftsregnskap (kontoklasse 1 / 3)
        //I = Investeringsregnskap (kontoklasse 0 / 4)
        List<String> exceptionsOrgnr = List.of(
                "958935420" // 0301 Oslo
                , "921234554" // 3005 Drammen
                , "964338531" // 4601 Bergen
                , "817920632" // 5000 Sør-Trøndelag
        );
        if (Comparator.isCodeInCodelist(args.getSkjema(), bevilgningRegnskapList)) {
            if (!Comparator.isCodeInCodelist(args.getRegion(), osloBydeler) && !Comparator.isCodeInCodelist(args.getOrgnr(), exceptionsOrgnr)) {
                int motpostAvskrivninger = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(args.getSkjema()).get("D"))
                                && p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("860")
                                && p.getFieldAsString("art_sektor").equalsIgnoreCase("990")
                        )
                        .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                        .reduce(0, Integer::sum);

                if (motpostAvskrivninger == 0) {
                    // For 0I og 0K: Myk kontroll / NORMAL_ERROR, For alle andre: hard kontroll / CRITICAL_ERROR
                    int errorType = (Comparator.isCodeInCodelist(args.getSkjema(), List.of("0I", "0K")))? Constants.NORMAL_ERROR : Constants.CRITICAL_ERROR;

                    er.addEntry(new ErrorReportEntry(
                            summeringskontroller, "Avskrivninger", " ", " "
                            , "Kontroll Avskrivninger, motpost avskrivninger"
                            , "Korrigér i fila slik at den inneholder motpost avskrivninger (" + motpostAvskrivninger + "), føres på funksjon 860 og art 990."
                            , errorType
                    ));
                }

                int avskrivninger = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(args.getSkjema()).get("D"))
                                && Between.betweenInclusive(p.getFieldAsIntegerDefaultEquals0("funksjon_kapittel"), 100, 799)
                                && p.getFieldAsString("art_sektor").equalsIgnoreCase("590"))
                        .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                        .reduce(0, Integer::sum);

                if (avskrivninger == 0) {
                    er.addEntry(new ErrorReportEntry(
                            summeringskontroller, "Avskrivninger", " ", " "
                            , "Kontroll Avskrivninger, avskrivninger"
                            , "Korrigér i fila slik at den inneholder avskrivninger (" + avskrivninger + "), føres på tjenestefunksjon og art 590."
                            , Constants.NORMAL_ERROR
                    ));
                }

                int differanse = motpostAvskrivninger + avskrivninger;

                if (!Between.betweenInclusive(differanse, -30, 30)) {
                    er.addEntry(new ErrorReportEntry(
                            summeringskontroller, "Avskrivninger", " ", " "
                            , "Kontroll Avskrivninger, differanse"
                            , "Korrigér i fila slik at avskrivninger (" + avskrivninger + ") stemmer overens med motpost avskrivninger (" + motpostAvskrivninger + ") (margin på +/- 30')"
                            , Constants.CRITICAL_ERROR
                    ));
                }

                int avskrivningerAndreFunksjoner = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(args.getSkjema()).get("D"))
                                && Between.betweenInclusive(p.getFieldAsIntegerDefaultEquals0("funksjon_kapittel"), 800, 899)
                                && p.getFieldAsString("art_sektor").equalsIgnoreCase("590")
                        )
                        .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                        .reduce(0, Integer::sum);

                if (!(avskrivningerAndreFunksjoner == 0)) {
                    er.addEntry(new ErrorReportEntry(
                            summeringskontroller, "Avskrivninger", " ", " "
                            , "Kontroll Avskrivninger, avskrivninger ført på andre funksjoner"
                            , "Korrigér i fila slik at avskrivningene føres på tjenestefunksjon. Avskrivninger fra andre funksjoner/art590 er (" + avskrivningerAndreFunksjoner + ")"
                            , Constants.CRITICAL_ERROR
                    ));
                }

                int motpostAvskrivningerAndreFunksjoner = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(args.getSkjema()).get("D"))
                                && !p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("860")
                                && p.getFieldAsString("art_sektor").equalsIgnoreCase("990")
                        )
                        .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                        .reduce(0, Integer::sum);

                if (!(motpostAvskrivningerAndreFunksjoner == 0)) {
                    er.addEntry(new ErrorReportEntry(
                            summeringskontroller, "Avskrivninger", " ", " "
                            , "Kontroll Avskrivninger, motpost avskrivninger ført på andre funksjoner"
                            , "Korrigér i fila slik at motpost avskrivninger kun er ført på funksjon 860, art 990."
                            + "Sum motpost avskrivninger fra andre funksjoner/art 990 er (" + motpostAvskrivningerAndreFunksjoner + ")"
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
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(args.getSkjema()).get("I"))
                                && p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("290"))
                        .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                        .reduce(0, Integer::sum);

                if (!(Between.betweenInclusive(funksjon290Investering, -30, 30))) {
                    er.addEntry(new ErrorReportEntry(
                            summeringskontroller, "Funksjon 290", " ", " "
                            , "Kontroll Funksjon 290, investeringsregnskapet"
                            , "Korrigér i fila slik at differanse (" + funksjon290Investering + ") på funksjon 290 interkommunale samarbeid (§27-samarbeid) går i 0 i investeringsregnskapet . (margin på +/- 30')"
                            , Constants.CRITICAL_ERROR
                    ));
                }

                // 2) Funksjon D.290 for sum artene 010-590 + funksjon D.290 for sum artene 600-990 = < 30 og > - 30. Differanser opptil +30' godtas, og skal ikke utlistes.
                int funksjon290Drift = regnskap.stream()
                        .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(args.getSkjema()).get("D"))
                                && p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("290"))
                        .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                        .reduce(0, Integer::sum);

                if (!(Between.betweenInclusive(funksjon290Drift, -30, 30))) {
                    er.addEntry(new ErrorReportEntry(
                            summeringskontroller, "Funksjon 290", " ", " "
                            , "Kontroll Funksjon 290, driftsregnskapet"
                            , "Korrigér i fila slik at differanse (" + funksjon290Drift + ") på funksjon 290 interkommunale samarbeid (§27-samarbeid) går i 0 i driftsregnskapet . (margin på +/- 30')"
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
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(args.getSkjema()).get("I"))
                            && p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("465"))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            if (!(Between.betweenInclusive(funksjon465Investering, -30, 30))) {
                er.addEntry(new ErrorReportEntry(
                        summeringskontroller, "Funksjon 465", " ", " "
                        , "Kontroll Funksjon 465, investeringsregnskapet"
                        , "Korrigér i fila slik at differanse (" + funksjon465Investering + ") på "
                        + "funksjon 465 Interfylkeskommunale samarbeid (§§ 27/28a-samarbeid) går i 0 i investeringsregnskapet . (margin på +/- 30')"
                        , Constants.CRITICAL_ERROR
                ));
            }

            // 2) Funksjon D.465 for sum artene 010-590 + funksjon D.465 for sum artene 600-990 = < 30 og > - 30. Differanser opptil +-30' godtas, og skal ikke utlistes.
            int funksjon465Drift = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(args.getSkjema()).get("D"))
                            && p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("465"))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            if (!(Between.betweenInclusive(funksjon465Drift, -30, 30))) {
                er.addEntry(new ErrorReportEntry(
                        summeringskontroller, "Funksjon 465", " ", " "
                        , "Kontroll Funksjon 465, driftsregnskapet"
                        , "Korrigér i fila slik at differanse (" + funksjon465Drift + ") på "
                        + "funksjon 465 Interfylkeskommunale samarbeid (§§ 27/28a-samarbeid) går i 0 i driftsregnskapet . (margin på +/- 30')"
                        , Constants.CRITICAL_ERROR
                ));
            }
        }

        // Kontroll Memoriakonti
        //B = Balanseregnskap (kontoklasse 2 / 5)
        if (balanseRegnskapList.contains(args.getSkjema())) {
            int sumMemoriaKonti = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(args.getSkjema()).get("B"))
                            && List.of("9100", "9110", "9200").contains(p.getFieldAsTrimmedString("funksjon_kapittel"))
                    )
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            int sumMotkontoMemoriaKonti = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(args.getSkjema()).get("B"))
                            && Objects.equals("9999", p.getFieldAsTrimmedString("funksjon_kapittel"))
                    )
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            int differanse = sumMemoriaKonti + sumMotkontoMemoriaKonti;

            if (!(Between.betweenInclusive(differanse, -10, 10))) {
                er.addEntry(new ErrorReportEntry(
                        summeringskontroller, "Balanseregnskap", " ", " "
                        , "Kontroll Memoriakonti"
                        , "Korrigér i fila slik at differansen (" + differanse + ") mellom "
                        + "memoriakontiene (" + sumMemoriaKonti + ") og "
                        + "motkonto for memoriakontiene (" + sumMotkontoMemoriaKonti + ") går i 0. (margin på +/- 10')"
                        , Constants.CRITICAL_ERROR
                ));
            }
        }

        return er;
    }
}
