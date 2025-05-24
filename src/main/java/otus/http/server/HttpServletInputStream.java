package otus.http.server;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class HttpServletInputStream extends ServletInputStream {
    private final InputStream inputStream;
    private ReadListener readListener;
    public HttpServletInputStream(InputStream inputStream){
        Objects.requireNonNull(inputStream);
        this.inputStream = inputStream;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean isReady() {
        try {
            return inputStream.available() > 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        Objects.requireNonNull(readListener);
        this.readListener = readListener;
        //todo добавить вызов executor с проверкой isReady
    }

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }
}
