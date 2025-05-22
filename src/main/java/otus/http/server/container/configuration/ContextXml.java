package otus.http.server.container.configuration;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;

public class ContextXml {
    private final Path path;
    private String contextPath;

    public ContextXml(Path path) {
        Objects.requireNonNull(path);
        this.path = path;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newDefaultInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(path.toFile());
            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();
            if (!root.getNodeName().equals("Context")) {
                throw new RuntimeException("Expected 'Context' as root element");
            }
            contextPath = getCheckedPath(root.getAttribute("path"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getCheckedPath(String path) throws URISyntaxException {
        Objects.requireNonNull(path);
        if (path.isBlank()) {
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

    public String getContextPath() {
        return contextPath;
    }
}
