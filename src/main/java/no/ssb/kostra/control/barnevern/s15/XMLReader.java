package no.ssb.kostra.control.barnevern.s15;

import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.net.URL;
import java.util.*;

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
    public static StructuredNode convert(Node node) {
        return new XMLNodeImpl(node);
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        // Delegate to active handlers...
        String cData = new String(ch).substring(start, start + length);
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
    public void endElement(String uri, String localName, String name) {
        // Delegate to active handlers and deletes them if they are finished...
        activeHandlers.removeIf(handler -> handler.endElement(uri, name));
    }

    @Override
    public void processingInstruction(String target, String data) {
        // Delegate to active handlers...
        for (SAX2DOMHandler handler : activeHandlers) {
            handler.processingInstruction(target, data);
        }
    }

    @Override
    public void startElement(String ignored, String localName, String name,
                             Attributes attributes) throws SAXException {
        // Delegate to active handlers...
        for (SAX2DOMHandler handler : activeHandlers) {
            handler.startElement(name, attributes);
        }
        // Start a new handler is necessary
        try {
            NodeHandler handler = handlers.get(name);
            if (handler != null) {
                activeHandlers.add(new SAX2DOMHandler(handler, name,
                        attributes));
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
    public void addHandler(String name, NodeHandler handler) {
        handlers.put(name, handler);
    }

    /**
     * Parses the given stream and using the given monitor
     */
    public void parse(InputStream stream) throws ParserConfigurationException,
            SAXException, IOException {
        try (stream) {
            org.xml.sax.XMLReader reader = createXMLReader();
            reader.setEntityResolver((publicId, systemId) -> {
                URL url = new URL(systemId);
                // Check if file is local
                if ("file".equals(url.getProtocol())) {
                    // Check if file exists
                    File file = new File(url.getFile());
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
    public void parse(String xmlAsString) throws ParserConfigurationException,
            SAXException {
        try {
            org.xml.sax.XMLReader reader = createXMLReader();
            reader.setEntityResolver((publicId, systemId) -> {
                if (!xmlAsString.isEmpty()) {
                    return new InputSource(new StringReader(xmlAsString));
                }
                return null;
            });
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
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

        SAXParser saxParser = factory.newSAXParser();
        return saxParser.getXMLReader();
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    static class UserInterruptException extends RuntimeException {

        private static final long serialVersionUID = -7454219131982518216L;
    }
}
