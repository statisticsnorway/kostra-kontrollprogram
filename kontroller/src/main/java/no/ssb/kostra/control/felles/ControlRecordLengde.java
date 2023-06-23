package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@SuppressWarnings("SpellCheckingInspection")
public class ControlRecordLengde {

    public static boolean doControl(
            final List<String> s, final ErrorReport errorReport, final int length) {

        errorReport.incrementCount();

        final var recordLengdeFeil = new ArrayList<String>();

        for (var i = 0; i < s.size(); i++) {
            if (s.get(i).length() != length || Pattern.matches("\\t", s.get(i))) {
                recordLengdeFeil.add(String.valueOf(i + 1));
            }
        }

        if (0 == recordLengdeFeil.size()) {
            return false;
        }

        errorReport.addEntry(new ErrorReportEntry("1. Filbeskrivelse"
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
}
