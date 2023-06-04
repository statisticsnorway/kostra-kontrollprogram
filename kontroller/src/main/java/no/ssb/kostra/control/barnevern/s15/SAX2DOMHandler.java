package no.ssb.kostra.control.barnevern.s15;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Used to create a dom-tree for incoming nodes.
 */
@Deprecated
class SAX2DOMHandler {

	private final Document document;
	private Node root;
	private Node currentNode;
	private final NodeHandler nodeHandler;

	public SAX2DOMHandler(
			NodeHandler handler,
			String name,
			Attributes attributes) throws ParserConfigurationException {

		this.nodeHandler = handler;
		document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

		createElement(name, attributes);
	}

	private boolean nodeUp() {
		if (isComplete()) {
			nodeHandler.process(new XMLNodeImpl(root));
			return true;
		}
		currentNode = currentNode.getParentNode();
		return false;
	}

	private boolean isComplete() {
		return currentNode.equals(root);
	}

	private void createElement(final String name, final Attributes attributes) {
		final var element = document.createElement(name);

		for (var i = 0; i < attributes.getLength(); i++) {
			var attrName = attributes.getLocalName(i);
			if (attrName == null || "".equals(attrName)) {
				attrName = attributes.getQName(i);
			}
			if (attrName != null) {
				element.setAttribute(attrName, attributes.getValue(i));
			}
		}
		if (currentNode != null) {
			currentNode.appendChild(element);
		} else {
			root = element;
			document.appendChild(element);
		}
		currentNode = element;
	}

	public Node getRoot() {
		return root;
	}

	public void startElement(final String name, final Attributes attributes) {
		createElement(name, attributes);
	}

	public void processingInstruction(final String target, final String data) {
		final var instruction = document.createProcessingInstruction(target, data);
		currentNode.appendChild(instruction);
	}

	public boolean endElement(final String uri, final String name) {
		if (!currentNode.getNodeName().equals(name)) {
			throw new DOMException(DOMException.SYNTAX_ERR,
					"Unexpected end-tag: " + name + " expected: "
							+ currentNode.getNodeName());
		}
		return nodeUp();
	}

	public void text(final String data) {
		currentNode.appendChild(document.createTextNode(data));
	}

	public NodeHandler getNodeHandler() {
		return nodeHandler;
	}
}
