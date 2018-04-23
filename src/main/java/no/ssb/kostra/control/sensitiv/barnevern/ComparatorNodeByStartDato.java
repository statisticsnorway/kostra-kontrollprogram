package no.ssb.kostra.control.sensitiv.barnevern;

import java.util.Comparator;

import javax.xml.xpath.XPathExpressionException;

import org.joda.time.DateTime;

public class ComparatorNodeByStartDato implements Comparator<StructuredNode> {

	@Override
	public int compare(StructuredNode o1, StructuredNode o2) {
		try {
			DateTime d1 = o1.assignDateFromString(o1.queryString("StartDato"), no.ssb.kostra.control.Constants.datoFormatLangt);
			DateTime d2 = o2.assignDateFromString(o2.queryString("StartDato"), no.ssb.kostra.control.Constants.datoFormatLangt);

			if (d1 != null && d2 != null) {
				if (d1.isBefore(d2)) {
					return 1;

				} else if (d1.isAfter(d2)) {
					return -1;
				} else {
					return 0;
				}
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}
}
