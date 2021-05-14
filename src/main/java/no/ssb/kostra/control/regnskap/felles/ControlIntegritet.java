package no.ssb.kostra.control.regnskap.felles;

import no.ssb.kostra.control.felles.*;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.felles.Record;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static no.ssb.kostra.control.felles.Comparator.isCodeInCodelist;

public class ControlIntegritet {
    public static void doControl(List<Record> regnskap, ErrorReport er, int l, Arguments args
            , List<String> bevilgningRegnskapList, List<String> balanseRegnskapList
            , List<String> kontoklasseList, List<String> funksjonkapittelList, List<String> artsektorList
    ) {

        regnskap.forEach(p -> {
            ControlIntegritetFelt1InneholderKode.doControl(er, l, "Skjema", p.getFieldAsString("skjema"), args.getSkjema(), Constants.CRITICAL_ERROR);
            ControlIntegritetFelt1InneholderKode.doControl(er, l, "Årgang", p.getFieldAsString("aargang"), args.getAargang(), Constants.CRITICAL_ERROR);
            ControlIntegritetFelt1InneholderKode.doControl(er, l, "Kvartal", p.getFieldAsString("kvartal"), args.getKvartal(), Constants.CRITICAL_ERROR);
            ControlIntegritetFelt1InneholderKode.doControl(er, l, "Region, fylkeskommune-/kommune-/bydelsnummer", p.getFieldAsString("region"), args.getRegion(), Constants.CRITICAL_ERROR);
            ControlIntegritetFelt1InneholderKodeFraKodeliste.doControl(er, l, "Organisasjonsnummer", p.getFieldAsString("orgnr"), Stream.of(args.getOrgnr().split(",")).collect(Collectors.toList()), Constants.CRITICAL_ERROR);
            ControlIntegritetFelt1InneholderKodeFraKodeliste.doControl(er, l, "Foretaksnummer", p.getFieldAsString("foretaksnr"), Stream.of(args.getForetaknr().concat(",").concat(args.getOrgnr()).split(",")).collect(Collectors.toList()), Constants.CRITICAL_ERROR);
            ControlIntegritetFelt1InneholderKodeFraKodeliste.doControl(er, l, "Kontoklasse", p.getFieldAsString("kontoklasse"), kontoklasseList, Constants.CRITICAL_ERROR);

            if (isCodeInCodelist(args.getSkjema(), bevilgningRegnskapList)) {
                ControlIntegritetFelt1InneholderKodeFraKodeliste.doControl(er, l, "Funksjon", p.getFieldAsString("funksjon_kapittel"), funksjonkapittelList, Constants.CRITICAL_ERROR);
                ControlIntegritetFelt1InneholderKodeFraKodeliste.doControl(er, l, "Art", p.getFieldAsString("art_sektor"), artsektorList, Constants.CRITICAL_ERROR);
            }

            if (isCodeInCodelist(args.getSkjema(), balanseRegnskapList)) {
                ControlIntegritetFelt1InneholderKodeFraKodeliste.doControl(er, l, "Kapittel", p.getFieldAsString("funksjon_kapittel"), funksjonkapittelList, Constants.CRITICAL_ERROR);
                ControlIntegritetFelt1InneholderKodeFraKodeliste.doControl(er, l, "Sektor", p.getFieldAsString("art_sektor"), artsektorList, Constants.CRITICAL_ERROR);
            }

            ControlHeltall.doControl(
                    er
                    , new ErrorReportEntry("3. Integritetskontroller", Utils.createLinenumber(l, p), " ", " "
                            , "Kontroll beløp."
                            , "Korrigér feil beløp (" + p.getFieldAsString("belop") + ")"
                            , Constants.CRITICAL_ERROR
                    )
                    , p.getFieldAsInteger("belop")
            );

            ControlUlovligTegnITallfelt.doControl(
                    er
                    , new ErrorReportEntry("3. Integritetskontroller", Utils.createLinenumber(l, p), " ", " "
                            , "Kontroll beløp, ugyldig tegn i tallfelt"
                            , "Fant tegn for tabulator i tallfeltet. Mellomrom skal brukes for blanke tegn. Korrigér feil beløp (" + p.getFieldAsString("belop") + ")"
                            , Constants.CRITICAL_ERROR
                    )
                    , p.getFieldAsString("belop")
            );
        });
    }
}
