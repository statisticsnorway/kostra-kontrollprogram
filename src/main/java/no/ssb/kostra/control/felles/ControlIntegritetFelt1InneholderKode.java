package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.felles.Record;

import java.util.Collections;

public class ControlIntegritetFelt1InneholderKode {
    public static boolean doControl(ErrorReport er, int l, String title, String fieldvalue, String expected, int errorType) {
        // TODO dobbeltsjekk denne med tanke på journalnummer / visning av hele recorden
        return ControlFelt1InneholderKodeFraKodeliste.doControl(
                er
                , new ErrorReportEntry("3. Integritetskontroller", " ", " ", " "
                        , "Kontroll " + title
                        , "Korrigér " + title.toLowerCase() + ". Fant '" + fieldvalue + "', forventet '" + expected + "'"
                        , errorType
                )
                , fieldvalue
                , Collections.singletonList(expected)
        );
    }
}
