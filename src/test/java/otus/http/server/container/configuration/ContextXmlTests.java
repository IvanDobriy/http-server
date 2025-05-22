package otus.http.server.container.configuration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.nio.file.Paths;
import java.util.logging.Logger;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContextXmlTests {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Test
    void positiveTest() {
        final var contextFilePath = Paths.get(getClass().getResource("/otus/http/server/container/configuration/context.xml").getPath());
        final var contextXMl = new ContextXml(contextFilePath);
        final var contextPath = contextXMl.getContextPath();
        Assertions.assertEquals("/test", contextPath);
    }
}
