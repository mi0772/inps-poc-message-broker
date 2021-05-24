package it.inps.pocmessagebroker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static it.inps.pocmessagebroker.utils.XMLUtils.nodeToString;

public class EventoArcaDetails implements Serializable {

    private String xml;

    private EventoArcaDetails(String xml) {
        this.xml = xml;
    }

    public String getXml() {
        return xml;
    }

    public static EventoArcaDetails fromWSResponse(String rawResponse) {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();

            var in = new ByteArrayInputStream(rawResponse.getBytes(StandardCharsets.UTF_8));
            Document doc = db.parse(in);
            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("p115:RicercaConCodiceFiscaleInteroResponse");

            return new EventoArcaDetails(nodeToString(list.item(0)));

        } catch (SAXException | ParserConfigurationException | IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
