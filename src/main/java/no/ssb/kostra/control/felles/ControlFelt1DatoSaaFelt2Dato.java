package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.Record;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ControlFelt1DatoSaaFelt2Dato {
    public static Record doControl(Record r, ErrorReport er, ErrorReportEntry ere, String field1, String field2) {
        boolean hasErrors = false;
        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern(r.getFieldDefinitionByName(field1).getDatePattern());
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern(r.getFieldDefinitionByName(field1).getDatePattern());

        try {
            LocalDate date1 = LocalDate.parse(r.getFieldAsString(field1), dtf1);
            LocalDate date2 = LocalDate.parse(r.getFieldAsString(field2), dtf2);

            if (date1.isAfter(date2)){
                hasErrors = true;
            }
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
