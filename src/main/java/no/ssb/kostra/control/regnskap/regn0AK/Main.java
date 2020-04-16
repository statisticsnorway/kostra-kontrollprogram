package no.ssb.kostra.control.regnskap.regn0AK;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.felles.ControlFeltInneholderKodeFraKodeliste;
import no.ssb.kostra.control.felles.ControlRecordLengde;
import no.ssb.kostra.controlprogram.Arguments;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    Arguments args;
    ErrorReport er;
    List<String> list1;

    public Main(Arguments args) {
        this.args = args;
        er = new ErrorReport(this.args);
        list1 = this.args.getInputFileContent();
    }

    public ErrorReport doControls() {
        List<FieldDefinition> fieldDefinitions = FieldDefinitionFileReader.getFieldDefinition("/filbeskrivelse_bevilgningsregnskap");

        List<Record> regnskap = list1.stream()
                .map(p -> new Record(p, fieldDefinitions))
                .map(p -> ControlRecordLengde.doControl(
                        p
                        , this.er
                        , new ErrorReportEntry(" ", " ", " ", String.valueOf(p.getLine())
                                , "Kontroll 1, recordlengde"
                                , "Korreksjon: Rett opp slik at alle record er på 48 tegn og avslutter med linjeskift."
                                , Constants.CRITICAL_ERROR
                        )
                        , 48
                ))
                .map(p -> ControlFeltInneholderKodeFraKodeliste.doControl(
                        p
                        , this.er
                        , new ErrorReportEntry(" ", " ", " ", String.valueOf(p.getLine())
                                , "Kontroll 2, Årgang"
                                , "Korreksjon: Rett opp til rett årgang (" + args.getAargang() +")"
                                , Constants.CRITICAL_ERROR
                        )
                        , "aargang"
                        , Arrays.asList(args.getAargang())
                ))
                .map(p -> ControlFeltInneholderKodeFraKodeliste.doControl(
                        p
                        , this.er
                        , new ErrorReportEntry(" ", " ", " ", String.valueOf(p.getLine())
                                , "Kontroll 3, Kvartal"
                                , "Korreksjon: Rett opp til rett kvartal (" + args.getKvartal() +")"
                                , Constants.CRITICAL_ERROR
                        )
                        , "kvartal"
                        , Arrays.asList(args.getKvartal())
                ))
                .map(p -> ControlFeltInneholderKodeFraKodeliste.doControl(
                        p
                        , this.er
                        , new ErrorReportEntry(" ", " ", " ", String.valueOf(p.getLine())
                                , "Kontroll 4, kommunenummeret/bydelsnummeret."
                                , "Korreksjon: Rett kommunenummeret/bydelsnummeret. (" + args.getRegion() +")"
                                , Constants.CRITICAL_ERROR
                        )
                        , "region"
                        , Arrays.asList(args.getRegion())
                ))
                .map(p -> ControlFeltInneholderKodeFraKodeliste.doControl(
                        p
                        , this.er
                        , new ErrorReportEntry(" ", " ", " ", String.valueOf(p.getLine())
                                , "Kontroll 5, Organisasjonsnummer."
                                , "Korreksjon: Organisasjonsnummer skal være 9 blanke tegn / mellomrom. (" + args.getOrgnr() +")"
                                , Constants.CRITICAL_ERROR
                        )
                        , "orgnr"
                        , Arrays.asList("         ")
                ))
                .map(p -> ControlFeltInneholderKodeFraKodeliste.doControl(
                        p
                        , this.er
                        , new ErrorReportEntry(" ", " ", " ", String.valueOf(p.getLine())
                                , "Kontroll 6, Foretaksnummer."
                                , "Korreksjon: Foretaksnummer skal være 9 blanke tegn / mellomrom. (" + args.getForetaknr() +")"
                                , Constants.CRITICAL_ERROR
                        )
                        , "foretaksnr"
                        , Arrays.asList("         ")
                ))
                .collect(Collectors.toList());


        return er;
    }
}
