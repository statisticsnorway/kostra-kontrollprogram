package no.ssb.kostra.control.sensitiv.meklinger_55;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ojj on 09.04.2019.
 */
public class SumChecker {
    public static boolean validateSum( String line, Integer sum, List<Integer> fields) {
        int checkSum = fields.stream()
                .mapToInt(Integer::intValue)
                .map(i -> Integer.parseInt(RecordFields.getFieldValue(line, i)))
                .sum();

        return sum == checkSum;
    }

    public static boolean validateIdenticalSum( String line, List<Integer> fields) {
        Integer first = fields.get(0);

        List<Integer> filtered = fields.stream()
                .map(i -> Integer.parseInt(RecordFields.getFieldValue(line, i)))
                .filter(i -> i == first  )
                .collect(Collectors.toList());

        return fields.size() == filtered.size();
    }

}
