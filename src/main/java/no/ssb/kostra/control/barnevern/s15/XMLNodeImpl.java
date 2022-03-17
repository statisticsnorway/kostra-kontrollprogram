package no.ssb.kostra.control.barnevern.s15;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation for XMLNode
 */
public class XMLNodeImpl implements StructuredNode {

    private static final XPathFactory XPATH = XPathFactory.newInstance();
    private static final String DATO_FORMAT_LANGT = "yyyy-MM-dd";
    private final Node node;

    public XMLNodeImpl(Node root) {
        node = root;
    }

    @Override
    public Node getNode() {
        return node;
    }

    public StructuredNode queryNode(final String path) throws XPathExpressionException {
        var result = (Node) XPATH.newXPath().compile(path)
                .evaluate(node, XPathConstants.NODE);

        return result == null
                ? null
                : new XMLNodeImpl(result);
    }

    public List<StructuredNode> queryNodeList(final String path) throws XPathExpressionException {
        var result = (NodeList) XPATH.newXPath().compile(path)
                .evaluate(node, XPathConstants.NODESET);

        var resultList = new ArrayList<StructuredNode>(result.getLength());
        for (var i = 0; i < result.getLength(); i++) {
            resultList.add(new XMLNodeImpl(result.item(i)));
        }
        return resultList;
    }

    public StructuredNode[] queryNodes(final String path) throws XPathExpressionException {
        final var nodes = queryNodeList(path);
        return nodes.toArray(new StructuredNode[nodes.size()]); // TODO: Suspicious
    }

    public String queryString(final String path) throws XPathExpressionException {
        final var result = XPATH.newXPath().compile(path).evaluate(node, XPathConstants.NODE);

        if (result == null) {
            return null;
        }
        if (result instanceof Node) {
            final var s = ((Node) result).getTextContent();
            return s != null
                    ? s.trim()
                    : null;
        }
        return result.toString().trim();
    }

    public boolean isEmpty(final String path) throws XPathExpressionException {
        final var result = queryString(path);
        return result == null || "".equals(result);
    }

    public String getNodeName() {
        return node.getNodeName();
    }

    @Override
    public String toString() {
        return getNodeName();
    }

    public Value queryValue(final String path) throws XPathExpressionException {
        return Value.of(queryString(path));
    }

    @Override
    public int compareTo(final StructuredNode other) {
        var result = 0;

        try {
            var startDato = this.assignDateFromString(
                    queryString("@StartDato"), DATO_FORMAT_LANGT);
            var otherStartDato = other.assignDateFromString(
                    other.queryString("@StartDato"), DATO_FORMAT_LANGT);

            if (startDato.isBefore(otherStartDato)) {
                result = -1;
            } else if (otherStartDato.isBefore(startDato)) {
                result = 1;
            }
        } catch (XPathExpressionException ignored) {
            // Ignore exception
        }
        return result;
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
            final var formatter = DateTimeFormatter.ofPattern(format);
            return LocalDate.parse(date, formatter);
        }
        return null;
    }
}
