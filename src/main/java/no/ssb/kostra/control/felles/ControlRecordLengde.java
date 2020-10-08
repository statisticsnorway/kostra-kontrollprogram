package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;
import no.ssb.kostra.control.regnskap.FieldDefinitions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ControlRecordLengde {
    public static boolean doControl(List<String> s, ErrorReport er, int length) {
        List<String> recordLengdeFeil = new ArrayList<>();

        for (int i = 0; i < s.size(); i++) {
            if (s.get(i).length() != length){
                recordLengdeFeil.add(String.valueOf(i+1));
            }
        }

        er.incrementCount();

        if (!recordLengdeFeil.isEmpty()) {
            er.addEntry(new ErrorReportEntry("Filuttrekk"
                    , "Integritetskontroller", " ", " "
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
