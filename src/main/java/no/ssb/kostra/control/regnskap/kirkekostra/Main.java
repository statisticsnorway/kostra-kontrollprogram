package no.ssb.kostra.control.regnskap.kirkekostra;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.felles.*;
import no.ssb.kostra.control.regnskap.FieldDefinitions;
import no.ssb.kostra.control.regnskap.felles.ControlIntegritet;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.utils.Between;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static no.ssb.kostra.control.felles.Comparator.isCodeInCodelist;

public class Main {
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
        List<String> bevilgningRegnskapList = List.of("0F");
        List<String> balanseRegnskapList = List.of("0G");
        List<Record> regnskap1 = list1.stream()
                .map(p -> new Record(p, fieldDefinitions))
                .collect(Collectors.toList());
        String saksbehandler = "Filuttrekk";
        Integer n = regnskap1.size();
        int l = String.valueOf(n).length();

        // filbeskrivelsesskontroller
        ControlFilbeskrivelse.doControl(regnskap1, er);

        if (er.getErrorType() == Constants.CRITICAL_ERROR) {
            return er;
        }

        // integritetskontroller
        ControlIntegritet.doControl(regnskap1, er, l, args, bevilgningRegnskapList, balanseRegnskapList
                , Definitions.getKontoklasseAsList(args.getSkjema())
                , Definitions.getFunksjonKapittelAsList(args.getSkjema(), args.getRegion())
                , Definitions.getArtSektorAsList(args.getSkjema(), args.getRegion())
        );

        // Fjerner posteringer der beløp = 0
        List<Record> regnskap = regnskap1.stream()
                // fjerner record der beløpet er 0, brukes ifm. med alle regnskap
                .filter(p -> {
                    try {
                        return p.getFieldAsInteger("belop") != 0;
                    } catch (NullPointerException e) {
                        return true;
                    }
                })
                .collect(Collectors.toList());

        // Kombinasjonskontroller, per record
        regnskap.forEach(p -> {
            if (isCodeInCodelist(args.getSkjema(), bevilgningRegnskapList)) {
                String investeringKontoklasse = Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("I");
                List<String> investeringArtList = Definitions.getSpesifikkeArter(investeringKontoklasse);
                ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, Utils.createLinenumber(l, p), " ", " "
                                , "Kontroll Kombinasjon kontoklasse og art i investeringsregnskapet"
                                , "Korrigér art (" + p.getFieldAsTrimmedString("art_sektor") + ") til gyldig art i investeringsregnskapet (én av " + investeringArtList + "), eller overfør posteringen til driftsregnskapet"
                                , Constants.CRITICAL_ERROR
                        )
                        , "art_sektor"
                        , investeringArtList
                        , "kontoklasse"
                        , List.of(investeringKontoklasse));

                String driftKontoklasse = Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D");
                List<String> driftArtList = Definitions.getSpesifikkeArter(driftKontoklasse);
                ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                        p
                        , er
                        , new ErrorReportEntry(saksbehandler, Utils.createLinenumber(l, p), " ", " "
                                , "Kontroll Kombinasjon kontoklasse og art i driftsregnskapet"
                                , "Korrigér art (" + p.getFieldAsTrimmedString("art_sektor") + ") til gyldig art i driftsregnskapet (én av " + driftArtList + "), eller overfør posteringen til investeringsregnskapet"
//                                , "Korrigér til art som er gyldig i driftsregnskapet, eller overfør posteringen til investeringsregnskapet"
                                , Constants.CRITICAL_ERROR
                        )
                        , "art_sektor"
                        , driftArtList
                        , "kontoklasse"
                        , List.of(driftKontoklasse));
            }
        });


        // Dublett kontroll
        if (isCodeInCodelist(args.getSkjema(), bevilgningRegnskapList)) {
            ControlDubletter.doControl(regnskap, er, List.of("kontoklasse", "funksjon_kapittel", "art_sektor"), List.of("kontoklasse", "funksjon", "art"));
        } else if (isCodeInCodelist(args.getSkjema(), balanseRegnskapList)) {
            ControlDubletter.doControl(regnskap, er, List.of("kontoklasse", "funksjon_kapittel", "art_sektor"), List.of("kontoklasse", "kapittel", "sektor"));
        }


        // SUMMERINGSKONTROLLER

        //Kontroll Summeringskontroller bevilgningsregnskap
        //D = Driftsregnskap (kontoklasse 3)
        //I = Investeringsregnskap (kontoklasse 4)
        if (isCodeInCodelist(args.getSkjema(), bevilgningRegnskapList)) {
            int sumInvesteringsUtgifter = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("I"))
                            && Between.betweenInclusive(p.getFieldAsIntegerDefaultEquals0("art_sektor"), 10, 590))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);


            int sumInvesteringsInntekter = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("I"))
                            && Between.betweenInclusive(p.getFieldAsIntegerDefaultEquals0("art_sektor"), 600, 990))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);


            // 1) Sum investering = Sum investeringsutgifter + Sum investeringsinntekter. Differanser opptil +30' godtas, og skal ikke utlistes.
            int sumInvestering = sumInvesteringsUtgifter + sumInvesteringsInntekter;

            if (!Between.betweenInclusive(sumInvestering, -30, 30)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Summeringskontroller investeringsregnskap, differanse"
                        , "Korrigér differansen (" + sumInvestering + ") mellom inntekter (" + sumInvesteringsInntekter + ") og utgifter (" + sumInvesteringsUtgifter + ") i investeringsregnskapet. Differanse +/- 30' godtas."
                        , Constants.CRITICAL_ERROR
                ));
            }

            // 2) Driftsregnskapet må ha utgiftsføringer, dvs. være høyere enn 0
            int sumDriftsUtgifter = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                            && Between.betweenInclusive(p.getFieldAsIntegerDefaultEquals0("art_sektor"), 10, 590))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            if (!(0 < sumDriftsUtgifter)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Summeringskontroller bevilgningsregnskap, utgifter"
                        , "Korrigér slik at fila inneholder utgiftsposteringene (" + sumDriftsUtgifter + ") i driftsregnskapet"
                        , Constants.CRITICAL_ERROR
                ));
            }

            // 3) Driftsregnskapet må ha inntektsføringer, dvs. være mindre enn 0
            int sumDriftsInntekter = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                            && Between.betweenInclusive(p.getFieldAsIntegerDefaultEquals0("art_sektor"), 600, 990))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            if (!(sumDriftsInntekter < 0)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Summeringskontroller bevilgningsregnskap, inntekter"
                        , "Korrigér slik at fila inneholder inntektsposteringene (" + sumDriftsInntekter + ") i driftsregnskapet"
                        , Constants.CRITICAL_ERROR
                ));
            }

            // 4) Sum drift = Sum driftsutgifter + Sum driftsinntekter.. Differanser opptil +30' godtas, og skal ikke utlistes.
            int sumDrift = sumDriftsUtgifter + sumDriftsInntekter;

            if (!Between.betweenInclusive(sumDrift, -30, 30)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Summeringskontroller bevilgningsregnskap, differanse"
                        , "Korrigér differansen (" + sumDrift + ") mellom utgifter (" + sumDriftsUtgifter + ") og inntekter (" + sumDriftsInntekter + ") i driftsregnskapet.  Differanse +/- 30' godtas."
                        , Constants.CRITICAL_ERROR
                ));
            }

            // 5) Sum art 830 for sum funksjoner 3.089 < 0
            int sumTilskudd = regnskap.stream()
                    .filter(p -> p.getFieldAsString("art_sektor").equalsIgnoreCase("830"))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            if (!(sumTilskudd < 0)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Summeringskontroller, tilskudd"
                        , "Korrigér slik at fila inneholder tilskudd (" + sumTilskudd + ") fra kommunen"
                        , Constants.CRITICAL_ERROR
                ));
            }
        }

        //Kontroll Summeringskontroller balanseregnskap
        //B = Driftsregnskap (kontoklasse 5)
        if (isCodeInCodelist(args.getSkjema(), balanseRegnskapList)) {
            // 1) Balanse må ha føring på aktiva, dvs. være høyere enn 0
            int sumAktiva = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("B"))
                            && Between.betweenInclusive(p.getFieldAsIntegerDefaultEquals0("funksjon_kapittel"), 10, 27))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            if (!(0 < sumAktiva)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Summeringskontroller balanseregnskap, aktiva"
                        , "Korrigér slik at fila inneholder registrering av aktiva (" + sumAktiva + ")"
                        , Constants.CRITICAL_ERROR
                ));
            }

            // 2) Balanse må ha føring på passiva, dvs. være mindre enn 0
            int sumPassiva = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("B"))
                            && Between.betweenInclusive(p.getFieldAsIntegerDefaultEquals0("funksjon_kapittel"), 31, 5990))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            if (!(sumPassiva < 0)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Summeringskontroller balanseregnskap, passiva"
                        , "Korrigér slik at fila inneholder registrering av passiva (" + sumPassiva + ")"
                        , Constants.CRITICAL_ERROR
                ));
            }

            // 3) Aktiva skal være lik passiva. Differanser opptil +10' godtas, og skal ikke utlistes.
            int sumBalanse = sumAktiva + sumPassiva;

            if (!Between.betweenInclusive(sumBalanse, -10, 10)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Summeringskontroller balanseregnskap, differanse"
                        , "Korrigér differansen (" + sumBalanse + ") mellom aktiva (" + sumAktiva + ") og passiva (" + sumPassiva + ") i fila (Differanser opptil +10' godtas)"
                        , Constants.CRITICAL_ERROR
                ));
            }
        }


        // Kontroll Interne overføringer
        //D = Driftsregnskap (kontoklasse 3)
        //I = Investeringsregnskap (kontoklasse 4)
        if (bevilgningRegnskapList.contains(args.getSkjema())) {
            int sumInternKjop = regnskap.stream()
                    .filter(p -> Objects.equals("380", p.getFieldAsString("art_sektor")))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            int sumInternSalg = regnskap.stream()
                    .filter(p -> Objects.equals("780", p.getFieldAsString("art_sektor")))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            int sumInternKjopOgSalg = sumInternKjop + sumInternSalg;

            if (!Between.betweenInclusive(sumInternKjopOgSalg, -30, 30)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Interne overføringer, internkjøp og internsalg"
                        , "Korrigér i fila slik at differansen (" + sumInternKjopOgSalg + ") mellom internkjøp (" + sumInternKjop + ") og internsalg (" + sumInternSalg + ")  stemmer overens (margin på +/- 30')"
                        , Constants.CRITICAL_ERROR
                ));
            }

            int sumKalkulatoriskeUtgifter = regnskap.stream()
                    .filter(p -> Objects.equals("390", p.getFieldAsString("art_sektor")))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            int sumKalkulatoriskeInntekter = regnskap.stream()
                    .filter(p -> Objects.equals("790", p.getFieldAsString("art_sektor")))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            int sumKalkulatoriske = sumKalkulatoriskeUtgifter + sumKalkulatoriskeInntekter;

            if (!Between.betweenInclusive(sumKalkulatoriske, -30, 30)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Interne overføringer, kalkulatoriske utgifter og inntekter"
                        , "Korrigér i fila slik at differansen (" + sumKalkulatoriske + ") mellom kalkulatoriske utgifter (" + sumKalkulatoriskeUtgifter + ") og inntekter (" + sumKalkulatoriskeInntekter + ")ved kommunal tjenesteytelse stemmer overens (margin på +/- 30')"
                        , Constants.CRITICAL_ERROR
                ));
            }

            int sumOverforinger = regnskap.stream()
                    .filter(p -> Objects.equals("465", p.getFieldAsString("art_sektor")))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            int sumInnsamledeMidler = regnskap.stream()
                    .filter(p -> Objects.equals("865", p.getFieldAsString("art_sektor")))
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            int sumMidler = sumOverforinger + sumInnsamledeMidler;

            if (!Between.betweenInclusive(sumMidler, -30, 30)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Interne overføringer, midler"
                        , "Korrigér i fila slik at differansen (" + sumMidler + ") mellom overføringer av midler (" + sumOverforinger + ") og innsamlede midler (" + sumInnsamledeMidler + ") stemmer overens (margin på +/- 30')"
                        , Constants.CRITICAL_ERROR
                ));
            }
        }

        // Kontroll Overføring mellom drifts- og investeringsregnskap
        //D = Driftsregnskap (kontoklasse 3)
        //I = Investeringsregnskap (kontoklasse 4)
        if (bevilgningRegnskapList.contains(args.getSkjema())) {
            int sumDriftsoverforinger = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                            && p.getFieldAsString("art_sektor").equalsIgnoreCase("570")
                    )
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            int sumInvesteringsoverforinger = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("I"))
                            && p.getFieldAsString("art_sektor").equalsIgnoreCase("970")
                    )
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            int sumDifferanse = sumDriftsoverforinger + sumInvesteringsoverforinger;

            if (!Between.betweenInclusive(sumDifferanse, -30, 30)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Overføring mellom drifts- og investeringsregnskap"
                        , "Korrigér i fila slik at differansen (" + sumDifferanse + ") i overføringer mellom drifts- (" + sumDriftsoverforinger + ") og investeringsregnskapet (" + sumInvesteringsoverforinger + ") stemmer overens"
                        , Constants.CRITICAL_ERROR
                ));
            }
        }

        // Kontroll Avskrivninger
        //D = Driftsregnskap (kontoklasse 1 / 3)
        //I = Investeringsregnskap (kontoklasse 0 / 4)
        if (bevilgningRegnskapList.contains(args.getSkjema())) {
            // 1) Art 590 for sum funksjoner D.041-D.089 + art 990 for sum funksjoner D.041-D.089 = < 30 og > - 30. Differanser opptil +30' godtas, og skal ikke utlistes.
            int sumAvskrivninger590 = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                            && Objects.equals("590", p.getFieldAsString("art_sektor"))
                    )
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            int sumMotpostAvskrivninger990 = regnskap.stream()
                    .filter(p -> p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D"))
                            && Objects.equals("990", p.getFieldAsString("art_sektor"))
                    )
                    .map(p -> p.getFieldAsIntegerDefaultEquals0("belop"))
                    .reduce(0, Integer::sum);

            int sumAvskrivninger = sumAvskrivninger590 + sumMotpostAvskrivninger990;

            if (!Between.betweenInclusive(sumAvskrivninger, -30, 30)) {
                er.addEntry(new ErrorReportEntry(
                        saksbehandler, " ", " ", " "
                        , "Kontroll Avskrivninger, art 590, art 990"
                        , "Korrigér i fila slik at differansen (" + sumAvskrivninger + ") mellom art 590 (" + sumAvskrivninger590 + ") stemmer overens med art 990 (" + sumMotpostAvskrivninger990 + ") (margin på +/- 30')"
                        , Constants.CRITICAL_ERROR
                ));
            }
        }

        // Kontroll Funksjon 089
        if (bevilgningRegnskapList.contains(args.getSkjema())) {
            List<String> funksjon089Arter = List.of(
                    "500", "510", "520", "530", "540", "550", "570", "580",
                    "830",
                    "900", "905", "910", "920", "930", "940", "950", "970", "980"
            );

            regnskap.stream()
                    .filter(p -> p.getFieldAsTrimmedString("funksjon_kapittel").equalsIgnoreCase("089"))
                    .forEach(p -> ControlFelt1InneholderKodeFraKodeliste.doControl(
                            p
                            , er
                            , new ErrorReportEntry(saksbehandler, Utils.createLinenumber(l, p), " ", " "
                                    , "Kontroll Funksjon 089"
                                    , "Korrigér i fila slik at art (" + p.getFieldAsString("art_sektor") + ") er gyldig mot funksjon 089. Gyldige arter er: " + funksjon089Arter
                                    , Constants.NORMAL_ERROR
                            )
                            , "art_sektor"
                            , funksjon089Arter)
                    );
        }

        return er;
    }
}
