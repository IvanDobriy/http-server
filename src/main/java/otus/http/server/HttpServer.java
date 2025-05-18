package otus.http.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class HttpServer {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());
    private int port;

    public HttpServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("server has been run with port: {}", port);
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    final var httpRequest = new HttpRequest(socket);
                    final var httpResponse = new HttpResponse(socket);

                }
            }
        } catch (Exception e) {
            logger.error("Unhandled error: {}, stacktrace: {}", e.getMessage(), Arrays.asList(e.getStackTrace()));
        }
    }
}
