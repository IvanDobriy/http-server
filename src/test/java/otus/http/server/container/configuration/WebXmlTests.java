package otus.http.server.container.configuration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WebXmlTests {
    private static String contextPathPrefix = "/otus/http/server/container/configuration";

    @Test
    void positiveTest() {
        final var expectedConfig = new ApplicationServletConfig();
        expectedConfig.setUrlPatterns(List.of("/hello", "/world"));
        expectedConfig.setClassName("com.example.MainServlet");
        expectedConfig.setName("MainServlet");
        expectedConfig.setInitParameters(Map.of("value1", "1", "value2", "2"));
        expectedConfig.setContextParameters(Map.of("param1", "p1", "param2", "p2"));


        final var webXmlFilePath = Paths.get(getClass().getResource(contextPathPrefix + "/web.xml").getPath());
        final var webXML = new WebXml(webXmlFilePath);
        final var configMap = webXML.getServletConfigMap();
        Assertions.assertEquals(2, configMap.size());
        final var configByClassKey = configMap.get("com.example.MainServlet");

        Assertions.assertEquals(expectedConfig.getClassName(), configByClassKey.getClassName());
        Assertions.assertEquals(expectedConfig.getName(), configByClassKey.getName());
        Assertions.assertEquals(expectedConfig.getUrlPatterns(), configByClassKey.getUrlPatterns());
        Assertions.assertEquals(expectedConfig.getInitParameters(), configByClassKey.getInitParameters());
        Assertions.assertEquals(expectedConfig.getContextParameters(), configByClassKey.getContextParameters());
        final var configByNameKey = configMap.get("MainServlet");
        Assertions.assertEquals(configByClassKey, configByNameKey);
        Assertions.assertEquals(expectedConfig.getInitParameters(), configByNameKey.getInitParameters());
        Assertions.assertEquals(expectedConfig.getContextParameters(), configByNameKey.getContextParameters());
    }

    @Test
    void urlIsBlank() {
        final var expectedUrlPatterns = List.of("", "");
        final var webXmlFilePath = Paths.get(getClass().getResource(contextPathPrefix + "/webUrlIsBlank.xml").getPath());
        final var webXML = new WebXml(webXmlFilePath);
        final var configMap = webXML.getServletConfigMap();
        final var config = configMap.get("MainServlet");
        Assertions.assertEquals(expectedUrlPatterns, config.getUrlPatterns());
    }

    @Test
    void urlsNotFound(){
        final var expectedUrlPatterns = List.of();
        final var webXmlFilePath = Paths.get(getClass().getResource(contextPathPrefix + "/webUrlIsNotFound.xml").getPath());
        final var webXML = new WebXml(webXmlFilePath);
        final var configMap = webXML.getServletConfigMap();
        final var config = configMap.get("MainServlet");
        Assertions.assertEquals(expectedUrlPatterns, config.getUrlPatterns());
    }

    @Test
    void webAppNodeNotFound() {
        final var webXmlFilePath = Paths.get(getClass().getResource(contextPathPrefix + "/webWebAppNodeNotFound.xml").getPath());
        Assertions.assertThrows(Exception.class, ()->{
            new WebXml(webXmlFilePath);
        });
    }

    @Test
    void unsupportedUrl(){
        final var webXmlFilePath = Paths.get(getClass().getResource(contextPathPrefix + "/webUnsupportedUrl.xml").getPath());
        Assertions.assertThrows(Exception.class, ()->{
            new WebXml(webXmlFilePath);
        });
    }
}
