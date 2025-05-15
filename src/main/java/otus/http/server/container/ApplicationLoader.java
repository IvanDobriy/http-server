package otus.http.server.container;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import otus.http.server.container.configuration.ApplicationConfig;

import java.nio.file.Path;

public class ApplicationLoader {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());
    private final Path path;
    private Application application;

    public ApplicationLoader(Path path) {
        this.path = path;
    }

    public Application load() {
        if (application == null) {
            //todo load servlet
            final var applicatonConfig = new ApplicationConfig(path);
            application = new Application(applicatonConfig);
        }
        return application;
    }
}
