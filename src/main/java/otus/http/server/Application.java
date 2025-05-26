package otus.http.server;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import otus.http.server.container.Container;

import java.util.Arrays;

public class Application {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());


    private void run() {
        try {
            final var dispatcher = new Dispatcher();
            final var container = new Container(dispatcher);
            final var httpServer = new HttpServer(8080, dispatcher);
            container.run();
            httpServer.start();
        } catch (Exception e) {
            logger.info("unhandled exception: {}, stacktrace: {}", e.getMessage(), Arrays.asList(e.getStackTrace()));
        }
    }


    public static void main(String[] args) {
        final var application = new Application();
        application.run();
    }
}
