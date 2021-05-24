package it.inps.pocmessagebroker.model;

import lombok.Data;
import lombok.ToString;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static it.inps.pocmessagebroker.utils.XMLUtils.getTagValueFromName;
import static it.inps.pocmessagebroker.utils.XMLUtils.nodeToString;

@Data
@ToString(callSuper = false)
public class EventoArca {
    private String CFCCC1_EV0;
    private String PROGR_EV0;
    private String TIMEST1_EV0;
    private String SET_EV0;
    private String FUNZ_EV0;
    private String VARIAZ_IM0;
    private String CODAGGR_EV0;
    private String CFCCC1F_EV0;
    private String PROGRF_EV0;
    private String FIRVAR_EV0;
    private String ARCHGEOR_EV0;
    private String PROGETOR_EV0;
    private String FIRVARO_EV0;
    private String ARKAPPL_EV0;
    private String PGKCOD_EV0;
    private String ARKHTRAN_EV0;
    private String PROFILO;
    private String RETURNCODE;
    private String xml;

    public String getChiaveArca() {
        return String.format("%s%08d", this.getCFCCC1_EV0(), Integer.parseInt(this.getPROGR_EV0()));
    }

    public static List<EventoArca> getFromWSResponse(String rawResponse) {
        var response = new ArrayList<EventoArca>(1000);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();

            var in = new ByteArrayInputStream(rawResponse.getBytes(StandardCharsets.UTF_8));
            Document doc = db.parse(in);
            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("EventoArca");

            for (int temp = 0; temp < list.getLength(); temp++) {
                Node node = list.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    EventoArca r = new EventoArca();
                    r.setCFCCC1_EV0     (getTagValueFromName(element, "CFCCC1_EV0").orElse(""));
                    r.setPROGR_EV0      (getTagValueFromName(element, "PROGR_EV0").orElse(""));
                    r.setTIMEST1_EV0    (getTagValueFromName(element, "TIMEST1_EV0").orElse(""));
                    r.setSET_EV0        (getTagValueFromName(element, "SET_EV0").orElse(""));
                    r.setFUNZ_EV0       (getTagValueFromName(element, "FUNZ_EV0").orElse(""));
                    r.setVARIAZ_IM0     (getTagValueFromName(element, "VARIAZ_IM0").orElse(""));
                    r.setCODAGGR_EV0    (getTagValueFromName(element, "CODAGGR_EV0").orElse(""));
                    r.setCFCCC1F_EV0    (getTagValueFromName(element, "CFCCC1F_EV0").orElse(""));
                    r.setPROGRF_EV0     (getTagValueFromName(element, "PROGRF_EV0").orElse(""));
                    r.setFIRVAR_EV0     (getTagValueFromName(element, "FIRVAR_EV0").orElse(""));
                    r.setARCHGEOR_EV0   (getTagValueFromName(element, "ARCHGEOR_EV0").orElse(""));
                    r.setPROGETOR_EV0   (getTagValueFromName(element, "PROGETOR_EV0").orElse(""));
                    r.setFIRVARO_EV0    (getTagValueFromName(element, "FIRVARO_EV0").orElse(""));
                    r.setARKAPPL_EV0    (getTagValueFromName(element, "ARKAPPL_EV0").orElse(""));
                    r.setPGKCOD_EV0     (getTagValueFromName(element, "PGKCOD_EV0").orElse(""));
                    r.setARKHTRAN_EV0   (getTagValueFromName(element, "ARKHTRAN_EV0").orElse(""));
                    r.setPROFILO        (getTagValueFromName(element, "PROFILO").orElse(""));
                    r.setRETURNCODE     (getTagValueFromName(element, "RETURNCODE").orElse(""));
                    r.setXml(nodeToString(node));
                    response.add(r);
                }
            }

        } catch (SAXException | ParserConfigurationException | IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        return response;
    }


}
