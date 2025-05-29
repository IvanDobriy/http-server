package otus.http.server.container.configuration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.nio.file.Paths;
import java.util.logging.Logger;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContextXmlTests {
    private static String contextPathPrefix = "/otus/http/server/container/configuration";

    @Test
    void positiveTest() {
        final var contextFilePath = Paths.get(getClass().getResource(contextPathPrefix + "/context.xml").getPath());
        final var contextXMl = new ContextXml(contextFilePath);
        final var contextPath = contextXMl.getContextPath();
        Assertions.assertEquals("/test", contextPath);
    }

    @Test
    void pathWithoutSlashInFrontOfUri() {
        final var contextFilePath = Paths.get(getClass().getResource(contextPathPrefix + "/contextPathWithoutBackSlashInFrontOfUrl.xml").getPath());
        final var contextXMl = new ContextXml(contextFilePath);
        final var contextPath = contextXMl.getContextPath();
        Assertions.assertEquals("/test", contextPath);
    }

    @Test
    void pathIsBlank() {
        final var contextFilePath = Paths.get(getClass().getResource(contextPathPrefix + "/contextPathIsBlank.xml").getPath());
        Assertions.assertThrows(Exception.class, () -> {
            new ContextXml(contextFilePath);
        });
    }

    @Test
    void pathNotFound() {
        final var contextFilePath = Paths.get(getClass().getResource(contextPathPrefix + "/contextPathNotFound.xml").getPath());
        Assertions.assertThrows(Exception.class, () -> {
            new ContextXml(contextFilePath);
        });
    }

    @Test
    void pathEndsWithSlash() {
        final var contextFilePath = Paths.get(getClass().getResource(contextPathPrefix + "/contextPathEndsWithSlash.xml").getPath());
        final var contextXMl = new ContextXml(contextFilePath);
        final var contextPath = contextXMl.getContextPath();
        Assertions.assertEquals("/test", contextPath);
    }

    @Test
    void unsupportedPath() {
        final var contextFilePath = Paths.get(getClass().getResource(contextPathPrefix + "/unsupportedPath.xml").getPath());
        Assertions.assertThrows(Exception.class, () -> {
            new ContextXml(contextFilePath);
        });
    }

    @Test
    void unsupportedWildcardPath(){
        final var contextFilePath = Paths.get(getClass().getResource(contextPathPrefix + "/contextWithWildcard.xml").getPath());
        Assertions.assertThrows(Exception.class, () -> {
            new ContextXml(contextFilePath);
        });
    }
    @Test
    void contextPathContainsOnlySlash() {
        final var contextFilePath = Paths.get(getClass().getResource(contextPathPrefix + "/contextContainsOnlySlash.xml").getPath());
        Assertions.assertThrows(Exception.class, () -> {
            new ContextXml(contextFilePath);
        });
    }

    @Test
    void contextNodeNotFound() {
        final var contextFilePath = Paths.get(getClass().getResource(contextPathPrefix + "/contextNodeNotFound.xml").getPath());
        Assertions.assertThrows(Exception.class, () -> {
            new ContextXml(contextFilePath);
        });
    }
}
