package no.ssb.kostra.control.sensitiv.meklinger_55;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ojj on 09.04.2019.
 */
public class SumChecker {
    public static boolean validateSum( String line, Integer sum, List<Integer> fields) {
        int checkSum = fields.stream()
                .filter(i -> integerFilter(i, line))
                .mapToInt(Integer::intValue)
                .map(i -> Integer.parseInt(RecordFields.getFieldValue(line, i)))
                .sum();

        return sum == checkSum;
    }

    public static boolean validateIdenticalSum( String line, List<Integer> fields) {
        Integer first = fields.get(0);

        List<Integer> filtered = fields.stream()
                .filter(i -> integerFilter(i, line))
                .map(i -> Integer.parseInt(RecordFields.getFieldValue(line, i)))
                .filter(i -> i == first  )
                .collect(Collectors.toList());

        return fields.size() == filtered.size();
    }

    private static boolean integerFilter(int i, String line){
        try {
            Integer temp = Integer.parseInt(RecordFields.getFieldValue(line, i));
            if (temp != null){
                return true;
            }

        } catch (NumberFormatException e) {
            return false;
        }

        return false;
    }

}
