package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;

import java.util.Collections;

public class ControlIntegritetFelt1InneholderKode {
    public static void doControl(Record r, ErrorReport er, int l, String title, String field, String expected, int errorType) {
        ControlFelt1InneholderKodeFraKodeliste.doControl(
                r
                , er
                , new ErrorReportEntry("Integritetskontroller", Utils.createLinenumber(l, r), " ", " "
                        , "Kontroll " + title
                        , "Korrigér " + title.toLowerCase() + ". Fant '" + r.getFieldAsString(field) + "', forventet '" + expected + "'"
                        , errorType
                )
                , field
                , Collections.singletonList(expected)
        );
    }
}
