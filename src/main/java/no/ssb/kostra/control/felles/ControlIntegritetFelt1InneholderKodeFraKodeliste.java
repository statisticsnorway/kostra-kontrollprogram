package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.felles.Record;

import java.util.List;

public class ControlIntegritetFelt1InneholderKodeFraKodeliste {
    public static boolean doControl(ErrorReport er, int l, String title, String fieldvalue, List<String> expectedList, int errorType) {
        // TODO sjekk journalnummer / recordinnhold
        return ControlFelt1InneholderKodeFraKodeliste.doControl(
                er
                , new ErrorReportEntry("3. Integritetskontroller", " ", " ", " "
                        , "Kontroll " + title
                        , "Korrigér " + title.toLowerCase() + " i linjenummer " + l +  ". Fant ugyldig kode '" + fieldvalue.trim() + "' for " +  title.toLowerCase() + "."
                        , errorType
                )
                , fieldvalue
                , expectedList
        );
    }
}