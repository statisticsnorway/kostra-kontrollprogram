package no.ssb.kostra.test.control.sensitiv.barnevern;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.TreeMap;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.sensitiv.barnevern.IndividNodeHandler;
import no.ssb.kostra.control.sensitiv.barnevern.NodeHandler;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class NodeHandlerTest {
	Map<String, String> avgiver;
	ErrorReport er;
	ErrorReportEntry ere;
	NodeHandler nh;
	String xsd = "Individ.xsd";

	@Before
	public void beforeTest() {
		avgiver = new TreeMap<String, String>();
		avgiver.put("Organisasjonsnummer", "123456789");
		avgiver.put("Versjon", "2013");
		avgiver.put("Kommunenummer", "060200");
		avgiver.put("Kommunenavn", "Drammen");
		er = new ErrorReport();
		nh = new IndividNodeHandler(er, "060200", avgiver);
		ere = new ErrorReportEntry();

	}

	@Test
	public void testNodeHandler() {
		// fail("Not yet implemented");
	}

	@Test
	public void testRegion() {
		String region = "060200";
		nh.setRegion(region);

		assertEquals("Skal være like", region, nh.getRegion());
	}

	@Test
	public void testControlValidateByXSD() {
		// fail("Not yet implemented");
	}

	@Test
	public void testControlEquals() {
		assertTrue(nh.controlEquals(er, ere, "test", "test"));
		assertTrue(nh.controlEquals(er, ere, "test", "TEST"));
		assertFalse(nh.controlEquals(er, ere, "test", "false"));
		assertFalse(nh.controlEquals(er, ere, "false", "test"));

	}

	@Test
	public void testControlExists() {
		assertTrue(nh.controlExists(er, ere, "true"));
		assertTrue(nh.controlExists(er, ere, ""));
		assertFalse(nh.controlExists(er, ere, null));
	}

	@Test
	public void testControlExistsAndHasLength() {
		assertTrue(nh.controlExistsAndHasLength(er, ere, "true"));
		assertFalse(nh.controlExistsAndHasLength(er, ere, ""));
		assertFalse(nh.controlExistsAndHasLength(er, ere, null));

	}

	@Test
	public void testControlBoolean() {
		assertTrue(nh.controlBoolean(er, ere, true));
		assertFalse(nh.controlBoolean(er, ere, false));
	}

	@Test
	public void testAssignDateFromString() {
		DateTime dt1 = nh.assignDateFromString("2013-12-31", Constants.datoFormatLangt);
		assertTrue(dt1 != null);
		assertTrue(dt1.getYear() == 2013);
		assertTrue(dt1.getMonthOfYear() == 12);
		assertTrue(dt1.getDayOfMonth() == 31);

		DateTime dt2 = nh.assignDateFromString("010203", Constants.datoFormatKort);
		assertTrue(dt2 != null);
		assertTrue(dt2.getYear() == 2003);
		assertTrue(dt2.getMonthOfYear() == 2);
		assertTrue(dt2.getDayOfMonth() == 1);

		DateTime dt3 = nh.assignDateFromString("01020304185", Constants.datoFormatKort);
		assertTrue(dt3 == null);

	}

	@Test
	public void testControlPresisering() {
		String kode = "1";
		String[] kodeliste = { "1", "2" };

		assertTrue(nh.controlPresisering(er, ere, null, null, null));
		assertTrue(nh.controlPresisering(er, ere, null, kodeliste, null));
		assertTrue(nh.controlPresisering(er, ere, kode, null, null));
		assertTrue(nh.controlPresisering(er, ere, kode, kodeliste, "Presisering"));
		assertTrue(nh.controlPresisering(er, ere, "3", kodeliste, null));

		assertFalse(nh.controlPresisering(er, ere, kode, kodeliste, null));
		assertFalse(nh.controlPresisering(er, ere, kode, kodeliste, ""));

	}

}
