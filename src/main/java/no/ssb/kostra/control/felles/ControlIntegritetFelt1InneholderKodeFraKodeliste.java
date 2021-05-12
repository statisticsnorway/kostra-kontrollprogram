package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.felles.Record;

import java.util.List;

public class ControlIntegritetFelt1InneholderKodeFraKodeliste {
    public static void doControl(Record r, ErrorReport er, int l, String title, String field, List<String> expectedList, int errorType) {
        ControlFelt1InneholderKodeFraKodeliste.doControl(
                r
                , er
                , new ErrorReportEntry("3. Integritetskontroller", Utils.createLinenumber(l, r), " ", " "
                        , "Kontroll " + title
                        , "Korrigér " + title.toLowerCase() + " i linjenummer " + l +  ". Fant ugyldig kode '" + r.getFieldAsTrimmedString(field) + "' for " +  title.toLowerCase() + "."
                        , errorType
                )
                , field
                , expectedList
        );
    }
}