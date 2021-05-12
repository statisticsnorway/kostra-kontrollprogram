package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.felles.Record;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ControlFelt1Dato {
    public static Record doControl(Record r, ErrorReport er, ErrorReportEntry ere, String field1) {
        boolean hasErrors = false;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(r.getFieldDefinitionByName(field1).getDatePattern());

        try {
            LocalDate.parse(r.getFieldAsString(field1), dtf);
        } catch (Exception e) {
            hasErrors = true;
        }

        if (hasErrors) {
            ere.setRefNr(String.valueOf(r.getLine()));
            er.addEntry(ere);
        }

        return r;
    }
}
