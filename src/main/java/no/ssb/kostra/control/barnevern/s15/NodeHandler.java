package no.ssb.kostra.control.barnevern.s15;

import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.felles.Comparator;
import no.ssb.kostra.controlprogram.Arguments;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


public abstract class NodeHandler {
    protected ErrorReport er;
    protected Arguments args;

    /**
     * @param er   ErrorReport
     * @param args Arguments
     */
    public NodeHandler(ErrorReport er, Arguments args) {
        this.er = er;
        this.args = args;
    }


    protected static String defaultString(String s, String defaultString) {
        return (!empty(s)) ? s : defaultString;
    }

    protected static boolean empty(final String s) {
        // Null-safe, short-circuit evaluation.
        return s == null || s.trim().isEmpty();
    }

    protected static String datePresentionFormat(){
        return "dd-MM-yyyy";
    }

    /**
     * Abstract metode som arves og implementeres mot den XML-noden som skal
     * sjekkes
     *
     * @param node XML-noden som skal kontrolleres
     */
    abstract void process(StructuredNode node);

    /**
     * Validérer en grein i XML-en mot en tilsvarende XSD *
     *
     * @param er       ErrorReport
     * @param ere      ErrorReportEntry
     * @param document XML-tre som skal kontrolleres
     * @param xsd      XSD som XML-tre skal valideres mot
     */
    public void controlValidateByXSD(ErrorReport er, ErrorReportEntry ere, Document document, String xsd) {
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

        } catch (SAXException e) {
            // instance document is invalid!
            ere.setErrorText(ere.getErrorText() + ". SAXException: "
                    + e.getMessage());
            er.addEntry(ere);

        } catch (NullPointerException e) {
            ere.setErrorText(ere.getErrorText() + ". NullPointerException: "
                    + e.getMessage());
            er.addEntry(ere);
        }

    }

    /**
     * Sjekker om to tekststrenger er like. Returnérer true hvis de er like,
     * ellers false samt at ErrorReportEntry legges til i ErrorReport
     *
     * @param er   ErrorReport
     * @param ere  ErrorReport
     * @param val1 String
     * @param val2 String
     */
    public void controlEquals(ErrorReport er, ErrorReportEntry ere, String val1, String val2) {
        try {
            if (!empty(val1) && !empty(val2) && !val2.equalsIgnoreCase(val1)) {
                er.addEntry(ere);
            }

        } catch (StringIndexOutOfBoundsException e) {
            er.addEntry(ere);
        }

    }

    /**
     * Sjekker om tekststreng eksisterer. Returnérer true hvis den fins, ellers
     * false samt at ErrorReportEntry legges til i ErrorReport
     *
     * @param er   ErrorReport
     * @param ere  ErrorReportEntry
     * @param val1 String
     * @return boolean
     */
    public boolean controlExists(ErrorReport er, ErrorReportEntry ere, String val1) {
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
     * @param er   ErrorReport
     * @param ere  ErrorReportEntry
     * @param val1 String
     * @return boolean
     */
    public boolean controlExistsAndHasLength(ErrorReport er, ErrorReportEntry ere, String val1) {
        if (val1 != null && val1.length() > 0) {
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
     * @param date   String
     * @param format String
     * @return LocalDate
     */
    public LocalDate assignDateFromString(String date, String format) {
        if (date != null && format != null
                && date.length() == format.length()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDate.parse(date, formatter);
        }
        return null;
    }

    /**
     * Bruker {@link #erKodeIKodeliste} for å sjekke om kode fins i kodeliste,
     * hvis ja så kjøres {@link #controlExistsAndHasLength} på presisering om
     * den fins og har innhold.
     *
     * @param er          ErrorReport
     * @param ere         ErrorReportEntry
     * @param kode        String
     * @param kodeliste   List<String>
     * @param presisering String
     * @return boolean
     * @see #erKodeIKodeliste(String, List<String>)
     * @see #controlExistsAndHasLength(ErrorReport, ErrorReportEntry, String)
     */
    public boolean controlPresisering(ErrorReport er, ErrorReportEntry ere, String kode, List<String> kodeliste, String presisering) {
        if (Comparator.isCodeInCodelist(kode, kodeliste)) {
            return controlExistsAndHasLength(er, ere, presisering);
        }

        return true;
    }

    /**
     * Sjekker om kode fins i kodeliste. Returnérer true hvis den fins, ellers
     * false
     *
     * @param kode      String
     * @param kodeliste List<String>
     * @return boolean
     */
    public boolean erKodeIKodeliste(String kode, List<String> kodeliste) {
        return kodeliste.stream().anyMatch(item -> item.equalsIgnoreCase(kode));
    }

    /**
     * Sjekker om organisajonsnummer er gyldig. Returnérer true hvis gyldig, ellers
     * false
     *
     * @param er    ErrorReport
     * @param ere   ErrorReportEntry
     * @param orgnr String
     * @return boolean
     */
    public boolean controlOrgnr(ErrorReport er, ErrorReportEntry ere, String orgnr) {
        if (!Comparator.isValidOrgnr(orgnr)) {
            er.addEntry(ere);
            return false;
        }

        return true;
    }
}
