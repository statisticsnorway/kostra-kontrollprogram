package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ControlPosteringLengde {
    /**
     * Sjekker om hver tekststreng i listen har en gitt lengde. Returnérer true hvis lengden er korrekt,
     * ellers false samt at ErrorReportEntry legges til i ErrorReport
     *
     * @param er     ErrorReport
     * @param ere    ErrorReport
     * @param list1  List<String>
     * @param length int
     * @return boolean
     */
    public static boolean run(ErrorReport er, ErrorReportEntry ere, List<String> list1, int length) {
        AtomicInteger index = new AtomicInteger();

        return !list1.stream()
                .filter((line) -> line != null && line.length() != length)
                .map((line) -> {
                    ere.setRefNr(String.valueOf(index.getAndIncrement()));
                    er.addEntry(ere);
                    return line;
                })
                .collect(Collectors.toList())
                .isEmpty();

    }
}
