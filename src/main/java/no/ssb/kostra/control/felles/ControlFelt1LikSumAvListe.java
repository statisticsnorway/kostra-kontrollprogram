package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;

import java.util.List;

public class ControlFelt1LikSumAvListe {
    public static Record doControl(Record r, ErrorReport er, ErrorReportEntry ere, List<List<String>> fieldLists) {
        boolean ok = fieldLists.stream()
                .allMatch(fieldList -> {
                            String sumField = fieldList.remove(0);
                            int sum = r.getFieldAsInteger(sumField);
                            Integer sumList = fieldList.stream()
                                    .map(r::getFieldAsInteger)
                                    .reduce(0, Integer::sum);

                            return sum == sumList;
                        }
                );

        if (!ok) {
            ere.setRefNr(String.valueOf(r.getLine()));
            er.addEntry(ere);
        }

        return r;
    }
}
