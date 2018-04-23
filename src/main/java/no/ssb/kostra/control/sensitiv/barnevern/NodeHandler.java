package no.ssb.kostra.control.sensitiv.barnevern;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.joda.time.DateTime;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;

/**
 * 
 * @author ojj
 * @version $Revision$
 * 
 */

public abstract class NodeHandler {
	protected Map<String, String> avgiver;
	protected ErrorReport er;
	protected String region;

	/**
	 * 
	 * @param er
	 *            ErrorReport
	 * @param region
	 *            Regionsnummer for den avgiver som blir kontrollert
	 * @param avgiver
	 *            Et Map for å holde avgiverinformasjon ved kontroll av
	 *            Individ-noder
	 */
	public NodeHandler(ErrorReport er, String region,
			Map<String, String> avgiver) {
		this.er = er;
		this.setRegion(region);
		this.avgiver = avgiver;
	}

	/**
	 * Abstract metode som arves og implementeres mot den XML-noden som skal
	 * sjekkes
	 * 
	 * @param node
	 *            XML-noden som skal kontrolleres
	 */
	abstract void process(StructuredNode node);

	/**
	 * 
	 * @return String Getter for regionsnummer
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * 
	 * @param region
	 *            Setter for regionsnummer
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * Validérer en grein i XML-en mot en tilsvarende XSD *
	 * 
	 * @param er
	 *            ErrorReport
	 * @param ere
	 *            ErrorReportEntry
	 * @param document
	 *            XML-tre som skal kontrolleres
	 * @param xsd
	 *            XSD som XML-tre skal valideres mot
	 * @return boolean Returnerer true hvis alt har gått bra, ellers false
	 */
	public boolean controlValidateByXSD(ErrorReport er, ErrorReportEntry ere,
			Document document, String xsd) {
		try {
			// create a SchemaFactory capable of understanding WXS schemas
			SchemaFactory factory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

			// load a WXS schema, represented by a Schema instance
			InputStream iStream = this.getClass().getClassLoader()
					.getResourceAsStream(xsd);
			if (iStream == null) {
				throw new RuntimeException("Kunne ikke finne fil " + xsd);
			}

			Source schemaFile = new StreamSource(iStream);
			Schema schema = factory.newSchema(schemaFile);

			// create a Validator instance, which can be used to
			// validate an
			// instance document
			Validator validator = schema.newValidator();

			// validate the DOM tree
			validator.validate(new DOMSource(document));

		} catch (IOException e) {
			ere.setErrorText(ere.getErrorText() + ". IOException: "
					+ e.getMessage());
			er.addEntry(ere);
			return false;

		} catch (SAXException e) {
			// instance document is invalid!
			ere.setErrorText(ere.getErrorText() + ". SAXException: "
					+ e.getMessage());
			er.addEntry(ere);
			return false;

		} catch (NullPointerException e) {
			ere.setErrorText(ere.getErrorText() + ". NullPointerException: "
					+ e.getMessage());
			er.addEntry(ere);
			return false;
		}

		return true;
	}

	/**
	 * Sjekker om to tekststrenger er like. Returnérer true hvis de er like,
	 * ellers false samt at ErrorReportEntry legges til i ErrorReport
	 * 
	 * @param er
	 *            ErrorReport
	 * @param ere
	 *            ErrorReport
	 * @param val1
	 *            String
	 * @param val2
	 *            String
	 * @return boolean
	 */
	public boolean controlEquals(ErrorReport er, ErrorReportEntry ere,
			String val1, String val2) {
		try {
			if (val1 != null && val2 != null && !val2.equalsIgnoreCase(val1)) {
				er.addEntry(ere);
				return false;
			}

		} catch (StringIndexOutOfBoundsException e) {
			er.addEntry(ere);
			return false;
		}

		return true;
	}

	/**
	 * Sjekker om tekststreng eksisterer. Returnérer true hvis den fins, ellers
	 * false samt at ErrorReportEntry legges til i ErrorReport
	 * 
	 * @param er
	 *            ErrorReport
	 * @param ere
	 *            ErrorReportEntry
	 * @param val1
	 *            String
	 * @return boolean
	 */
	public boolean controlExists(ErrorReport er, ErrorReportEntry ere,
			String val1) {
		if (val1 != null) {
			return true;
		} else {
			er.addEntry(ere);
			return false;
		}
	}

	/**
	 * Sjekker om tekststreng eksisterer og har innhold/lengde. Returnérer true
	 * hvis den fins, ellers false samt at ErrorReportEntry legges til i
	 * ErrorReport
	 * 
	 * @param er
	 *            ErrorReport
	 * @param ere
	 *            ErrorReportEntry
	 * @param val1
	 *            String
	 * @return boolean
	 */
	public boolean controlExistsAndHasLength(ErrorReport er,
			ErrorReportEntry ere, String val1) {
		if (val1 != null && val1.length() > 0) {
			return true;
		} else {
			er.addEntry(ere);
			return false;
		}
	}

	/**
	 * Sjekker om boolean er true. Returnérer true hvis den er sann, ellers
	 * false samt at ErrorReportEntry legges til i ErrorReport
	 * 
	 * @param er
	 *            ErrorReport
	 * @param ere
	 *            ErrorReportEntry
	 * @param bool
	 *            boolean
	 * @return boolean
	 */
	public boolean controlBoolean(ErrorReport er, ErrorReportEntry ere,
			boolean bool) {
		if (bool == true) {
			return true;
		} else {
			er.addEntry(ere);
			return false;
		}
	}

	/**
	 * Oppretter en DateTime fra en tekst som inneholder dato og tekst med
	 * datoformat
	 * 
	 * @param date
	 *            String
	 * @param format
	 *            String
	 * @return DateTime
	 */
	public DateTime assignDateFromString(String date, String format) {
		try {
			if (date != null && format != null
					&& date.length() == format.length()) {
				DateTimeFormatter formatter = DateTimeFormat.forPattern(format);
				return formatter.parseDateTime(date);
			}
		} catch (IllegalFieldValueException ifve) {
			ifve.getMessage();

		}
		return null;
	}

	/**
	 * Bruker {@link #erKodeIKodeliste} for å sjekke om kode fins i kodeliste,
	 * hvis ja så kjøres {@link #controlExistsAndHasLength} på presisering om
	 * den fins og har innhold.
	 * 
	 * @param er
	 *            ErrorReport
	 * @param ere
	 *            ErrorReportEntry
	 * @param kode
	 *            String
	 * @param kodeliste
	 *            String[]
	 * @param presisering
	 *            String
	 * @return boolean
	 * @see #erKodeIKodeliste(String, String[])
	 * @see #controlExistsAndHasLength(ErrorReport, ErrorReportEntry, String)
	 */
	public boolean controlPresisering(ErrorReport er, ErrorReportEntry ere,
			String kode, String[] kodeliste, String presisering) {
		if (erKodeIKodeliste(kode, kodeliste)) {
			return controlExistsAndHasLength(er, ere, presisering);
		}

		return true;
	}

	/**
	 * Sjekker om kode fins i kodeliste. Returnérer true hvis den fins, ellers
	 * false
	 * 
	 * @param kode
	 *            String
	 * @param kodeliste
	 *            String[]
	 * @return boolean
	 */
	public boolean erKodeIKodeliste(String kode, String[] kodeliste) {
		if (kode != null && kodeliste != null) {
			for (String kodeIListe : kodeliste) {
				if (kode.equalsIgnoreCase(kodeIListe)) {
					return true;
				}
			}
		}

		return false;
	}
}
