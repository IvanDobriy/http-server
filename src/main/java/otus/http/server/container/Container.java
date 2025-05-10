package otus.http.server.container;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import otus.http.server.file.War;

import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

public class Container {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());
    private final FileListener fileListener;
    private final ConcurrentHashMap<Path, ServletLoader> servlets;

    public Container() {
        final var path = Paths.get("./containers");
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        servlets = new ConcurrentHashMap<>();
        fileListener = new FileListener(path);
        fileListener.setOnCreate((into) -> {
            if(into.toString().endsWith(".war")){
                logger.info("Extract war with name: {}", into);
                War.extract(into.toAbsolutePath(), path.resolve(into.getFileName().toString().split("\\.")[0]));
                return;
            }
            if(servlets.contains(into)){
                logger.info("current servlet {} exists", into);
                return;
            }
            final var servletLoader = new ServletLoader(into);
            final var httpServlet = servletLoader.load();
            servlets.put(path, servletLoader);
            logger.info("on create {}", into);
        });
        fileListener.setOnDelete((into) -> {
            if(!servlets.contains(into)){
               return;
            }
            //todo stop servlet
//            final var httpServlet = servlets.remove(into);
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
