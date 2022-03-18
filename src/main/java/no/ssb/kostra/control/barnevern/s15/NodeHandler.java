package no.ssb.kostra.control.barnevern.s15;

import no.ssb.kostra.control.felles.Comparator;
import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@SuppressWarnings("SpellCheckingInspection")
public abstract class NodeHandler {
    protected final ErrorReport errorReport;
    protected final Arguments args;

    /**
     * @param errorReport ErrorReport
     * @param args        Arguments
     */
    protected NodeHandler(ErrorReport errorReport, Arguments args) {
        this.errorReport = errorReport;
        this.args = args;
    }

    protected static String defaultString(final String s, final String defaultString) {
        return (!empty(s)) ? s : defaultString;
    }

    protected static boolean empty(final String s) {
        // Null-safe, short-circuit evaluation.
        return s == null || s.trim().isEmpty();
    }

    protected static final String datePresentionFormat = "dd-MM-yyyy";

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
     * @param errorReport      ErrorReport
     * @param errorReportEntry ErrorReportEntry
     * @param document         XML-tre som skal kontrolleres
     * @param xsd              XSD som XML-tre skal valideres mot
     */
    public void controlValidateByXSD(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry,
            final Document document, final String xsd) {

        try {
            // create a SchemaFactory capable of understanding WXS schemas
            final var factory = SchemaFactory
                    .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

            // load a WXS schema, represented by a Schema instance
            final var inputStream = this.getClass().getClassLoader().getResourceAsStream(xsd);
            if (inputStream == null) {
                throw new FileNotFoundException("Kunne ikke finne fil " + xsd);
            }

            final var schemaFile = new StreamSource(inputStream);
            final var schema = factory.newSchema(schemaFile);

            // create a Validator instance, which can be used to
            // validate an
            // instance document
            final var validator = schema.newValidator();

            // validate the DOM tree
            validator.validate(new DOMSource(document));

        } catch (IOException e) {
            errorReportEntry.setErrorText(errorReportEntry.getErrorText()
                    + ". IOException: "
                    + e.getMessage());
            errorReport.addEntry(errorReportEntry);
        } catch (SAXException e) {
            // instance document is invalid!
            errorReportEntry.setErrorText(errorReportEntry.getErrorText()
                    + ". SAXException: "
                    + e.getMessage());
            errorReport.addEntry(errorReportEntry);
        } catch (NullPointerException e) {
            errorReportEntry.setErrorText(errorReportEntry.getErrorText()
                    + ". NullPointerException: "
                    + e.getMessage());
            errorReport.addEntry(errorReportEntry);
        }
    }

    /**
     * Sjekker om to tekststrenger er like. Returnérer true hvis de er like,
     * ellers false samt at ErrorReportEntry legges til i ErrorReport
     *
     * @param errorReport      ErrorReport
     * @param errorReportEntry ErrorReport
     * @param val1             String
     * @param val2             String
     */
    public void controlEquals(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry,
            final String val1, final String val2) {

        try {
            if (!empty(val1) && !empty(val2) && !val2.equalsIgnoreCase(val1)) {
                errorReport.addEntry(errorReportEntry);
            }
        } catch (StringIndexOutOfBoundsException e) {
            errorReport.addEntry(errorReportEntry);
        }
    }

    /**
     * Sjekker om tekststreng eksisterer. Returnérer true hvis den fins, ellers
     * false samt at ErrorReportEntry legges til i ErrorReport
     *
     * @param errorReport      ErrorReport
     * @param errorReportEntry ErrorReportEntry
     * @param val1             String
     * @return boolean
     */
    public boolean controlExists(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry, final String val1) {

        if (val1 != null) {
            return true;
        } else {
            errorReport.addEntry(errorReportEntry);
            return false;
        }
    }

    /**
     * Sjekker om tekststreng eksisterer og har innhold/lengde. Returnérer true
     * hvis den fins, ellers false samt at ErrorReportEntry legges til i
     * ErrorReport
     *
     * @param errorReport      ErrorReport
     * @param errorReportEntry ErrorReportEntry
     * @param val1             String
     * @return boolean
     */
    public boolean controlExistsAndHasLength(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry, final String val1) {

        if (val1 != null && val1.length() > 0) {
            return true;
        } else {
            errorReport.addEntry(errorReportEntry);
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
    public LocalDate assignDateFromString(final String date, final String format) {
        if (date != null
                && format != null
                && date.length() == format.length()) {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern(format));
        }
        return null;
    }

    /**
     * Bruker {@link #erKodeIKodeliste} for å sjekke om kode fins i kodeliste,
     * hvis ja så kjøres {@link #controlExistsAndHasLength} på presisering om
     * den fins og har innhold.
     *
     * @param errorReport      ErrorReport
     * @param errorReportEntry ErrorReportEntry
     * @param kode             String
     * @param kodeliste        List<String>
     * @param presisering      String
     * @return boolean
     * @see #erKodeIKodeliste(String, List)
     * @see #controlExistsAndHasLength(ErrorReport, ErrorReportEntry, String)
     */
    public boolean controlPresisering(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry,
            final String kode, final List<String> kodeliste, final String presisering) {

        if (Comparator.isCodeInCodeList(kode, kodeliste)) {
            return controlExistsAndHasLength(errorReport, errorReportEntry, presisering);
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
    public boolean erKodeIKodeliste(final String kode, final List<String> kodeliste) {
        return kodeliste.stream().anyMatch(item -> item.equalsIgnoreCase(kode));
    }

    /**
     * Sjekker om organisajonsnummer er gyldig. Returnérer true hvis gyldig, ellers
     * false
     *
     * @param errorReport    ErrorReport
     * @param errorReportEntry   ErrorReportEntry
     * @param orgnr String
     * @return boolean
     */
    public boolean controlOrgnr(
            final ErrorReport errorReport, final ErrorReportEntry errorReportEntry, final String orgnr) {

        if (Comparator.isValidOrgnr(orgnr)) {
            return true;
        }

        errorReport.addEntry(errorReportEntry);
        return false;
    }
}
