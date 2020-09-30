package no.ssb.kostra.control.regnskap.kvartal;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.felles.*;
import no.ssb.kostra.control.regnskap.FieldDefinitions;
import no.ssb.kostra.control.regnskap.felles.ControlIntegritet;
import no.ssb.kostra.controlprogram.Arguments;

import java.util.List;

public class Main {
    public static ErrorReport doControls(Arguments args) {
        // fjern kvartalsdelen av skjemanummeret da kvartalet også kommer som et eget argument
        args.setSkjema(args.getSkjema().substring(0, 2));

        if (args.getRegion().endsWith("0000")){
            if (args.getSkjema().equalsIgnoreCase("0A")){
                args.setSkjema("0C");
            } else if (args.getSkjema().equalsIgnoreCase("0B")){
                args.setSkjema("0D");
            }

        }

        ErrorReport er = new ErrorReport(args);
        List<String> list1 = args.getInputContentAsStringList();

        // alle records må være med korrekt lengde, ellers vil de andre kontrollene kunne feile
        // Kontroll Recordlengde
        boolean hasErrors = ControlRecordLengde.doControl(list1, er, FieldDefinitions.getFieldLength());

        if (hasErrors) {
            return er;
        }

        List<FieldDefinition> fieldDefinitions = Utils.mergeFieldDefinitionsAndArguments(FieldDefinitions.getFieldDefinitions(), args);
        List<Record> regnskap = Utils.getValidRecords(list1, fieldDefinitions);
        List<String> bevilgningRegnskapList = List.of("0A", "0C");
        List<String> balanseRegnskapList = List.of("0B", "0D");

        String saksbehandler = "Filuttrekk";
        Integer n = regnskap.size();
        int l = String.valueOf(n).length();

        // integritetskontroller
        ControlIntegritet.doControl(regnskap, er, l, args, bevilgningRegnskapList, balanseRegnskapList
                , Definitions.getKontoklasseAsList(args.getSkjema())
                , Definitions.getFunksjonKapittelAsList(args.getSkjema(), args.getRegion())
                , Definitions.getArtSektorAsList(args.getSkjema(), args.getRegion())
        );

        // Kombinasjonskontroller, per record
        regnskap.forEach(p -> {
            if (Comparator.isCodeInCodelist(args.getSkjema(), bevilgningRegnskapList)) {
                if (p.getFieldAsString("kontoklasse").equalsIgnoreCase(no.ssb.kostra.control.regnskap.kostra.Definitions.getKontoklasseAsMap(args.getSkjema()).get("D"))) {
                    // driftsregnskapet
                    List<String> gyldigeDriftFunksjoner = no.ssb.kostra.control.regnskap.kostra.Definitions.getSpesifikkeFunksjoner(args.getSkjema(), args.getRegion(), p.getFieldAsString("kontoklasse"));
                    ControlFelt1InneholderKodeFraKodeliste.doControl(
                            p
                            , er
                            , new ErrorReportEntry(saksbehandler, Utils.createLinenumber(l, p), " ", " "
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
                            , new ErrorReportEntry(saksbehandler, Utils.createLinenumber(l, p), " ", " "
                                    , "Kontroll Kombinasjon i driftsregnskapet, kontoklasse og art"
                                    , "Korrigér ugyldig art (" + p.getFieldAsTrimmedString("art_sektor") + ") til en gyldig art i driftsregnskapet, én av ("
                                    + gyldigeDriftArter
                                    + "), eller overfør posteringen til investeringsregnskapet."
                                    , Constants.CRITICAL_ERROR
                            )
                            , "art_sektor"
                            , gyldigeDriftArter);

                } else if (p.getFieldAsString("kontoklasse").equalsIgnoreCase(Definitions.getKontoklasseAsMap(args.getSkjema()).get("I"))) {
                    List<String> gyldigeInvesteringArter = Definitions.getSpesifikkeArter(args.getSkjema(), args.getRegion(), p.getFieldAsString("kontoklasse"));
                    ControlFelt1InneholderKodeFraKodeliste.doControl(
                            p
                            , er
                            , new ErrorReportEntry(saksbehandler, Utils.createLinenumber(l, p), " ", " "
                                    , "Kontroll Kombinasjon i investeringsregnskapet, kontoklasse og art"
                                    , "Korrigér ugyldig art (" + p.getFieldAsTrimmedString("art_sektor") + ") til en gyldig art i investeringsregnskapet, én av ("
                                    + gyldigeInvesteringArter
                                    + "), eller overfør posteringen til driftsregnskapet."
                                    , Constants.CRITICAL_ERROR
                            )
                            , "art_sektor"
                            , gyldigeInvesteringArter);
                }
            }
        });

        return er;
    }
}
