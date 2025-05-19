package otus.http.server;


import otus.http.server.container.Container;

public class Application {
    public static void main(String[] args) {
        final var dispatcher = new Dispatcher();
        final var container = new Container(dispatcher);
        final var httpServer = new HttpServer(8080, dispatcher);
        container.run();
        httpServer.start();
    }
}
