package otus.http.server;


import org.apache.logging.log4j.LogManager;

public class Application {
    public static void main(String[] args) {
        final var logger = LogManager.getLogger(Application.class.getName());
        logger.info("hello world");
    }
}
