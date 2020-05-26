package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;
import no.ssb.kostra.control.regnskap.FieldDefinitions;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ControlRecordLengde {
    public static boolean doControl(Stream<Record> s, ErrorReport er, int length) {
        List<String> recordLengdeFeil = s
                .filter(p -> p.getRecord().length() != length)
                .map(Record::getLine)
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.toList());
        er.incrementCount();

        if (!recordLengdeFeil.isEmpty()) {
            er.addEntry(new ErrorReportEntry("Filuttrekk", "Filuttrekk", " ", " "
                    , "Kontroll Recordlengde"
                    , "Korreksjon: Rett opp slik at alle record er på " + length +
                    " tegn og avslutter med linjeskift. <br/>\n(Gjelder for linjene:" + String.join(", ", recordLengdeFeil) + ")"
                    , Constants.CRITICAL_ERROR
            ));
            return true;
        }
        return false;
    }
}
