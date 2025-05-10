package otus.http.server.container;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Container {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());
    private final FileListener fileListener;

    public Container() {
        final var path = Paths.get("./containers");
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        fileListener = new FileListener(path);
        fileListener.setOnCreate((into) -> {
            logger.info("on create {}", into);
        });
        fileListener.setOnDelete((into) -> {
            logger.info("on delete {}", into);
        });
    }

    public void run() {
        fileListener.run();
    }

    public void stop() {
        fileListener.stop();
    }
}
