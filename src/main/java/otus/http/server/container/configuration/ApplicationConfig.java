package otus.http.server.container.configuration;

import java.nio.file.Path;
import java.util.Map;

public class ApplicationConfig {
    private final Map<String, ServletConfig> servletConfigMap;
    private final Path webXmlPath;
    private final Path libPath;
    private final Path classesPath;
    private final Path applicatinPath;

    public ApplicationConfig(Path path) {
        applicatinPath = path;
        webXmlPath = path.resolve("./WEB-INF/web.xml");
        libPath = path.resolve("./WEB-INF/lib");
        classesPath = path.resolve("./WEB-INF/classes");
        final var webXml = new WebXml(webXmlPath);
        servletConfigMap = webXml.getServletConfigMap();
    }

    public Map<String, ServletConfig> getServletConfigMap() {
        return servletConfigMap;
    }

    public Path getClassesPath() {
        return classesPath;
    }
}
