package it.inps.pocmessagebroker.utils;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.Optional;

public interface XMLUtils {
    static Optional<String> getTagValueFromName(Element e, String tagName) {
        NodeList nodeList = e.getElementsByTagName(tagName);
        for (int i=0;i<nodeList.getLength(); i++) {
            Node x = nodeList.item(i);
            if (nodeList.item(i).getFirstChild() != null)
                return Optional.ofNullable(nodeList.item(i).getFirstChild().getNodeValue());
        }
        return Optional.empty();
    }

    static String nodeToString(Node node) {
        StringWriter sw = new StringWriter();
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException te) {
            System.out.println("nodeToString Transformer Exception");
        }
        return sw.toString();
    }
}
