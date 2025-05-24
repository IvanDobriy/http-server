package otus.http.server.container.configuration;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.*;

public class WebXml {
    private final Path path;
    private final Map<String, ApplicationServletConfig> servletConfigMap;

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
            if(!root.getNodeName().equals("web-app")){
                throw new RuntimeException("Expected `web-app` as root node into web.xml file");
            }
            NodeList servletNodes = root.getElementsByTagName("servlet");
            Map<String, String> initParameters;
            for (int i = 0; i < servletNodes.getLength(); i++) {
                Element servletElement = (Element) servletNodes.item(i);
                String servletName = getChildTextContent(servletElement,  "servlet-name").trim();
                String servletClass = getChildTextContent(servletElement, "servlet-class").trim();
                initParameters = new HashMap<>();
                NodeList initParamNodes = servletElement.getElementsByTagName("init-param");
                for(int j = 0; j < initParamNodes.getLength(); j++){
                    Element initParmaNode = (Element) initParamNodes.item(j);
                    String paramName = getChildTextContent(initParmaNode, "param-name").trim();
                    String paramValue = getChildTextContent(initParmaNode, "param-value").trim();
                    initParameters.put(paramName, paramValue);
                }
                if (servletConfigMap.containsKey(servletName)) {
                    throw new RuntimeException("Found servlet with same name into web.xml");
                }
                final var servletConfig = new ApplicationServletConfig();
                servletConfig.setName(servletName);
                servletConfig.setClassName(servletClass);
                servletConfig.setInitParameters(initParameters);

                servletConfigMap.put(servletName, servletConfig);
                servletConfigMap.put(servletConfig.getClassName(), servletConfig);
            }
            NodeList mappingNodes = root.getElementsByTagName("servlet-mapping");
            for (int i = 0; i < mappingNodes.getLength(); i++) {
                List<String> urlPatterns = new ArrayList<>();
                Element mappingElement = (Element) mappingNodes.item(i);
                String servletName = getChildTextContent(mappingElement,  "servlet-name");
                for(String url: getChildTextContentList(mappingElement,  "url-pattern")){
                    urlPatterns.add(getCheckedPath(url));
                }
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

    public Map<String, ApplicationServletConfig> getServletConfigMap() {
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

    private String getCheckedPath(String path) throws URISyntaxException {
        Objects.requireNonNull(path);
        if (path.isBlank()) {
            return "";
        }
        if(path.length() == 1 && path.startsWith("/")){
            return "";
        }
        new URI(path);
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        if (path.startsWith("/")) {
            return path;
        }
        return "/" + path;
    }
}
