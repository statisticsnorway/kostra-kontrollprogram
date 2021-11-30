package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.Record;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static no.ssb.kostra.control.felles.Comparator.isCodeInCodelist;

public class ControlIntegritet {
    public static void doControl(List<Record> regnskap, ErrorReport er, Arguments args
            , List<String> bevilgningRegnskapList, List<String> balanseRegnskapList
            , List<String> kontoklasseList, List<String> funksjonkapittelList, List<String> artsektorList
    ) {
        controlSkjema(er, regnskap);
        controlAargang(er, regnskap);
        controlKvartal(er, regnskap);
        controlRegion(er, regnskap);
        controlOrganisasjonsnummer(er, regnskap);
        controlForetaksnummer(er, regnskap);
        controlKontoklasse(er, regnskap, kontoklasseList);

        if (isCodeInCodelist(args.getSkjema(), bevilgningRegnskapList)) {
            controlFunksjon(er, regnskap, funksjonkapittelList);
            controlArt(er, regnskap, artsektorList);
        }

        if (isCodeInCodelist(args.getSkjema(), balanseRegnskapList)) {
            controlKapittel(er, regnskap, funksjonkapittelList);
            controlSektor(er, regnskap, artsektorList);
        }

        controlBelop(er, regnskap);
        controlUgyldigeBelop(er, regnskap);
    }

    public static boolean controlSkjema(ErrorReport er, List<Record> regnskap) {
        return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(er,
                "3. Feltkontroller",
                "Kontroll Skjema",
                "Fant ugyldig skjema '(%s)'. Korrigér skjema (" + er.getArgs().getSkjema() + ").",
                "skjema",
                regnskap,
                List.of(er.getArgs().getSkjema()),
                Constants.CRITICAL_ERROR);
    }

    public static boolean controlAargang(ErrorReport er, List<Record> regnskap) {
        return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(er,
                "3. Feltkontroller",
                "Kontroll Aargang",
                "Fant ugyldig årgang '(%s)'. Korrigér årgang til gjeldende årgang (" + er.getArgs().getAargang() + ").",
                "aargang",
                regnskap,
                List.of(er.getArgs().getAargang()),
                Constants.CRITICAL_ERROR);
    }

    public static boolean controlKvartal(ErrorReport er, List<Record> regnskap) {
        return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(er,
                "3. Feltkontroller",
                "Kontroll Kvartal",
                "Fant ugyldig kvartal '%s'.",
                "kvartal",
                regnskap,
                List.of(er.getArgs().getKvartal()),
                Constants.CRITICAL_ERROR);
    }

    public static boolean controlRegion(ErrorReport er, List<Record> regnskap) {
        return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(er,
                "3. Feltkontroller",
                "Kontroll Region, fylkeskommune-/kommune-/bydelsnummer",
                "Fant ugyldig region '(%s)'. Korrigér region til '" + er.getArgs().getRegion() + "'",
                "region",
                regnskap,
                List.of(er.getArgs().getRegion()),
                Constants.CRITICAL_ERROR);
    }

    public static boolean controlOrganisasjonsnummer(ErrorReport er, List<Record> regnskap) {
        return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(er,
                "3. Feltkontroller",
                "Kontroll Organisasjonsnummer",
                "Fant ugyldig organisasjonnummer '%s'. Korrigér organisasjonnummer til '" + er.getArgs().getOrgnr() + "'",
                "orgnr",
                regnskap,
                Stream.of(er.getArgs().getOrgnr().split(",")).collect(Collectors.toList()),
                Constants.CRITICAL_ERROR);
    }

    public static boolean controlForetaksnummer(ErrorReport er, List<Record> regnskap) {
        return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(er,
                "3. Feltkontroller",
                "Kontroll Foretaksnummer",
                "Fant ugyldig foretaksnummer '(%s)'. Korrigér foretaksnummer til '" + er.getArgs().getForetaknr() + "'",
                "foretaksnr",
                regnskap,
                Stream.of(er.getArgs().getForetaknr().concat(",").concat(er.getArgs().getOrgnr()).split(",")).collect(Collectors.toList()),
                Constants.CRITICAL_ERROR);
    }

    public static boolean controlKontoklasse(ErrorReport er, List<Record> regnskap, List<String> kontoklasseList) {
        return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(er,
                "3. Feltkontroller",
                "Kontroll Kontoklasse",
                "Fant ugyldig kontoklasse '(%s)'. Korrigér kontoklasse i henhold til KOSTRA kontoplan.",
                "kontoklasse",
                regnskap,
                kontoklasseList,
                Constants.CRITICAL_ERROR);
    }

    public static boolean controlFunksjon(ErrorReport er, List<Record> regnskap, List<String> funksjonList) {
        return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(er,
                "3. Feltkontroller",
                "Kontroll Funksjon",
                "Fant ugyldig funksjon '(%s)'. Korrigér funksjon i henhold til KOSTRA kontoplan.",
                "funksjon_kapittel",
                regnskap,
                funksjonList,
                Constants.CRITICAL_ERROR);
    }

    public static boolean controlArt(ErrorReport er, List<Record> regnskap, List<String> artList) {
        return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(er,
                "3. Feltkontroller",
                "Kontroll Art",
                "Fant ugyldig art '(%s)'. Korrigér art i henhold til KOSTRA kontoplan.",
                "art_sektor",
                regnskap,
                artList,
                Constants.CRITICAL_ERROR);
    }

    public static boolean controlKapittel(ErrorReport er, List<Record> regnskap, List<String> kapittelList) {
        return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(er,
                "3. Feltkontroller",
                "Kontroll Kapittel",
                "Fant ugyldig kapittel (%s) i liste",
                "funksjon_kapittel",
                regnskap,
                kapittelList,
                Constants.CRITICAL_ERROR);
    }

    public static boolean controlSektor(ErrorReport er, List<Record> regnskap, List<String> sektorList) {
        return ControlFelt1ListeInneholderKodeFraKodeliste.doControl(er,
                "3. Feltkontroller",
                "Kontroll Sektor",
                "Fant ugyldig sektor '(%s)'. Korrigér sektor i henhold til KOSTRA kontoplan.",
                "art_sektor",
                regnskap,
                sektorList,
                Constants.CRITICAL_ERROR);
    }

    public static boolean controlBelop(ErrorReport er, List<Record> regnskap) {
        return ControlFelt1ListeHeltall.doControl(er,
                "Kontroll Beløp",
                regnskap.stream().map(record -> record.getFieldAsInteger("belop")).collect(Collectors.toList()),
                Constants.CRITICAL_ERROR);
    }

    public static boolean controlUgyldigeBelop(ErrorReport er, List<Record> regnskap) {
        return ControlFelt1ListeUlovligTegnITallfelt.doControl(er,
                "Kontroll Ugyldige beløp",
                regnskap.stream().map(record -> record.getFieldAsTrimmedString("belop")).collect(Collectors.toList()),
                Constants.CRITICAL_ERROR);
    }
}
