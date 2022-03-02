package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

import java.util.ArrayList;
import java.util.List;

public class ControlRecordLengde {
    public static boolean doControl(List<String> s, ErrorReport er, int length) {
        er.incrementCount();

        List<String> recordLengdeFeil = new ArrayList<>();

        for (int i = 0; i < s.size(); i++) {
            if (s.get(i).length() != length) {
                recordLengdeFeil.add(String.valueOf(i + 1));
            }
        }

        if (0 < recordLengdeFeil.size()) {
            er.addEntry(new ErrorReportEntry("1. Filbeskrivelse"
                    , "", " ", " "
                    , "Kontroll 01 Recordlengde"
                    , "Korrigér filen slik at alle record er på " + length +
                    " tegn, mellomrom brukes for alle blanke posisjoner og avslutter med linjeskift. <br/>"
                    + "Denne feilen hindrer de andre kontrollene i å bli kjørt<br/>"
                    + "\n(Gjelder for linjene:" + String.join(", ", recordLengdeFeil) + ")"
                    , Constants.CRITICAL_ERROR
            ));
            return true;
        }

        return false;
    }
}
