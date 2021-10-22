package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static no.ssb.kostra.control.felles.Comparator.isValidDate;

public class ControlFelt1DatoSaaFelt2Dato {
    public static boolean doControl(ErrorReport er, ErrorReportEntry ere, String date1, String datePattern1, String date2, String datePattern2) {
        if (isValidDate(date1, datePattern1) && isValidDate(date2, datePattern2)) {
            DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern(datePattern1);
            DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern(datePattern2);

            LocalDate localdate1 = LocalDate.parse(date1, dtf1);
            LocalDate localdate2 = LocalDate.parse(date2, dtf2);

            if (localdate1.isAfter(localdate2)) {
                return er.addEntry(ere);
            }
        }

        return false;
    }
}
