package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;

import java.util.List;
import java.util.stream.Collectors;

public class ControlIntegritetFelt1InneholderKodeFraKodeliste {
    public static void doControl(Record r, ErrorReport er, int l, String title, String field, List<String> expectedList, int errorType) {
        ControlFelt1InneholderKodeFraKodeliste.doControl(
                r
                , er
                , new ErrorReportEntry("3. Integritetskontroller", Utils.createLinenumber(l, r), " ", " "
                        , "Kontroll " + title
                        , "Korrigér " + title.toLowerCase() + ". Fant '" + r.getFieldAsString(field) + "', forventet én av : "
                        + expectedList.stream().map(s -> "'".concat(s).concat("'")).collect(Collectors.joining(", "))
                        , errorType
                )
                , field
                , expectedList
        );
    }
}