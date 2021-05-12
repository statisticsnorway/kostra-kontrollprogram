package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.felles.Record;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ControlFelt1DatoSaaFelt2Dato {
    public static Record doControl(Record r, ErrorReport er, ErrorReportEntry ere, String field1, String field2) {
        boolean hasErrors = false;
        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern(r.getFieldDefinitionByName(field1).getDatePattern());
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern(r.getFieldDefinitionByName(field2).getDatePattern());

        if (isValidDate(r, field1) && isValidDate(r, field2)) {
            try {
                LocalDate date1 = LocalDate.parse(r.getFieldAsString(field1), dtf1);
                LocalDate date2 = LocalDate.parse(r.getFieldAsString(field2), dtf2);

                if (date1.isAfter(date2)) {
                    hasErrors = true;
                }
            } catch (Exception e) {
                hasErrors = true;
            }

            if (hasErrors) {
                ere.setRefNr(String.valueOf(r.getLine()));
                er.addEntry(ere);
            }
        }

        return r;
    }

    public static boolean isValidDate(Record r, String field) {
        String blankZeroDate = "0".repeat(r.getFieldDefinitionByName(field).getDatePattern().length());
        String blankSpaceDate = " ".repeat(r.getFieldDefinitionByName(field).getDatePattern().length());

        if (r.getFieldAsString(field).equalsIgnoreCase(blankZeroDate)
                || r.getFieldAsString(field).equalsIgnoreCase(blankSpaceDate)) {
            return false;
        }

        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(r.getFieldDefinitionByName(field).getDatePattern());
            LocalDate.parse(r.getFieldAsString(field), dtf);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
