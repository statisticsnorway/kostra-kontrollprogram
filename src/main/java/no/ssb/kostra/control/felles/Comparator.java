package no.ssb.kostra.control.felles;

import java.util.List;

public class Comparator {
    public static boolean compareIntegerOperatorInteger(Integer a, String op, Integer b) {
        boolean ok = true;

        if (a != null && !op.isEmpty() && b != null) {
            if (op.equalsIgnoreCase("<")) {
                ok = (a < b);

            } else if (op.equalsIgnoreCase("<=")) {
                ok = (a <= b);

            } else if (op.equalsIgnoreCase(">")) {
                ok = (a > b);

            } else if (op.equalsIgnoreCase(">=")) {
                ok = (a >= b);

            } else if (op.equalsIgnoreCase("==")) {
                ok = (a.intValue() == b.intValue());

            } else if (op.equalsIgnoreCase("!=")) {
                ok = !(a.intValue() == b.intValue());
            }
        }

        return ok;
    }

    public static boolean isCodeInCodelist(String code, List<String> codeList){
        return codeList.stream().anyMatch(item -> item.equalsIgnoreCase(code));
    }
}
