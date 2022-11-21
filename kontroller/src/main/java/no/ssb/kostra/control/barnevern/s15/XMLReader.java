package no.ssb.kostra.control.barnevern.s15;

import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class is a combination of DOM and SAX parsing since import files like
 * BMECat files are generally too big for DOM parsers but SAX is too
 * inconvenient, this class extracts part of the SAX-stream, converts them into
 * sub-DOMs and calls the application for each sub-DOM.
 */
public class XMLReader extends DefaultHandler {

    private final Map<String, NodeHandler> handlers = new TreeMap<>();
    private final List<SAX2DOMHandler> activeHandlers = new ArrayList<>();
    private String region;

    /**
     * Returns a XMLNode for the given w3c node.
     */
    public static StructuredNode convert(final Node node) {
        return new XMLNodeImpl(node);
    }

    @Override
    public void characters(final char[] ch, final int start, final int length) {
        // Delegate to active handlers...
        final var cData = new String(ch).substring(start, start + length);
        for (SAX2DOMHandler handler : activeHandlers) {
            handler.text(cData);
        }
    }

    @Override
    public void endDocument() {
        // Consider iterating over all activeHandler which are not complete
        // yet and raise an exception.
        // For now this is simply ignored to make processing more robust.
    }

    @Override
    public void endElement(final String uri, final String localName, final String name) {
        // Delegate to active handlers and deletes them if they are finished...
        activeHandlers.removeIf(handler -> handler.endElement(uri, name));
    }

    @Override
    public void processingInstruction(final String target, final String data) {
        // Delegate to active handlers...
        for (var handler : activeHandlers) {
            handler.processingInstruction(target, data);
        }
    }

    @Override
    public void startElement(
            final String ignored, final String localName,
            final String name, final Attributes attributes) throws SAXException {

        // Delegate to active handlers...
        for (var handler : activeHandlers) {
            handler.startElement(name, attributes);
        }

        // Start a new handler is necessary
        try {
            var handler = handlers.get(name);
            if (handler != null) {
                activeHandlers.add(new SAX2DOMHandler(handler, name, attributes));
            }
        } catch (ParserConfigurationException e) {
            throw new SAXException(e);
        }
    }

    /**
     * Registers a new handler for a qualified name of a node. Handlers are
     * invoked AFTER the complete node was read. Since documents like BMECat
     * usually don't mix XML-data, namespaces are ignored for now which eases
     * the processing a lot (especially xpath related tasks). Namespaces however
     * could be easily added by replacing String with QName here.
     */
    public void addHandler(final String name, final NodeHandler handler) {
        handlers.put(name, handler);
    }

    /**
     * Parses the given stream and using the given monitor
     */
    public void parse(final InputStream stream)
            throws ParserConfigurationException, SAXException, IOException {

        try (stream) {
            var reader = createXMLReader();
            reader.setEntityResolver((publicId, systemId) -> {
                var url = new URL(systemId);

                // Check if file is local
                if ("file".equals(url.getProtocol())) {
                    // Check if file exists
                    var file = new File(url.getFile());
                    if (file.exists()) {
                        return new InputSource(new FileInputStream(file));
                    }
                }
                return null;
            });
            reader.setContentHandler(this);
            reader.parse(new InputSource(stream));
        } catch (UserInterruptException e) {
            /*
             * IGNORED - this is used to cancel parsing if the used tried to
             * cancel a process.
             */
        }
    }

    /**
     * Parses the given stream and using the given monitor
     */
    public void parse(final String xmlAsString)
            throws ParserConfigurationException, SAXException {

        try {
            final var reader = createXMLReader();
            reader.setEntityResolver((publicId, systemId) -> !xmlAsString.isEmpty()
                    ? new InputSource(new StringReader(xmlAsString))
                    : null);
            reader.setContentHandler(this);
            reader.parse(new InputSource(new StringReader(xmlAsString)));
        } catch (UserInterruptException | IOException e) {
            /*
             * IGNORED - this is used to cancel parsing if the used tried to
             * cancel a process.
             */
        }
    }

    private org.xml.sax.XMLReader createXMLReader() throws ParserConfigurationException, SAXException {
        final var factory = SAXParserFactory.newInstance();
        // TODO: Investigate http
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

        final var saxParser = factory.newSAXParser();
        return saxParser.getXMLReader();
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    static class UserInterruptException extends RuntimeException {
        private static final long serialVersionUID = -7454219131982518216L;
    }
}
