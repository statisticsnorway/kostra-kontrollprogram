package no.ssb.kostra.control.barnevern.s15;

import javax.xml.xpath.XPathExpressionException;
import java.util.Comparator;

public class ComparatorNodeByStartDato implements Comparator<StructuredNode> {

    public static final String DATO_FORMAT_LANGT = "yyyy-MM-dd";

    @Override
    public int compare(final StructuredNode o1, final StructuredNode o2) {
        try {
            final var d1 = o1.assignDateFromString(o1.queryString("StartDato"), DATO_FORMAT_LANGT);
            final var d2 = o2.assignDateFromString(o2.queryString("StartDato"), DATO_FORMAT_LANGT);

            if (d1 != null && d2 != null) {
                if (d1.isBefore(d2)) {
                    return 1;
                } else if (d1.isAfter(d2)) {
                    return -1;
                } else {
                    return 0;
                }
            }
        } catch (XPathExpressionException ignored) {
            // noop
        }
        return 0;
    }
}
