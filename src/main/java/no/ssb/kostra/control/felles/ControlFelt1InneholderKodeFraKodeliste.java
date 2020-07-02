package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;

import java.util.List;
import java.util.Objects;

public class ControlFelt1InneholderKodeFraKodeliste {
    public static Record doControl(Record p, ErrorReport er, ErrorReportEntry ere, String field1, List<String> codeList1) {
        if (codeList1.stream().noneMatch(code -> Objects.equals(code, p.getFieldAsString(field1)))) {
            ere.setRefNr(String.valueOf(p.getLine()));
            er.addEntry(ere);
        }

        return p;
    }
}
