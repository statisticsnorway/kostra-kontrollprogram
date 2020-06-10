package no.ssb.kostra.control.regnskap.kvartal;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.felles.ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste;
import no.ssb.kostra.control.felles.ControlFelt1InneholderKodeFraKodeliste;
import no.ssb.kostra.control.felles.ControlHeltall;
import no.ssb.kostra.control.felles.ControlRecordLengde;
import no.ssb.kostra.control.regnskap.FieldDefinitions;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.utils.Format;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// TODO dobbeltsjekk denne
public class Main {
    public static ErrorReport doControls(Arguments args) {
        ErrorReport er = new ErrorReport(args);
        List<String> list1 = args.getInputContentAsStringList();
        List<FieldDefinition> fieldDefinitions = FieldDefinitions.getFieldDefinitions();
        List<String> bevilgningRegnskapList = List.of("0A", "0C");
        List<String> balanseRegnskapList = List.of("0B", "0D");
        List<Record> regnskap = list1.stream()
                // fjerner record som inneholder Z, z eller ~
                .filter(p -> !List.of("Z", "z", "~").contains(p))
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
                        , Collections.singletonList(args.getSkjema().substring(1, 2))
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
                                , "Korreksjon: Rett opp til rett kvartal (" + args.getKvartal() + ")"
                                , Constants.CRITICAL_ERROR
                        )
                        , "kvartal"
                        , Collections.singletonList(args.getKvartal())
                ))
                .peek(p -> ControlFelt1InneholderKodeFraKodeliste.doControl(
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
                .peek(p -> {
                    ControlFelt1InneholderKodeFraKodeliste.doControl(
                            p
                            , er
                            , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                    , "Kontroll Organisasjonsnummer."
                                    , "Korreksjon: Organisasjonsnummer skal være 9 blanke tegn / mellomrom. (" + args.getOrgnr() + ")"
                                    , Constants.CRITICAL_ERROR
                            )
                            , "orgnr"
                            , Collections.singletonList("         ")
                    );
                })
                .peek(p -> ControlFelt1InneholderKodeFraKodeliste.doControl(
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
                .peek(p -> ControlFelt1InneholderKodeFraKodeliste.doControl(
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
                .peek(p -> {
                            if (bevilgningRegnskapList.contains(args.getSkjema())) {
                                ControlFelt1InneholderKodeFraKodeliste.doControl(
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
                        }
                )
                .peek(p -> {
                            if (balanseRegnskapList.contains(args.getSkjema())) {
                                ControlFelt1InneholderKodeFraKodeliste.doControl(
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
                        }
                )
                .peek(p -> {
                            if (bevilgningRegnskapList.contains(args.getSkjema())) {
                                ControlFelt1InneholderKodeFraKodeliste.doControl(
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
                        }
                )
                .peek(p -> {
                            if (balanseRegnskapList.contains(args.getSkjema())) {
                                ControlFelt1InneholderKodeFraKodeliste.doControl(
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
                ))
                .close();

        // Kombinasjonskontroller, per record
        regnskap.stream()
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
                                , List.of("529", "670", "910", "911", "929", "970")
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
                                , List.of("509", "570", "590", "800", "870", "874", "875", "877", "909", "990")
                                , "kontoklasse"
                                , List.of(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D")));
                    }
                })
                .peek(p -> {
                    if (bevilgningRegnskapList.contains(args.getSkjema())) {
                        ControlFelt1InneholderKodeFraKodelisteSaaFelt2InneholderKodeFraKodeliste.doControl(
                                p
                                , er
                                , new ErrorReportEntry(saksbehandler, journalnummer + Format.sprintf("%0" + l + "d", p.getLine()), " ", " "
                                        , "Kontroll Kombinasjon kontoklasse og funksjon i driftsregnskapet"
                                        , "Rett opp til funksjon som er gyldig i investeringsregnskapet, eller overfør posteringen til driftsregnskapet."
                                        , Constants.CRITICAL_ERROR
                                )
                                , "funksjon_kapittel"
                                , List.of("800", "840", "860")
                                , "kontoklasse"
                                , List.of(Definitions.getKontoklasseAsMap(p.getFieldAsString("skjema")).get("D")));
                    }
                })
                .close();

        return er;
    }
}
