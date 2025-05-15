package otus.http.server.container;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import otus.http.server.file.War;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

public class Container {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());
    private final FileListener fileListener;
    private final ConcurrentHashMap<Path, ApplicationLoader> servlets;
    private final Path path;

    private void checkPath() {
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Path extractWar(Path path) {
        if (!Files.isDirectory(path) && path.toString().endsWith(".war")) {
            logger.info("Extract war with name: {}", path);
            String fileName = path.getFileName().toString().split("\\.")[0];
            Path extractTo;
            extractTo = path.getParent().resolve(fileName);
            if (Files.exists(extractTo)) {
                logger.info("Servlet: {} exists", extractTo);
                return extractTo;
            }
            War.extract(path, extractTo);
            return extractTo;
        }
        return null;
    }

    private void loadServlet(Path path) {
        if (servlets.containsKey(path)) {
            logger.info("current servlet {} exists", path);
            return;
        }
        final var applicationLoader = new ApplicationLoader(path);
        final var httpServlet = applicationLoader.load();
        servlets.put(path, applicationLoader);
        logger.info("servlet is loaded by path: {}", path);
    }

    public Container() {
        this.path = Paths.get("./containers");
        checkPath();
        servlets = new ConcurrentHashMap<>();
        fileListener = new FileListener(path);
        fileListener.setOnCreate((into) -> {
            if (extractWar(into) != null) {
                return;
            }
            loadServlet(into);
        });

        fileListener.setOnDelete((into) -> {
            if (!servlets.contains(into)) {
                return;
            }
            //todo stop servlet
//            final var httpServlet = servlets.remove(into);
            logger.info("on delete {}", into);
        });
    }

    private void loadServletsOnStart() {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
            Path extractedPath;
            for (Path path : directoryStream) {
                if ((extractedPath = extractWar(path)) != null) {
                    loadServlet(extractedPath);
                } else {
                    loadServlet(path);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        loadServletsOnStart();
        fileListener.run();
    }

    public void stop() {
        fileListener.stop();
    }
}
