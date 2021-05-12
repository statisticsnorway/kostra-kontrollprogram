package no.ssb.kostra.control.felles;

import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import no.ssb.kostra.felles.Record;
import no.ssb.kostra.utils.Fnr;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class ControlFodselsnummerDUFnummer {
    public static Record doControl(Record r, ErrorReport er, ErrorReportEntry ere, String fieldFNR, String fieldDUF) {
        List<Integer> weights = List.of(4, 6, 3, 2, 4, 6, 3, 2, 7, 5);
        String fNr = r.getFieldAsString(fieldFNR).replace(' ', '0');
        String dufNr = r.getFieldAsString(fieldDUF).replace(' ', '0');
        List<Character> numbers = dufNr.chars().mapToObj(e -> (char) e).collect(Collectors.toList());
        boolean hasErrors = false;

        if (Fnr.validNorwId(fNr) != 1) {
            try {
                Integer sum = IntStream.range(0, Math.min(numbers.size(), weights.size()))
                        .mapToObj(i -> Integer.parseInt(String.valueOf(numbers.get(i))) * weights.get(i))
                        .reduce(0, Integer::sum);
                int remainder = sum % 11;
                String kontrollTall = (remainder < 10 ? "0" : "") + remainder;
                hasErrors = !dufNr.substring(10).equalsIgnoreCase(kontrollTall);
            } catch (Exception e) {
                hasErrors = true;
            }
        }

        if (hasErrors) {
            ere.setRefNr(String.valueOf(r.getLine()));
            er.addEntry(ere);
        }

        return r;
    }
}
