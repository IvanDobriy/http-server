package otus.http.server.container.configuration;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Path;
import java.util.*;

public class WebXml {
    private final Path path;
    private final Map<String, ServletConfig> servletConfigMap;

    public WebXml(Path path) {
        Objects.requireNonNull(path);
        this.path = path;
        servletConfigMap = new HashMap<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newDefaultInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(path.toFile());
            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();
            NodeList servletNodes = root.getElementsByTagName("servlet");
            for (int i = 0; i < servletNodes.getLength(); i++) {
                Element servletElement = (Element) servletNodes.item(i);
                String servletName = getChildTextContent(servletElement,  "servlet-name");
                String servletClass = getChildTextContent(servletElement, "servlet-class");
                if (servletConfigMap.containsKey(servletName)) {
                    throw new RuntimeException("Found servlet with same name into web.xml");
                }
                final var servletConfig = new ServletConfig();
                servletConfig.setName(servletName);
                servletConfig.setClassName(servletClass);
                servletConfigMap.put(servletName, servletConfig);
            }
            NodeList mappingNodes = root.getElementsByTagName("servlet-mapping");
            for (int i = 0; i < mappingNodes.getLength(); i++) {
                Element mappingElement = (Element) mappingNodes.item(i);
                String servletName = getChildTextContent(mappingElement,  "servlet-name");
                List<String> urlPatterns = getChildTextContentList(mappingElement,  "url-pattern");
                if (!servletConfigMap.containsKey(servletName)) {
                    continue;
                }
                final var servletConfig = servletConfigMap.get(servletName);
                servletConfig.setUrlPatterns(urlPatterns);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, ServletConfig> getServletConfigMap() {
        return servletConfigMap;
    }

    private String getChildTextContent(Element parent, String localName) {
        NodeList nodes = parent.getElementsByTagName(localName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent().trim();
        }
        return null;
    }

    private List<String> getChildTextContentList(Element parent, String localName) {
        List<String> contents = new ArrayList<>();
        NodeList nodes = parent.getElementsByTagName(localName);
        for (int i = 0; i < nodes.getLength(); i++) {
            contents.add(nodes.item(i).getTextContent().trim());
        }
        return contents;
    }
}
