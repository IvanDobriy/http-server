package otus.http.server.container.configuration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WebXmlTests {
    private static String contextPathPrefix = "/otus/http/server/container/configuration";

    @Test
    void positiveTest() {
        final var expectedConfig = new ServletConfig();
        expectedConfig.setUrlPatterns(List.of("/hello", "/world"));
        expectedConfig.setClassName("com.example.MainServlet");
        expectedConfig.setName("MainServlet");

        final var webXmlFilePath = Paths.get(getClass().getResource(contextPathPrefix + "/web.xml").getPath());
        final var webXML = new WebXml(webXmlFilePath);
        final var configMap = webXML.getServletConfigMap();
        Assertions.assertEquals(2, configMap.size());
        final var configByClassKey = configMap.get("com.example.MainServlet");
        Assertions.assertEquals(expectedConfig.getClassName(), configByClassKey.getClassName());
        Assertions.assertEquals(expectedConfig.getName(), configByClassKey.getName());
        Assertions.assertEquals(expectedConfig.getUrlPatterns(), configByClassKey.getUrlPatterns());
        final var configByNameKey = configMap.get("MainServlet");
        Assertions.assertEquals(configByClassKey, configByNameKey);
    }

    @Test
    void urlIsBlank() {
        final var webXmlFilePath = Paths.get(getClass().getResource(contextPathPrefix + "/web.xml").getPath());

    }


}
