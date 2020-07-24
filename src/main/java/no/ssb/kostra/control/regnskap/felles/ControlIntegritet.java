package no.ssb.kostra.control.regnskap.felles;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;
import no.ssb.kostra.control.felles.ControlHeltall;
import no.ssb.kostra.control.felles.ControlIntegritetFelt1InneholderKode;
import no.ssb.kostra.control.felles.ControlIntegritetFelt1InneholderKodeFraKodeliste;
import no.ssb.kostra.control.felles.Utils;
import no.ssb.kostra.controlprogram.Arguments;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ControlIntegritet {
    public static void doControl(List<Record> regnskap, ErrorReport er, int l, Arguments args
            , List<String> bevilgningRegnskapList, List<String> balanseRegnskapList
            , List<String> kontoklasseList, List<String> funksjonkapittelList, List<String> artsektorList
    ) {
        regnskap.forEach(p -> {
            ControlIntegritetFelt1InneholderKode.doControl(p, er, l, "Skjema", "skjema", args.getSkjema(), Constants.CRITICAL_ERROR);
            ControlIntegritetFelt1InneholderKode.doControl(p, er, l, "Årgang", "aargang", args.getAargang(), Constants.CRITICAL_ERROR);
            ControlIntegritetFelt1InneholderKode.doControl(p, er, l, "Kvartal", "kvartal", args.getKvartal(), Constants.CRITICAL_ERROR);
            ControlIntegritetFelt1InneholderKode.doControl(p, er, l, "Region, fylkeskommune-/kommune-/bydelsnummer", "region", args.getRegion(), Constants.CRITICAL_ERROR);
            ControlIntegritetFelt1InneholderKodeFraKodeliste.doControl(p, er, l, "Organisasjonsnummer", "orgnr", Stream.of(args.getOrgnr().split(",")).collect(Collectors.toList()), Constants.CRITICAL_ERROR);
            ControlIntegritetFelt1InneholderKodeFraKodeliste.doControl(p, er, l, "Foretaksnummer", "foretaksnr", Stream.of(args.getForetaknr().concat(",").concat(args.getOrgnr()).split(",")).collect(Collectors.toList()), Constants.CRITICAL_ERROR);
            ControlIntegritetFelt1InneholderKodeFraKodeliste.doControl(p, er, l, "Kontoklasse", "kontoklasse", kontoklasseList, Constants.CRITICAL_ERROR);

            if (bevilgningRegnskapList.contains(args.getSkjema())) {
                ControlIntegritetFelt1InneholderKodeFraKodeliste.doControl(p, er, l, "Funksjon", "funksjon_kapittel", funksjonkapittelList, Constants.CRITICAL_ERROR);
                ControlIntegritetFelt1InneholderKodeFraKodeliste.doControl(p, er, l, "Art", "art_sektor", artsektorList, Constants.CRITICAL_ERROR);
            }

            if (balanseRegnskapList.contains(args.getSkjema())) {
                ControlIntegritetFelt1InneholderKodeFraKodeliste.doControl(p, er, l, "Kapittel", "funksjon_kapittel", funksjonkapittelList, Constants.CRITICAL_ERROR);
                ControlIntegritetFelt1InneholderKodeFraKodeliste.doControl(p, er, l, "Sektor", "art_sektor", artsektorList, Constants.CRITICAL_ERROR);
            }

            ControlHeltall.doControl(
                    p
                    , er
                    , new ErrorReportEntry("Integritetskontroller", Utils.createLinenumber(l, p), " ", " "
                            , "Kontroll beløp."
                            , "Korrigér feil beløp (" + p.getFieldAsString("belop") + ")"
                            , Constants.CRITICAL_ERROR
                    )
                    , "belop"
            );
        });
    }
}
