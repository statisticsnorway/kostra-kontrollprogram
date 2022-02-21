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
    private Node node;

    public XMLNodeImpl(Node root) {
        node = root;
    }

    @Override
    public Node getNode() {
        return node;
    }

    public StructuredNode queryNode(String path)
            throws XPathExpressionException {
        Node result = (Node) XPATH.newXPath().compile(path)
                .evaluate(node, XPathConstants.NODE);
        if (result == null) {
            return null;
        }
        return new XMLNodeImpl(result);
    }

    public List<StructuredNode> queryNodeList(String path)
            throws XPathExpressionException {
        NodeList result = (NodeList) XPATH.newXPath().compile(path)
                .evaluate(node, XPathConstants.NODESET);
        List<StructuredNode> resultList = new ArrayList<>(
                result.getLength());
        for (int i = 0; i < result.getLength(); i++) {
            resultList.add(new XMLNodeImpl(result.item(i)));
        }
        return resultList;
    }

    public StructuredNode[] queryNodes(String path)
            throws XPathExpressionException {
        List<StructuredNode> nodes = queryNodeList(path);
        return nodes.toArray(new StructuredNode[nodes.size()]);
    }

    public String queryString(String path) throws XPathExpressionException {
        Object result = XPATH.newXPath().compile(path)
                .evaluate(node, XPathConstants.NODE);
        if (result == null) {
            return null;
        }
        if (result instanceof Node) {
            String s = ((Node) result).getTextContent();
            if (s != null) {
                return s.trim();
            }
            return s;
        }
        return result.toString().trim();
    }

    public boolean isEmpty(String path) throws XPathExpressionException {
        String result = queryString(path);
        return result == null || "".equals(result);
    }

    public String getNodeName() {
        return node.getNodeName();
    }

    @Override
    public String toString() {
        return getNodeName();
    }

    public Value queryValue(String path) throws XPathExpressionException {
        return Value.of(queryString(path));
    }

    @Override
    public int compareTo(StructuredNode other) {
        int result = 0;

        try {
            LocalDate startDato = this.assignDateFromString(
                    queryString("@StartDato"), DATO_FORMAT_LANGT);
            LocalDate otherStartDato = other.assignDateFromString(
                    other.queryString("@StartDato"), DATO_FORMAT_LANGT);

            if (startDato.isBefore(otherStartDato)) {
                result = -1;

            } else if (otherStartDato.isBefore(startDato)) {
                result = 1;
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
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
    public LocalDate assignDateFromString(String date, String format) {
        if (date != null && format != null
                && date.length() == format.length()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDate.parse(date, formatter);
        }
        return null;
    }
}
