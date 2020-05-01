package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;

import java.util.List;

public class ControlFeltInneholderKodeFraKodeliste {
    public static Record doControl(Record p, ErrorReport er, ErrorReportEntry ere, String field, List<String> codeList) {
        if (!codeList.stream().anyMatch(code -> code.equalsIgnoreCase(p.getFieldAsString(field)))) {
            ere.setRefNr(String.valueOf(p.getLine()));
            er.addEntry(ere);
        }

        return p;
    }
}
