package otus.http.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HttpServer {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());
    private final Executor executor;
    private int port;
    private Dispatcher dispatcher;


    public HttpServer(int port, Dispatcher dispatcher) {
        Objects.requireNonNull(dispatcher);
        this.port = port;
        this.dispatcher = dispatcher;
        this.executor = Executors.newFixedThreadPool(10);
    }

    private Socket accept(ServerSocket serverSocket) {
        Socket socket = null;
        try {
            socket = serverSocket.accept();
        } catch (Exception e) {
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception ex) {
                    logger.error("Unhandled error: {}, stacktrace: {}", e.getMessage(), Arrays.asList(e.getStackTrace()));
                }
            }
            logger.error("Unhandled error: {}, stacktrace: {}", e.getMessage(), Arrays.asList(e.getStackTrace()));
        }
        return socket;
    }

    private void execute(Socket clientSocket){
        executor.execute(() -> {
            try {
                final var httpRequest = new HttpRequest(clientSocket);
                final var httpResponse = new HttpResponse(clientSocket);
                dispatcher.dispatch(httpRequest, httpResponse);
            } catch (Exception e) {
                logger.error("Unhandled error: {}, stacktrace: {}", e.getMessage(), Arrays.asList(e.getStackTrace()));
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    logger.error("Unhandled error: {}, stacktrace: {}", e.getMessage(), Arrays.asList(e.getStackTrace()));
                }
            }
        });
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("server has been run with port: {}", port);
            while (true) {
                final Socket clientSocket = accept(serverSocket);
                logger.info("accept client socket");
                if (clientSocket == null) {
                    continue;
                }
                execute(clientSocket);
            }
        } catch (Exception e) {
            logger.error("Unhandled error: {}, stacktrace: {}", e.getMessage(), Arrays.asList(e.getStackTrace()));
        }
    }
}
