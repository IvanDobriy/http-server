package otus.http.server;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class HttpCap {
    private final StringBuilder result;
    private final HttpResponse response;

    public HttpCap(HttpResponse response) {
        Objects.requireNonNull(response);
        this.response = response;
        result = new StringBuilder();
    }

    public HttpCap build() {
        result.append(String.format("HTTP/1.1 %d %s\r\n", response.getStatus(), response.getStatusMessage()));
        for(String name: response.getHeaderNames()){
            result.append(String.format("%s:%s\r\n", name, response.getHeader(name)));
        }
        result.append("\r\n");
        return this;
    }

    public byte[] toByteArray() {
        return result.toString().getBytes(StandardCharsets.UTF_8);
    }
}
