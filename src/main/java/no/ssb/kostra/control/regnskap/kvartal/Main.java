package no.ssb.kostra.control.regnskap.kvartal;

import no.ssb.kostra.control.felles.*;
import no.ssb.kostra.control.regnskap.FieldDefinitions;
import no.ssb.kostra.control.felles.ControlIntegritet;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.*;

import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class Main {

    public static ErrorReport doControls(final Arguments args) {

        // fjern kvartalsdelen av skjemanummeret da kvartalet også kommer som et eget argument
        args.setSkjema(args.getSkjema().substring(0, 2));

        if (args.getRegion().endsWith("0000")) {
            if (args.getSkjema().equalsIgnoreCase("0A")) {
                args.setSkjema("0C");
            } else if (args.getSkjema().equalsIgnoreCase("0B")) {
                args.setSkjema("0D");
            }
        }

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
        final var bevilgningRegnskapList = List.of("0A", "0C");
        final var balanseRegnskapList = List.of("0B", "0D");

        final var saksbehandler = "Filuttrekk";
        final var n = regnskap.size();
        final var l = String.valueOf(n).length();

        // integritetskontroller
        ControlIntegritet.doControl(
                regnskap,
                errorReport,
                args,
                bevilgningRegnskapList,
                balanseRegnskapList,
                Definitions.getKontoklasseAsList(args.getSkjema()),
                Definitions.getFunksjonKapittelAsList(args.getSkjema(), args.getRegion()),
                Definitions.getArtSektorAsList(args.getSkjema(), args.getRegion()));

        // Kombinasjonskontroller, per record
        regnskap.forEach(currentRecord -> {
            if (Comparator.isCodeInCodeList(args.getSkjema(), bevilgningRegnskapList)) {
                if (Comparator.isCodeInCodeList(currentRecord.getFieldAsString("kontoklasse"), Definitions.getKontoklasseAsMap(args.getSkjema()).get("D"))) {
                    // driftsregnskapet
                    final var gyldigeDriftFunksjoner = Definitions.getSpesifikkeFunksjoner(args.getSkjema(), args.getRegion(), currentRecord.getFieldAsString("kontoklasse"));
                    ControlFelt1InneholderKodeFraKodeliste.doControl(
                            errorReport,
                            new ErrorReportEntry(saksbehandler, Utils.createLinenumber(l, currentRecord), " ", " "
                                    , "Kontroll Kombinasjon i driftsregnskapet, kontoklasse og funksjon"
                                    , "Korrigér ugyldig funksjon (" + currentRecord.getFieldAsTrimmedString("funksjon_kapittel") + ") til en gyldig funksjon i driftsregnskapet, én av ("
                                    + gyldigeDriftFunksjoner
                                    + "), eller overfør posteringen til investeringsregnskapet."
                                    , Constants.NORMAL_ERROR
                            ),
                            currentRecord.getFieldAsString("funksjon_kapittel"),
                            gyldigeDriftFunksjoner);

                    List<String> gyldigeDriftArter = Definitions.getSpesifikkeArter(args.getSkjema(), args.getRegion(), currentRecord.getFieldAsString("kontoklasse"));
                    ControlFelt1InneholderKodeFraKodeliste.doControl(
                            errorReport,
                            new ErrorReportEntry(saksbehandler, Utils.createLinenumber(l, currentRecord), " ", " ",
                                    "Kontroll Kombinasjon i driftsregnskapet, kontoklasse og art",
                                    "Korrigér ugyldig art (" + currentRecord.getFieldAsTrimmedString("art_sektor") + ") til en gyldig art i driftsregnskapet, én av ("
                                            + gyldigeDriftArter
                                            + "), eller overfør posteringen til investeringsregnskapet.",
                                    Constants.NORMAL_ERROR
                            ),
                            currentRecord.getFieldAsString("art_sektor"),
                            gyldigeDriftArter);

                } else if (Comparator.isCodeInCodeList(currentRecord.getFieldAsString("kontoklasse"), Definitions.getKontoklasseAsMap(args.getSkjema()).get("I"))) {
                    final var gyldigeInvesteringArter = Definitions.getSpesifikkeArter(args.getSkjema(), args.getRegion(), currentRecord.getFieldAsString("kontoklasse"));
                    ControlFelt1InneholderKodeFraKodeliste.doControl(
                            errorReport,
                            new ErrorReportEntry(saksbehandler, Utils.createLinenumber(l, currentRecord), " ", " "
                                    , "Kontroll Kombinasjon i investeringsregnskapet, kontoklasse og art"
                                    , "Korrigér ugyldig art (" + currentRecord.getFieldAsTrimmedString("art_sektor") + ") til en gyldig art i investeringsregnskapet, én av ("
                                    + gyldigeInvesteringArter
                                    + "), eller overfør posteringen til driftsregnskapet."
                                    , Constants.NORMAL_ERROR
                            ),
                            currentRecord.getFieldAsString("art_sektor"),
                            gyldigeInvesteringArter);
                }
            }
        });
        return errorReport;
    }
}
