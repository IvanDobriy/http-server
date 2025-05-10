package otus.http.server;


import org.apache.logging.log4j.LogManager;
import otus.http.server.container.Container;

public class Application {
    public static void main(String[] args) {
        final var logger = LogManager.getLogger(Application.class.getName());
        final var container = new Container();
        container.run();
        logger.info("hello world");
    }
}
