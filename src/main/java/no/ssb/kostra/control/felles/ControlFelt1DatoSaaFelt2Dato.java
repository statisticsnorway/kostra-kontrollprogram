package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static no.ssb.kostra.control.felles.Comparator.isValidDate;

public class ControlFelt1DatoSaaFelt2Dato {

    public static boolean doControl(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry,
            final String date1, final String datePattern1, final String date2, final String datePattern2) {

        if (!(isValidDate(date1, datePattern1) && isValidDate(date2, datePattern2))) {
            return false;
        }

        final var firstDateFormatter = DateTimeFormatter.ofPattern(datePattern1);
        final var secondDateFormatter = DateTimeFormatter.ofPattern(datePattern2);

        final var firstLocalDate = LocalDate.parse(date1, firstDateFormatter);
        final var secondLocalDate = LocalDate.parse(date2, secondDateFormatter);

        return firstLocalDate.isAfter(secondLocalDate)
                && errorReport.addEntry(errorReportEntry);
    }
}
