package no.ssb.kostra.test.control.sensitiv.barnevern;

import com.sun.jdi.connect.Connector;
import no.ssb.kostra.control.Constants;
import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.sensitiv.barnevern.IndividNodeHandler;
import no.ssb.kostra.controlprogram.Arguments;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class IndividNodeHandlerTest {
    Map<String, String> avgiver;
    Arguments args;
    ErrorReport er;
    ErrorReportEntry ere;
    IndividNodeHandler inh;
    String xsd = "Individ.xsd";

    @Before
    public void beforeTest() {
        avgiver = new TreeMap<String, String>();
        args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "060200"});
        er = new ErrorReport(args);
        inh = new IndividNodeHandler(er, "060200", avgiver);
        ere = new ErrorReportEntry();

    }

    @Test
    public void testInit() {
        assertNotNull(avgiver);
        assertNotNull(er);
        assertNotNull(inh);
    }

    @Test
    public void testSubString() {
        String fnr = "26117500100";
        String sub = fnr.substring(0, 6);

        assertTrue(sub.equalsIgnoreCase("261175"));

    }

    @Test
    public void testSetIndividAlder() {
        DateTime fodselsDato = inh.assignDateFromString("2010-12-31", Constants.datoFormatLangt);
        assertNotNull(fodselsDato);

        DateTime telleDato = inh.assignDateFromString("2013-12-31", Constants.datoFormatLangt);
        assertNotNull(telleDato);

        inh.setIndividAlder(fodselsDato, telleDato);
        int alder = inh.getIndividAlder();
        int expected = 3;

        assertEquals("Individets alder er feil", expected, alder);

        fodselsDato = inh.assignDateFromString("26117500100".substring(0, 6), Constants.datoFormatKort);
        assertNotNull(fodselsDato);

        inh.setIndividAlder(fodselsDato, telleDato);
        alder = inh.getIndividAlder();
        expected = 38;

        assertEquals("Individets alder er feil", expected, alder);
    }

    @Test
    public void testControlUndefinedOrHasLength() {
        ErrorReportEntry ere = new ErrorReportEntry();
        assertNotNull(ere);

        assertTrue(inh.controlUndefinedOrHasLength(er, ere, null));
        assertTrue(inh.controlUndefinedOrHasLength(er, ere, " "));
        assertFalse(inh.controlUndefinedOrHasLength(er, ere, ""));
    }

    @Test
    public void testControlStartDatoEtterSluttDato() {
        DateTime startDato = inh.assignDateFromString("2010-12-31", Constants.datoFormatLangt);
        assertNotNull(startDato);

        DateTime sluttDato = inh.assignDateFromString("2013-12-31", Constants.datoFormatLangt);
        assertNotNull(sluttDato);

        ErrorReportEntry ere = new ErrorReportEntry();
        assertNotNull(ere);

        assertTrue(inh.controlDatoEtterDato(er, ere, startDato, sluttDato));
    }

    @Test
    public void testGetStreamSourceFromJAR() {
        Source schemaFile = new StreamSource(xsd);
        assertNotNull(schemaFile);
        assertNotNull(schemaFile.toString().length() > 0);

    }

//	@Test
//	public void testCreateValidator() {
//		try {
//			// create a SchemaFactory capable of understanding WXS schemas
//			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//
//			// load a WXS schema, represented by a Schema instance
//			InputStream iStream = this.getClass().getClassLoader().getResourceAsStream(xsd);
//			if (iStream == null) {
//				throw new RuntimeException("Kunne ikke finne fil " + xsd);
//			}
//
//			Source schemaFile = new StreamSource(iStream);
//			Schema schema = factory.newSchema(schemaFile);
//
//			// create a Validator instance, which can be used to
//			// validate an
//			// instance document
//			schema.newValidator();
//		} catch (SAXException e) {
//
//		}
//	}

    @Test
    public void testControlAvslutta3112() {
        String kode = "1";
        DateTime sluttDato = inh.assignDateFromString("2013-12-31", Constants.datoFormatLangt);
        assertNotNull(sluttDato);

        DateTime frist = inh.assignDateFromString("2010-12-31", Constants.datoFormatLangt);
        assertNotNull(frist);

        ErrorReportEntry ere = new ErrorReportEntry();
        assertNotNull(ere);

        assertFalse(inh.controlAvslutta3112(er, ere, kode, sluttDato, frist));
        assertFalse(inh.controlAvslutta3112(er, ere, null, frist, sluttDato));
        assertFalse(inh.controlAvslutta3112(er, ere, null, sluttDato, frist));
        assertFalse(inh.controlAvslutta3112(er, ere, null, null, frist));
        assertFalse(inh.controlAvslutta3112(er, ere, null, sluttDato, null));
        assertFalse(inh.controlAvslutta3112(er, ere, null, null, null));

        assertTrue(inh.controlAvslutta3112(er, ere, kode, frist, sluttDato));
    }

    @Test
    public void testControlOver7OgIBarnehage() {
        assertTrue(inh.controlOver7OgIBarnehage(er, ere, "4.1", 6));
        assertTrue(inh.controlOver7OgIBarnehage(er, ere, "4.1", 7));
        assertFalse(inh.controlOver7OgIBarnehage(er, ere, "4.1", 8));

        assertTrue(inh.controlOver7OgIBarnehage(er, ere, "4.2", 6));
        assertTrue(inh.controlOver7OgIBarnehage(er, ere, "4.2", 7));
        assertTrue(inh.controlOver7OgIBarnehage(er, ere, "4.2", 8));
    }

    @Test
    public void testControlAlder() {
        assertTrue(inh.controlAlder(er, ere, 22));
        assertTrue(inh.controlAlder(er, ere, 23));
        assertFalse(inh.controlAlder(er, ere, 24));
    }

    @Test
    public void testControlTiltakLovhjemmelOmsorgstiltakSluttDato() {
        DateTime sluttDato = inh.assignDateFromString("2013-12-31", Constants.datoFormatLangt);
        assertNotNull(sluttDato);
        String kode = "4";
        String[] kodeliste = {"4"};

        assertFalse(inh.controlTiltakLovhjemmelOmsorgstiltakSluttDato(er, ere, sluttDato, "4", "12", null, kode, kodeliste, null));
        assertFalse(inh.controlTiltakLovhjemmelOmsorgstiltakSluttDato(er, ere, sluttDato, "4", "8", "2", kode, kodeliste, null));
        assertFalse(inh.controlTiltakLovhjemmelOmsorgstiltakSluttDato(er, ere, sluttDato, "4", "8", "3", kode, kodeliste, null));

        assertFalse(inh.controlTiltakLovhjemmelOmsorgstiltakSluttDato(er, ere, sluttDato, "4", "12", null, kode, kodeliste, ""));
        assertFalse(inh.controlTiltakLovhjemmelOmsorgstiltakSluttDato(er, ere, sluttDato, "4", "8", "2", kode, kodeliste, ""));
        assertFalse(inh.controlTiltakLovhjemmelOmsorgstiltakSluttDato(er, ere, sluttDato, "4", "8", "3", kode, kodeliste, ""));

        assertTrue(inh.controlTiltakLovhjemmelOmsorgstiltakSluttDato(er, ere, sluttDato, "4", "12", null, kode, kodeliste, "Tekst for opphevelse"));
        assertTrue(inh.controlTiltakLovhjemmelOmsorgstiltakSluttDato(er, ere, sluttDato, "4", "8", "2", kode, kodeliste, "Tekst for opphevelse"));
        assertTrue(inh.controlTiltakLovhjemmelOmsorgstiltakSluttDato(er, ere, sluttDato, "4", "8", "3", kode, kodeliste, "Tekst for opphevelse"));
    }


    @Test
    public void testControlFodselsnummerOgDUFnummer() {
        assertTrue(inh.controlFodselsnummerOgDUFnummer(er, ere, "01020304185", "123456789012", "", "", ""));
        assertTrue(inh.controlFodselsnummerOgDUFnummer(er, ere, "01020300100", "123456789012", "", "", ""));
        assertTrue(inh.controlFodselsnummerOgDUFnummer(er, ere, "01020300200", "123456789012", "", "", ""));
        assertTrue(inh.controlFodselsnummerOgDUFnummer(er, ere, "01020399999", "123456789012", "", "", ""));
        assertTrue(inh.controlFodselsnummerOgDUFnummer(er, ere, "41020304098", "123456789012", "", "", ""));

        assertTrue(inh.controlFodselsnummerOgDUFnummer(er, ere, null, "123456789012", "", "", ""));

        assertFalse(inh.controlFodselsnummerOgDUFnummer(er, ere, null, "12345678901", "", "", ""));
        assertFalse(inh.controlFodselsnummerOgDUFnummer(er, ere, null, null, "", "", ""));
    }

    @Test
    public void testControlFodselsnummer() {
        assertTrue(inh.controlFodselsnummer(er, ere, "01020304185", ""));
        assertTrue(inh.controlFodselsnummer(er, ere, "41020304098", ""));

        assertFalse(inh.controlFodselsnummer(er, ere, "01020300100", ""));
        assertFalse(inh.controlFodselsnummer(er, ere, "01020300200", ""));
        assertFalse(inh.controlFodselsnummer(er, ere, "01020355555", ""));
        assertFalse(inh.controlFodselsnummer(er, ere, "01020399999", ""));

        assertFalse(inh.controlFodselsnummer(er, ere, null, ""));

        assertFalse(inh.controlFodselsnummer(er, ere, "12345678901", ""));
        assertFalse(inh.controlFodselsnummer(er, ere, null, ""));
    }

    @Test
    public void testControlDublettFodselsnummer() {
        assertTrue(inh.controlDublettFodselsnummer(er, ere, "01020304185"));
        assertFalse(inh.controlDublettFodselsnummer(er, ere, "01020304185"));
        assertFalse(inh.controlDublettFodselsnummer(er, ere, "01020304185"));

        assertTrue(inh.controlDublettFodselsnummer(er, ere, "01020304185@Bydel_01"));
        assertTrue(inh.controlDublettFodselsnummer(er, ere, "01020304185@Bydel_02"));
        assertFalse(inh.controlDublettFodselsnummer(er, ere, "01020304185@Bydel_01"));
        assertFalse(inh.controlDublettFodselsnummer(er, ere, "01020304185@Bydel_02"));

        assertTrue(inh.controlDublettFodselsnummer(er, ere, null));
    }

    @Test
    public void testControlDublettJournalnummer() {
        assertTrue(inh.controlDublettJournalnummer(er, ere, "01020304185"));
        assertFalse(inh.controlDublettJournalnummer(er, ere, "01020304185"));
        assertFalse(inh.controlDublettJournalnummer(er, ere, "01020304185"));

    }


    @Test
    public void testControlTidManeder() {
        DateTime dato1 = inh.assignDateFromString("2013-09-01", Constants.datoFormatLangt);
        DateTime dato2 = inh.assignDateFromString("2013-11-15", Constants.datoFormatLangt);
        DateTime telledato = inh.assignDateFromString("2013-12-31", Constants.datoFormatLangt);

        assertTrue(dato2.isAfter(dato1));
        assertTrue(inh.controlTidManeder(er, ere, dato2, null, telledato, 2));
        assertTrue(inh.controlTidManeder(er, ere, dato1, dato2, telledato, 3));
        assertFalse(inh.controlTidManeder(er, ere, dato1, dato2, telledato, 2));
        assertFalse(inh.controlTidManeder(er, ere, dato1, null, telledato, 3));
    }

    @Test
    public void testJodaTime() {
        // lager en startdato
        DateTime dato1 = inh.assignDateFromString("2015-01-02", Constants.datoFormatLangt);
        // lager en sluttdato som skal være lik fristen
        DateTime dato2 = inh.assignDateFromString("2015-04-09", Constants.datoFormatLangt);
        // utleder frist 7 dager + 3 måneder etter startdato
        DateTime frist = dato1.plusDays(7).plusMonths(3);

        assertTrue(dato2.isAfter(dato1));
        assertFalse(dato2.isAfter(frist));

        // legger til en dag på sluttdatoene slik at den overskrider fristen.
        dato2 = dato2.plusDays(1);
        assertTrue(dato2.isAfter(frist));
    }


    /*
    *
    *
    * @Test public void testControlAlder() { fail("Not yet implemented"); }
    *
    * @Test public void testControlTidManederDager() {
    * fail("Not yet implemented"); }
    *
    * @Test public void testControlTidDager() {
    * fail("Not yet implemented"); }
    *
    * @Test public void testControlFeltMedKoderSkalHaUndernoder() {
    * fail("Not yet implemented"); }
    *
    * @Test public void testControlKonkludertMelding() {
    * fail("Not yet implemented"); }
    *
    * @Test public void testControlTiltakIndividetsAlder() {
    * fail("Not yet implemented"); }
    *
    * @Test public void testControlTiltakOmsorgstiltakSluttDato() {
    * fail("Not yet implemented"); }
    *
    * @Test public void testControlFlerePlasseringstiltakISammePeriode() {
    * fail("Not yet implemented"); }
    *
    * @Test public void testControlOver18OgPaOmsorgstiltak() {
    * fail("Not yet implemented"); }
    *
    * @Test public void testControlOver7OgIBarnehage() {
    * fail("Not yet implemented"); }
    *
    * @Test public void testControlOver11OgISFO() {
    * fail("Not yet implemented"); }
    *
    * @Test public void testControlValidateByXSD() {
    * fail("Not yet implemented"); }
    *
    * @Test public void testControlEquals() { fail("Not yet implemented"); }
    *
    * @Test public void testControlExists() { fail("Not yet implemented"); }
    *
    * @Test public void testControlExistsAndHasLength() {
    * fail("Not yet implemented"); }
    *
    * @Test public void testControlBoolean() { fail("Not yet implemented"); }
    *
    * @Test public void testControlPresisering() { fail("Not yet implemented");
    * }
    */
    @Test
    public void testControlTiltakLovhjemmel() {
        assertTrue(inh.controlTiltakLovhjemmel(er, ere, "1", "1"));

        assertFalse(inh.controlTiltakLovhjemmel(er, ere, "0", "0"));
        assertFalse(inh.controlTiltakLovhjemmel(er, ere, "01", "01"));

    }

    @Test
    public void testControlUniqueID() {
        assertTrue(inh.controlUniqueID(er, ere, "Individ", "1"));
        assertFalse(inh.controlUniqueID(er, ere, "Individ", "1"));

        assertTrue(inh.controlUniqueID(er, ere, "Individ", "2"));
        assertFalse(inh.controlUniqueID(er, ere, "Individ", "2"));
    }
}
