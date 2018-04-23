package no.ssb.kostra.control.sensitiv.barnevern;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class is a combination of DOM and SAX parsing since import files like
 * BMECat files are generally too big for DOM parsers but SAX is too
 * inconvenient, this class extracts part of the SAX-stream, converts them into
 * sub-DOMs and calls the application for each sub-DOM.
 * 
 * 
 */
public class XMLReader extends DefaultHandler {

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// Delegate to active handlers...
		String cData = new String(ch).substring(start, start + length);
		for (SAX2DOMHandler handler : activeHandlers) {
			handler.text(cData);
		}
	}

	@Override
	public void endDocument() throws SAXException {
		// Consider iterating over all activeHandler which are not complete
		// yet and raise an exception.
		// For now this is simply ignored to make processing more robust.
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		// Delegate to active handlers and deletes them if they are finished...
		Iterator<SAX2DOMHandler> iter = activeHandlers.iterator();
		while (iter.hasNext()) {
			SAX2DOMHandler handler = iter.next();
			if (handler.endElement(uri, name)) {
				iter.remove();
			}
		}
	}

	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
		// Delegate to active handlers...
		for (SAX2DOMHandler handler : activeHandlers) {
			handler.processingInstruction(target, data);
		}
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		// Delegate to active handlers...
		for (SAX2DOMHandler handler : activeHandlers) {
			handler.startElement(uri, name, attributes);
		}
		// Start a new handler is necessary
		try {
			// QName qualifiedName = new QName(uri, localName);
			NodeHandler handler = handlers.get(name);
			if (handler != null) {
				activeHandlers.add(new SAX2DOMHandler(handler, uri, name,
						attributes));
			}
		} catch (ParserConfigurationException e) {
			throw new SAXException(e);
		}
	}

	private Map<String, NodeHandler> handlers = new TreeMap<String, NodeHandler>();
	private List<SAX2DOMHandler> activeHandlers = new ArrayList<SAX2DOMHandler>();
	private Map<String, String> avgiver = new TreeMap<String, String>();
	private String region;

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
	 * Returns a XMLNode for the given w3c node.
	 */
	public static StructuredNode convert(Node node) {
		return new XMLNodeImpl(node);
	}

	class UserInterruptException extends RuntimeException {

		private static final long serialVersionUID = -7454219131982518216L;
	}

	/**
	 * Parses the given stream and using the given monitor
	 */
	public void parse(InputStream stream) throws ParserConfigurationException,
			SAXException, IOException {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			org.xml.sax.XMLReader reader = saxParser.getXMLReader();
			reader.setEntityResolver(new EntityResolver() {

				public InputSource resolveEntity(String publicId,
						String systemId) throws SAXException, IOException {
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
				}
			});
			reader.setContentHandler(this);
			reader.parse(new InputSource(stream));
		} catch (UserInterruptException e) {
			/*
			 * IGNORED - this is used to cancel parsing if the used tried to
			 * cancel a process.
			 */
		} finally {
			stream.close();
		}
	}

	public String getAvgiver(String key) {
		return avgiver.get(key);
	}

	public void setAvgiver(String key, String value) {
		this.avgiver.put(key, value);
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}
}
