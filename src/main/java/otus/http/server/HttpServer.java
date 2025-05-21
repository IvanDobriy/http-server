package otus.http.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;

public class HttpServer {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());
    private int port;
    private Dispatcher dispatcher;

    public HttpServer(int port, Dispatcher dispatcher) {
        Objects.requireNonNull(dispatcher);
        this.port = port;
        this.dispatcher = dispatcher;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("server has been run with port: {}", port);
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    final var httpRequest = new HttpRequest(socket);
                    final var httpResponse = new HttpResponse(socket);
                    dispatcher.dispatch(httpRequest, httpResponse);
                }catch (Exception e){
                    logger.error("Unhandled error: {}, stacktrace: {}", e.getMessage(), Arrays.asList(e.getStackTrace()));
                }
            }
        } catch (Exception e) {
            logger.error("Unhandled error: {}, stacktrace: {}", e.getMessage(), Arrays.asList(e.getStackTrace()));
        }
    }
}
