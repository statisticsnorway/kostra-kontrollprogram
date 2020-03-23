package no.ssb.kostra.control.felles;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ControlListeInneholderKodeFraKodeliste {
    /**
     * Sjekker om kodene i en liste fins i kodeliste.
     *
     * @param list1
     *            List<String>
     * @param codelist1
     *            List<String>
     * @return boolean
     */
    public static boolean run(ErrorReport er, ErrorReportEntry ere, List<String> list1, List<String> codelist1) {
        AtomicInteger index = new AtomicInteger();

        return !list1.stream()
                .filter((line) -> !codelist1.contains(line))
                .map((line) -> {
                    ere.setRefNr(String.valueOf(index.getAndIncrement()));
                    index.incrementAndGet();
                    er.addEntry(ere);
                    return line;
                })
                .collect(Collectors.toList())
                .isEmpty();

    }
}
