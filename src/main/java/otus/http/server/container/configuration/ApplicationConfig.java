package otus.http.server.container.configuration;

import java.nio.file.Path;
import java.util.Map;

public class ApplicationConfig {
    private final Map<String, ApplicationServletConfig> servletConfigMap;
    private final Path webXmlPath;
    private final Path contextXmlPath;
    private final Path libPath;
    private final Path classesPath;
    private final Path applicatinPath;
    private final String contextPath;

    public ApplicationConfig(Path path) {
        applicatinPath = path;
        webXmlPath = path.resolve("./WEB-INF/web.xml");
        libPath = path.resolve("./WEB-INF/lib");
        classesPath = path.resolve("./WEB-INF/classes");
        contextXmlPath = path.resolve("./META-INF/context.xml");
        final var webXml = new WebXml(webXmlPath);
        servletConfigMap = webXml.getServletConfigMap();
        final var contextXml = new ContextXml(contextXmlPath);
        contextPath = contextXml.getContextPath();
    }

    public Map<String, ApplicationServletConfig> getServletConfigMap() {
        return servletConfigMap;
    }

    public Path getClassesPath() {
        return classesPath;
    }

    public Path getLibPath() {
        return libPath;
    }

    public String getContextPath() {
        return contextPath;
    }
}
